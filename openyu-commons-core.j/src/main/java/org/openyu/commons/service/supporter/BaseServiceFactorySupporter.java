package org.openyu.commons.service.supporter;

import org.openyu.commons.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseService工廠類
 * 
 * 1.service
 */
public abstract class BaseServiceFactorySupporter<T extends BaseService> extends BaseFactorySupporter<BaseService> {

	private static final long serialVersionUID = -2017478815658304020L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseServiceFactorySupporter.class);

	protected T service;

	public BaseServiceFactorySupporter() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected abstract T createService() throws Exception;

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected T shutdownService() throws Exception {
		try {
			if (this.service != null) {
				T oldInstance = this.service;
				oldInstance.shutdown();
				this.service = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownService()").toString(), e);
			throw e;
		}
		return this.service;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected T restartService() throws Exception {
		try {
			if (this.service != null) {
				T oldInstance = this.service;
				oldInstance.restart();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartService()").toString(), e);
			throw e;
		}
		return this.service;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.service = createService();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.service = shutdownService();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.service = restartService();
	}

	@Override
	public BaseService getObject() throws Exception {
		return service;
	}

	@Override
	public Class<? extends BaseService> getObjectType() {
		return ((this.service != null) ? this.service.getClass() : BaseService.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
