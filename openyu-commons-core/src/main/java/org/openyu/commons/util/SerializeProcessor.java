package org.openyu.commons.util;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.processor.BaseProcessor;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 序列化處理器
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface SerializeProcessor extends BaseProcessor {

	String KEY = SerializeProcessor.class.getName();

	/**
	 * 是否序列化
	 * 
	 * @return
	 */
	boolean isSerialize();

	void setSerialize(boolean serialize);

	/**
	 * 序列化類別
	 * 
	 * @return
	 */
	SerializeType getSerializeType();

	void setSerializeType(SerializeType serializeType);

	/**
	 * 序列化類別列舉
	 * 
	 * @return
	 */
	Set<SerializeType> getSerializeTypes();

	void setSerializeTypes(Set<SerializeType> serializeTypes);

	/**
	 * 序列化
	 *
	 * @param serializeTypeValue
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @return
	 */
	byte[] serialize(String serializeTypeValue, Serializable value);

	/**
	 * 序列化
	 *
	 * @param serializeTypeValue
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @return
	 */
	byte[] serialize(int serializeTypeValue, Serializable value);

	/**
	 * 序列化
	 *
	 * @param value
	 * @return
	 */
	byte[] serialize(Serializable value);

	/**
	 * 反序列化
	 *
	 * @param serializeTypeValue
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @return
	 */
	<T> T deserialize(String serializeTypeValue, byte[] value);

	/**
	 * 反序列化
	 *
	 * @param serializeTypeValue
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @param clazz
	 * @return
	 */
	<T> T deserialize(String serializeTypeValue, byte[] value, Class<?> clazz);

	/**
	 * 反序列化
	 *
	 * @param serializeTypeValue
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @return
	 */
	<T> T deserialize(int serializeTypeValue, byte[] value);

	/**
	 * 反序列化
	 *
	 * @param serializeTypeValue
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @param clazz
	 * @return
	 */
	<T> T deserialize(int serializeTypeValue, byte[] value, Class<?> clazz);

	/**
	 * 反序列化
	 *
	 * @param value
	 * @return
	 */
	<T> T deserialize(byte[] value);

	/**
	 * 反序列化
	 *
	 * @param value
	 * @param clazz
	 * @return
	 */
	<T> T deserialize(byte[] value, Class<?> clazz);

	/**
	 * 重置
	 */
	void reset();

}
