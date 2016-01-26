package org.openyu.commons.smack.impl;

import java.io.IOException;
import java.net.SocketException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.newegg.sso.service.impl.AbstractAppService;
import com.newegg.sso.util.xmpp.XmppFactory;
import com.newegg.sso.util.xmpp.XmppConnectionFactory;

public class XmppConnectionFactoryImpl extends AbstractAppService implements XmppConnectionFactory {

	private static final long serialVersionUID = 8466854041599525298L;

	private static transient final Logger LOGGER = LogManager.getLogger(XmppConnectionFactoryImpl.class);

	private String host;

	private int port;

	private String serviceName;

	private String username;

	private String password;

	private String resourceId;

	private int connectTimeout;

	private boolean sendPresence;

	private boolean compressionEnabled;
	//
	private boolean sslEnabled;

	private boolean useSSLContext;

	private long packetReplyTimeout;

	// 初始大小
	private int initialSize = 1;

	private volatile boolean restartNeeded;

	private GenericObjectPool.Config config = new GenericObjectPool.Config();
	//
	private volatile XmppConnectionFactory instance;

	protected GenericObjectPool<XMPPConnection> objectPool;

	private boolean closed;

	// ============================================
	// listener
	// ============================================
	private Map<StanzaListener, StanzaFilter> asyncRecvListeners;

	private Set<ConnectionListener> connectionListeners;

	private Map<StanzaListener, StanzaFilter> interceptors;

	private Map<StanzaListener, StanzaFilter> sendListeners;

	private Map<StanzaListener, StanzaFilter> syncRecvListeners;

	private Set<IQRequestHandler> setIqRequestHandler;

	private Set<IQRequestHandler> getIqRequestHandler;

	private Set<StanzaFilter> requestAckPredicates;

	private Collection<StanzaListener> stanzaAcknowledgedListeners;

	private Map<String, StanzaListener> stanzaIdAcknowledgedListeners;

	public XmppConnectionFactoryImpl() {
	}

	public synchronized String getHost() {
		return host;
	}

	public synchronized void setHost(String host) {
		this.host = host;
	}

	public synchronized int getPort() {
		return port;
	}

	public synchronized void setPort(int port) {
		this.port = port;
	}

	public synchronized String getServiceName() {
		return serviceName;
	}

	public synchronized void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	//
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

	public synchronized String getResourceId() {
		return resourceId;
	}

	public synchronized void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public synchronized int getConnectTimeout() {
		return connectTimeout;
	}

	public synchronized void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	//
	public boolean isSendPresence() {
		return sendPresence;
	}

	public void setSendPresence(boolean sendPresence) {
		this.sendPresence = sendPresence;
	}

	public boolean isCompressionEnabled() {
		return compressionEnabled;
	}

	public void setCompressionEnabled(boolean compressionEnabled) {
		this.compressionEnabled = compressionEnabled;
	}

