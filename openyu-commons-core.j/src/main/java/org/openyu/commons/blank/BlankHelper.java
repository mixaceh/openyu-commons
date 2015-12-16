package org.openyu.commons.blank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * Blank輔助類
 */
public class BlankHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankHelper.class);

	private BlankHelper() {
		throw new HelperException(
				new StringBuilder().append(BlankHelper.class.getSimpleName()).append(" can not construct").toString());
	}
}
