package org.openyu.commons.smack;

import org.openyu.commons.service.BaseService;
import org.openyu.commons.smack.ex.XmppException;

public interface XmppSessionFactory extends BaseService {

	XmppSession openSession() throws XmppException;

	void closeSession() throws XmppException;

	void close() throws XmppException;

}
