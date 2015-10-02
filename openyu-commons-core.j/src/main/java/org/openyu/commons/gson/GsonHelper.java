package org.openyu.commons.gson;

import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GsonHelper extends BaseHelperSupporter {
	
	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(GsonHelper.class);

	private static GsonHelper instance;

	private GsonHelper() {
	}

	public static synchronized GsonHelper getInstance() {
		if (instance == null) {
			instance = new GsonHelper();
		}
		return instance;
	}

}
