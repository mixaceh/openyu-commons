package org.openyu.commons.smack.impl;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.smack.XmppCallback;
import org.openyu.commons.smack.XmppSession;
import org.openyu.commons.smack.XmppSessionFactory;
import org.openyu.commons.smack.XmppTemplate;
import org.openyu.commons.smack.ex.XmppTemplateException;
import org.openyu.commons.util.AssertHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XmppTemplateImpl extends BaseServiceSupporter implements XmppTemplate {

	private static final long serialVersionUID = -3622934785953049788L;

	private static transient final Logger LOGGER = LogManager.getLogger(XmppTemplateImpl.class);

	private XmppSessionFactory xmppSessionFactory;

	public XmppTemplateImpl(XmppSessionFactory xmppSessionFactory) {
		this.xmppSessionFactory = xmppSessionFactory;
	}

	public XmppTemplateImpl() {
		this(null);
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		// AssertHelper.notNull(xmppSessionFactory, "The XmppSessionFactory is
		// required");
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

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
		AssertHelper.notNull(action, "The XmppCallback must not be null");
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
