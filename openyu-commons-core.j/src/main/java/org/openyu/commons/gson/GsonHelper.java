package org.openyu.commons.gson;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GsonHelper extends BaseHelperSupporter {
	xxx
	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(GsonHelper.class);


	private GsonHelper() {
		throw new HelperException(
				new StringBuilder().append(GsonHelper.class.getName()).append(" can not construct").toString());
	}


}
