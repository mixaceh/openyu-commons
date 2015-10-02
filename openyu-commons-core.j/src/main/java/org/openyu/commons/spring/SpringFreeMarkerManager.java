package org.openyu.commons.spring;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;

public class SpringFreeMarkerManager extends FreemarkerManager
{

	private static final String FREE_MARKER_CONFIGURER = "freeMarkerConfigurer";

	public SpringFreeMarkerManager()
	{}

	protected Configuration createConfiguration(ServletContext servletContext)
		throws TemplateException
	{
		Configuration configuration = null;
		//Configuration configuration = super.createConfiguration(servletContext);

		ApplicationContext applicationContext = SpringHelper.getApplicationContext(servletContext);

		//configuration
		FreeMarkerConfigurer freeMarkerConfigurer = (FreeMarkerConfigurer) applicationContext
				.getBean(FREE_MARKER_CONFIGURER);
		configuration = freeMarkerConfigurer.getConfiguration();

		//directive
		Map<String, TemplateDirectiveModel> beans = applicationContext
				.getBeansOfType(TemplateDirectiveModel.class);
		for (Map.Entry<String, TemplateDirectiveModel> entry : beans.entrySet())
		{
			String key = entry.getKey();
			TemplateDirectiveModel value = entry.getValue();
			if (value instanceof TemplateDirectiveModel)
			{
				configuration.setSharedVariable(key, value);
			}
		}
		//
		return configuration;

	}
}
