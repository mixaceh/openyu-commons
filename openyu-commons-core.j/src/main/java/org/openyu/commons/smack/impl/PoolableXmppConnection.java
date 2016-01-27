package org.openyu.commons.smack.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool.ObjectPool;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ExceptionCallback;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketCollector.Configuration;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.util.CollectionHelper;

public class PoolableXmppConnection implements XMPPConnection {

	private boolean closed = false;

	private XMPPConnection delegate;

	private ObjectPool<XMPPConnection> pool = null;

	private Socket socket;

	public PoolableXmppConnection(XMPPConnection delegate, ObjectPool<XMPPConnection> pool) {
		this.delegate = delegate;
		this.pool = pool;
		//
		if (delegate != null) {
			if (delegate instanceof XMPPTCPConnection) {
				socket = ClassHelper.getDeclaredFieldValue(delegate, "socket");
			}
		}
	}

	public XMPPConnection getDelegate() {
		return delegate;
	}

	protected void checkOpen() throws SocketException, IOException {
		if (this.closed) {
			if (null != this.delegate) {
				String label = "";
				try {
					label = this.delegate.toString();
				} catch (Exception ex) {
				}
				throw new SocketException("XMPPConnection " + label + " was already closed");
			}
			throw new SocketException("XMPPConnection is null");
		}
	}

	/**
	 * 返回pool
	 */
	public synchronized void disconnect() {
		if (this.closed) {
			return;
		}
		boolean isUnderlyingConectionClosed;
		try {
			isUnderlyingConectionClosed = !(this.delegate.isConnected());
		} catch (Exception e) {
			try {
				this.pool.invalidateObject(this);
			} catch (Exception ex) {
				this.closed = true;
				reallyClose();
			}
			throw new RuntimeException("Cannot close XMPPConnection (isClosed check failed)");
		}
		if (!(isUnderlyingConectionClosed)) {
			try {
				this.pool.returnObject(this);
				// }
			} catch (Exception e) {
				this.closed = true;
				reallyClose();
				throw new RuntimeException("Cannot close XMPPConnection (return to pool failed)");
			}
		} else {
			try {
				this.pool.invalidateObject(this);
			} catch (Exception e) {
				this.closed = true;
				reallyClose();
			}
			throw new RuntimeException("Already closed.");
		}
	}

