package org.openyu.commons.redis.supporter;

import org.openyu.commons.mark.Supporter;
import org.openyu.commons.redis.BaseRedisSerializer;

public abstract class BaseRedisSerializerSupporter<T> implements BaseRedisSerializer<T>, Supporter {

	public BaseRedisSerializerSupporter() {

	}

}
