package org.openyu.commons.quartz;

import java.text.ParseException;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

/**
 * Needed to set Quartz useProperties=true when using Spring classes, because
 * Spring sets an object reference on JobDataMap that is not a String
 * 
 * @see http://site.trimplement.com/using-spring-and-quartz-with-jobstore-
 *      properties/
 * @see http://forum.springsource.org/showthread.php?130984-Quartz-error-
 *      IOException
 */
public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean {

	public PersistableCronTriggerFactoryBean() {

	}

	@Override
	public void afterPropertiesSet() throws ParseException {
		super.afterPropertiesSet();

		// Remove the JobDetail element
		getJobDataMap().remove("jobDetail");
	}
}
