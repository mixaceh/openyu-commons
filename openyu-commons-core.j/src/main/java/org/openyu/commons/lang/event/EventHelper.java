package org.openyu.commons.lang.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.BooleanHelper;
import org.openyu.commons.lang.event.supporter.EventAttachSupporter;

/**
 * The Class EventHelper.
 */
public class EventHelper extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(EventHelper.class);

	/** The instance. */
	private static EventHelper instance;

	/**
	 * Instantiates a new event helper.
	 */
	private EventHelper() {
	}

	/**
	 * Gets the single instance of EventHelper.
	 *
	 * @return single instance of EventHelper
	 */
	public static synchronized EventHelper getInstance() {
		if (instance == null) {
			instance = new EventHelper();
		}
		return instance;
	}

	/**
	 * 事件附件.
	 *
	 * @param <OLD_VALUE>
	 *            the generic type
	 * @param <NEW_VALUE>
	 *            the generic type
	 * @param oldValue
	 *            the old value
	 * @param newValue
	 *            the new value
	 * @return the event attach
	 */
	public static <OLD_VALUE, NEW_VALUE> EventAttach<OLD_VALUE, NEW_VALUE> eventAttach(
			OLD_VALUE oldValue, NEW_VALUE newValue) {
		EventAttach<OLD_VALUE, NEW_VALUE> eventAttach = new EventAttachSupporter<OLD_VALUE, NEW_VALUE>(
				oldValue, newValue);
		return eventAttach;
	}

}
