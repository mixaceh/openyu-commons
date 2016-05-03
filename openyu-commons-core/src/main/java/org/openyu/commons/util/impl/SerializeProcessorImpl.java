package org.openyu.commons.util.impl;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.processor.supporter.BaseProcessorSupporter;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.ChecksumType;
import org.openyu.commons.util.SerializeType;
import org.openyu.commons.util.SerializeProcessor;
import org.openyu.commons.util.SerializeHelper;
import org.openyu.commons.util.adapter.SerializeTypeXmlAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "serializeProcessor")
@XmlAccessorType(XmlAccessType.FIELD)
public class SerializeProcessorImpl extends BaseProcessorSupporter implements SerializeProcessor {

	private static final long serialVersionUID = 7763775982495411425L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(SerializeProcessorImpl.class);
	// --------------------------------------------------
	// 此有系統預設值,只是為了轉出xml,並非給企劃編輯用
	// --------------------------------------------------
	/**
	 * 序列化類別
	 */
	@XmlJavaTypeAdapter(SerializeTypeXmlAdapter.class)
	private static Set<SerializeType> serializeTypes = new LinkedHashSet<SerializeType>();

	// --------------------------------------------------
	/**
	 * 預設是否序列化
	 */
	public static final boolean DEFAULT_SERIALIZE = true;

	/**
	 * 是否序列化
	 */
	private boolean serialize = DEFAULT_SERIALIZE;

	/**
	 * 預設序列化類別
	 */
	public static final SerializeType DEFAULT_SERIALIZE_TYPE = SerializeType.JDK;

	/**
	 * 序列化類別
	 */
	private SerializeType serializeType = DEFAULT_SERIALIZE_TYPE;

	static {
		// 此有系統預設值,只是為了轉出xml,並非給企劃編輯用
		serializeTypes = EnumHelper.valuesSet(SerializeType.class);
	}

	public SerializeProcessorImpl() {
	}

	/**
	 * 是否序列化
	 * 
	 * @return
	 */
	public boolean isSerialize() {
		return serialize;
	}

	public void setSerialize(boolean serialize) {
		this.serialize = serialize;
	}

	/**
	 * 序列化類別
	 * 
	 * @return
	 */
	public SerializeType getSerializeType() {
		return serializeType;
	}

	public void setSerializeType(SerializeType serializeType) {
		this.serializeType = serializeType;
	}

	/**
	 * 序列化類別列舉
	 * 
	 * @return
	 */
	public Set<SerializeType> getSerializeTypes() {
		if (serializeTypes == null) {
			serializeTypes = new LinkedHashSet<SerializeType>();
		}
		return serializeTypes;
	}

	public void setSerializeTypes(Set<SerializeType> serialableTypes) {
		SerializeProcessorImpl.serializeTypes = serialableTypes;
	}

	/**
	 * 序列化
	 *
	 * @param serializeTypeValue
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @return
	 */
	public byte[] serialize(String serializeTypeValue, Object value) {
		SerializeType serializeType = EnumHelper.valueOf(SerializeType.class, serializeTypeValue);
		AssertHelper.notNull(serializeType, "The SerializeType must not be null");
		this.serializeType = serializeType;
		return serialize(value);
	}

	/**
	 * 序列化
	 *
	 * @param serializeTypeValue
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @return
	 */
	public byte[] serialize(int serializeTypeValue, Object value) {
		SerializeType serializeType = EnumHelper.valueOf(SerializeType.class, serializeTypeValue);
		AssertHelper.notNull(serializeType, "The SerializeType must not be null");
		this.serializeType = serializeType;
		return serialize(value);
	}

