package org.openyu.commons.commons.net.ftp;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.openyu.commons.service.BaseService;

/**
 * Apache Commons Net FTP DataSource
 */
public interface CnfDataSource extends BaseService {

	/**
	 * 取得連線
	 * 
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	FTPClient getFTPClient() throws SocketException, IOException;
}
