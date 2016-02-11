package org.openyu.commons.commons.net.ftp;

import org.openyu.commons.service.BaseService;
import org.openyu.commons.commons.net.ftp.ex.FtpClientException;

public interface FtpClientSessionFactory extends BaseService {

	FtpClientSession openSession() throws FtpClientException;

	void closeSession() throws FtpClientException;

	void close() throws FtpClientException;

}
