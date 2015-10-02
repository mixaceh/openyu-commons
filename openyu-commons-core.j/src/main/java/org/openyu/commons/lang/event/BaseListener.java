package org.openyu.commons.lang.event;

import java.util.EventListener;

/**
 * 監聽器
 *
 * The listener interface for receiving base events. The class that is
 * interested in processing a base event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addBaseListener<code> method. When
 * the base event occurs, that object's appropriate
 * method is invoked.
 *
 * @see BaseEvent
 */
public interface BaseListener extends EventListener {

	/** The key. */
	String KEY = BaseListener.class.getName();

}
