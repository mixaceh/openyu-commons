package org.openyu.commons.bootstrap.supporter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.mark.Supporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * 啟動器,只提供static method/field
 * 
 * 1.無法用 new 單例建構
 * 
 * 2.無法用 createInstance() 建構
 * 
 * 3.無法用 getInstance() 單例建構
 */
public abstract class BootstrapSupporter implements Supporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BootstrapSupporter.class);

	/**
	 * for spring
	 */
	protected static ApplicationContext applicationContext;

	protected static String DEFAULT_APPLICATION_CONTEXT_XML = "applicationContext.xml";

	/**
	 * for extends
	 */
	protected BootstrapSupporter() {

	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
