package org.openyu.commons.fto.commons.net.ftp;

import org.openyu.commons.commons.net.ftp.FtpClientSession;
import org.openyu.commons.fto.commons.net.ftp.ex.FtpClientTemplateException;

public interface FtpClientCallback<T> {

	T doInAction(FtpClientSession session) throws FtpClientTemplateException;
}
