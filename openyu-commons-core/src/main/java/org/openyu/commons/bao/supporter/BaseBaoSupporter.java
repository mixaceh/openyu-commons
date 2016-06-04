package org.openyu.commons.bao.supporter;

import org.openyu.commons.bao.BaseBao;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * Big Data Access Object
 */
public abstract class BaseBaoSupporter extends BaseServiceSupporter implements BaseBao {

	private static final long serialVersionUID = -1164210143803135010L;

	public BaseBaoSupporter() {
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
	 * @throws Exception
	 */
	protected abstract void checkConfig() throws Exception;
}
