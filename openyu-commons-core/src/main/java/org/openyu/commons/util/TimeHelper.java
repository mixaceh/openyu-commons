package org.openyu.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

public final class TimeHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(TimeHelper.class);

	private TimeHelper() {
		throw new HelperException(
				new StringBuilder().append(TimeHelper.class.getName()).append(" can not construct").toString());
	}

	public static Time toTime(String value) {
		if (value != null) {
			return Time.valueOf(value);
		}
		return null;
	}
}
