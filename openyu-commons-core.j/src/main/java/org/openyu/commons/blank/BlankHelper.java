package org.openyu.commons.blank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * Blank輔助類
 */
public class BlankHelper extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankHelper.class);

	/**
	 * Instantiates a new helper.
	 */
	// private BlankHelper() {
	public BlankHelper() {
		if (InstanceHolder.INSTANCE != null) {
			throw new HelperException(
					new StringBuilder().append(getDisplayName()).append(" can not construct").toString());
		}
	}

	/**
	 * The Class InstanceHolder.
	 */
	private static class InstanceHolder {

		/** The Constant INSTANCE. */
		// private final static BlankHelper INSTANCE = new BlankHelper();
		private static BlankHelper INSTANCE = new BlankHelper();
	}

	/**
	 * Gets the single instance of BlankHelper.
	 *
	 * @return single instance of BlankHelper
	 */
	// public static BlankHelper getInstance() {
	// return InstanceHolder.INSTANCE;
	// }
	public synchronized static BlankHelper getInstance() {
		if (InstanceHolder.INSTANCE == null) {
			InstanceHolder.INSTANCE = new BlankHelper();
		}
		//
		if (!InstanceHolder.INSTANCE.isStarted()) {
			InstanceHolder.INSTANCE.setGetInstance(true);
			// 啟動
			InstanceHolder.INSTANCE.start();
		}
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 單例關閉
	 * 
	 * @return
	 */
	public synchronized static BlankHelper shutdownInstance() {
		if (InstanceHolder.INSTANCE != null) {
			BlankHelper oldInstance = InstanceHolder.INSTANCE;
			InstanceHolder.INSTANCE = null;
			//
			if (oldInstance != null) {
				oldInstance.shutdown();
			}
		}
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 單例刷新
	 * 
	 * @return
	 */
	public synchronized static BlankHelper restartInstance() {
		if (InstanceHolder.INSTANCE != null) {
			InstanceHolder.INSTANCE.restart();
		}
		return InstanceHolder.INSTANCE;
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
