package org.openyu.commons.thread.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.service.ShutdownCallback;
import org.openyu.commons.service.StartCallback;
import org.openyu.commons.service.ex.ServiceException;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.thread.ThreadService;

/**
 * 執行緒服務
 */
public class ThreadServiceImpl extends BaseServiceSupporter implements ThreadService {

	private static final long serialVersionUID = 5884115787791640094L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ThreadServiceImpl.class);

	private static ThreadServiceImpl instance;

	/**
	 * executor的最大數目
	 */
	private int maxExecutorSize = 1;

	private int corePoolSize = 8;

	private int keepAliveSeconds = 60;

	/**
	 * thread的最大數目
	 */
	private int maxPoolSize = 8;

	private int queueCapacity = 8;

	private transient ThreadPoolTaskExecutor taskExecutors[];

	private transient AtomicInteger counter = new AtomicInteger(0);

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			try {
				//
			} catch (Throwable e) {
				LOGGER.error(new StringBuilder("Exception encountered during Static()").toString(), e);
				throw new ServiceException(e);
			}
		}
	}

	// SchedulingTaskExecutor
	// org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
	// poolTaskExecutor.setQueueCapacity(10000);
	// poolTaskExecutor.setCorePoolSize(5);
	// poolTaskExecutor.setMaxPoolSize(10);
	// poolTaskExecutor.setKeepAliveSeconds(5000);
	// poolTaskExecutor.initialize();

	// <property name="corePoolSize" value="300" />
	// <property name="keepAliveSeconds" value="300" />
	// <property name="maxPoolSize" value="300" />
	// <property name="queueCapacity" value="150" />

	public ThreadServiceImpl(int maxExecutorSize, int corePoolSize, int keepAliveSeconds, int maxPoolSize,
			int queueCapacity) {
		this.maxExecutorSize = maxExecutorSize;
		this.corePoolSize = corePoolSize;
		this.keepAliveSeconds = keepAliveSeconds;
		this.maxPoolSize = maxPoolSize;
		this.queueCapacity = queueCapacity;

		// 2015/09/19 多加callback方式
		addServiceCallback("StartCallbacker", new StartCallbacker());
		addServiceCallback("ShutdownCallbacker", new ShutdownCallbacker());
	}

	public ThreadServiceImpl() {
		this(1, 8, 60, 8, 8);
	}

	public synchronized static ThreadService getInstance() {
		return getInstance(1, 8, 60, 8, 8);
	}

	/**
	 * 單例啟動
	 * 
	 * @return
	 */
	public synchronized static ThreadService getInstance(int maxExecutorSize, int corePoolSize, int keepAliveSeconds,
			int maxPoolSize, int queueCapacity) {
		try {
			if (instance == null) {
				instance = new ThreadServiceImpl(maxExecutorSize, corePoolSize, keepAliveSeconds, maxPoolSize,
						queueCapacity);
				instance.setGetInstance(true);
				// 啟動
				instance.start();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during getInstance()").toString(), e);
			instance = (ThreadServiceImpl) shutdownInstance();
		}
		return instance;
	}

	/**
	 * 單例關閉
	 * 
	 * @return
	 */
	public synchronized static ThreadService shutdownInstance() {
		try {
			if (instance != null) {
				ThreadServiceImpl oldInstance = instance;
				//
				oldInstance.shutdown();
				instance = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownInstance()").toString(), e);
		}
		return instance;
	}

	/**
	 * 單例重啟
	 * 
	 * @return
	 */
	public synchronized static ThreadService restartInstance() {
		try {
			if (instance != null) {
				instance.restart();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartInstance()").toString(), e);
		}
		return instance;
	}

	@Override
	public int getMaxExecutorSize() {
		return maxExecutorSize;
	}

	@Override
	public void setMaxExecutorSize(int maxExecutorSize) {
		this.maxExecutorSize = maxExecutorSize;
	}

	@Override
	public int getCorePoolSize() {
		return corePoolSize;
	}

	@Override
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	@Override
	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	@Override
	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

	@Override
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	@Override
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	@Override
	public int getQueueCapacity() {
		return queueCapacity;
	}

	@Override
	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	@Override
	public Future<?> submit(Callable<?> callable) {
		return nextExecutor().submit(callable);
	}

	@Override
	public Future<?> submit(Runnable runnable) {
		return nextExecutor().submit(runnable);
	}

	/**
	 * 輪循取得下一個ThreadPool
	 * 
	 * @return
	 */
	protected ThreadPoolTaskExecutor nextExecutor() {
		if (Integer.MAX_VALUE == counter.get()) {
			counter.set(0);
		}
		int index = counter.getAndIncrement() % maxExecutorSize;
		return taskExecutors[index];
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {

	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}

	/**
	 * 內部啟動
	 */
	protected class StartCallbacker implements StartCallback {

		@Override
		public void doInAction() throws Exception {
			taskExecutors = new ThreadPoolTaskExecutor[maxExecutorSize];
			//
			int singleCorePoolSize = (int) NumberHelper.divide(corePoolSize, maxExecutorSize);
			int singleMaxPoolSize = (int) NumberHelper.divide(maxPoolSize, maxExecutorSize);
			int singleQueueCapacity = (int) NumberHelper.divide(queueCapacity, maxExecutorSize);
			//
			int lastCorePoolSize = singleCorePoolSize + corePoolSize % maxExecutorSize;
			int lastMaxPoolSize = singleMaxPoolSize + maxPoolSize % maxExecutorSize;
			int lastQueueCapacity = singleQueueCapacity + queueCapacity % maxExecutorSize;
			for (int i = 0; i < taskExecutors.length; ++i) {
				ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
				// when [0-3]
				if (i != taskExecutors.length - 1) {
					executor.setCorePoolSize(singleCorePoolSize);
					executor.setMaxPoolSize(singleMaxPoolSize);
					executor.setQueueCapacity(singleQueueCapacity);

				} // when [4] 最後一個
				else {
					executor.setCorePoolSize(lastCorePoolSize);
					executor.setMaxPoolSize(lastMaxPoolSize);
					executor.setQueueCapacity(lastQueueCapacity);
				}
				//
				executor.setKeepAliveSeconds(keepAliveSeconds);
				executor.initialize();
				//
				taskExecutors[i] = executor;
			}
		}
	}

	/**
	 * 內部關閉
	 */
	protected class ShutdownCallbacker implements ShutdownCallback {

		@Override
		public void doInAction() throws Exception {
			for (int i = 0; i < taskExecutors.length; i++) {
				ThreadPoolTaskExecutor oldExecutor = taskExecutors[i];
				//
				if (oldExecutor != null) {
					oldExecutor.shutdown();
					taskExecutors[i] = null;
				}
			}
		}
	}
}
