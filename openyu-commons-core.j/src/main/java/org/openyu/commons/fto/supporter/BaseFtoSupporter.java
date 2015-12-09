package org.openyu.commons.fto.supporter;

import org.openyu.commons.bao.BaseBao;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * File Transfer Object
 */
public abstract class BaseFtoSupporter extends BaseServiceSupporter implements
		BaseBao {

	private static final long serialVersionUID = 4612111321524921472L;

	public BaseFtoSupporter() {
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
