package org.openyu.commons.commons.net.ftp.impl;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.openyu.commons.commons.net.ftp.FtpClientFactory;
import org.openyu.commons.commons.net.ftp.ex.FtpClientException;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.nio.NioHelper;
import org.openyu.commons.thread.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpClientFactoryImpl extends BaseServiceSupporter implements FtpClientFactory {

	private static final long serialVersionUID = -6031828409170764020L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(FtpClientFactoryImpl.class);

	private String ip;

	private int port;

	private int connectTimeout;

	private int retryNumber = NioHelper.DEFAULT_RETRY_NUMBER;

	private long retryPauseMills = NioHelper.DEFAULT_RETRY_PAUSE_MILLS;

	private String username;

	private String password;

	private int bufferSize;

	private int clientMode;

	private int fileType;

	private String controlEncoding;

	private String remotePath;

	public FtpClientFactoryImpl(String ip, int port, int connectTimeout, int retryNumber, long retryPauseMills,
			String username, String password, int bufferSize, int clientMode, int fileType, String controlEncoding,
			String remotePath) {
		this.ip = ip;
		this.port = port;
		this.connectTimeout = connectTimeout;
		this.retryNumber = retryNumber;
		this.retryPauseMills = retryPauseMills;
		this.username = username;
		this.password = password;
		this.bufferSize = bufferSize;
		this.clientMode = clientMode;
		this.fileType = fileType;
		this.controlEncoding = controlEncoding;
		this.remotePath = remotePath;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {

	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}

	public FTPClient createFTPClient() throws FtpClientException {
		FTPClient result = null;
		//
		try {
			result = createConneciton();
			int tries = 0;
			boolean success = false;
			for (;;) {
				try {
					result.connect(this.ip, this.port);
					success = true;
					break;
				} catch (ConnectException ex) {
					// 當連線失敗時,重試
					// ex.printStackTrace();
					result = createConneciton();
					//
					++tries;
					// [1/3] time(s) java.net.ConnectException: Connection
					// refused:
					// connect
					LOGGER.warn("[" + tries + "/" + (this.retryNumber != 0 ? this.retryNumber : "INFINITE")
							+ "] time(s) Failed to get the connection");
					// 0=無限
					if (this.retryNumber != 0 && tries >= this.retryNumber) {
						break;
					}
					// 重試暫停毫秒
					ThreadHelper.sleep(NioHelper.retryPause(tries, this.retryPauseMills));
					// Retrying connect to [127.0.0.1:21]. Already tried [1/3]
					// time(s).
					LOGGER.info("Retrying connect to [" + ip + ":" + port + "]. Already tried [" + (tries + 1) + "/"
							+ (this.retryNumber != 0 ? this.retryNumber : "INFINITE") + "] time(s)");
				}
			}
			//
			if (!success) {
				LOGGER.error("Can't connect to [" + this.ip + ":" + this.port + "]");
				throw new FtpClientException("Connection refused");
			} else {
				boolean login = false;
				try {
					login = result.login(this.username, this.password);
					if (!login) {
						LOGGER.error("[" + this.username + "] Can't login to [" + this.ip + ":" + this.port + "]");
						throw new SocketException("Login failed");
					}
					//
					int reply = result.getReplyCode();
					if (!FTPReply.isPositiveCompletion(reply)) {
						throw new SocketException("Reply failed, reply: " + reply);
					}
					//
					result.setBufferSize(this.bufferSize);
					switch (this.clientMode) {
					case 0:
						result.enterLocalActiveMode();
						break;
					case 2:
						result.enterLocalPassiveMode();
						break;
					}
					result.setFileType(this.fileType);
					result.setControlEncoding(this.controlEncoding);
					result.changeWorkingDirectory(this.remotePath);
				} catch (Exception e) {
					try {
						if (login) {
							result.logout();
						}
						if (result.isConnected()) {
							result.disconnect();
						}
					} catch (Exception e2) {
					}
					//
					throw new FtpClientException(e);
				}
			}
		} catch (Exception e) {
			throw new FtpClientException(e);
		}
		return result;
	}

	/**
	 * 建立連線
	 * 
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	protected FTPClient createConneciton() throws Exception {
		FTPClient result = new FTPClient();
		//
		result.setConnectTimeout(this.connectTimeout);
		result.setSendBufferSize(this.bufferSize);
		result.setReceiveBufferSize(this.bufferSize);
		return result;
	}
}
