package org.openyu.commons.smack.impl;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.pool.ObjectPool;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import com.newegg.sso.service.impl.AbstractAppService;
import com.newegg.sso.util.xmpp.XmppConnectionFactory;

public class PoolingXmppConnectionFactory extends AbstractAppService implements XmppConnectionFactory {

	private ObjectPool<XMPPConnection> pool;

	public PoolingXmppConnectionFactory() {
		this(null);
	}

	public PoolingXmppConnectionFactory(ObjectPool<XMPPConnection> pool) {
		this.pool = pool;
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
