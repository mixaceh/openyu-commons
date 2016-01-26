package org.openyu.commons.smack.impl;

import javax.annotation.PostConstruct;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import com.newegg.sso.service.impl.AbstractAppService;
import com.newegg.sso.util.AssertUtils;
import com.newegg.sso.util.xmpp.XmppCallback;
import com.newegg.sso.util.xmpp.XmppSession;
import com.newegg.sso.util.xmpp.XmppSessionFactory;
import com.newegg.sso.util.xmpp.XmppTemplate;
import com.newegg.sso.util.xmpp.exception.XmppTemplateException;

public class XmppTemplateImpl extends AbstractAppService implements XmppTemplate {

	private static transient final Logger LOGGER = LogManager.getLogger(XmppTemplateImpl.class);

	private XmppSessionFactory xmppSessionFactory;

	public XmppTemplateImpl(XmppSessionFactory xmppSessionFactory) {
		this.xmppSessionFactory = xmppSessionFactory;
	}

	public XmppTemplateImpl() {
		this(null);
	}

	@PostConstruct
	protected void init() throws Exception {
		super.init();
		//
		AssertUtils.notNull(xmppSessionFactory, "The XmppSessionFactory is required");
	}

	public XmppSessionFactory getXmppSessionFactory() {
		return xmppSessionFactory;
	}

	public void setXmppSessionFactory(XmppSessionFactory xmppSessionFactory) {
		this.xmppSessionFactory = xmppSessionFactory;
	}

	public XmppSession getSession() {
		try {
			return xmppSessionFactory.openSession();
		} catch (Exception ex) {
			throw new XmppTemplateException("Could not open XmppSession", ex);
		}
	}

	public void closeSession() {
		try {
			xmppSessionFactory.closeSession();
		} catch (Exception ex) {
			throw new XmppTemplateException("Could not close XmppSession", ex);
		}
	}

	public <T> T execute(XmppCallback<T> action) throws XmppTemplateException {
		return doExecute(action);
	}

	/**
	 * @param action
	 * @return
	 * @throws XmppTemplateException
	 */
	protected <T> T doExecute(XmppCallback<T> action) throws XmppTemplateException {
		Assert.notNull(action, "XmppCallback must not be null");
		//
		T result = null;
		XmppSession session = null;
		try {
			session = getSession();
			result = action.doInAction(session);
			return result;
		} catch (Exception ex) {
			throw new XmppTemplateException(ex);
		} finally {
			if (session != null) {
				closeSession();
			}
		}
	}

	// --------------------------------------------------

	public void sendMessage(final String userJID, final String text) throws NotConnectedException {
		execute(new XmppCallback<Object>() {
			public Object doInAction(XmppSession session) throws XmppTemplateException {
				try {
					session.sendMessage(userJID, text);
					return null;
				} catch (Exception ex) {
					throw new XmppTemplateException(ex);
				}
			}
		});
	}

	public void sendMessage(final String userJID, final Message message) throws NotConnectedException {
		execute(new XmppCallback<Object>() {
			public Object doInAction(XmppSession session) throws XmppTemplateException {
				try {
					session.sendMessage(userJID, message);
					return null;
				} catch (Exception ex) {
					throw new XmppTemplateException(ex);
				}
			}
		});
	}

}
