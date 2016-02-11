package org.openyu.commons.spring;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SpringHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(SpringHelper.class);

	private SpringHelper() {
		throw new HelperException(
				new StringBuilder().append(SpringHelper.class.getName()).append(" can not construct").toString());
	}

	public static ApplicationContext getApplicationContext(ServletContext servletContext) {
		return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

}
