package org.openyu.commons.lock.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.openyu.commons.lock.supporter.DistLockSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;

public class MysqlLock extends DistLockSupporter {

	private static final long serialVersionUID = -323114105619677559L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MysqlLock.class);

	private String name;

	private DataSource dataSource;

	private final AtomicReference<Connection> connection;

	public MysqlLock(DataSource dataSource, String name) {
		this.dataSource = dataSource;
		this.name = name;
		//
		this.connection = new AtomicReference<Connection>();
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected void doLock() {
		doTryLock(Integer.MAX_VALUE, TimeUnit.SECONDS);
	}

	@Override
	protected void doLockInterruptibly() {
		doTryLock(Integer.MAX_VALUE, TimeUnit.SECONDS);
	}

	@Override
	protected boolean doTryLock() {
		return doTryLock(0, TimeUnit.SECONDS);
	}

	@Override
	protected boolean doTryLock(long timeout, TimeUnit unit) {
		Integer r = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			this.connection.set(this.dataSource.getConnection());
			ps = this.connection.get().prepareStatement("SELECT GET_LOCK(?, ?)");
			ps.setString(1, this.name);
			ps.setInt(2, (int) TimeUnit.SECONDS.convert(timeout, unit));
			rs = ps.executeQuery();
			if (rs.next()) {
				r = rs.getInt(1);
				if (rs.wasNull())
					r = null;
			}
			//
			LOGGER.info("lock, name: {}, result: {}", this.name, r);
		} catch (Exception e) {
			throw new NestableRuntimeException("failed to lock, name: " + this.name, e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
		}
		final boolean succeed = (r != null && r == 1);
		return succeed;
	}

	@Override
	protected void doUnlock() {
		Integer r = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			//
			ps = this.connection.get().prepareStatement("SELECT RELEASE_LOCK(?)");
			ps.setString(1, this.name);
			rs = ps.executeQuery();
			if (rs.next()) {
				r = rs.getInt(1);
				if (rs.wasNull())
					r = null;
			}

			//
			if (r == null) {
				LOGGER.warn("lock does NOT exist, name: {}", this.name);
			} else if (r == 0) {
				LOGGER.warn("lock was NOT accquired by current thread, name: {}", this.name);
			}
			//
			LOGGER.info("unlock, name: {}, result: {}", this.name, r);
		} catch (Exception e) {
			throw new NestableRuntimeException("failed to unlock, name: " + this.name, e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
			JdbcUtils.closeConnection(this.connection.getAndSet(null));
		}
	}
}
