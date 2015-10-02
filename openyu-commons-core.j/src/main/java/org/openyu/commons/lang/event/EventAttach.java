package org.openyu.commons.lang.event;

/**
 * 事件附件,不提供setter,只有在建構時才能設定,避免在處理過程中,被修改新舊值.
 *
 * The Interface EventAttach.
 *
 * @param <OLD_VALUE>
 *            the generic type
 * @param <NEW_VALUE>
 *            the generic type
 */
public interface EventAttach<OLD_VALUE, NEW_VALUE> {

	/**
	 * Gets the old value.
	 *
	 * @return the old value
	 */
	OLD_VALUE getOldValue();

	/**
	 * Gets the new value.
	 *
	 * @return the new value
	 */
	NEW_VALUE getNewValue();

	/**
	 * Gets the diff value.
	 *
	 * @return the diff value
	 */
	Double getDiffValue();

}
