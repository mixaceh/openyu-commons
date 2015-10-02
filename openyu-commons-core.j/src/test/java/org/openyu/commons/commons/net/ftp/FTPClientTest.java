package org.openyu.commons.commons.net.ftp;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;
import org.openyu.commons.lang.SystemHelper;

public class FTPClientTest {

	// native
	public static FTPClient createFTPClient() {
		FTPClient result = null;
		try {
			result = new FTPClient();
			//
			result.setConnectTimeout(5000);
			result.setSendBufferSize(128 * 1024);
			result.setReceiveBufferSize(128 * 1024);
			//
			result.connect("10.1.24.143", 21);
			boolean login = result.login("mktftp", "Mkt2BestDev");
			if (!login) {
				System.out.println("login: " + login);
				return null;
			}
			//
			int reply = result.getReplyCode();
			// FTPReply stores a set of constants for FTP reply codes.
			if (!FTPReply.isPositiveCompletion(reply)) {
				result.disconnect();
				return null;
			}
			//
			result.enterLocalPassiveMode();
			result.setFileType(FTPClient.BINARY_FILE_TYPE);
			result.setControlEncoding("UTF-8");
			result.changeWorkingDirectory("MKTPLS_Batch/inbound/Inventory/");
			//
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Test
	public void ftpClient() {
		FTPClient ftpClient = createFTPClient();
		System.out.println(ftpClient);
		assertNotNull(ftpClient);
	}

	@Test
	// 10 times: 21933 mills.
	public void listNames() throws Exception {
		int count = 10;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			FTPClient ftpClient = createFTPClient();
			System.out.println(ftpClient);
			//
			String[] result = ftpClient.listNames();
			//
			SystemHelper.println(result);
			ftpClient.disconnect();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}
}
