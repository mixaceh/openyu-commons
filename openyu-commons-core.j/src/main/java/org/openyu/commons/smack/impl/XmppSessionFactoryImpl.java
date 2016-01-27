package org.openyu.commons.smack.impl;

import org.jivesoftware.smack.XMPPConnection;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.smack.XmppConnectionFactory;
import org.openyu.commons.smack.XmppSession;
import org.openyu.commons.smack.XmppSessionFactory;
import org.openyu.commons.smack.ex.XmppException;
import org.openyu.commons.util.AssertHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XmppSessionFactoryImpl extends BaseServiceSupporter implements XmppSessionFactory {

	private static final long serialVersionUID = -7949738806757793967L;

	private static transient final Logger LOGGER = LogManager.getLogger(XmppSessionFactoryImpl.class);

	private XmppConnectionFactory xmppConnectionFactory;

	private ThreadLocal<XmppSessionImpl> sessionHolder = new ThreadLocal<XmppSessionImpl>();

	private boolean closed = false;

	public XmppSessionFactoryImpl() {
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		AssertHelper.notNull(xmppConnectionFactory, "The XmppConnectionFactory is required");
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		close();
	}

	public XmppConnectionFactory getXmppConnectionFactory() {
		return xmppConnectionFactory;
	}

	public void setXmppConnectionFactory(XmppConnectionFactory xmppConnectionFactory) {
		this.xmppConnectionFactory = xmppConnectionFactory;
	}

	public XmppSession openSession() throws XmppException {
		XmppSessionImpl result = null;
		try {
			result = sessionHolder.get();
			if (result == null) {
				XMPPConnection xmppConnection = xmppConnectionFactory.getXMPPConnection();
				result = new XmppSessionImpl(this, xmppConnection);
				//
				sessionHolder.set(result);
			}
		} catch (Exception ex) {
			throw new XmppException("Cannot get a XmppSession, general error", ex);
		}
		return result;
	}

	public void closeSession() throws XmppException {
		try {
			XmppSessionImpl xmppSession = sessionHolder.get();
			sessionHolder.set(null);
			if (xmppSession != null) {
				xmppSession.closed = true;
				if (xmppSession.xmppConnection != null) {
					if (xmppSession.xmppConnection instanceof PoolableXmppConnection) {
						PoolableXmppConnection conn = (PoolableXmppConnection) xmppSession.xmppConnection;
						conn.disconnect();
					}
				}
			}
		} catch (Exception ex) {
			throw new XmppException("Cannot close XmppSession, general error", ex);
		}
	}

	public synchronized void close() throws XmppException {
		if (this.closed) {
			throw new XmppException("XmppSessionFactory was already closed");
		}
		//
		try {
			this.closed = true;
			// TODO sessionHolder 未清
			if (xmppConnectionFactory instanceof XmppConnectionFactoryImpl) {
				XmppConnectionFactoryImpl oldFactory = (XmppConnectionFactoryImpl) xmppConnectionFactory;
				xmppConnectionFactory = null;
				if (oldFactory != null) {
					oldFactory.close();
				}
			}
		} catch (Exception ex) {
			throw new XmppException("Cannot close XmppSessionFactory, general error", ex);
		}
	}
}
