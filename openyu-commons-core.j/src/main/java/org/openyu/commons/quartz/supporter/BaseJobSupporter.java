package org.openyu.commons.quartz.supporter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.quartz.BaseJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseJobSupporter extends QuartzJobBean implements BaseJob, Supporter {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseJobSupporter.class);

	private transient String displayName;

	public BaseJobSupporter() {
	}

	protected String getDisplayName() {
		if (displayName == null) {
			StringBuilder buff = new StringBuilder();
			buff.append(ClassHelper.getSimpleName(getClass()));
			buff.append(" @" + Integer.toHexString(hashCode()));
			displayName = buff.toString();
		}
		return displayName;
	}

	@Override
	protected final void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			LOGGER.info(new StringBuilder().append("T[" + Thread.currentThread().getId() + "] ").append("Executing ")
					.append(getDisplayName()).toString());
			// --------------------------------------------------
			doExecute(jobExecutionContext);
			// --------------------------------------------------
		} catch (JobExecutionException e) {
			LOGGER.error(new StringBuilder("Exception encountered during executeInternal()").toString(), e);
			// throw e;
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during executeInternal()").toString(), e);
			// throw new JobExecutionException(e);
		}
	}

	/**
	 * 內部執行
	 */
	protected abstract void doExecute(JobExecutionContext jobExecutionContext) throws Exception;

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
