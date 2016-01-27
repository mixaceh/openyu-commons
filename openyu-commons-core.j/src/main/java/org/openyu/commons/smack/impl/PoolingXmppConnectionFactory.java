package org.openyu.commons.smack.impl;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.pool.ObjectPool;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.smack.XmppConnectionFactory;

public class PoolingXmppConnectionFactory extends BaseServiceSupporter implements XmppConnectionFactory {

	private static final long serialVersionUID = -7009645615525983801L;

	private ObjectPool<XMPPConnection> pool;

	public PoolingXmppConnectionFactory() {
		this(null);
	}

	public PoolingXmppConnectionFactory(ObjectPool<XMPPConnection> pool) {
		this.pool = pool;
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

	public void setPool(ObjectPool<XMPPConnection> pool) throws IllegalStateException, NullPointerException {
		if (null != this.pool)
			throw new IllegalStateException("Pool already set");
		if (null == pool) {
			throw new NullPointerException("Pool must not be null.");
		}
		this.pool = pool;
	}

	public XMPPConnection getXMPPConnection() throws SmackException, IOException, XMPPException {
		XMPPConnection result = null;
		try {
			result = this.pool.borrowObject();
		} catch (SocketException e) {
			throw e;
		} catch (IOException e) {
			throw new IOException("Cannot get a XMPPConnection, pool error " + e.getMessage(), e);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException("Cannot get a XMPPConnection, general error", e);
		}
		return result;
	}

}
