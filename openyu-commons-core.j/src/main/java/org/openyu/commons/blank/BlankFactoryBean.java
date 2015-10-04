package org.openyu.commons.blank;

import org.openyu.commons.service.BaseService;
import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Blank服務
 */
public final class BlankFactoryBean<T> extends BaseFactorySupporter<BlankService> {

	private static final long serialVersionUID = -1401366707657809071L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankFactoryBean.class);

	private BlankService blankService;

	public BlankFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	@Override
	public BlankService createInstance() {
		BlankServiceImpl result = null;
		try {
			result = new BlankServiceImpl();
			result.setCreateInstance(true);
			// TODO for extendedProperties

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createInstance()").toString(), e);
			result = (BlankServiceImpl) shutdownInstance();
		}
		return result;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	@Override
	public BlankService shutdownInstance() {
		try {
			BlankService oldInstance = blankService;
			//
			if (oldInstance != null) {
				oldInstance.shutdown();
			}
			blankService = null;
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownInstance()").toString(), e);
		}
		return blankService;

	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	@Override
	public BlankService restartInstance() {
		try {
			BlankService oldInstance = blankService;
			//
			if (oldInstance != null) {
				oldInstance.restart();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartInstance()").toString(), e);
		}
		return blankService;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		blankService = createInstance();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.blankService = shutdownInstance();
	}

	@Override
	protected void doRestart() throws Exception {
		this.blankService = restartInstance();
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
