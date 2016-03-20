package org.openyu.commons.lock.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.openyu.commons.lock.supporter.DistributedLockSupporter;
import org.springframework.jdbc.support.JdbcUtils;

public class MysqlLock extends DistributedLockSupporter {

	private static final long serialVersionUID = -323114105619677559L;

	private DataSource dataSource;

	/**
	 * 鎖的名稱
	 */
	private String name;

	private final AtomicReference<Connection> connection = new AtomicReference<Connection>();

	private int timeout;

	public MysqlLock(DataSource dataSource, String name, int timeout) {
		this.dataSource = dataSource;
		this.name = name;
		this.timeout = timeout;
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

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	protected void doLock() {
		// doTryLock(Integer.MAX_VALUE, TimeUnit.SECONDS);
		doTryLock(this.timeout, TimeUnit.SECONDS);
	}

	@Override
	protected void doLockInterruptibly() {
		// doTryLock(Integer.MAX_VALUE, TimeUnit.SECONDS);
		doTryLock(this.timeout, TimeUnit.SECONDS);
	}

	@Override
	protected boolean doTryLock() {
		// return doTryLock(0, TimeUnit.SECONDS);
		return doTryLock(this.timeout, TimeUnit.SECONDS);
	}

	@Override
	protected boolean doTryLock(long timeout, TimeUnit unit) {
		boolean reuslt = false;
		//
		Integer locked = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			// get conn from pool
			Connection conn = this.dataSource.getConnection();
			this.connection.set(conn);
			//
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT GET_LOCK(?, ?)");
			//
			ps = this.connection.get().prepareStatement(sql.toString());
			ps.setString(1, this.name);
			//
			int lockTimeout = (int) TimeUnit.SECONDS.convert(timeout, unit);
			ps.setInt(2, lockTimeout);
			rs = ps.executeQuery();
			if (rs.next()) {
				locked = rs.getInt(1);
				if (rs.wasNull()) {
					locked = null;
				}
			}
		} catch (Exception e) {
			throw new NestableRuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
		}
		reuslt = (locked != null && locked == 1);
		//
		return reuslt;
	}

	@Override
	protected void doUnlock() {
		Integer released = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			// sql
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT RELEASE_LOCK(?)");
			//
			conn = this.connection.get();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, this.name);
			//
			rs = ps.executeQuery();
			if (rs.next()) {
				released = rs.getInt(1);
				if (rs.wasNull()) {
					released = null;
				}
			}
		} catch (Exception e) {
			throw new NestableRuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
			// JdbcUtils.closeConnection(this.connection.getAndSet(null));
			this.connection.set(null);
			// conn return to pool
			JdbcUtils.closeConnection(conn);
		}
	}
}
