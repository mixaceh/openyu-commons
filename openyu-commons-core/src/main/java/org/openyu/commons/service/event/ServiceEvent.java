package org.openyu.commons.service.event;

import java.util.EventListener;

import org.openyu.commons.lang.event.EventDispatchable;
import org.openyu.commons.lang.event.supporter.BaseEventSupporter;

public class ServiceEvent extends BaseEventSupporter implements EventDispatchable
{

	private static final long serialVersionUID = -3074731041984440464L;

	public static final int INITIALIZED = 0;

	public static final int DESTROYED = 1;

	public ServiceEvent(Object source, int type, Object attach)
	{
		super(source, type, attach);
	}

	public ServiceEvent(Object source, int type)
	{
		this(source, type, null);
	}

	public void dispatch(EventListener listener)
	{
		switch (getType())
		{
			case INITIALIZED:
				((ServiceListener) listener).serviceInitialized(this);
				break;
			case DESTROYED:
				((ServiceListener) listener).serviceDestroyed(this);
				break;

		}
	}

}
