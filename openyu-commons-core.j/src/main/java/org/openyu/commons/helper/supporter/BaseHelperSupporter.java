package org.openyu.commons.helper.supporter;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.mark.Supporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 輔助器,以下1種建構方式
 * 
 * 1.有的無法用 spring 配置建構
 * 
 * 2.getInstance() 單例建構
 * 
 * 3.無法用 createInstance() 建構
 */
public abstract class BaseHelperSupporter implements Supporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseHelperSupporter.class);

	/**
	 * 是否正啟動
	 */
	private transient AtomicBoolean starting = new AtomicBoolean(false);

	/**
	 * 是否已啟動
	 */
	private transient AtomicBoolean started = new AtomicBoolean(false);
	/**
	 * 是否正關閉
	 */
	private transient AtomicBoolean shuttingdown = new AtomicBoolean(false);

	/**
	 * 是否已關閉
	 */
	private transient AtomicBoolean shutdown = new AtomicBoolean(false);

	/**
	 * 是否正重啟
	 */
	private transient AtomicBoolean restarting = new AtomicBoolean(false);

	/**
	 * 是否已重啟
	 * 
	 * shutdown -> start
	 */
	private transient AtomicBoolean restarted = new AtomicBoolean(false);

	/**
	 * 是否使用getInstance()建構
	 */
	private transient AtomicBoolean getInstance = new AtomicBoolean(false);

	/**
	 * bean id
	 */
	private String beanId;

	/**
	 * 顯示名稱
	 * 
	 * BaseHelperSupporter 'baseHelperSupporter'
	 * 
	 * BaseHelperSupporter (getInstance())
	 */
	private transient String displayName;

	/**
	 * Instantiates a new base helper supporter.
	 */
	public BaseHelperSupporter() {
	}

	public String getBeanId() {
		return getBeanName();
	}

	public String getBeanName() {
		return beanId;
	}

	public void setBeanName(String beanName) {
		this.beanId = beanName;
	}

	protected final boolean isStarting() {
		return starting.get();
	}

	protected final boolean isStarted() {
		return started.get();
	}

	protected final boolean isShuttingdown() {
		return shuttingdown.get();
	}

	protected final boolean isShutdown() {
		return shutdown.get();
	}

	protected final boolean isRestarting() {
		return restarting.get();
	}

	protected final boolean isRestarted() {
		return restarted.get();
	}

	protected final boolean isGetInstance() {
		return getInstance.get();
	}

	protected final void setGetInstance(boolean getInstance) {
		this.getInstance.set(getInstance);
	}

	protected String getDisplayName() {
		if (displayName == null) {
			StringBuilder buff = new StringBuilder();
			buff.append(getClass().getSimpleName());
			if (this.beanId != null) {
				buff.append(" '");
				buff.append(this.beanId);
				buff.append("'");
			} else {
				if (isGetInstance()) {
					buff.append(" (getInstance())");
				} else {
					// just for pretty
				}
			}
			//
			displayName = buff.toString();
		}
		return displayName;
	}

	/**
	 * 啟動
	 */
	public synchronized void start() {
		try {
			if (isStarting()) {
				throw new IllegalStateException(
						new StringBuilder().append(getDisplayName()).append(" is starting").toString());
			}
			//
			if (isStarted()) {
				throw new IllegalStateException(
						new StringBuilder().append(getDisplayName()).append(" was already started").toString());
			}
			//
			this.starting.set(true);
			LOGGER.info(new StringBuilder().append("Starting ").append(getDisplayName()).toString());
			// --------------------------------------------------
			doStart();
			// --------------------------------------------------
			this.starting.set(false);
			this.started.set(true);
			//
			this.shutdown.set(false);
			this.restarted.set(false);
		} catch (HelperException e) {
			LOGGER.error(new StringBuilder("Exception encountered during start()").toString(), e);
			throw e;
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder("Exception encountered during start()").toString(), e);
			throw new HelperException(e);
		}
	}

	/**
	 * 檢查是否有啟動
	 */
	protected void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(
					new StringBuilder().append(getDisplayName()).append(" not start. Call start()").toString());
		}
	}

	/**
	 * 內部啟動
	 */
	protected void doStart() throws Exception {

	}

	/**
	 * 關閉
	 */
	public synchronized void shutdown() {
		try {
			if (isShuttingdown()) {
				throw new IllegalStateException(
						new StringBuilder().append(getDisplayName()).append("  is shuttingdown").toString());
			}
			//
			if (isShutdown()) {
				throw new IllegalStateException(
						new StringBuilder().append(getDisplayName()).append(" was already shutdown").toString());
			}
			//
			this.shuttingdown.set(true);
			LOGGER.info(new StringBuilder().append("Shutting down ").append(getDisplayName()).toString());
			// --------------------------------------------------
			doShutdown();
			// --------------------------------------------------
			this.shuttingdown.set(false);
			this.shutdown.set(true);
			//
			this.started.set(false);
		} catch (HelperException e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdown()").toString(), e);
			throw e;
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdown()").toString(), e);
			throw new HelperException(e);
		}
	}

	/**
	 * 內部關閉
	 */
	protected void doShutdown() throws Exception {

	}

	/**
	 * 重啟
	 * 
	 * shutdown -> start
	 */
	public synchronized void restart() {
		try {
			if (isRestarting()) {
				throw new IllegalStateException(
						new StringBuilder().append(getDisplayName()).append(" is restarting").toString());
			}
			//
			if (isRestarted()) {
				throw new IllegalStateException(
						new StringBuilder().append(getDisplayName()).append(" was already restarted").toString());
			}
			// 檢查是否有啟動
			checkStarted();
			//
			this.restarting.set(true);
			LOGGER.info(new StringBuilder().append("Restarting ").append(getDisplayName()).toString());
			// --------------------------------------------------
			shutdown();
			start();
			// --------------------------------------------------
			// 2015/09/22 不宣告為抽象方法
			doRestart();
			// --------------------------------------------------
			this.restarting.set(false);
		} catch (HelperException e) {
			LOGGER.error(new StringBuilder("Exception encountered during restart()").toString(), e);
			throw e;
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder("Exception encountered during restart()").toString(), e);
			throw new HelperException(e);
		}
	}

	/**
	 * 內部重啟
	 *
	 * 不宣告為抽象方法
	 */
	protected void doRestart() throws Exception {

	}

	/**
	 * To string.
	 *
	 * @return String
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
