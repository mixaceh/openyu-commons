import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.Scheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class ApplicationContextSchedulerTest extends BaseTestSupporter {

	private static ApplicationContext applicationContext;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"applicationContext-scheduler.xml",//
		});
	}

	@Test
	public void scheduler() {
		Scheduler bean = (Scheduler) applicationContext.getBean("scheduler");
		System.out.println(bean);
		assertNotNull(bean);
	}
}
