package org.openyu.commons.spring.util;

import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.springframework.util.StopWatch.TaskInfo;

public class StopWatch extends BaseModelSupporter {

	private static final long serialVersionUID = -3783962490445314831L;

	private boolean enabled;

	private org.springframework.util.StopWatch delegate;

	public StopWatch(String id, boolean enabled) {
		this.enabled = enabled;
		if (this.enabled) {
			this.delegate = new org.springframework.util.StopWatch(id);
		}
	}

	public StopWatch() {
		this("", true);
	}

	public StopWatch(String id) {
		this(id, true);
	}

	public StopWatch(boolean enabled) {
		this("", enabled);
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
		if (enabled) {
			return delegate.getTotalTimeSeconds();
		}
		return 0d;
	}

	public int getTaskCount() {
		if (enabled) {
			return delegate.getTaskCount();
		}
		return 0;
	}

	public TaskInfo[] getTaskInfo() {
		if (enabled) {
			return delegate.getTaskInfo();
		}
		return null;
	}

	public String shortSummary() {
		if (enabled) {
			return delegate.shortSummary();
		}
		return null;
	}

	public String prettyPrint() {
		if (enabled) {
			return delegate.prettyPrint();
		}
		return null;
	}

	public void printResult() {
		if (enabled) {
			System.out.println(prettyPrint());
		}
	}

	public boolean equals(Object obj) {
		if (enabled) {
			return delegate.equals(obj);
		}
		return false;
	}

	public int hashCode() {
		if (enabled) {
			return delegate.hashCode();
		}
		return 0;
	}

	public String toString() {
		if (enabled) {
			return delegate.toString();
		}
		return this.toString();
	}

	// 無法抓到原始行數
	// public void printResult() {
	// if (enabled) {
	// logger.info(prettyPrint());
	// }
	// }

}
