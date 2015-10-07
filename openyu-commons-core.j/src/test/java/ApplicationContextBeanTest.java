import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.velocity.app.VelocityEngine;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import freemarker.template.Configuration;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.util.ConfigHelper;

public class ApplicationContextBeanTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/service/applicationContext-service.xml", //
				"applicationContext-bean.xml", //
		});
	}

	@Test
	public void configHelper() {
		ConfigHelper bean = (ConfigHelper) applicationContext.getBean("configHelper");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer bean = (PropertyPlaceholderConfigurer) applicationContext
				.getBean("propertyPlaceholderConfigurer");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void servicePropertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer bean = (PropertyPlaceholderConfigurer) applicationContext
				.getBean("servicePropertyPlaceholderConfigurer");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void propertyOverrideConfigurer() {
		PropertyOverrideConfigurer bean = (PropertyOverrideConfigurer) applicationContext
				.getBean("propertyOverrideConfigurer");
		System.out.println(bean);
		assertNotNull(bean);
	}

	/**
	 * for using Spring's VelocityView for web MVC
	 */
	// @Test
	// public void velocityConfigurer() {
	// VelocityConfigurer bean = (VelocityConfigurer)
	// applicationContext.getBean("velocityConfigurer");
	// System.out.println(ToStringBuilder.reflectionToString(bean,
	// ToStringStyle.MULTI_LINE_STYLE));
	// assertNotNull(bean);
	// }

	@Test
	public void velocityEngine() {
		VelocityEngine bean = (VelocityEngine) applicationContext.getBean("velocityEngine");
		System.out.println(ToStringBuilder.reflectionToString(bean, ToStringStyle.MULTI_LINE_STYLE));
		assertNotNull(bean);
	}

	/**
	 * for using Spring's FreeMarkerView for web MVC
	 */
	// @Test
	// public void freeMarkerConfigurer() {
	// FreeMarkerConfigurer bean = (FreeMarkerConfigurer)
	// applicationContext.getBean("freeMarkerConfigurer");
	// System.out.println(ToStringBuilder.reflectionToString(bean,
	// ToStringStyle.MULTI_LINE_STYLE));
	// assertNotNull(bean);
	// }

	@Test
	public void freemarkerConfiguration() {
		Configuration bean = (Configuration) applicationContext.getBean("freemarkerConfiguration");
		System.out.println(ToStringBuilder.reflectionToString(bean, ToStringStyle.MULTI_LINE_STYLE));
		assertNotNull(bean);
	}
}
