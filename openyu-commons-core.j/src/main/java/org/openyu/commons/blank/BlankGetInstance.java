package org.openyu.commons.blank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Blank單例, 如:service不使用spring建構時, 自行建構單例
 */
public class BlankGetInstance {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankGetInstance.class);

	/**
	 * Instantiates a new helper.
	 */
	// private BlankGetInstance() {
	public BlankGetInstance() {
		if (InstanceHolder.INSTANCE != null) {
			throw new SecurityException(new StringBuilder().append(BlankGetInstance.class.getSimpleName())
					.append(" can not construct").toString());
		}
	}

	/**
	 * The Class InstanceHolder.
	 */
	private static class InstanceHolder {

		/** The Constant INSTANCE. */
		// private final static BlankGetInstance INSTANCE = new
		// BlankGetInstance();
		private static BlankGetInstance INSTANCE = new BlankGetInstance();
	}

	/**
	 * Gets the single instance of BlankGetInstance.
	 *
	 * @return single instance of BlankGetInstance
	 */
	// public static BlankGetInstance getInstance() {
	// return InstanceHolder.INSTANCE;
	// }
	public synchronized static BlankGetInstance getInstance() {
		if (InstanceHolder.INSTANCE == null) {
			InstanceHolder.INSTANCE = new BlankGetInstance();
		}
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 單例關閉
	 * 
	 * @return
	 */
	public synchronized static BlankGetInstance shutdownInstance() {
		if (InstanceHolder.INSTANCE != null) {
			InstanceHolder.INSTANCE = null;
		}
		return InstanceHolder.INSTANCE;
	}

	public synchronized static BlankGetInstance restartInstance() {
		shutdownInstance();
		getInstance();
		return InstanceHolder.INSTANCE;
	}

}
