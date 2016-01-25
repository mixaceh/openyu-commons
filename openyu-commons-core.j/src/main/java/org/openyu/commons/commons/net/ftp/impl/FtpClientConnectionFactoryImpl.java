package org.openyu.commons.commons.net.ftp.impl;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.commons.net.ftp.FtpClientConnectionFactory;
import org.openyu.commons.commons.net.ftp.FtpClientFactory;
import org.openyu.commons.nio.NioHelper;

public class FtpClientConnectionFactoryImpl extends BaseServiceSupporter implements FtpClientConnectionFactory {

	private static final long serialVersionUID = 8466854041599525298L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(FtpClientConnectionFactoryImpl.class);

	private String ip;

	private int port;

	private int connectTimeout;

	/**
	 * 重試次數
	 */
	private int retryNumber = NioHelper.DEFAULT_RETRY_NUMBER;

	/**
	 * 重試暫停毫秒
	 */
	private long retryPauseMills = NioHelper.DEFAULT_RETRY_PAUSE_MILLS;

	private String username;

	private String password;

	private int bufferSize;

	private int clientMode;

	private int fileType;

	private String controlEncoding;

	private String remotePath;

	private int initialSize = 1;

	private volatile boolean restartNeeded;

	private GenericObjectPool.Config config = new GenericObjectPool.Config();

	//
	private volatile FtpClientConnectionFactory instance;

	protected GenericObjectPool<FTPClient> objectPool;

	private boolean closed;

	public FtpClientConnectionFactoryImpl() {
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

	public synchronized int getConnectTimeout() {
		return connectTimeout;
	}

	public synchronized void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
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

	public synchronized String getUsername() {
		return username;
	}

	public synchronized void setUsername(String username) {
		this.username = username;
	}

	public synchronized String getPassword() {
		return password;
	}

	public synchronized void setPassword(String password) {
		this.password = password;
	}

	public synchronized int getBufferSize() {
		return bufferSize;
	}

	public synchronized void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public synchronized int getClientMode() {
		return clientMode;
	}

	public synchronized void setClientMode(int clientMode) {
		this.clientMode = clientMode;
	}

	public synchronized int getFileType() {
		return fileType;
	}

	public synchronized void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public synchronized String getControlEncoding() {
		return controlEncoding;
	}

	public synchronized void setControlEncoding(String controlEncoding) {
		this.controlEncoding = controlEncoding;
	}

	public synchronized String getRemotePath() {
		return remotePath;
	}

	public synchronized void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
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

	public FTPClient getFTPClient() throws SocketException, IOException {
		return createFtpClientConnectionFactory().getFTPClient();
	}

	public synchronized void close() throws IOException {
		// if (this.closed) {
		// throw new IOException("FtpClientConnectionFactory was already
		// closed");
		// }
		//
		this.closed = true;
		GenericObjectPool<FTPClient> oldpool = this.objectPool;
		this.objectPool = null;
		this.instance = null;
		try {
			if (oldpool != null) {
				oldpool.close();
			}
		} catch (Exception ex) {
			throw new IOException("Cannot close pool", ex);
		}
	}

	protected synchronized FtpClientConnectionFactory createFtpClientConnectionFactory()
			throws SocketException, IOException {
		if (this.closed) {
			throw new SocketException("FtpClientConnectionFactory was already closed");
		}

		if (this.instance != null) {
			return this.instance;
		}
		FtpClientFactory ftpClientFactory = createFtpClientFactory();
		createObjectPool();
		createPoolableFtpClientFactory(ftpClientFactory);
		createInstance();
		try {
			for (int i = 0; i < this.initialSize; ++i)
				this.objectPool.addObject();
		} catch (Exception e) {
			throw new IOException("Error preloading the pool", e);
		}
		//
		LOGGER.info("Create FtpClientConnectionFactory [" + ip + ":" + port + "]");
		return this.instance;
	}

	protected FtpClientFactory createFtpClientFactory() throws IOException {
		FtpClientFactory result = null;
		//
		result = new FtpClientFactoryImpl(ip, port, connectTimeout, retryNumber, retryPauseMills, username, password,
				bufferSize, clientMode, fileType, controlEncoding, remotePath);
		return result;
	}

	protected void createObjectPool() {
		GenericObjectPool<FTPClient> objectPool = new GenericObjectPool<FTPClient>();
		objectPool.setConfig(config);
		this.objectPool = objectPool;
	}

	protected void createInstance() throws SocketException {
		this.instance = new PoolingFtpClientConnectionFactory(this.objectPool);
	}

	protected void createPoolableFtpClientFactory(FtpClientFactory ftpClientFactory) throws IOException {
		PoolableFtpClientFactory result = null;
		try {
			result = new PoolableFtpClientFactory(ftpClientFactory, this.objectPool);
			validateConnectionFactory(result);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(new StringBuilder().append("Cannot create PoolableFtpClientFactory (")
					.append(e.getMessage()).append(")").toString(), e);
		}
	}

	protected void validateConnectionFactory(PoolableFtpClientFactory poolableFactory) throws Exception {
		FTPClient ftpClient = null;
		try {
			ftpClient = (FTPClient) poolableFactory.makeObject();
			poolableFactory.activateObject(ftpClient);
			poolableFactory.passivateObject(ftpClient);
		} finally {
			poolableFactory.destroyObject(ftpClient);
		}
	}
}
