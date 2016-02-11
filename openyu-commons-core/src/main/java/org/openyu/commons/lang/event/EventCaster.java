package org.openyu.commons.lang.event;

import java.lang.reflect.Array;
import java.util.EventListener;

import org.openyu.commons.mark.Supporter;

/**
 * The Class EventCaster.
 */
public class EventCaster implements Supporter {

	/** The listeners. */
	private transient Object[] listeners;

	/**
	 * Instantiates a new event caster.
	 */
	public EventCaster() {
	}

	/**
	 * 使用 try/catch,不拋出ex.
	 *
	 * @param eventDispatchable
	 *            the event dispatchable
	 */
	public synchronized void dispatch(EventDispatchable eventDispatchable) {
		if (!isEmpty()) {
			for (int index = 0; index < listeners.length; index++) {
				try {
					eventDispatchable
							.dispatch((EventListener) listeners[index]);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * 拋出ex.
	 *
	 * @param exceptionDispatchable
	 *            the exception dispatchable
	 * @throws Exception
	 *             the exception
	 */
	public synchronized void exceptionDispatch(
			ExceptionDispatchable exceptionDispatchable) throws Exception {
		if (!isEmpty()) {
			for (int index = 0; index < listeners.length; index++) {
				exceptionDispatchable
						.exceptionDispatch((EventListener) listeners[index]);
			}
		}
	}

	// ----------------------------------

	/**
	 * 搜尋,by reference.
	 *
	 * @param eventListener
	 *            the event listener
	 * @return the int
	 */
	protected synchronized int find(EventListener eventListener) {
		int result = -1;
		if (!isEmpty()) {
			for (int i = 0; i < listeners.length; i++) {
				if (eventListener == listeners[i]) {
					result = i;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 當找不到時才加入.
	 *
	 * @param eventListener
	 *            the event listener
	 */
	protected synchronized void add(EventListener eventListener) {
		int index = find(eventListener);
		if (index < 0) {
			Object[] newListeners;
			if (listeners == null) {
				newListeners = new Object[1]; // length=1
			} else {
				newListeners = new Object[listeners.length + 1]; // 5+1=6
				System.arraycopy(listeners, 0, newListeners, 0,
						listeners.length);
			}
			newListeners[newListeners.length - 1] = eventListener;
			listeners = newListeners;
		}
	}

	/**
	 * 當有找到時才移除.
	 *
	 * @param eventListener
	 *            the event listener
	 */
	protected synchronized void remove(EventListener eventListener) {
		int index = find(eventListener);
		if (index > -1) {
			if (listeners.length == 1) {
				listeners = null;
			} else {
				Object[] newListeners = new Object[listeners.length - 1]; // 5-1=4
				System.arraycopy(listeners, 0, newListeners, 0, index);
				if (index < newListeners.length) {
					System.arraycopy(listeners, index + 1, newListeners, index,
							newListeners.length - index);

				}
				listeners = newListeners;
			}
		}
	}

	/**
	 * Adds the.
	 *
	 * @param eventCaster
	 *            the event caster
	 * @param eventListener
	 *            the event listener
	 * @return the event caster
	 */
	public synchronized static EventCaster add(EventCaster eventCaster,
			EventListener eventListener) {

		if (eventCaster == null) {
			eventCaster = new EventCaster();
		}
		eventCaster.add(eventListener);
		return eventCaster;
	}

	/**
	 * Removes the.
	 *
	 * @param eventCaster
	 *            the event caster
	 * @param eventListener
	 *            the event listener
	 * @return the event caster
	 */
	public synchronized static EventCaster remove(EventCaster eventCaster,
			EventListener eventListener) {
		if (eventCaster != null) {
			eventCaster.remove(eventListener);
		}
		return eventCaster;
	}

	/**
	 * Gets the listeners.
	 *
	 * @param clazz
	 *            the clazz
	 * @return the listeners
	 */
	public synchronized EventListener[] getListeners(Class<?> clazz) {
		EventListener[] result = null;
		if (!isEmpty()) {
			result = (EventListener[]) Array.newInstance(clazz, size());
			for (int i = 0; i < result.length; i++) {
				result[i] = (EventListener) listeners[i];
			}
		}
		return result;
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public synchronized int size() {
		return (listeners != null) ? listeners.length : 0;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public synchronized boolean isEmpty() {
		return (listeners == null || listeners.length == 0);
	}

	/**
	 * Clear.
	 */
	public synchronized void clear() {
		listeners = null;
	}

}
