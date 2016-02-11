package org.openyu.commons.service.event;

import java.util.EventListener;

import org.openyu.commons.lang.event.EventDispatchable;
import org.openyu.commons.lang.event.supporter.BaseEventSupporter;

/**
 * bean事件
 */
public class BeanEvent extends BaseEventSupporter implements EventDispatchable
{

	private static final long serialVersionUID = -3074731041984440464L;

	public static final int INSERTING = 0;

	public static final int INSERTED = 1;

	public static final int FINDTING = 2;

	public static final int FOUND = 3;

	public static final int UPDATING = 4;

	public static final int UPDATED = 5;

	public static final int DELETING = 6;

	public static final int DELETED = 7;

	public BeanEvent(Object source, int type, Object attach)
	{
		super(source, type, attach);
	}

	public BeanEvent(Object source, int type)
	{
		this(source, type, null);
	}

	public void dispatch(EventListener listener)
	{
		switch (getType())
		{
			case INSERTING:
				((BeanListener) listener).beanInserting(this);
				break;
			case INSERTED:
				((BeanListener) listener).beanInserted(this);
				break;
			case FINDTING:
				((BeanListener) listener).beanFinding(this);
				break;
			case FOUND:
				((BeanListener) listener).beanFound(this);
				break;
			case UPDATING:
				((BeanListener) listener).beanUpdating(this);
				break;
			case UPDATED:
				((BeanListener) listener).beanUpdated(this);
				break;
			case DELETING:
				((BeanListener) listener).beanDeleting(this);
				break;
			case DELETED:
				((BeanListener) listener).beanDeleted(this);
				break;
		}
	}

}
