package org.openyu.commons.lock.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.openyu.commons.lock.MysqlLock;
import org.openyu.commons.lock.supporter.DistributedLockSupporter;
import org.springframework.jdbc.support.JdbcUtils;

public class MysqlLockImpl extends DistributedLockSupporter implements MysqlLock {

	private static final long serialVersionUID = -323114105619677559L;

	/**
	 * 連線池
	 */
	private transient DataSource dataSource;

	/**
	 * 鎖的名稱
	 */
	private String name;

	/**
	 * 逾時秒數
	 */
	private int timeout;

	public static final String DEFAULT_LOCK_SQL = "SELECT GET_LOCK(?, ?)";

	/**
	 * lock sql
	 */
	private String lockSql;

	public static final String DEFAULT_UNLOCK_SQL = "SELECT RELEASE_LOCK(?)";

	/**
	 * unlock sql
	 */
	private String unlockSql;

	private transient final AtomicReference<Connection> connection = new AtomicReference<Connection>();

	private transient final AtomicReference<Integer> locked = new AtomicReference<Integer>();

	public MysqlLockImpl(DataSource dataSource, String name, int timeout, String lockSql, String unlockSql) {
		this.dataSource = dataSource;
		this.name = name;
		this.timeout = timeout;
		this.lockSql = lockSql;
		this.unlockSql = unlockSql;
	}

	public MysqlLockImpl(DataSource dataSource, String name, int timeout) {
		this(dataSource, name, timeout, DEFAULT_LOCK_SQL, DEFAULT_UNLOCK_SQL);
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public String getLockSql() {
		return lockSql;
	}

	@Override
	public void setLockSql(String lockSql) {
		this.lockSql = lockSql;
	}

	@Override
	public String getUnlockSql() {
		return unlockSql;
	}

	@Override
	public void setUnlockSql(String unlockSql) {
		this.unlockSql = unlockSql;
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
			ps = this.connection.get().prepareStatement(this.lockSql);
			// lock name
			ps.setString(1, this.name);
			//
			int lockTimeout = (int) TimeUnit.SECONDS.convert(timeout, unit);
			// get lock timeout
			ps.setInt(2, lockTimeout);
			rs = ps.executeQuery();
			if (rs.next()) {
				locked = rs.getInt(1);
				if (rs.wasNull()) {
					locked = null;
				}
			}
			//
			if (locked != null && locked == 1) {
				reuslt = true;
				this.locked.set(locked);
			}
		} catch (Exception e) {
			throw new NestableRuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
		}
		//
		return reuslt;
	}

	@Override
	protected void doUnlock() {
		Integer locked = this.locked.get();
		if (locked == null || locked != 1) {
			return;
		}
		//
		Integer unlocked = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = this.connection.get();
			ps = conn.prepareStatement(this.unlockSql);
			// lock name
			ps.setString(1, this.name);
			//
			rs = ps.executeQuery();
			if (rs.next()) {
				unlocked = rs.getInt(1);
				if (rs.wasNull()) {
					unlocked = null;
				}
			}
		} catch (Exception e) {
			throw new NestableRuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
			this.connection.set(null);
			// conn return to pool
			JdbcUtils.closeConnection(conn);
			this.locked.set(null);
		}
	}

	public boolean equals(Object object) {
		if (!(object instanceof MysqlLockImpl)) {
			return false;
		}
		if (this == object) {
			return true;
		}
		MysqlLockImpl other = (MysqlLockImpl) object;
		if (name == null || other.name == null) {
			return false;
		}
		return new EqualsBuilder().append(name, other.name).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(name).toHashCode();
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("name", name);
		builder.append("timeout", timeout);
		builder.append("lockSql", lockSql);
		builder.append("unlockSql", unlockSql);
		return builder.toString();
	}
}
