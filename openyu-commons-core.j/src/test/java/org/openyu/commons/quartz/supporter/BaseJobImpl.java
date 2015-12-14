package org.openyu.commons.quartz.supporter;

import org.openyu.commons.quartz.supporter.BaseJobSupporter;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BaseJobImpl extends BaseJobSupporter {

	public BaseJobImpl() {

	}

	@Override
	protected void doExecute(JobExecutionContext jobExecutionContext) throws Exception {
		Scheduler scheduler = jobExecutionContext.getScheduler();
		System.out.println(scheduler.getSchedulerName() + ", " + scheduler.getSchedulerInstanceId());
	}
}
