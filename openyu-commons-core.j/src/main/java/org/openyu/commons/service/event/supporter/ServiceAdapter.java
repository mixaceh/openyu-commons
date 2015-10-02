package org.openyu.commons.service.event.supporter;

import org.openyu.commons.bean.SeqBean;
import org.openyu.commons.entity.SeqEntity;
import org.openyu.commons.lang.event.supporter.BaseListenerSupporter;
import org.openyu.commons.service.event.BeanEvent;
import org.openyu.commons.service.event.BeanListener;
import org.openyu.commons.service.event.ServiceEvent;
import org.openyu.commons.service.event.ServiceListener;

/**
 * 1.若設計成singleton, 使用getInstance
 * 
 * 2.若用spring,不需使用getInstance,直接在xml上設定即可
 */
public class ServiceAdapter extends BaseListenerSupporter implements ServiceListener
{

	public ServiceAdapter()
	{

	}

	/**
	 * 初始
	 */
	public void initialize()
	{

	}

	/**
	 * 服務建構後
	 * 
	 * @param serviceEvent
	 */
	public void serviceInitialized(ServiceEvent serviceEvent)
	{

	}

	/**
	 * 服務除構後
	 * 
	 * @param serviceEvent
	 */
	public void serviceDestroyed(ServiceEvent serviceEvent)
	{

	}

}
