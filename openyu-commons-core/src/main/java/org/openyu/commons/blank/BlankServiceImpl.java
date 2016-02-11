package org.openyu.commons.blank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * Blank服務
 */
public final class BlankServiceImpl extends BaseServiceSupporter implements BlankService {

	private static final long serialVersionUID = -1401366707657809071L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankServiceImpl.class);

	private static BlankServiceImpl instance;

	public BlankServiceImpl() {
	}

	/**
	 * 單例啟動
	 * 
	 * @return
	 */
	public synchronized static BlankService getInstance() {
		try {
			if (instance == null) {
				instance = new BlankServiceImpl();
				instance.setGetInstance(true);
				// 啟動
				instance.start();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during getInstance()").toString(), e);
			instance = (BlankServiceImpl) shutdownInstance();
		}
		return instance;
	}

	/**
	 * 單例關閉
	 * 
	 * @return
	 */
	public synchronized static BlankService shutdownInstance() {
		try {
			if (instance != null) {
				BlankServiceImpl oldInstance = instance;
				//
				oldInstance.shutdown();
				instance = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownInstance()").toString(), e);
		}
		return instance;
	}

	/**
	 * 單例重啟
	 * 
	 * @return
	 */
	public synchronized static BlankService restartInstance() {
		try {
			if (instance != null) {
				instance.restart();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartInstance()").toString(), e);
		}
		return instance;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {

	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}
}
