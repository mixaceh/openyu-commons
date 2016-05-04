package org.openyu.commons.redis.serializer.impl;

import org.openyu.commons.redis.serializer.supporter.BaseRedisSerializerSupporter;
import org.openyu.commons.util.SerializeHelper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class JdkRedisSerializer extends BaseRedisSerializerSupporter<Object> implements RedisSerializer<Object> {

	public JdkRedisSerializer() {

	}

	@Override
	public byte[] serialize(Object value) throws SerializationException {
		if (value == null) {
			return null;
		}
		return SerializeHelper.jdk(value);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null) {
			return null;
		}
		return SerializeHelper.dejdk(bytes);
	}

}
