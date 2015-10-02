import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
//import org.openyu.adm.login.web.struts2.LoginAction;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class ApplicationContextSecurityTest extends BaseTestSupporter {

	private static ApplicationContext applicationContext;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"applicationContext-init.xml",//
				"META-INF/applicationContext-commons-core.xml",//
				"applicationContext-database.xml",//
				"applicationContext-database-log.xml",//
				"org/openyu/adm/authz/user/applicationContext-user.xml",//
				"applicationContext-security.xml",//
		});
	}

//	@Test
//	public void userDetailsService() {
//		UserDetailsService bean = (UserDetailsService) applicationContext
//				.getBean("userDetailsService");
//		System.out.println(bean);
//		assertNotNull(bean);
//	}

	// @Test
	// public void loginAction()
	// {
	// LoginAction bean = (LoginAction)
	// applicationContext.getBean("loginAction");
	// System.out.println(bean);
	// assertNotNull(bean);
	// }

}
