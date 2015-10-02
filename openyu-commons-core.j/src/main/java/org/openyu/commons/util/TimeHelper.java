package org.openyu.commons.util;

import java.sql.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

public class TimeHelper extends BaseHelperSupporter {

	private static transient final Logger log = LogManager
			.getLogger(TimeHelper.class);

	private static TimeHelper instance;

	public TimeHelper() {
	}

	public static synchronized TimeHelper getInstance() {
		if (instance == null) {
			instance = new TimeHelper();
		}
		return instance;
	}

	public static Time parse(String value) {
		if (value != null) {
			return Time.valueOf(value);
		}
		return null;
	}

	public static Time wrap(Object value) {
		Time result = null;
		if (value instanceof Time) {
			result = (Time) value;
		} else if (value instanceof String) {
			result = parse((String) value);
		}
		return result;
	}

}
