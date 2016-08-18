package org.openyu.commons.lock;

import javax.sql.DataSource;

/**
 * Mysql lock
 */
public interface MysqlLock extends DistributedLock {

	/**
	 * 連線池
	 * 
	 * @return
	 */
	DataSource getDataSource();

	void setDataSource(DataSource dataSource);

	/**
	 * 鎖的id
	 * 
	 * @return
	 */
	String getId();

	void setId(String id);

	/**
	 * 逾時秒數
	 * 
	 * @return
	 */
	int getTimeout();

	void setTimeout(int timeout);

	/**
	 * lock sql
	 * 
	 * @return
	 */
	String getLockSql();

	void setLockSql(String lockSql);

	/**
	 * unlock sql
	 * 
	 * @return
	 */
	String getUnlockSql();

	void setUnlockSql(String unlockSql);

}
