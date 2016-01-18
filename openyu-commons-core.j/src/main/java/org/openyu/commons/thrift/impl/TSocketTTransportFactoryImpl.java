package org.openyu.commons.thrift.impl;

import java.net.Socket;

import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.openyu.commons.thrift.TTransportFactory;
import org.openyu.commons.thrift.supporter.TTransportFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.nio.NioHelper;
import org.openyu.commons.thread.ThreadHelper;

/**
 * blockingTransport
 */
public class TSocketTTransportFactoryImpl extends TTransportFactorySupporter implements TTransportFactory {

	private static final long serialVersionUID = 627149637168825381L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(TSocketTTransportFactoryImpl.class);

	public TSocketTTransportFactoryImpl(String ip, int port, int timeout, int retryNumber, long retryPauseMills) {
		super(ip, port, timeout, retryNumber, retryPauseMills);
	}

	public TSocketTTransportFactoryImpl(String ip, int port) {
		super(ip, port);
	}

	public TSocketTTransportFactoryImpl(Socket socket) {
		super(socket);
	}

	public TTransport createTTransport() throws TTransportException {
		TTransport result = createConneciton();
		//
		int tries = 0;
		boolean connected = false;
		Exception cause = null;
		for (;;) {
			try {
				result.open();
				connected = true;
				break;
			} catch (Exception ex) {
				cause = ex;
				// 當連線失敗時,重試
				// ex.printStackTrace();
				result = createConneciton();
				//
				++tries;
				// [1/3] time(s) java.net.ConnectException: Connection refused:
				// connect
				LOGGER.warn("[" + tries + "/" + (this.retryNumber != 0 ? this.retryNumber : "INFINITE")
						+ "] time(s) Failed to get the connection");
				// 0=無限
				if (this.retryNumber != 0 && tries >= this.retryNumber) {
					break;
				}
				// 重試暫停毫秒
				ThreadHelper.sleep(NioHelper.retryPause(tries, this.retryPauseMills));
				// Retrying connect to [172.22.30.12:9099]. Already tried [2/3]
				// time(s).
				LOGGER.info("Retrying connect to [" + this.ip + ":" + this.port + "]. Already tried [" + (tries + 1)
						+ "/" + (this.retryNumber != 0 ? this.retryNumber : "INFINITE") + "] time(s)");
			}
		}
		//
		if (!connected) {
			LOGGER.error("Can't connect to [" + this.ip + ":" + this.port + "]");
			throw new TTransportException(cause);
		}
		return result;
	}
}
