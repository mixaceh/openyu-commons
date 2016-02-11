package org.openyu.commons.service.event;

import org.openyu.commons.lang.event.BaseListener;

public interface ServiceListener extends BaseListener
{
	/**
	 * 服務建構後
	 * 
	 * @param serviceEvent
	 */
	void serviceInitialized(ServiceEvent serviceEvent);

	/**
	 * 服務除構後
	 * 
	 * @param serviceEvent
	 */
	void serviceDestroyed(ServiceEvent serviceEvent);

}
