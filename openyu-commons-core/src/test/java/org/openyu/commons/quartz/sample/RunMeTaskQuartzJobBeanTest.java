package org.openyu.commons.quartz.sample;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
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
				new String[] { "org/openyu/commons/quartz/sample/testContext-quartz-sample.xml",//
		});
	}

	@Test
	public void runMeTaskQuartzJobDetail() {
		JobDetail bean = (JobDetail) applicationContext.getBean("runMeTaskQuartzJobDetail");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void runMeTaskQuartzJobTrigger() {
		CronTrigger bean = (CronTrigger) applicationContext.getBean("runMeTaskQuartzJobTrigger");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void runMeTaskMethodInvokingJobDetail() {
		JobDetail bean = (JobDetail) applicationContext.getBean("runMeTaskMethodInvokingJobDetail");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void runMeTaskMethodInvokingJobTrigger() {
		CronTrigger bean = (CronTrigger) applicationContext.getBean("runMeTaskMethodInvokingJobTrigger");
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
