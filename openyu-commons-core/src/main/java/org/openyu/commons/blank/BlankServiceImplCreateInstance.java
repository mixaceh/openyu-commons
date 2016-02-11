package org.openyu.commons.blank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * Blank服務
 */
public final class BlankServiceImplCreateInstance extends BaseServiceSupporter implements BlankService {

	private static final long serialVersionUID = -1401366707657809071L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankServiceImplCreateInstance.class);

	private static BlankServiceImplCreateInstance instance;

	public BlankServiceImplCreateInstance() {
	}

	/**
	 * 單例啟動
	 * 
	 * @return
	 */
	public synchronized static BlankService getInstance() {
		try {
			if (instance == null) {
				instance = new BlankServiceImplCreateInstance();
				instance.setGetInstance(true);
				// 啟動
				instance.start();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during getInstance()").toString(), e);
			instance = (BlankServiceImplCreateInstance) shutdownInstance();
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
				BlankServiceImplCreateInstance oldInstance = instance;
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
	 * 建構
	 *
	 * @return
	 */
	public static BlankService createInstance() {
		BlankServiceImplCreateInstance result = null;
		try {
			result = new BlankServiceImplCreateInstance();
			result.setCreateInstance(true);
			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createInstance()").toString(), e);
			result = (BlankServiceImplCreateInstance) shutdownInstance(result);
		}
		return result;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	public static BlankService shutdownInstance(BlankService blankService) {
		try {
			if (blankService instanceof BlankService) {
				BlankService oldInstance = blankService;
				//
				oldInstance.shutdown();
				blankService = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownInstance(BlankService)").toString(),
					e);
		}
		return blankService;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	public static BlankService restartInstance(BlankService blankService) {
		try {
			if (blankService instanceof BlankService) {
				BlankService oldInstance = blankService;
				oldInstance.restart();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartInstance(BlankService)").toString(), e);
		}
		return blankService;
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
