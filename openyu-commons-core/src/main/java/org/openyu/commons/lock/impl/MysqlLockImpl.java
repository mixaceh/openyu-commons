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
import org.openyu.commons.lock.MysqlLock;
import org.openyu.commons.lock.ex.DistributedLockException;
import org.openyu.commons.lock.supporter.DistributedLockSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;

import com.newegg.commons.util.AssertUtil;

public class MysqlLockImpl extends DistributedLockSupporter implements MysqlLock {

	private static final long serialVersionUID = -323114105619677559L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(MysqlLockImpl.class);

	/**
	 * 連線池
	 */
	private transient DataSource dataSource;

	/**
	 * 鎖的id
	 */
	private String id;

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

	public MysqlLockImpl(DataSource dataSource, String id, int timeout, String lockSql, String unlockSql) {
		this.dataSource = dataSource;
		this.id = id;
		this.timeout = timeout;
		this.lockSql = lockSql;
		this.unlockSql = unlockSql;
	}

	public MysqlLockImpl(DataSource dataSource, String id, int timeout) {
		this(dataSource, id, timeout, DEFAULT_LOCK_SQL, DEFAULT_UNLOCK_SQL);
	}

	public MysqlLockImpl() {
		this(null, null, 0, DEFAULT_LOCK_SQL, DEFAULT_UNLOCK_SQL);
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
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
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
		try {
			doTryLock(this.timeout, TimeUnit.SECONDS);
		} catch (Throwable e) {
			throw new DistributedLockException("Could not acquire lock: " + id, e);
		}
	}

	@Override
	protected void doLockInterruptibly() throws InterruptedException {
		// doTryLock(Integer.MAX_VALUE, TimeUnit.SECONDS);
		doTryLock(this.timeout, TimeUnit.SECONDS);
	}

	@Override
	protected boolean doTryLock() {
		// return doTryLock(0, TimeUnit.SECONDS);
		try {
			return doTryLock(this.timeout, TimeUnit.SECONDS);
		} catch (Throwable e) {
			throw new DistributedLockException("Could not acquire lock: " + id, e);
		}
	}

	@Override
	protected boolean doTryLock(long timeout, TimeUnit unit) throws InterruptedException {
		boolean reuslt = false;
		//
		AssertHelper.notNull(dataSource, "The DataSource must not be null");
		AssertHelper.notNull(dataSource, "The Id must not be null");
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
			// lock id
			ps.setString(1, this.id);
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
			} else {
				throw new DistributedLockException("Could not acquire lock: " + id);
			}
		} catch (DistributedLockException e) {
			throw e;
		} catch (Throwable e) {
			throw new DistributedLockException("Could not acquire lock: " + id, e);
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
			// lock id
			ps.setString(1, this.id);
			//
			rs = ps.executeQuery();
			if (rs.next()) {
				unlocked = rs.getInt(1);
				if (rs.wasNull()) {
					unlocked = null;
				}
			}
			//
			if (unlocked == null || unlocked == 0) {
				// 無法釋放鎖, 有可能是重複釋放, 只寫log, 不拋出ex
				LOGGER.warn("Could not release lock: " + id);
			}
		} catch (Throwable e) {
			throw new DistributedLockException("Could not release lock: " + id, e);
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
		if (id == null || other.id == null) {
			return false;
		}
		return new EqualsBuilder().append(id, other.id).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("id", id);
		builder.append("timeout", timeout);
		builder.append("lockSql", lockSql);
		builder.append("unlockSql", unlockSql);
		return builder.toString();
	}
}
