package org.openyu.commons.job.supporter;

import java.lang.reflect.Method;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.StopWatch;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.openyu.commons.job.BaseJob;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.mark.Supporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseJobSupporter extends QuartzJobBean implements
		BaseJob, Supporter {

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(BaseJobSupporter.class);

	private boolean cancel;

	private boolean logEnable;

	public BaseJobSupporter() {
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public boolean isLogEnable() {
		return logEnable;
	}

	public void setLogEnable(boolean logEnable) {
		this.logEnable = logEnable;
	}

	protected void executeInternal(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		try {
			execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void log(Logger logger, Class<?> clazz, String methodName) {
		log(logger, clazz, methodName, null);
	}

	// [thread-19] LogService int save(), in 356 mills.
	protected void log(Logger logger, Class<?> clazz, String methodName,
			StopWatch stopWatch) {
		if (logEnable) {
			StringBuilder buff = new StringBuilder();
			// thrad id
			buff.append("T[" + Thread.currentThread().getId() + "]");
			// s
			Method method = ClassHelper.getDeclaredMethod(clazz, methodName);
			if (method != null) {
				buff.append(method.getDeclaringClass().getSimpleName());
				buff.append(" ");
				buff.append(method.getReturnType().getSimpleName());
				buff.append(" ");
				buff.append(method.getName());
				//
				Class<?>[] paramTypes = ClassHelper.getParameterTypesAndCache(
						method.getDeclaringClass(), method);
				if (paramTypes != null) {
					if (paramTypes.length == 0) {
						buff.append("()");
					} else {
						buff.append("(");
						for (int i = 0; i < paramTypes.length; i++) {
							buff.append(paramTypes[i].getSimpleName());
							if (i < paramTypes.length - 1) {
								buff.append(StringHelper.COMMA + " ");
							} else {
								buff.append("),");
							}
						}
					}
				}
			}

			//
			if (stopWatch != null) {
				buff.append(", execute at ");
				buff.append(stopWatch.getTotalTimeMillis());
				buff.append(" mills.");
			}
			//
			logger.info(buff.toString());
		}
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
