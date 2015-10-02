package org.openyu.commons.commons.net.ftp;

import java.io.IOException;
import java.net.SocketException;
import org.openyu.commons.service.BaseService;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Apache Commons Net FTP FtpClient
 */
public interface FtpClientFactory extends BaseService {

	FTPClient createFTPClient() throws SocketException, IOException;
}
