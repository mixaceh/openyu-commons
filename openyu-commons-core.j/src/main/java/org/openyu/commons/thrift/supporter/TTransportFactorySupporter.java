package org.openyu.commons.thrift.supporter;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.thrift.TTransportFactory;
import org.openyu.commons.nio.NioHelper;

public abstract class TTransportFactorySupporter extends BaseServiceSupporter
		implements TTransportFactory {

	private static final long serialVersionUID = 7554826535443090258L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(TTransportFactorySupporter.class);

	protected String ip;

	protected int port;

	protected int timeout;

	protected Socket socket;

	protected int retryNumber = NioHelper.DEFAULT_RETRY_NUMBER;

	protected long retryPauseMills = NioHelper.DEFAULT_RETRY_PAUSE_MILLS;

	public TTransportFactorySupporter(String ip, int port, int timeout,
			int retryNumber, long retryPauseMills) {
		this.ip = ip;
		this.port = port;
		this.timeout = timeout;
		this.retryNumber = retryNumber;
		this.retryPauseMills = retryPauseMills;
	}

	public TTransportFactorySupporter(String ip, int port) {
		this(ip, port, 0, NioHelper.DEFAULT_RETRY_NUMBER,
				NioHelper.DEFAULT_RETRY_PAUSE_MILLS);
	}

	public TTransportFactorySupporter(Socket socket) {
		this.socket = socket;
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
}
