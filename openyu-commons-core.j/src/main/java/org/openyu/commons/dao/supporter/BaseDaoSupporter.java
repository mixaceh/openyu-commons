package org.openyu.commons.dao.supporter;

import org.openyu.commons.dao.BaseDao;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * 基底Relational Data Access Object
 */
public abstract class BaseDaoSupporter extends BaseServiceSupporter implements
		BaseDao {

	private static final long serialVersionUID = -7248126336974035005L;

	public BaseDaoSupporter() {
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		checkConfig();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}

	/**
	 * 檢查設置
	 * 
	 * @throws IllegalArgumentException
	 */
	protected abstract void checkConfig() throws IllegalArgumentException;
}