	/**
	 * 序列化
	 *
	 * @param value
	 * @return
	 */
	public byte[] serialize(Object value) {
		byte[] result = new byte[0];
		//
		if (!serialize) {
			return result;
		}
		AssertHelper.notNull(serializeType, "The SerializeType must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		switch (serializeType) {
		case JGROUP: {
			result = SerializeHelper.jgroup(value);
			break;
		}
		case JDK: {
			result = SerializeHelper.jdk(value);
			break;
		}
		case FST: {
			result = SerializeHelper.fst(value);
			break;
		}
		case KRYO: {
			result = SerializeHelper.kryo(value);
			break;
		}
		case JACKSON: {
			result = SerializeHelper.jackson(value);
			break;
		}
		case SMILE: {
			result = SerializeHelper.smile(value);
			break;
		}
		case SMILE_JAXRS: {
			result = SerializeHelper.smileJaxrs(value);
			break;
		}
		default: {
			AssertHelper.unsupported("The SerializeType [" + serializeType + "] is unsupported");
			break;
		}
		}
		return result;
	}

	/**
	 * 反序列化執行
	 *
	 * @param serializeTypeValue
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @return
	 */
	public <T> T deserialize(String serializeTypeValue, byte[] value) {
		return deserialize(serializeTypeValue, value, null);
	}

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
	public <T> T deserialize(String serializeTypeValue, byte[] value, Class<?> clazz) {
		SerializeType serializeType = EnumHelper.valueOf(SerializeType.class, serializeTypeValue);
		AssertHelper.notNull(serializeType, "The SerializeType must not be null");
		this.serializeType = serializeType;
		return deserialize(value, clazz);
	}

	/**
	 * 反序列化執行
	 *
	 * @param serializeTypeValue
	 *            序列化類別
	 * @see SerializeType
	 * @param value
	 * @return
	 */
	public <T> T deserialize(int serializeTypeValue, byte[] value) {
		return deserialize(serializeTypeValue, value, null);
	}

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
	public <T> T deserialize(int serializeTypeValue, byte[] value, Class<?> clazz) {
		SerializeType serializeType = EnumHelper.valueOf(SerializeType.class, serializeTypeValue);
		AssertHelper.notNull(serializeType, "The SerializeType must not be null");
		this.serializeType = serializeType;
		return deserialize(value, clazz);
	}

	public <T> T deserialize(byte[] value) {
		return deserialize(value, null);
	}

	/**
	 * 反序列化執行
	 *
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T deserialize(byte[] value, Class<?> clazz) {
		T result = null;
		//
		if (!serialize) {
			return result;
		}
		AssertHelper.notNull(serializeType, "The SerializeType must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		switch (serializeType) {
		case JGROUP: {
			result = SerializeHelper.dejgroup(value);
			break;
		}
		case JDK: {
			result = SerializeHelper.dejdk(value);
			break;
		}
		case FST: {
			result = SerializeHelper.defst(value);
			break;
		}
		case KRYO: {
			AssertHelper.notNull(clazz, "When use KRYO to deserialize the value, the Clazz must not be null");
			//
			result = (T) SerializeHelper.dekryo(value, clazz);
			break;
		}
		case JACKSON: {
			AssertHelper.notNull(clazz, "When use JSON to deserialize the value, the Clazz must not be null");
			//
			result = (T) SerializeHelper.dejackson(value, clazz);
			break;
		}
		case SMILE: {
			AssertHelper.notNull(clazz, "When use SMILE to deserialize the value, the Clazz must not be null");
			//
			result = (T) SerializeHelper.desmile(value, clazz);
			break;
		}
		case SMILE_JAXRS: {
			AssertHelper.notNull(clazz, "When use SMILE_JAXRS to deserialize the value, the Clazz must not be null");
			//
			result = (T) SerializeHelper.desmileJaxrs(value, clazz);
			break;
		}
		default: {
			AssertHelper.unsupported("The SerializeType [" + serializeType + "] is unsupported");
			break;
		}
		}
		return result;
	}

	/**
	 * 重置
	 */
	public void reset() {
		// nothing to do
	}

	public Object clone() {
		SerializeProcessorImpl copy = null;
		copy = (SerializeProcessorImpl) super.clone();
		//
		SerializeProcessorImpl.serializeTypes = clone(serializeTypes);
		return copy;
	}
}
