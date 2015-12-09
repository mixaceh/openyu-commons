package org.openyu.commons.web.struts2.supporter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openyu.commons.web.struts2.BaseSession;

/**
 * 使用者session,web用
 */
public class BaseSessionSupporter extends BaseActionSupporter implements BaseSession
{

	private static final long serialVersionUID = 7746586053581141581L;

	private static transient final Logger log = LogManager.getLogger(BaseSessionSupporter.class);

	public BaseSessionSupporter()
	{}
}
