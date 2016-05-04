package org.openyu.commons.redis.serializer.supporter;

import org.openyu.commons.mark.Supporter;
import org.openyu.commons.redis.serializer.BaseRedisSerializer;

public abstract class BaseRedisSerializerSupporter<T> implements BaseRedisSerializer<T>, Supporter {

	public BaseRedisSerializerSupporter() {

	}

}
