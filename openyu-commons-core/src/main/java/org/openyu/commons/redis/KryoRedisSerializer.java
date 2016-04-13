package org.openyu.commons.redis;

import org.openyu.commons.redis.supporter.BaseRedisSerializerSupporter;
import org.openyu.commons.util.SerializeHelper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class KryoRedisSerializer extends BaseRedisSerializerSupporter<Object> implements RedisSerializer<Object> {

	public KryoRedisSerializer() {

	}

	@Override
	public byte[] serialize(Object value) throws SerializationException {
		if (value == null) {
			return null;
		}
		return SerializeHelper.kryoWriteClass(value);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null) {
			return null;
		}
		return SerializeHelper.dekryoReadClass(bytes);
	}

}