	/**
	 * 真正關閉連線
	 */
	public synchronized void reallyClose() {
		try {
			if (delegate != null) {
				if (delegate instanceof XMPPTCPConnection) {
					XMPPTCPConnection conn = (XMPPTCPConnection) delegate;
					conn.disconnect();
					// 移除所有的listener
					removeAllListeners(conn);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	protected void removeAllListeners(XMPPTCPConnection conn) {

		// addAsyncStanzaListener(packetListener, packetFilter)
		@SuppressWarnings("rawtypes")
		Map asyncRecvListeners = ClassHelper.getDeclaredFieldValue(conn, "asyncRecvListeners");
		if (CollectionHelper.notEmpty(asyncRecvListeners)) {
			asyncRecvListeners.clear();
		}
		// addConnectionListener(connectionListener)
		Set<ConnectionListener> connectionListeners = ClassHelper.getDeclaredFieldValue(conn, "connectionListeners");
		if (CollectionHelper.notEmpty(connectionListeners)) {
			connectionListeners.clear();
		}

		// addPacketInterceptor(packetInterceptor, packetFilter)
		@SuppressWarnings("rawtypes")
		Map interceptors = ClassHelper.getDeclaredFieldValue(conn, "interceptors");
		if (CollectionHelper.notEmpty(interceptors)) {
			interceptors.clear();
		}

		// addPacketSendingListener(packetListener, packetFilter)
		@SuppressWarnings("rawtypes")
		Map sendListeners = ClassHelper.getDeclaredFieldValue(conn, "sendListeners");
		if (CollectionHelper.notEmpty(sendListeners)) {
			sendListeners.clear();
		}

		// addSyncStanzaListener(packetListener, packetFilter)
		@SuppressWarnings("rawtypes")
		Map syncRecvListeners = ClassHelper.getDeclaredFieldValue(conn, "syncRecvListeners");
		if (CollectionHelper.notEmpty(syncRecvListeners)) {
			syncRecvListeners.clear();
		}

		// registerIQRequestHandler(final IQRequestHandler iqRequestHandler)
		@SuppressWarnings("rawtypes")
		Map setIqRequestHandler = ClassHelper.getDeclaredFieldValue(conn, "setIqRequestHandler");
		if (CollectionHelper.notEmpty(setIqRequestHandler)) {
			setIqRequestHandler.clear();
		}
		@SuppressWarnings("rawtypes")
		Map getIqRequestHandler = ClassHelper.getDeclaredFieldValue(conn, "getIqRequestHandler");
		if (CollectionHelper.notEmpty(getIqRequestHandler)) {
			getIqRequestHandler.clear();
		}
		//
		conn.removeAllRequestAckPredicates();
		conn.removeAllStanzaAcknowledgedListeners();
		conn.removeAllStanzaIdAcknowledgedListeners();
	}

	public String getServiceName() {
		return delegate.getServiceName();
	}

	public String getHost() {
		return delegate.getHost();
	}

	public int getPort() {
		return delegate.getPort();
	}

	public String getUser() {
		return delegate.getUser();
	}

	public String getStreamId() {
		return delegate.getStreamId();
	}

	public boolean isConnected() {
		return (!closed) || (this.delegate.isConnected());
	}

	public boolean isAuthenticated() {
		return delegate.isAuthenticated();
	}

	public boolean isAnonymous() {
		return delegate.isAnonymous();
	}

	public boolean isSecureConnection() {
		return delegate.isSecureConnection();
	}

	public boolean isUsingCompression() {
		return delegate.isUsingCompression();
	}

	public void sendPacket(Stanza packet) throws NotConnectedException {
		delegate.sendPacket(packet);
	}

	public void sendStanza(Stanza stanza) throws NotConnectedException {
		delegate.sendStanza(stanza);
	}

	public void send(PlainStreamElement element) throws NotConnectedException {
		delegate.send(element);
	}

	public void addConnectionListener(ConnectionListener connectionListener) {
		delegate.addConnectionListener(connectionListener);
	}

	public void removeConnectionListener(ConnectionListener connectionListener) {
		delegate.removeConnectionListener(connectionListener);
	}

	public PacketCollector createPacketCollectorAndSend(IQ packet) throws NotConnectedException {
		return delegate.createPacketCollectorAndSend(packet);
	}

	public PacketCollector createPacketCollectorAndSend(StanzaFilter packetFilter, Stanza packet)
			throws NotConnectedException {
		return delegate.createPacketCollectorAndSend(packetFilter, packet);
	}

	public PacketCollector createPacketCollector(StanzaFilter packetFilter) {
		return delegate.createPacketCollector(packetFilter);
	}

	public PacketCollector createPacketCollector(Configuration configuration) {
		return delegate.createPacketCollector(configuration);
	}

	public void removePacketCollector(PacketCollector collector) {
		delegate.removePacketCollector(collector);
	}

	public void addPacketListener(StanzaListener packetListener, StanzaFilter packetFilter) {
		delegate.addPacketListener(packetListener, packetFilter);
	}

	public boolean removePacketListener(StanzaListener packetListener) {
		return delegate.removePacketListener(packetListener);
	}

	public void addSyncStanzaListener(StanzaListener packetListener, StanzaFilter packetFilter) {
		delegate.addSyncStanzaListener(packetListener, packetFilter);
	}

	public boolean removeSyncStanzaListener(StanzaListener packetListener) {
		return delegate.removeSyncStanzaListener(packetListener);
	}

	public void addAsyncStanzaListener(StanzaListener packetListener, StanzaFilter packetFilter) {
		delegate.addAsyncStanzaListener(packetListener, packetFilter);
	}

	public boolean removeAsyncStanzaListener(StanzaListener packetListener) {
		return delegate.removeAsyncStanzaListener(packetListener);
	}

	public void addPacketSendingListener(StanzaListener packetListener, StanzaFilter packetFilter) {
		delegate.addPacketSendingListener(packetListener, packetFilter);
	}

	public void removePacketSendingListener(StanzaListener packetListener) {
		delegate.removePacketSendingListener(packetListener);
	}

	public void addPacketInterceptor(StanzaListener packetInterceptor, StanzaFilter packetFilter) {
		delegate.addPacketInterceptor(packetInterceptor, packetFilter);
	}

	public void removePacketInterceptor(StanzaListener packetInterceptor) {
		delegate.removePacketInterceptor(packetInterceptor);
	}

	public long getPacketReplyTimeout() {
		return delegate.getPacketReplyTimeout();
	}

	public void setPacketReplyTimeout(long timeout) {
		delegate.setPacketReplyTimeout(timeout);
	}

	public int getConnectionCounter() {
		return delegate.getConnectionCounter();
	}

	public void setFromMode(FromMode fromMode) {
		delegate.setFromMode(fromMode);
	}

	public FromMode getFromMode() {
		return delegate.getFromMode();
	}

	public <F extends ExtensionElement> F getFeature(String element, String namespace) {
		return delegate.getFeature(element, namespace);
	}

	public boolean hasFeature(String element, String namespace) {
		return delegate.hasFeature(element, namespace);
	}

	public void sendStanzaWithResponseCallback(Stanza stanza, StanzaFilter replyFilter, StanzaListener callback)
			throws NotConnectedException {
		delegate.sendStanzaWithResponseCallback(stanza, replyFilter, callback);
	}

	public void sendStanzaWithResponseCallback(Stanza stanza, StanzaFilter replyFilter, StanzaListener callback,
			ExceptionCallback exceptionCallback) throws NotConnectedException {
		delegate.sendStanzaWithResponseCallback(stanza, replyFilter, callback, exceptionCallback);
	}

	public void sendStanzaWithResponseCallback(Stanza stanza, StanzaFilter replyFilter, StanzaListener callback,
			ExceptionCallback exceptionCallback, long timeout) throws NotConnectedException {
		delegate.sendStanzaWithResponseCallback(stanza, replyFilter, callback, exceptionCallback, timeout);
	}

	public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback) throws NotConnectedException {
		delegate.sendIqWithResponseCallback(iqRequest, callback);
	}

	public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback, ExceptionCallback exceptionCallback)
			throws NotConnectedException {
		delegate.sendIqWithResponseCallback(iqRequest, callback, exceptionCallback);
	}

	public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback, ExceptionCallback exceptionCallback,
			long timeout) throws NotConnectedException {
		delegate.sendIqWithResponseCallback(iqRequest, callback, exceptionCallback, timeout);
	}

	public void addOneTimeSyncCallback(StanzaListener callback, StanzaFilter packetFilter) {
		delegate.addOneTimeSyncCallback(callback, packetFilter);
	}

	public IQRequestHandler registerIQRequestHandler(IQRequestHandler iqRequestHandler) {
		return delegate.registerIQRequestHandler(iqRequestHandler);
	}

	public IQRequestHandler unregisterIQRequestHandler(IQRequestHandler iqRequestHandler) {
		return delegate.unregisterIQRequestHandler(iqRequestHandler);
	}

	public long getLastStanzaReceived() {
		return delegate.getLastStanzaReceived();
	}

	public IQRequestHandler unregisterIQRequestHandler(String arg0, String arg1, Type arg2) {
		return delegate.unregisterIQRequestHandler(arg0, arg1, arg2);
	}

	public boolean isAlive() {
		boolean result = false;
		try {
			socket.sendUrgentData(0xFF);
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

}
