package org.openyu.commons.smack;

import org.jivesoftware.smack.packet.Message;
import org.openyu.commons.service.BaseService;
import org.openyu.commons.smack.ex.XmppTemplateException;

public interface XmppTemplate extends BaseService {

	XmppSessionFactory getXmppSessionFactory();

	void setXmppSessionFactory(XmppSessionFactory xmppSessionFactory);

	XmppSession getSession();

	void closeSession();

	<T> T execute(XmppCallback<T> action) throws XmppTemplateException;

	// --------------------------------------------------

	void sendMessage(String userJID, String text) throws XmppTemplateException;

	void sendMessage(String userJID, Message message) throws XmppTemplateException;

}
