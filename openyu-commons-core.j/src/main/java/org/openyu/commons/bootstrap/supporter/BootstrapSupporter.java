package org.openyu.commons.bootstrap.supporter;

import org.openyu.commons.mark.Supporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class BootstrapSupporter implements Supporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(BootstrapSupporter.class);

	/** The application context. */
	protected static ApplicationContext applicationContext;
	
	protected static String DEFAULT_APPLICATION_CONTEXT_XML = "applicationContext.xml";


	public BootstrapSupporter() {

	}

}
