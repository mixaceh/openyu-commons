package org.openyu.commons.spring.util;

import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.slf4j.Logger;
import org.springframework.util.StopWatch.TaskInfo;

public class StopWatch extends BaseModelSupporter {

	private static final long serialVersionUID = -3783962490445314831L;

	private boolean enabled;

	private transient Logger logger;

	private org.springframework.util.StopWatch delegate;

	public StopWatch(boolean enabled, Logger logger) {
		this.enabled = enabled;
		if (enabled) {
			this.logger = logger;
			this.delegate = new org.springframework.util.StopWatch();
		}
	}

	public StopWatch(boolean enabled) {
		this(enabled, null);
	}

	public StopWatch() {
		this(true, null);
	}

	public String getId() {
		if (enabled) {
			return delegate.getId();
		}
		return null;
	}

	public void setKeepTaskList(boolean keepTaskList) {
		if (enabled) {
			delegate.setKeepTaskList(keepTaskList);
		}
	}

	public void start() throws IllegalStateException {
		if (enabled) {
			delegate.start();
		}
	}

	public void start(String taskName) throws IllegalStateException {
		if (enabled) {
			delegate.start(taskName);
		}
	}

	public void stop() throws IllegalStateException {
		if (enabled) {
			delegate.stop();
		}
	}

	public boolean isRunning() {
		if (enabled) {
			return delegate.isRunning();
		}
		return false;
	}

	public String currentTaskName() {
		if (enabled) {
			return delegate.currentTaskName();
		}
		return null;
	}

	public long getLastTaskTimeMillis() throws IllegalStateException {
		if (enabled) {
			return delegate.getLastTaskTimeMillis();
		}
		return 0L;
	}

	public String getLastTaskName() throws IllegalStateException {
		if (enabled) {
			return delegate.getLastTaskName();
		}
		return null;
	}

	public TaskInfo getLastTaskInfo() throws IllegalStateException {
		if (enabled) {
			return delegate.getLastTaskInfo();
		}
		return null;
	}

	public long getTotalTimeMillis() {
		if (enabled) {
			return delegate.getTotalTimeMillis();
		}
		return 0L;
	}

	public double getTotalTimeSeconds() {
		return delegate.getTotalTimeSeconds();
	}

	public int getTaskCount() {
		return delegate.getTaskCount();
	}

	public TaskInfo[] getTaskInfo() {
		return delegate.getTaskInfo();
	}

	public String shortSummary() {
		return delegate.shortSummary();
	}

	public String prettyPrint() {
		return delegate.prettyPrint();
	}

	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

}
