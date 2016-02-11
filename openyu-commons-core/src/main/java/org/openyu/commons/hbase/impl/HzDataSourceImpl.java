package org.openyu.commons.hbase.impl;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.hbase.HConnectionFactory;
import org.openyu.commons.hbase.HzDataSource;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public class HzDataSourceImpl extends BaseServiceSupporter implements HzDataSource {

	private static final long serialVersionUID = -3083482858192075169L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(HzDataSourceImpl.class);

	private Properties properties = new Properties();

	private Configuration configuration = HBaseConfiguration.create();
	//
	private int initialSize = 1;

	private volatile boolean restartNeeded;

	private GenericObjectPool.Config config = new GenericObjectPool.Config();

	//
	private volatile HzDataSource instance;

	protected GenericObjectPool<HConnection> objectPool;

	private boolean closed;

	public HzDataSourceImpl() {
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

	public synchronized Properties getProperties() {
		return properties;
	}

	@SuppressWarnings("rawtypes")
	public synchronized void setProperties(Properties properties) {
		this.properties = properties;
		//
		for (Map.Entry entry : properties.entrySet()) {
			configuration.set((String) entry.getKey(), (String) entry.getValue());
		}
	}

	public synchronized Configuration getConfiguration() {
		return configuration;
	}

	public synchronized void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

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

	public synchronized void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.config.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public synchronized int getNumTestsPerEvictionRun() {
		return this.config.numTestsPerEvictionRun;
	}

	public synchronized void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.config.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public synchronized long getMinEvictableIdleTimeMillis() {
		return this.config.minEvictableIdleTimeMillis;
	}

	public synchronized void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
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

	public synchronized void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
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

	public HConnection getHConnection() throws ZooKeeperConnectionException {
		return createHzDataSource().getHConnection();
	}

	public synchronized void close() throws ZooKeeperConnectionException {
		if (this.closed) {
			throw new ZooKeeperConnectionException("HzDataSource was already closed");
		}
		//
		this.closed = true;
		GenericObjectPool<HConnection> oldpool = this.objectPool;
		this.objectPool = null;
		this.instance = null;
		try {
			if (oldpool != null)
				oldpool.close();
		} catch (Exception ex) {
			throw new ZooKeeperConnectionException("Cannot close pool", ex);
		}
	}

	protected synchronized HzDataSource createHzDataSource() throws ZooKeeperConnectionException {
		if (this.closed) {
			throw new ZooKeeperConnectionException("HzDataSource was already closed");
		}
		//
		if (this.instance != null) {
			return this.instance;
		}
		HConnectionFactory hConnectionFactory = createHConnectionFactory();
		createObjectPool();
		createPoolableHConnectionFactory(hConnectionFactory);
		createInstance();
		try {
			for (int i = 0; i < this.initialSize; ++i)
				this.objectPool.addObject();
		} catch (Exception e) {
			throw new ZooKeeperConnectionException("Error preloading the pool", e);
		}
		//
		String quorum = this.configuration.get("hbase.zookeeper.quorum", null);
		int clientPort = this.configuration.getInt("hbase.zookeeper.property.clientPort", 0);
		LOGGER.info("Create HzDataSource [" + quorum + ":" + clientPort + "]");
		return this.instance;
	}

	protected HConnectionFactory createHConnectionFactory() throws ZooKeeperConnectionException {
		HConnectionFactory result = null;
		//
		result = new HConnectionFactoryImpl(configuration);
		return result;
	}

	protected void createObjectPool() {
		GenericObjectPool<HConnection> objectPool = new GenericObjectPool<HConnection>();
		objectPool.setConfig(config);
		this.objectPool = objectPool;
	}

	protected void createInstance() throws ZooKeeperConnectionException {
		this.instance = new PoolingHzDataSource(this.objectPool);
	}

	protected void createPoolableHConnectionFactory(HConnectionFactory hConnectionFactory)
			throws ZooKeeperConnectionException {
		PoolableHConnectionFactory result = null;
		try {
			result = new PoolableHConnectionFactory(hConnectionFactory, this.objectPool, this.configuration);
			validateConnectionFactory(result);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new ZooKeeperConnectionException(new StringBuilder()
					.append("Cannot create PoolableHConnectionFactory (").append(e.getMessage()).append(")").toString(),
					e);
		}
	}

	protected void validateConnectionFactory(PoolableHConnectionFactory poolableFactory) throws Exception {
		HConnection hConnection = null;
		try {
			hConnection = (HConnection) poolableFactory.makeObject();
			poolableFactory.activateObject(hConnection);
			poolableFactory.passivateObject(hConnection);
		} finally {
			poolableFactory.destroyObject(hConnection);
		}
	}

	// public synchronized void restart() {
	// try {
	// close();
	// // TODO restart, 2015/09/17 use refresh()
	// } catch (Exception e) {
	// LOGGER.error(new StringBuilder()
	// .append("Could not restart HzDataSource, cause: ")
	// .append(e.getMessage()).toString());
	// }
	// }

}
