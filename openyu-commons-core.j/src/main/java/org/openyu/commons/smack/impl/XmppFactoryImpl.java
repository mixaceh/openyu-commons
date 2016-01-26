package org.openyu.commons.smack.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.smack.XmppFactory;
import org.openyu.commons.smack.ex.XmppException;
import org.openyu.commons.util.CollectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmppFactoryImpl extends BaseServiceSupporter implements XmppFactory {

	private static final long serialVersionUID = -5603189109995600990L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(XmppFactoryImpl.class);

	private XMPPTCPConnectionConfiguration config;

	private String host;

	private int port;

	private String serviceName;

	//
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

	public XmppFactoryImpl(String host, int port, String serviceName, String username, String password,
			String resourceId, int connectTimeout, boolean sendPresence, boolean compressionEnabled, boolean sslEnabled,
			boolean useSSLContext, long packetReplyTimeout) {
		this.host = host;
		this.port = port;
		this.serviceName = serviceName;
		//
		this.username = username;
		this.password = password;
		this.resourceId = resourceId;
		this.connectTimeout = connectTimeout;
		this.sendPresence = sendPresence;
		this.compressionEnabled = compressionEnabled;
		//
		this.sslEnabled = sslEnabled;
		this.useSSLContext = useSSLContext;
		this.packetReplyTimeout = packetReplyTimeout;
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

	/**
	 * 建立連線
	 * 
	 * @return
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	@Override
	public XMPPConnection createXMPPConnection() throws XmppException {
		XMPPTCPConnection result = null;
		//
		try {
			XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
			configBuilder//
					.setHost(host)//
					.setPort(port)//
					.setServiceName(serviceName)//
					.setConnectTimeout(connectTimeout)//
					.setSendPresence(sendPresence)//
					.setCompressionEnabled(compressionEnabled);
			//
			configBuilder.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			//
			if (sslEnabled == false) {
				configBuilder.setSecurityMode(SecurityMode.disabled);
			} else if (useSSLContext) {
				// ssl direct (predev and dev)
				configBuilder.setSecurityMode(SecurityMode.required).setCustomSSLContext(createSSLContext());
			} else {
				// ssl with load balance (preprd and prd)
				configBuilder.setSecurityMode(SecurityMode.disabled)
						.setSocketFactory(createSSLContext().getSocketFactory());
			}
			//
			config = configBuilder.build();
			//
			result = new XMPPTCPConnection(config);
			result.setPacketReplyTimeout(packetReplyTimeout);

			// add listeners
			addListeners(result);

			// setup reconnect
			ReconnectionManager reconnectMgmt = ReconnectionManager.getInstanceFor(result);
			reconnectMgmt.enableAutomaticReconnection();

			// connect
			result.connect();
			SASLAuthentication.unBlacklistSASLMechanism(SASLMechanism.PLAIN);
			SASLAuthentication.blacklistSASLMechanism(SASLMechanism.DIGESTMD5);
			SASLAuthentication.blacklistSASLMechanism(SASLMechanism.CRAMMD5);
			SASLAuthentication.blacklistSASLMechanism(SASLMechanism.EXTERNAL);
			SASLAuthentication.blacklistSASLMechanism(SASLMechanism.GSSAPI);

			// login
			// org.jivesoftware.smack.XMPPException$StreamErrorException:
			// conflict
			// You can read more about the meaning of this stream error at
			// http://xmpp.org/rfcs/rfc6120.html#streams-error-conditions
			// result.login(username, password, resourceId);

			// #fix
			StringBuilder resourceUid = new StringBuilder(resourceId + "." + StringHelper.randomUnique());
			LOGGER.info("XMPP resourceUniqueId: " + resourceUid);
			result.login(username, password, resourceUid.toString());
		} catch (Exception e) {
			throw new XmppException(e);
		}
		//
		return result;
	}

	protected SSLContext createSSLContext() throws GeneralSecurityException {
		SSLContext result = null;
		//
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		// Create an SSLContext that uses our TrustManager
		result = SSLContext.getInstance("TLSv1");
		result.init(null, trustAllCerts, new SecureRandom());
		result.createSSLEngine().setEnabledCipherSuites(new String[] { "TLS_DHE_RSA_WITH_AES_128_CBC_SHA" });
		//
		return result;
	}

	/**
	 * 新增所有liteners
	 */
	protected void addListeners(XMPPTCPConnection connection) throws SmackException {
		if (CollectionHelper.notEmpty(asyncRecvListeners)) {
			for (Map.Entry<StanzaListener, StanzaFilter> entry : asyncRecvListeners.entrySet()) {
				connection.addAsyncStanzaListener(entry.getKey(), entry.getValue());
			}
		}
		//
		if (CollectionHelper.notEmpty(connectionListeners)) {
			for (ConnectionListener listener : connectionListeners) {
				connection.addConnectionListener(listener);
			}
		}
		//
		if (CollectionHelper.notEmpty(interceptors)) {
			for (Map.Entry<StanzaListener, StanzaFilter> entry : interceptors.entrySet()) {
				connection.addPacketInterceptor(entry.getKey(), entry.getValue());
			}
		}
		//
		if (CollectionHelper.notEmpty(sendListeners)) {
			for (Map.Entry<StanzaListener, StanzaFilter> entry : sendListeners.entrySet()) {
				connection.addPacketSendingListener(entry.getKey(), entry.getValue());
			}
		}
		//
		if (CollectionHelper.notEmpty(syncRecvListeners)) {
			for (Map.Entry<StanzaListener, StanzaFilter> entry : syncRecvListeners.entrySet()) {
				connection.addSyncStanzaListener(entry.getKey(), entry.getValue());
			}
		}
		//
		if (CollectionHelper.notEmpty(setIqRequestHandler)) {
			for (IQRequestHandler entry : setIqRequestHandler) {
				connection.registerIQRequestHandler(entry);
			}
		}
		//
		if (CollectionHelper.notEmpty(getIqRequestHandler)) {
			for (IQRequestHandler entry : getIqRequestHandler) {
				connection.registerIQRequestHandler(entry);
			}
		}
		//
		if (CollectionHelper.notEmpty(requestAckPredicates)) {
			for (StanzaFilter entry : requestAckPredicates) {
				connection.addRequestAckPredicate(entry);
			}
		}
		//
		if (CollectionHelper.notEmpty(stanzaAcknowledgedListeners)) {
			for (StanzaListener entry : stanzaAcknowledgedListeners) {
				connection.addStanzaAcknowledgedListener(entry);
			}
		}
		//
		if (CollectionHelper.notEmpty(stanzaIdAcknowledgedListeners)) {
			for (Map.Entry<String, StanzaListener> entry : stanzaIdAcknowledgedListeners.entrySet()) {
				connection.addStanzaIdAcknowledgedListener(entry.getKey(), entry.getValue());
			}
		}
	}
}
