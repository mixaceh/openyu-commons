package org.openyu.commons.smack.impl;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jivesoftware.smack.XMPPConnection;

import com.newegg.sso.service.impl.AbstractAppService;
import com.newegg.sso.util.xmpp.XmppFactory;

public class PoolableXmppFactory extends AbstractAppService implements PoolableObjectFactory<XMPPConnection> {

	private static final long serialVersionUID = -732567508101509392L;

	private static transient final Logger LOGGER = LogManager.getLogger(PoolableXmppFactory.class);

	protected volatile XmppFactory xmppFactory = null;

	protected volatile ObjectPool<XMPPConnection> pool = null;

	public PoolableXmppFactory(XmppFactory xmppFactory, ObjectPool<XMPPConnection> pool) {
		this.xmppFactory = xmppFactory;
		this.pool = pool;
		this.pool.setFactory(this);
	}

	public void setXmppFactory(XmppFactory xmppFactory) {
		this.xmppFactory = xmppFactory;
	}

	public synchronized void setPool(ObjectPool<XMPPConnection> pool) {
		if ((null != this.pool) && (pool != this.pool))
			try {
				this.pool.close();
			} catch (Exception e) {
			}
		this.pool = pool;
	}

	public synchronized ObjectPool<XMPPConnection> getPool() {
		return this.pool;
	}

	public XMPPConnection makeObject() throws Exception {
		XMPPConnection result = xmppFactory.createXMPPConnection();
		if (result == null) {
			throw new IllegalStateException("XMPPConnection factory returned null from createXMPPConnection");
		}
		try {
			result = new PoolableXmppConnection(result, this.pool);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = null;
		}
		return result;
	}

	public void destroyObject(XMPPConnection obj) throws Exception {
		if (obj != null) {
			if (obj instanceof PoolableXmppConnection) {
				PoolableXmppConnection conn = (PoolableXmppConnection) obj;
				conn.reallyClose();
			}
		}
	}

	public boolean validateObject(XMPPConnection obj) {
		boolean result = false;
		//
		try {
			// 只要連過線,之後斷線還是true
			// if (obj.isConnected()) {
			// return true;
			// } else {
			// return false;
			// }

			// #fix
			if (obj != null) {
				if (obj instanceof PoolableXmppConnection) {
					PoolableXmppConnection conn = (PoolableXmppConnection) obj;
					result = conn.isAlive();
				}
			}
		} catch (Exception e) {
		}
		return result;
	}

	public void activateObject(XMPPConnection obj) throws Exception {
	}

	public void passivateObject(XMPPConnection obj) throws Exception {
	}

}
