package org.openyu.commons.web.struts2.supporter;

import org.openyu.commons.web.struts2.BaseSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用者session,web用
 */
public class BaseSessionSupporter extends BaseActionSupporter implements BaseSession {

	private static final long serialVersionUID = 7746586053581141581L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseSessionSupporter.class);

	public BaseSessionSupporter() {
	}
}
