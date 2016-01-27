package org.openyu.commons.smack.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.smack.XmppSession;
import org.openyu.commons.smack.ex.XmppSessionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XmppSessionImpl extends BaseModelSupporter implements XmppSession {


	private static final long serialVersionUID = -3354677815131036254L;

	private static transient final Logger LOGGER = LogManager.getLogger(XmppSessionImpl.class);

	protected boolean closed = false;

	protected XMPPConnection xmppConnection;

	private transient XmppSessionFactoryImpl xmppSessionFactoryImpl;

	private ChatManager delegate;

	public XmppSessionImpl(XmppSessionFactoryImpl xmppSessionFactoryImpl, XMPPConnection xmppConnection) {
		this.xmppSessionFactoryImpl = xmppSessionFactoryImpl;
		this.xmppConnection = xmppConnection;
		//
		this.delegate = ChatManager.getInstanceFor(xmppConnection);
	}

	public boolean isClosed() {
		return this.closed;
	}

	protected void errorIfClosed() {
		if (this.closed)
			throw new XmppSessionException("XmppSession was already closed");
	}

	public void close() throws XmppSessionException {
		if (isClosed()) {
			throw new XmppSessionException("XmppSession was already closed");
		}
		this.closed = true;
		//
		try {
			this.xmppSessionFactoryImpl.closeSession();
		} catch (Exception ex) {
			throw new XmppSessionException("Cannot close XmppSession");
		}
	}

	public boolean isConnected() {
		return (!isClosed()) && (this.xmppConnection != null) && (this.xmppConnection.isConnected());
	}

	@Override
	public void sendMessage(String userJID, String text) throws NotConnectedException {
		Chat chat = delegate.createChat(userJID);
		chat.sendMessage(text);
	}

	@Override
	public void sendMessage(String userJID, Message message) throws NotConnectedException {
		Chat chat = delegate.createChat(userJID);
		chat.sendMessage(message);
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("closed", closed);
		builder.append("xmppConnection", xmppConnection);
		return builder.toString();
	}

}
