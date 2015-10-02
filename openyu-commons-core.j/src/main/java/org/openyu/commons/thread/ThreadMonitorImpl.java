package org.openyu.commons.thread;

public class ThreadMonitorImpl implements ThreadMonitor, Runnable {

	private Monitorable monitorable;

	private long period = 1000; // 預設一秒間隔

	private boolean enabled;

	private Thread thread;

	public ThreadMonitorImpl(Monitorable monitorable) {
		this.monitorable = monitorable;
	}

	/**
	 * 設定可開關thread,用notify,注意 synchronized 的鎖
	 * 
	 * @param enabled
	 *            boolean
	 */
	public synchronized void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled && thread == null) {
			thread = new Thread(this);
			thread.setDaemon(true); // true=監督線程,false=用戶線程(預設)
			thread.start();
		}
		//
		if (enabled) {
			notify();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public long getPeriod() {
		return period;
	}

	public void run() {
		try {
			while (true) {
				// -----------------------
				synchronized (this) {
					while (!enabled) {
						wait();
					}
				}
				// -----------------------
				// 判斷是否還存在,否則就退出
				if (monitorable != null) {
					monitorable.monitor();
					// monitorable=null;
					// System.out.println(monitorable != null);
				} else {
					System.exit(1);
				}
				if (period != 0) {
					Thread.sleep(period);
				}
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
