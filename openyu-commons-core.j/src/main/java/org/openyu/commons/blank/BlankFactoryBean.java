package org.openyu.commons.blank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.service.supporter.BaseFactorySupporter;

/**
 * Blank服務
 */
public final class BlankFactoryBean extends BaseFactorySupporter<BlankService> {

	private static final long serialVersionUID = -1401366707657809071L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankFactoryBean.class);

	private BlankService blankService;

	public BlankFactoryBean() {
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		BlankServiceImpl impl = new BlankServiceImpl();
		impl.setCreateInstance(true);
		// 啟動
		impl.start();
		//
		blankService = impl;
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		BlankService oldInstance = blankService;
		oldInstance.shutdown();
		blankService = null;
	}

	@Override
	public BlankService getObject() throws Exception {
		return blankService;
	}

	@Override
	public Class<? extends BlankService> getObjectType() {
		return ((this.blankService != null) ? this.blankService.getClass() : BlankService.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
