package org.openyu.commons.security;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DigestHelper extends BaseHelperSupporter {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(DigestHelper.class);

	private DigestHelper() {
		throw new HelperException(
				new StringBuilder().append(DigestHelper.class.getName()).append(" can not construct").toString());

	}

}
