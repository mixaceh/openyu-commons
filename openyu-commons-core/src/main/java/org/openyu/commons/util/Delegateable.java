package org.openyu.commons.util;

/**
 * 可委派
 *
 * @param <T>
 */
public interface Delegateable<T> {

	T getDelegate();
}
