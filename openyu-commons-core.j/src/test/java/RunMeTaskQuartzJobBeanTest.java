import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.thread.ThreadHelper;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunMeTaskQuartzJobBeanTest {

	private static ApplicationContext applicationContext;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext-schedule-runmetask.xml",//
				});
	}

	@Test
	public void runMeTaskQuartzJobn() {
		JobDetail bean = (JobDetail) applicationContext.getBean("runMeTaskQuartzJob");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void runMeTaskQuartzJobTrigger() {
		CronTrigger bean = (CronTrigger) applicationContext
				.getBean("runMeTaskQuartzJobTrigger");
		System.out.println(bean);
		assertNotNull(bean);
	}
	
	@Test
	public void runMeTaskMethodInvokingJob() {
		JobDetail bean = (JobDetail) applicationContext.getBean("runMeTaskMethodInvokingJob");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void runMeTaskMethodInvokingJobTrigger() {
		CronTrigger bean = (CronTrigger) applicationContext
				.getBean("runMeTaskMethodInvokingJobTrigger");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void scheduler() {
		Scheduler bean = (Scheduler) applicationContext.getBean("scheduler");
		System.out.println(bean);
		assertNotNull(bean);
	}
}
