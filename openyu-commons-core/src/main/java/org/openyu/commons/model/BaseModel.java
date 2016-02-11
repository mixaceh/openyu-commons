package org.openyu.commons.model;

import java.io.Serializable;

/**
 * The Interface BaseModel.
 * 
 * BaseModel
 * +-BaseBean
 * +-BaseEntity
 * 
 * +-BaseServie
 */
public interface BaseModel extends Cloneable, Serializable {

	/** The key. */
	String KEY = BaseModel.class.getName();

	/**
	 * Clone.
	 *
	 * @return the object
	 */
	Object clone();

	/**
	 * Clone.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @return the t
	 */
	<T> T clone(BaseModel value);

	/**
	 * Clone.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @return the t
	 */
	<T> T clone(Object value);
}
