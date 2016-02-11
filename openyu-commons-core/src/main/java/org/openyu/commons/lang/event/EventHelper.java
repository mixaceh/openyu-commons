package org.openyu.commons.lang.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.event.supporter.EventAttachSupporter;

/**
 * 事件輔助類
 */
public final class EventHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(EventHelper.class);

	private EventHelper() {
		throw new HelperException(
				new StringBuilder().append(EventHelper.class.getName()).append(" can not construct").toString());
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
	public static <OLD_VALUE, NEW_VALUE> EventAttach<OLD_VALUE, NEW_VALUE> eventAttach(OLD_VALUE oldValue,
			NEW_VALUE newValue) {
		EventAttach<OLD_VALUE, NEW_VALUE> eventAttach = new EventAttachSupporter<OLD_VALUE, NEW_VALUE>(oldValue,
				newValue);
		return eventAttach;
	}

}
