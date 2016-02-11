package org.openyu.commons.lang.event.supporter;

import org.openyu.commons.lang.event.BaseListener;
import org.openyu.commons.mark.Supporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class BaseListenerSupporter.
 */
public class BaseListenerSupporter implements BaseListener, Supporter {

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(BaseListenerSupporter.class);

	/**
	 * Instantiates a new base listener supporter.
	 */
	public BaseListenerSupporter() {
	}
}
