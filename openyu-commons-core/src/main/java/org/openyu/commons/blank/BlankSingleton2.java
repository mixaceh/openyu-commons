package org.openyu.commons.blank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Blank單例, 如:service不使用spring建構時, 自行建構單例
 */
public class BlankSingleton2 {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankSingleton2.class);

	private static BlankSingleton2 instance;

	private BlankSingleton2() {
	}

	public static synchronized BlankSingleton2 getInstance() {
		if (instance == null) {
			instance = new BlankSingleton2();
		}
		return instance;
	}
}