	public boolean isSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
	}

	public boolean isUseSSLContext() {
		return useSSLContext;
	}

	public void setUseSSLContext(boolean useSSLContext) {
		this.useSSLContext = useSSLContext;
	}

	public long getPacketReplyTimeout() {
		return packetReplyTimeout;
	}

	public void setPacketReplyTimeout(long packetReplyTimeout) {
		this.packetReplyTimeout = packetReplyTimeout;
	}

	// ============================================
	// listener
	// ============================================
	public Map<StanzaListener, StanzaFilter> getAsyncRecvListeners() {
		return asyncRecvListeners;
	}

	public void setAsyncRecvListeners(Map<StanzaListener, StanzaFilter> asyncRecvListeners) {
		this.asyncRecvListeners = asyncRecvListeners;
	}

	public Set<ConnectionListener> getConnectionListeners() {
		return connectionListeners;
	}

	public void setConnectionListeners(Set<ConnectionListener> connectionListeners) {
		this.connectionListeners = connectionListeners;
	}

	public Map<StanzaListener, StanzaFilter> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(Map<StanzaListener, StanzaFilter> interceptors) {
		this.interceptors = interceptors;
	}

	public Map<StanzaListener, StanzaFilter> getSendListeners() {
		return sendListeners;
	}

	public void setSendListeners(Map<StanzaListener, StanzaFilter> sendListeners) {
		this.sendListeners = sendListeners;
	}

	public Map<StanzaListener, StanzaFilter> getSyncRecvListeners() {
		return syncRecvListeners;
	}

	public void setSyncRecvListeners(Map<StanzaListener, StanzaFilter> syncRecvListeners) {
		this.syncRecvListeners = syncRecvListeners;
	}

	public Set<IQRequestHandler> getSetIqRequestHandler() {
		return setIqRequestHandler;
	}

	public void setSetIqRequestHandler(Set<IQRequestHandler> setIqRequestHandler) {
		this.setIqRequestHandler = setIqRequestHandler;
	}

	public Set<IQRequestHandler> getGetIqRequestHandler() {
		return getIqRequestHandler;
	}

	public void setGetIqRequestHandler(Set<IQRequestHandler> getIqRequestHandler) {
		this.getIqRequestHandler = getIqRequestHandler;
	}

	public Set<StanzaFilter> getRequestAckPredicates() {
		return requestAckPredicates;
	}

	public void setRequestAckPredicates(Set<StanzaFilter> requestAckPredicates) {
		this.requestAckPredicates = requestAckPredicates;
	}

	public Collection<StanzaListener> getStanzaAcknowledgedListeners() {
		return stanzaAcknowledgedListeners;
	}

	public void setStanzaAcknowledgedListeners(Collection<StanzaListener> stanzaAcknowledgedListeners) {
		this.stanzaAcknowledgedListeners = stanzaAcknowledgedListeners;
	}

	public Map<String, StanzaListener> getStanzaIdAcknowledgedListeners() {
		return stanzaIdAcknowledgedListeners;
	}

	public void setStanzaIdAcknowledgedListeners(Map<String, StanzaListener> stanzaIdAcknowledgedListeners) {
		this.stanzaIdAcknowledgedListeners = stanzaIdAcknowledgedListeners;
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

	public XMPPConnection getXMPPConnection() throws SmackException, IOException, XMPPException {
		return createXmppConnectionFactory().getXMPPConnection();
	}

	public synchronized void close() throws IOException {
		// if (this.closed) {
		// throw new IOException("XmppConnectionFactory was already closed");
		// }
		//
		this.closed = true;
		GenericObjectPool<XMPPConnection> oldpool = this.objectPool;
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

	protected synchronized XmppConnectionFactory createXmppConnectionFactory() throws SocketException, IOException {
		if (this.closed) {
			throw new SocketException("XmppConnectionFactory was already closed");
		}

		if (this.instance != null) {
			return this.instance;
		}
		XmppFactory xmppFactory = createXmppFactory();
		createObjectPool();
		createPoolableXmppFactory(xmppFactory);
		createInstance();
		try {
			for (int i = 0; i < this.initialSize; ++i)
				this.objectPool.addObject();
		} catch (Exception e) {
			throw new IOException("Error preloading the pool", e);
		}
		//
		LOGGER.info("Create XmppConnectionFactory [" + host + ":" + port + ", " + serviceName + "]");
		return this.instance;
	}

	protected XmppFactory createXmppFactory() throws IOException {
		XmppFactoryImpl result = null;
		//
		result = new XmppFactoryImpl(host, port, serviceName, username, password, resourceId, connectTimeout,
				sendPresence, compressionEnabled, sslEnabled, useSSLContext, packetReplyTimeout);

		// set listeners
		result.setAsyncRecvListeners(asyncRecvListeners);
		result.setConnectionListeners(connectionListeners);
		result.setInterceptors(interceptors);
		result.setSendListeners(sendListeners);
		result.setSyncRecvListeners(syncRecvListeners);
		result.setSetIqRequestHandler(setIqRequestHandler);
		result.setGetIqRequestHandler(getIqRequestHandler);
		result.setRequestAckPredicates(requestAckPredicates);
		result.setStanzaAcknowledgedListeners(stanzaAcknowledgedListeners);
		result.setStanzaIdAcknowledgedListeners(stanzaIdAcknowledgedListeners);
		//
		return result;
	}

	protected void createObjectPool() {
		GenericObjectPool<XMPPConnection> objectPool = new GenericObjectPool<XMPPConnection>();
		objectPool.setConfig(config);
		this.objectPool = objectPool;
	}

	protected void createInstance() throws SocketException {
		this.instance = new PoolingXmppConnectionFactory(this.objectPool);
	}

	protected void createPoolableXmppFactory(XmppFactory xmppFactory) throws IOException {
		PoolableXmppFactory result = null;
		try {
			result = new PoolableXmppFactory(xmppFactory, this.objectPool);
			validateFactory(result);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(new StringBuilder().append("Cannot create PoolableXmppFactory (")
					.append(e.getMessage()).append(")").toString(), e);
		}
	}

	protected void validateFactory(PoolableXmppFactory poolableFactory) throws Exception {
		XMPPConnection xmppConnection = null;
		try {
			xmppConnection = (XMPPConnection) poolableFactory.makeObject();
			poolableFactory.activateObject(xmppConnection);
			poolableFactory.passivateObject(xmppConnection);
		} finally {
			poolableFactory.destroyObject(xmppConnection);
		}
	}
}
