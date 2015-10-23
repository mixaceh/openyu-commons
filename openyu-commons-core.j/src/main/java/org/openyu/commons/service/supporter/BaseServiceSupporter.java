package org.openyu.commons.service.supporter;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.openyu.commons.lang.event.EventAttach;
import org.openyu.commons.lang.event.EventHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.service.BaseService;
import org.openyu.commons.service.RestartCallback;
import org.openyu.commons.service.ShutdownCallback;
import org.openyu.commons.service.StartCallback;
import org.openyu.commons.service.ServiceCallback;
import org.openyu.commons.service.ex.ServiceException;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.LocaleHelper;

/**
 * 1.service 不要序列化,因有動態代理,反序列化會錯誤
 *
 * 因有aop,有一些其他的class無法排除序列化,如ClassPathXmlApplicationContext@26544ec1
 *
 * 2.靜態資料改由xxxCollector處理
 * 
 * 3.start() -> startCallback -> doStart()
 * 
 * 參考 com.google.common.util.concurrent.AbstractService
 *
 */
public abstract class BaseServiceSupporter extends BaseModelSupporter
		implements BaseService, Supporter, ApplicationContextAware, BeanFactoryAware, InitializingBean, DisposableBean,
		BeanNameAware, ResourceLoaderAware {

	private static final long serialVersionUID = 5282015590204095456L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseServiceSupporter.class);

	protected transient ApplicationContext applicationContext;

	protected transient DefaultListableBeanFactory beanFactory;

	/**
	 * bean名稱,等於id
	 */
	protected String beanName;

	protected transient ResourceLoader resourceLoader;
	/**
	 * start/shutdown/restart lock
	 */
	protected transient final Lock lock = new ReentrantLock();

	/**
	 * 狀態
	 */
	private transient int states;

	/**
	 * 是否正啟動
	 */
	private static final int STARTING = 0x00000001;// 1 << 0 = 1;

	/**
	 * 是否已啟動
	 */
	private static final int STARTED = 0x00000002;// 1 << 1 = 2

	/**
	 * 是否正關閉
	 */
	private static final int SHUTTINGDOWN = 0x00000004;// 1 << 2 = 4

	/**
	 * 是否已關閉
	 */
	private static final int SHUTDOWN = 0x00000008;// 1 << 3 = 8

	/**
	 * 是否正重啟
	 */
	private static final int RESTARTING = 0x00000010;// 1 << 4 = 16

	/**
	 * 是否已重啟
	 * 
	 * shutdown -> start
	 */
	private static final int RESTARTED = 0x00000020;// 1 << 5 = 32

	// 保留: 0x00000040 // 1 << 6 = 64
	// 保留: 0x00000080 // 1 << 7 = 128

	/**
	 * 是否使用getInstance()建構
	 */
	private static final int GET_INSTANCE = 0x00000100;// 1 << 8 = 256

	/**
	 * 是否使用createInstance()建構
	 */
	private static final int CREATE_INSTANCE = 0x00000200;// 1 << 9 = 512

	/**
	 * 顯示名稱
	 * 
	 * BaseServiceSupporter 'baseServiceSupporter'
	 * 
	 * BaseServiceSupporter (getInstance())
	 * 
	 * BaseServiceSupporter (new)
	 * 
	 * BaseServiceSupporter (createInstance())
	 */
	private transient String displayName;

	/**
	 * hack修正
	 */
	private transient static AtomicBoolean hack = new AtomicBoolean(false);

	/**
	 * 啟動callback
	 * 
	 * <className,callback>
	 */
	private transient Map<String, StartCallback> startCallbacks = new LinkedHashMap<String, StartCallback>();

	/**
	 * 關閉callback
	 *
	 * <className,callback>
	 */
	private transient Map<String, ShutdownCallback> shutdownCallbacks = new LinkedHashMap<String, ShutdownCallback>();

	/**
	 * 重啟callback
	 * 
	 * <className,callback>
	 */
	private transient Map<String, RestartCallback> restartCallbacks = new LinkedHashMap<String, RestartCallback>();

	public BaseServiceSupporter() {
	}

	// public ApplicationContext getApplicationContext() {
	// return applicationContext;
	// }

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	// public BeanFactory getBeanFactory() {
	// return beanFactory;
	// }

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
		// #fix circular reference
		if (!hack.get()) {
			this.beanFactory.setAllowRawInjectionDespiteWrapping(true);
			hack.set(true);
		}
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	// --------------------------------------------------
	// ServiceLifecycle states
	// --------------------------------------------------
	/**
	 * 取得狀態
	 * 
	 * @return
	 */
	protected final int getStates() {
		return states;
	}

	/**
	 * 設定狀態
	 * 
	 * @param states
	 */
	protected final void setStates(int states) {
		this.states = states;
	}

	/**
	 * 加入一項或多項狀態
	 * 
	 * @param state
	 */
	protected final void addStates(int states) {
		this.states |= states;
	}

	/**
	 * 移除一項或多項狀態
	 * 
	 * @param state
	 */
	protected final void removeStates(int states) {
		this.states &= ~states;
	}

	/**
	 * 是否有一項或多項狀態
	 * 
	 * @param state
	 * @return
	 */
	protected final boolean isStates(int states) {
		return (this.states & states) != 0;
	}

	public final String toStates() {
		StringBuffer buff = new StringBuffer();
		int len;

		if ((states & STARTING) != 0)
			buff.append("starting ");
		if ((states & STARTED) != 0)
			buff.append("started ");

		if ((states & SHUTTINGDOWN) != 0)
			buff.append("shuttingdown ");
		if ((states & SHUTDOWN) != 0)
			buff.append("shutdown ");

		if ((states & RESTARTING) != 0)
			buff.append("restarting ");
		if ((states & RESTARTED) != 0)
			buff.append("restarted ");

		if ((states & GET_INSTANCE) != 0)
			buff.append("getInstance ");
		if ((states & CREATE_INSTANCE) != 0)
			buff.append("createInstance ");

		if ((len = buff.length()) > 0)
			return buff.substring(0, len - 1);
		return "";
	}

	public final void setGetInstance(boolean getInstance) {
		if (getInstance) {
			addStates(GET_INSTANCE);
		} else {
			removeStates(GET_INSTANCE);
		}
	}

	public final void setCreateInstance(boolean createInstance) {
		if (createInstance) {
			addStates(CREATE_INSTANCE);
		} else {
			removeStates(CREATE_INSTANCE);
		}
	}

	/**
	 * 取得國際化訊息
	 * 
	 * @param key
	 * @param locale
	 * @return
	 */
	protected String getMessage(String key, Locale locale) {
		return getMessage(key, null, locale);
	}

	/**
	 * 取得國際化訊息
	 * 
	 * @param key
	 * @param params
	 * @param locale
	 * @return
	 */
	protected String getMessage(String key, Object[] params, Locale locale) {
		if (locale == null) {
			return this.applicationContext.getMessage(key, params, LocaleHelper.getLocale());
		}
		return this.applicationContext.getMessage(key, params, locale);
	}

	/**
	 * 加入callbacks
	 * 
	 * @param classNames
	 * @param actions
	 * @return
	 */
	public final boolean addServiceCallback(String[] classNames, ServiceCallback... actions) {
		AssertHelper.notNull(actions, new StringBuilder().append("The ClassNames must not be null").toString());
		AssertHelper.notNull(actions, new StringBuilder().append("The ServiceCallbacks must not be null").toString());
		//
		boolean result = false;

		// for (ServiceCallback action : actions) {
		// result &= addServiceCallback(action);
		// }

		int length = classNames.length;
		for (int i = 0; i < length; i++) {
			ServiceCallback buff = addServiceCallback(classNames[i], actions[i]);
			result &= (buff != null);
		}
		return result;
	}

	/**
	 * 加入callback
	 * 
	 * @param className
	 * @param action
	 * @return
	 */
	public final ServiceCallback addServiceCallback(String className, ServiceCallback action) {
		AssertHelper.notNull(action, new StringBuilder().append("The ServiceCallback must not be null").toString());
		//
		try {
			this.lock.lockInterruptibly();
			try {
				if (action instanceof StartCallback) {
					return startCallbacks.put(className, (StartCallback) action);
				} else if (action instanceof ShutdownCallback) {
					return shutdownCallbacks.put(className, (ShutdownCallback) action);
				} else if (action instanceof RestartCallback) {
					return restartCallbacks.put(className, (RestartCallback) action);
				} else {
					throw new ServiceException("Can not add " + className + ", ServiceCallback");
				}
			} catch (ServiceException e) {
				LOGGER.error(new StringBuilder("Exception encountered during addServiceCallback()").toString(), e);
				throw e;
			} catch (Throwable e) {
				LOGGER.error(new StringBuilder("Exception encountered during addServiceCallback()").toString(), e);
				throw new ServiceException(e);
			} finally {
				this.lock.unlock();
			}
		} catch (InterruptedException e) {
			LOGGER.error(new StringBuilder("Exception encountered during addServiceCallback()").toString(), e);
			throw new ServiceException(e);
		}
	}

	/**
	 * 移除callback
	 * 
	 * @param action
	 * @return
	 */
	public final ServiceCallback removeServiceCallback(String className) {
		AssertHelper.notNull(className, new StringBuilder().append("The ClassName must not be null").toString());
		//
		try {
			this.lock.lockInterruptibly();
			try {
				if (startCallbacks.containsKey(className)) {
					return startCallbacks.remove(className);
				} else if (shutdownCallbacks.containsKey(className)) {
					return shutdownCallbacks.remove(className);
				} else if (restartCallbacks.containsKey(className)) {
					return restartCallbacks.remove(className);
				} else {
					throw new ServiceException("Can not remove " + className + ", ServiceCallback");
				}
			} catch (ServiceException e) {
				LOGGER.error(new StringBuilder("Exception encountered during removeServiceCallback()").toString(), e);
				throw e;
			} catch (Throwable e) {
				LOGGER.error(new StringBuilder("Exception encountered during removeServiceCallback()").toString(), e);
				throw new ServiceException(e);
			} finally {
				this.lock.unlock();
			}
		} catch (InterruptedException e) {
			LOGGER.error(new StringBuilder("Exception encountered during addServiceCallback()").toString(), e);
			throw new ServiceException(e);
		}
	}

	/**
	 * 取得顯示名稱
	 * 
	 * @return
	 */
	protected String getDisplayName() {
		if (displayName == null) {
			StringBuilder buff = new StringBuilder();
			buff.append(getClass().getSimpleName());
			if (this.beanName != null) {
				buff.append(" '");
				buff.append(this.beanName);
				buff.append("'");
			} else {
				if (isStates(GET_INSTANCE)) {
					buff.append(" (getInstance())");
				} else {
					if (isStates(CREATE_INSTANCE)) {
						buff.append(" (createInstance())");
					} else {
						buff.append(" (new)");
					}
				}
			}
			//
			buff.append(" @" + Integer.toHexString(hashCode()));
			displayName = buff.toString();
		}
		return displayName;
	}

	public final void afterPropertiesSet() throws Exception {
		start();
	}

	/**
	 * 啟動
	 */
	public final void start() throws Exception {
		try {
			this.lock.lockInterruptibly();
			try {
				if (isStates(STARTING)) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" is starting").toString());
				}
				//
				if (isStates(STARTED)) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" was already started").toString());
				}
				//
				// this.starting = true;
				addStates(STARTING);
				LOGGER.info(new StringBuilder().append("Starting ").append(getDisplayName()).toString());
				// --------------------------------------------------
				// 2015/09/19 多加callback方式
				for (StartCallback action : startCallbacks.values()) {
					doStartCallback(action);
				}
				// for implement
				doStart();
				// --------------------------------------------------
				// this.starting = false;
				// this.started = true;
				removeStates(STARTING);
				addStates(STARTED);
				//
				// this.shutdown = false;
				// this.restarted = false;
				removeStates(SHUTDOWN);
				removeStates(RESTARTED);
				// Caused by: java.lang.ArithmeticException: / by zero
				// int d=1/0;
				//
				// Caused by: java.lang.OutOfMemoryError: Java heap space
				// Map<Integer, String> value = new HashMap<Integer, String>();
				// for (int i = 0; i < 10000000; i++) {
				// value.put(new Integer(i), new String("Object_" + i));
				// }
			} catch (ServiceException e) {
				LOGGER.error(new StringBuilder("Exception encountered during start()").toString(), e);
				throw e;
			} catch (Throwable e) {
				LOGGER.error(new StringBuilder("Exception encountered during start()").toString(), e);
				throw new ServiceException(e);
			} finally {
				this.lock.unlock();
			}
		} catch (InterruptedException e) {
			LOGGER.error(new StringBuilder("Exception encountered during start()").toString(), e);
			throw new ServiceException(e);
		}
	}

	/**
	 * 內部啟動
	 */
	protected final void doStartCallback(StartCallback action) throws Exception {
		AssertHelper.notNull(action, new StringBuilder().append("The StartCallback must not be null").toString());
		//
		try {
			action.doInAction();
		} catch (Throwable e) {
			throw e;
		} finally {
			// just for pretty
		}
	}

	/**
	 * 檢查是否有啟動
	 */
	public synchronized void checkStarted() {
		if (!isStates(STARTED)) {
			throw new IllegalStateException(
					new StringBuilder().append(getDisplayName()).append(" not start. Call start()").toString());
		}
	}

	/**
	 * 內部啟動
	 */
	protected abstract void doStart() throws Exception;

	public final void destroy() throws Exception {
		shutdown();
	}

	/**
	 * 關閉
	 */
	public final void shutdown() throws Exception {
		try {
			this.lock.lockInterruptibly();
			try {
				if (isStates(SHUTTINGDOWN)) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" is shuttingdown").toString());
				}
				//
				if (isStates(SHUTDOWN)) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" was already shutdown").toString());
				}
				//
				// this.shuttingdown = true;
				addStates(SHUTTINGDOWN);
				LOGGER.info(new StringBuilder().append("Shutting down ").append(getDisplayName()).toString());
				// --------------------------------------------------
				// 2015/09/19 多加callback方式
				for (ShutdownCallback action : shutdownCallbacks.values()) {
					doShutdownCallback(action);
				}
				// for implement
				doShutdown();
				// --------------------------------------------------
				// 移除callback
				this.startCallbacks.clear();
				this.shutdownCallbacks.clear();
				this.restartCallbacks.clear();
				// this.shuttingdown = false;
				// this.shutdown = true;
				removeStates(SHUTTINGDOWN);
				addStates(SHUTDOWN);
				//
				// this.started = false;
				removeStates(STARTED);
			} catch (ServiceException e) {
				LOGGER.error(new StringBuilder("Exception encountered during shutdown()").toString(), e);
				throw e;
			} catch (Throwable e) {
				LOGGER.error(new StringBuilder("Exception encountered during shutdown()").toString(), e);
				throw new ServiceException(e);
			} finally {
				this.lock.unlock();
			}
		} catch (InterruptedException e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdown()").toString(), e);
			throw new ServiceException(e);
		}
	}

	/**
	 * 內部關閉
	 */
	protected abstract void doShutdown() throws Exception;

	/**
	 * 內部關閉
	 */
	protected final void doShutdownCallback(ShutdownCallback action) throws Exception {
		AssertHelper.notNull(action, new StringBuilder().append("The ShutdownCallback must not be null").toString());
		//
		try {
			action.doInAction();
		} catch (Throwable e) {
			throw e;
		} finally {
			// just for pretty
		}
	}

	/**
	 * 重啟
	 * 
	 * shutdown -> start
	 */
	public final void restart() throws Exception {
		try {
			this.lock.lockInterruptibly();
			try {
				if (isStates(RESTARTING)) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" is restarting").toString());
				}
				//
				if (isStates(RESTARTED)) {
					throw new IllegalStateException(
							new StringBuilder().append(getDisplayName()).append(" was already restarted").toString());
				}
				// 檢查是否有啟動
				checkStarted();
				//
				// this.restarting = true;
				addStates(RESTARTING);
				LOGGER.info(new StringBuilder().append("Restarting ").append(getDisplayName()).toString());
				// --------------------------------------------------
				shutdown();
				start();
				// 2015/09/23 多加callback方式
				for (RestartCallback action : restartCallbacks.values()) {
					doRestartCallback(action);
				}
				// 2015/09/22 不宣告為抽象方法
				doRestart();
				// --------------------------------------------------
				// this.restarting = false;
				removeStates(RESTARTING);
			} catch (ServiceException e) {
				LOGGER.error(new StringBuilder("Exception encountered during restart()").toString(), e);
				throw e;
			} catch (Throwable e) {
				LOGGER.error(new StringBuilder("Exception encountered during restart()").toString(), e);
				throw new ServiceException(e);
			} finally {
				this.lock.unlock();
			}
		} catch (InterruptedException e) {
			LOGGER.error(new StringBuilder("Exception encountered during restart()").toString(), e);
			throw new ServiceException(e);
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
	 * 內部重啟
	 */
	protected final void doRestartCallback(RestartCallback action) throws Exception {
		AssertHelper.notNull(action, new StringBuilder().append("The RestartCallback must not be null").toString());
		//
		try {
			action.doInAction();
		} catch (Throwable e) {
			throw e;
		} finally {
			// just for pretty
		}
	}

	// ----------------------------------------------------------------
	// 只是為了簡化寫法
	// ----------------------------------------------------------------

	/**
	 * 事件附件
	 *
	 * @param oldValue
	 * @param newValue
	 * @return EventAttach
	 */
	protected <OLD_VALUE, NEW_VALUE> EventAttach<OLD_VALUE, NEW_VALUE> eventAttach(OLD_VALUE oldValue,
			NEW_VALUE newValue) {
		return EventHelper.eventAttach(oldValue, newValue);
	}
}
