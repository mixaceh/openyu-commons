package org.openyu.commons.commons.net.ftp;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.SystemHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class FTPClientTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	/**
	 * native
	 * 
	 * @return
	 */
	public static FTPClient createFTPClient() throws Exception {
		FTPClient result = null;
		result = new FTPClient();
		//
		result.setConnectTimeout(15000);
		result.setSendBufferSize(128 * 1024);
		result.setReceiveBufferSize(128 * 1024);
		//
		result.connect("127.0.0.1", 21);
		boolean login = result.login("root", "1111");
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
		result.changeWorkingDirectory("inbound/");
		//
		return result;
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.90 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.80, time.warmup: 0.00,
	// time.bench: 1.80
	public void ftpClient() throws Exception {
		FTPClient ftpClient = createFTPClient();
		System.out.println(ftpClient);
		assertNotNull(ftpClient);
	}

	@Test
	// 10 times: 21933 mills.
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 1.77 [+- 0.00], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.77, time.warmup: 0.00,
	// time.bench: 1.77
	public void listNames() throws Exception {
		FTPClient ftpClient = createFTPClient();
		System.out.println(ftpClient);
		//
		String[] result = ftpClient.listNames();
		//
		SystemHelper.println(result);
		ftpClient.disconnect();
	}
}
