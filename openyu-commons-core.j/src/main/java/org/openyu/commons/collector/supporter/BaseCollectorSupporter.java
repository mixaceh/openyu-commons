package org.openyu.commons.collector.supporter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.openyu.commons.collector.BaseCollector;
import org.openyu.commons.collector.ex.CollectorException;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 收集器
 * 
 * 1.處理靜態資料,當有setter/getter才會export成xml
 * 
 * 2.不放在spring上初始,直接使用getInstance(),內含啟動start()
 * 
 * 3.定為final類別,不要去繼承其他collector
 */
@XmlRootElement(name = "baseCollector")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseCollectorSupporter extends BaseModelSupporter
		implements BaseCollector {

	private static final long serialVersionUID = -5464492958179282519L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(BaseCollectorSupporter.class);

	private String id;

	/**
	 * 是否正啟動
	 */
	private transient boolean starting;

	/**
	 * 是否已啟動
	 */
	private transient boolean started;

	/**
	 * 是否正關閉
	 */
	private transient boolean shuttingdown;

	/**
	 * 是否已關閉
	 */
	private transient boolean shutdown;

	/**
	 * 是否正重啟
	 */
	private transient boolean restarting;
	/**
	 * 是否已重啟
	 * 
	 * shutdown -> start
	 */
	private transient boolean restarted;

	/**
	 * 顯示名稱
	 * 
	 * BaseCollectorSupporter
	 */
	private transient String displayName;

	public BaseCollectorSupporter() {
		id = BaseCollectorSupporter.class.getName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	protected final String getDisplayName() {
		if (displayName == null) {
			StringBuilder buff = new StringBuilder();
			buff.append(getClass().getSimpleName());
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
			if (this.starting) {
				throw new IllegalStateException(new StringBuilder()
						.append(getDisplayName()).append(" is starting")
						.toString());
			}
			//
			if (this.started) {
				throw new IllegalStateException(new StringBuilder()
						.append(getDisplayName())
						.append(" was already started").toString());
			}
			//
			this.starting = true;
			LOGGER.info(new StringBuilder().append("Starting ")
					.append(getDisplayName()).toString());
			// --------------------------------------------------
			doStart();
			// --------------------------------------------------
			this.starting = false;
			this.started = true;
			//
			this.shutdown = false;
			this.restarted = false;
		} catch (CollectorException e) {
			LOGGER.error(new StringBuilder(
					"Exception encountered during start()").toString(), e);
			throw e;
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder(
					"Exception encountered during start()").toString(), e);
			throw new CollectorException(e);
		}
	}

	/**
	 * 檢查是否有啟動
	 */
	protected void checkStarted() {
		if (!this.started) {
			throw new IllegalStateException(new StringBuilder()
					.append(getDisplayName())
					.append(" not start. Call start()").toString());
		}
	}

	/**
	 * 內部啟動
	 */
	protected abstract void doStart() throws Exception;

	/**
	 * 關閉
	 */
	public synchronized void shutdown() {
		try {
			if (this.shuttingdown) {
				throw new IllegalStateException(new StringBuilder()
						.append(getDisplayName()).append("  is shuttingdown")
						.toString());
			}
			//
			if (this.shutdown) {
				throw new IllegalStateException(new StringBuilder()
						.append(getDisplayName())
						.append(" was already shutdown").toString());
			}
			//
			this.shuttingdown = true;
			LOGGER.info(new StringBuilder().append("Shutting down ")
					.append(getDisplayName()).toString());
			// --------------------------------------------------
			doShutdown();
			// --------------------------------------------------
			this.shuttingdown = false;
			this.shutdown = true;
			//
			this.started = false;
		} catch (CollectorException e) {
			LOGGER.error(new StringBuilder(
					"Exception encountered during shutdown()").toString(), e);
			throw e;
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder(
					"Exception encountered during shutdown()").toString(), e);
			throw new CollectorException(e);
		}
	}

	/**
	 * 內部關閉
	 */
	protected abstract void doShutdown() throws Exception;

	/**
	 * 重啟
	 * 
	 * shutdown -> start
	 */
	public synchronized void restart() {
		try {
			if (this.restarting) {
				throw new IllegalStateException(new StringBuilder()
						.append(getDisplayName()).append(" is restartin")
						.toString());
			}
			//
			if (this.restarted) {
				throw new IllegalStateException(new StringBuilder()
						.append(getDisplayName())
						.append(" was already restarted").toString());
			}
			// 檢查是否有啟動
			checkStarted();
			//
			this.restarting = true;
			LOGGER.info(new StringBuilder().append("Restarting ")
					.append(getDisplayName()).toString());
			// --------------------------------------------------
			shutdown();
			start();
			// --------------------------------------------------
			// 2015/09/22 不宣告為抽象方法
			doRestart();
			// --------------------------------------------------
			this.restarting = false;
		} catch (CollectorException e) {
			LOGGER.error(new StringBuilder(
					"Exception encountered during restart()").toString(), e);
			throw e;
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder(
					"Exception encountered during restart()").toString(), e);
			throw new CollectorException(e);
		}
	}

	/**
	 * 內部重啟
	 *
	 * 不宣告為抽象方法
	 */
	protected void doRestart() throws Exception {

	}
}
