import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class ApplicationContextInitTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
		});
	}

	@Test
	public void applicationContext() {
		System.out.println(applicationContext);
		assertNotNull(applicationContext);
	}

}
