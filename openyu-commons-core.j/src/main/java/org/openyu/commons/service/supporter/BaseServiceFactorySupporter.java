package org.openyu.commons.service.supporter;

import org.openyu.commons.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseService工廠類
 * 
 * 1.instance
 */
public abstract class BaseServiceFactorySupporter<T extends BaseService> extends BaseFactorySupporter<BaseService> {

	private static final long serialVersionUID = -2017478815658304020L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseServiceFactorySupporter.class);

	protected T instance;

	public BaseServiceFactorySupporter() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected abstract T createInstance() throws Exception;

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected T shutdownInstance() throws Exception {
		try {
			if (this.instance != null) {
				T oldInstance = this.instance;
				oldInstance.shutdown();
				this.instance = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownInstance()").toString(), e);
			throw e;
		}
		return this.instance;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected T restartInstance() throws Exception {
		try {
			if (this.instance != null) {
				T oldInstance = this.instance;
				oldInstance.restart();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartInstance()").toString(), e);
			throw e;
		}
		return this.instance;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.instance = createInstance();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.instance = shutdownInstance();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.instance = restartInstance();
	}

	@Override
	public BaseService getObject() throws Exception {
		return instance;
	}

	@Override
	public Class<? extends BaseService> getObjectType() {
		return ((this.instance != null) ? this.instance.getClass() : BaseService.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
