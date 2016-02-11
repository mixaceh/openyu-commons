package org.openyu.commons.commons.net.ftp;


import org.openyu.commons.commons.net.ftp.ex.FtpClientException;
import org.openyu.commons.service.BaseService;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Apache Commons Net FTP FtpClient
 */
public interface FtpClientFactory extends BaseService {

	FTPClient createFTPClient() throws FtpClientException;
}
