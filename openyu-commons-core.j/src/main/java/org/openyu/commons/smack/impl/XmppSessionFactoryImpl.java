package org.openyu.commons.smack.impl;

import javax.annotation.PostConstruct;

import org.jivesoftware.smack.XMPPConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.newegg.sso.service.impl.AbstractAppService;
import com.newegg.sso.util.AssertUtils;
import com.newegg.sso.util.xmpp.XmppConnectionFactory;
import com.newegg.sso.util.xmpp.XmppSession;
import com.newegg.sso.util.xmpp.XmppSessionFactory;
import com.newegg.sso.util.xmpp.exception.XmppException;

public class XmppSessionFactoryImpl extends AbstractAppService implements XmppSessionFactory {

	private static final long serialVersionUID = 4731628785810628545L;

	private static transient final Logger LOGGER = LogManager.getLogger(XmppSessionFactoryImpl.class);

	private XmppConnectionFactory xmppConnectionFactory;

	private ThreadLocal<XmppSessionImpl> sessionHolder = new ThreadLocal<XmppSessionImpl>();

	private boolean closed = false;

	public XmppSessionFactoryImpl() {
	}

	@PostConstruct
	protected void init() throws Exception {
		super.init();
		//
		AssertUtils.notNull(xmppConnectionFactory, "The XmppConnectionFactory is required");
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
				XmppConnectionFactoryImpl factory = (XmppConnectionFactoryImpl) xmppConnectionFactory;
				XmppConnectionFactoryImpl oldFactory = factory;
				factory = null;
				if (oldFactory != null) {
					oldFactory.close();
				}
			}
		} catch (Exception ex) {
			throw new XmppException("Cannot close XmppSessionFactory, general error", ex);
		}
	}
}
