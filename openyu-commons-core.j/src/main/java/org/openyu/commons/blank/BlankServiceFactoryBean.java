package org.openyu.commons.blank;

import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BlankService工廠
 */
public final class BlankServiceFactoryBean<T> extends BaseFactorySupporter<BlankService> {

	private static final long serialVersionUID = -1401366707657809071L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankServiceFactoryBean.class);

	private BlankService blankService;

	public BlankServiceFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected BlankService createInstance() throws Exception {
		BlankServiceImpl result = null;
		try {
			result = new BlankServiceImpl();
			result.setCreateInstance(true);

			// 1.extendedProperties
			// LOGGER.info("" +
			// extendedProperties.getLong("org.openyu.blank.BlankService.aliveMills"));
			// LOGGER.info("" +
			// extendedProperties.getLong("org.openyu.blank.BlankService.listenMills"));
			// 2. injectiion

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createInstance()").toString(), e);
			try {
				result = (BlankServiceImpl) shutdownInstance();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected BlankService shutdownInstance() throws Exception {
		try {
			if (this.blankService != null) {
				BlankService oldInstance = this.blankService;
				oldInstance.shutdown();
				this.blankService = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownInstance()").toString(), e);
			throw e;
		}
		return this.blankService;

	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected BlankService restartInstance() throws Exception {
		try {
			if (this.blankService != null) {
				BlankService oldInstance = this.blankService;
				oldInstance.restart();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartInstance()").toString(), e);
			throw e;
		}
		return this.blankService;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.blankService = createInstance();
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
