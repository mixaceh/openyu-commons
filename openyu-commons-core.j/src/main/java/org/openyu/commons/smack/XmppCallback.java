package org.openyu.commons.smack;

import org.openyu.commons.smack.ex.XmppTemplateException;

public interface XmppCallback<T> {

	T doInAction(XmppSession session) throws XmppTemplateException;
}
