package org.openyu.commons.thrift.supporter;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.openyu.commons.thrift.TTransportFactory;
import org.openyu.commons.thrift.ThriftDataSource;
import org.openyu.commons.thrift.impl.PoolingThriftDataSource;
import org.openyu.commons.thrift.impl.TFramedTTransportFactoryImpl;
import org.openyu.commons.thrift.impl.TSocketTTransportFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.nio.NioHelper;

public abstract class ThriftDataSourceSupporter extends BaseServiceSupporter
		implements ThriftDataSource {

	private static final long serialVersionUID = 3731245735319377625L;

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ThriftDataSourceSupporter.class);

	protected String ip;

	protected int port;

	protected int timeout;

	protected boolean nonblocking;

	protected boolean compactProtocol;

	/**
	 * 重試次數
	 */
	protected int retryNumber = NioHelper.DEFAULT_RETRY_NUMBER;

	/**
	 * 重試暫停毫秒
	 */
	protected long retryPauseMills = NioHelper.DEFAULT_RETRY_PAUSE_MILLS;

	protected int initialSize = 1;

	//
	protected volatile boolean restartNeeded;

	protected GenericObjectPool.Config config = new GenericObjectPool.Config();
	//
	protected volatile ThriftDataSource instance;

	protected GenericObjectPool<TTransport> objectPool;

	protected boolean closed;

	protected String dataSourceType = "ThriftDataSource";

	public ThriftDataSourceSupporter() {
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
		close();
	}

	public synchronized String getIp() {
		return ip;
	}

	public synchronized void setIp(String ip) {
		this.ip = ip;
	}

	public synchronized int getPort() {
		return port;
	}

	public synchronized void setPort(int port) {
		this.port = port;
	}

	public synchronized int getTimeout() {
		return timeout;
	}

	public synchronized void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public synchronized boolean isNonblocking() {
		return nonblocking;
	}

	public synchronized void setNonblocking(boolean nonblocking) {
		this.nonblocking = nonblocking;
	}

	public synchronized boolean isCompactProtocol() {
		return compactProtocol;
	}

	public synchronized void setCompactProtocol(boolean compactProtocol) {
		this.compactProtocol = compactProtocol;
	}

	public synchronized int getRetryNumber() {
		return retryNumber;
	}

	public synchronized void setRetryNumber(int retryNumber) {
		this.retryNumber = retryNumber;
	}

	public synchronized long getRetryPauseMills() {
		return retryPauseMills;
	}

	public synchronized void setRetryPauseMills(long retryPauseMills) {
		this.retryPauseMills = retryPauseMills;
	}

	// GenericObjectPool.Config
	public synchronized int getMaxActive() {
		return config.maxActive;
	}

	public synchronized void setMaxActive(int maxActive) {
		this.config.maxActive = maxActive;
	}

	public synchronized int getMaxIdle() {
		return config.maxIdle;
	}

	public synchronized void setMaxIdle(int maxIdle) {
		this.config.maxIdle = maxIdle;
	}

	public synchronized int getMinIdle() {
		return config.minIdle;
	}

	public synchronized void setMinIdle(int minIdle) {
		this.config.minIdle = minIdle;
	}

	public synchronized int getInitialSize() {
		return this.initialSize;
	}

	public synchronized void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
		this.restartNeeded = true;
	}

	public synchronized long getMaxWait() {
		return config.maxWait;
	}

	public synchronized void setMaxWait(long maxWait) {
		this.config.maxWait = maxWait;
	}

	public synchronized boolean isTestOnBorrow() {
		return config.testOnBorrow;
	}

	public synchronized void setTestOnBorrow(boolean testOnBorrow) {
		this.config.testOnBorrow = testOnBorrow;
	}

	public synchronized boolean isTestOnReturn() {
		return config.testOnReturn;
	}

	public synchronized void setTestOnReturn(boolean testOnReturn) {
		this.config.testOnReturn = testOnReturn;
	}

	public synchronized long getTimeBetweenEvictionRunsMillis() {
		return this.config.timeBetweenEvictionRunsMillis;
	}

	public synchronized void setTimeBetweenEvictionRunsMillis(
			long timeBetweenEvictionRunsMillis) {
		this.config.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public synchronized int getNumTestsPerEvictionRun() {
		return this.config.numTestsPerEvictionRun;
	}

	public synchronized void setNumTestsPerEvictionRun(
			int numTestsPerEvictionRun) {
		this.config.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public synchronized long getMinEvictableIdleTimeMillis() {
		return this.config.minEvictableIdleTimeMillis;
	}

	public synchronized void setMinEvictableIdleTimeMillis(
			long minEvictableIdleTimeMillis) {
		this.config.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public synchronized boolean isTestWhileIdle() {
		return config.testWhileIdle;
	}

	public synchronized void setTestWhileIdle(boolean testWhileIdle) {
		this.config.testWhileIdle = testWhileIdle;
	}

	public synchronized byte getWhenExhaustedAction() {
		return config.whenExhaustedAction;
	}

	public synchronized void setWhenExhaustedAction(byte whenExhaustedAction) {
		this.config.whenExhaustedAction = whenExhaustedAction;
	}

	public synchronized long getSoftMinEvictableIdleTimeMillis() {
		return config.softMinEvictableIdleTimeMillis;
	}

	public synchronized void setSoftMinEvictableIdleTimeMillis(
			long softMinEvictableIdleTimeMillis) {
		this.config.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
	}

	public synchronized boolean isLifo() {
		return config.lifo;
	}

	public synchronized void setLifo(boolean lifo) {
		this.config.lifo = lifo;
	}

	protected boolean isRestartNeeded() {
		return this.restartNeeded;
	}

	public TTransport getTTransport() throws TTransportException {
		TTransport result = null;
		try {
			result = createThriftDataSource().getTTransport();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public synchronized void close() throws TTransportException {
		if (this.closed) {
			throw new TTransportException(dataSourceType
					+ " was already closed");
		}
		//
		this.closed = true;
		GenericObjectPool<TTransport> oldpool = this.objectPool;
		this.objectPool = null;
		this.instance = null;
		try {
			if (oldpool != null)
				oldpool.close();
		} catch (Exception e) {
			throw new TTransportException("Cannot close pool", e);
		}
	}

	protected synchronized ThriftDataSource createThriftDataSource()
			throws TTransportException {
		if (this.closed) {
			throw new TTransportException(dataSourceType
					+ " was already closed");
		}

		if (this.instance != null) {
			return this.instance;
		}
		TTransportFactory ttransportFactory = createTTransportFactory();
		createObjectPool();
		createPoolableTTransportFactory(ttransportFactory);
		createInstance();
		try {
			for (int i = 0; i < this.initialSize; ++i)
				this.objectPool.addObject();
		} catch (Exception e) {
			throw new TTransportException("Error preloading the pool", e);
		}
		//
		LOGGER.info("Create " + dataSourceType + " [" + ip + ":" + port + "]");
		return this.instance;
	}

	protected TTransportFactory createTTransportFactory()
			throws TTransportException {
		TTransportFactory result = null;
		//
		setTestOnBorrow(false);
		setTestOnReturn(false);
		setTestWhileIdle(false);
		//
		if (!nonblocking) {
			result = new TSocketTTransportFactoryImpl(ip, port, timeout,
					retryNumber, retryPauseMills);
		} else {
			result = new TFramedTTransportFactoryImpl(ip, port, timeout,
					retryNumber, retryPauseMills);
		}
		return result;
	}

	protected void createObjectPool() {
		GenericObjectPool<TTransport> objectPool = new GenericObjectPool<TTransport>();

		// 2014/11/02
		objectPool.setConfig(config);
		this.objectPool = objectPool;
	}

	protected void createInstance() throws TTransportException {
		this.instance = new PoolingThriftDataSource(this.objectPool);
	}

	/**
	 *
	 * @param ttransportFactory
	 * @throws TTransportException
	 */
	protected abstract void createPoolableTTransportFactory(
			TTransportFactory ttransportFactory) throws TTransportException;

	protected void validateConnectionFactory(
			PoolableObjectFactory<TTransport> poolableFactory) throws Exception {
		TTransport ttransport = null;
		try {
			ttransport = poolableFactory.makeObject();
			poolableFactory.activateObject(ttransport);
			poolableFactory.passivateObject(ttransport);
		} finally {
			poolableFactory.destroyObject(ttransport);
		}
	}

	// public synchronized void restart() {
	// try {
	// close();
	// // TODO restart, 2015/09/17 use refresh()
	// } catch (Exception e) {
	// LOGGER.error(new StringBuilder()
	// .append("Could not restart " + dataSourceType + ", cause: ")
	// .append(e.getMessage()).toString());
	// }
	// }
}
