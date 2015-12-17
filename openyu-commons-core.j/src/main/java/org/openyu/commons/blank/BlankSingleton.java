package org.openyu.commons.blank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Blank單例, 如:service不使用spring建構時, 自行建構單例
 */
public class BlankSingleton {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankSingleton.class);

	private BlankSingleton() {
		if (InstanceHolder.INSTANCE != null) {
			throw new SecurityException(new StringBuilder().append(BlankSingleton.class.getName())
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
		private static BlankSingleton INSTANCE = new BlankSingleton();
	}

	/**
	 * Gets the single instance
	 *
	 * @return
	 */
	// public static BlankGetInstance getInstance() {
	// return InstanceHolder.INSTANCE;
	// }
	public synchronized static BlankSingleton getInstance() {
		if (InstanceHolder.INSTANCE == null) {
			InstanceHolder.INSTANCE = new BlankSingleton();
		}
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 單例關閉
	 * 
	 * @return
	 */
	public synchronized static BlankSingleton shutdownInstance() {
		if (InstanceHolder.INSTANCE != null) {
			InstanceHolder.INSTANCE = null;
		}
		return InstanceHolder.INSTANCE;
	}

	public synchronized static BlankSingleton restartInstance() {
		shutdownInstance();
		getInstance();
		return InstanceHolder.INSTANCE;
	}

}
