import static org.junit.Assert.assertNotNull;
import java.util.Locale;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class ApplicationContextI18nTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-i18n.xml"//
		});
	}

	@Test
	public void messageSource() {
		ResourceBundleMessageSource bean = (ResourceBundleMessageSource) applicationContext.getBean("messageSource");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void getMessage() {
		System.out.println(applicationContext.getMessage("global.view", null, Locale.TRADITIONAL_CHINESE));
		System.out.println(applicationContext.getMessage("global.view", null, Locale.SIMPLIFIED_CHINESE));
		System.out.println(applicationContext.getMessage("global.view", null, Locale.US));
		System.out.println(applicationContext.getMessage("global.view", null, Locale.getDefault()));
		//
		System.out.println(applicationContext.getMessage("locale.zh_TW", null, Locale.TRADITIONAL_CHINESE));
		System.out.println(applicationContext.getMessage("locale.zh_TW", null, Locale.US));
	}
}
