package org.openyu.commons.commons.net.ftp;

import org.openyu.commons.commons.net.ftp.ex.FtpClientTemplateException;

public interface FtpClientCallback<T> {

	T doInAction(FtpClientSession session) throws FtpClientTemplateException;
}
