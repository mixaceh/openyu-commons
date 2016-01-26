package org.openyu.commons.smack;

import java.io.Closeable;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.openyu.commons.model.BaseModel;
import org.openyu.commons.smack.ex.XmppSessionException;

public interface XmppSession extends BaseModel, Closeable {

	void close() throws XmppSessionException;

	boolean isClosed();

	boolean isConnected();

	// --------------------------------------------------
	// 由此控制native方法是否開放給end使用
	// --------------------------------------------------
	void sendMessage(String userJID, String text) throws NotConnectedException;

	void sendMessage(String userJID, Message message) throws NotConnectedException;

}
