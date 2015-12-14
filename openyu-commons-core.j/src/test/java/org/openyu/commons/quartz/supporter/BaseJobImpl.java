package org.openyu.commons.quartz.supporter;

import org.openyu.commons.quartz.supporter.BaseJobSupporter;
import org.quartz.JobExecutionContext;

public class BaseJobImpl extends BaseJobSupporter {

	public BaseJobImpl() {

	}

	@Override
	protected void doExecute(JobExecutionContext jobExecutionContext) throws Exception {
		System.out.println("123");
	}
}
