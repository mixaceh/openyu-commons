package org.openyu.commons.mqo.supporter;

import org.openyu.commons.mqo.BaseMqo;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * 基底Message Queue Object
 */
public abstract class BaseMqoSupporter extends BaseServiceSupporter implements
		BaseMqo {

	private static final long serialVersionUID = -3865524980959357332L;

	public BaseMqoSupporter() {

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
