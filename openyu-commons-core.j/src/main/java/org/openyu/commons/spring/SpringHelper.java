package org.openyu.commons.spring;

import javax.servlet.ServletContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

public class SpringHelper extends BaseHelperSupporter
{

	private static transient final Logger log = LogManager.getLogger(SpringHelper.class);

	private static SpringHelper instance;

	public SpringHelper()
	{}

	public static synchronized SpringHelper getInstance()
	{
		if (instance == null)
		{
			instance = new SpringHelper();
		}
		return instance;
	}

	public static ApplicationContext getApplicationContext(ServletContext servletContext)
	{
		return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

}
