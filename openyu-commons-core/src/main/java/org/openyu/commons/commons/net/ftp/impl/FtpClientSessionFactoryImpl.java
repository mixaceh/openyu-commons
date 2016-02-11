package org.openyu.commons.commons.net.ftp.impl;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.commons.net.ftp.FtpClientConnectionFactory;
import org.openyu.commons.commons.net.ftp.FtpClientSession;
import org.openyu.commons.commons.net.ftp.FtpClientSessionFactory;
import org.openyu.commons.commons.net.ftp.ex.FtpClientException;

public class FtpClientSessionFactoryImpl extends BaseServiceSupporter implements FtpClientSessionFactory {

	private static final long serialVersionUID = 5691760949867661150L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(FtpClientSessionFactoryImpl.class);

	private FtpClientConnectionFactory ftpClientConnectionFactory;

	private ThreadLocal<FtpClientSessionImpl> sessionHolder = new ThreadLocal<FtpClientSessionImpl>();

	private boolean closed = false;

	public FtpClientSessionFactoryImpl() {
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		AssertHelper.notNull(ftpClientConnectionFactory, "The FtpClientConnectionFactory is required");
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		close();
	}

	public FtpClientConnectionFactory getFtpClientConnectionFactory() {
		return ftpClientConnectionFactory;
	}

	public void setFtpClientConnectionFactory(FtpClientConnectionFactory ftpClientConnectionFactory) {
		this.ftpClientConnectionFactory = ftpClientConnectionFactory;
	}

	public FtpClientSession openSession() throws FtpClientException {
		FtpClientSessionImpl result = null;
		try {
			result = sessionHolder.get();
			if (result == null) {
				FTPClient ftpClient = ftpClientConnectionFactory.getFTPClient();
				result = new FtpClientSessionImpl(this, ftpClient);
				//
				sessionHolder.set(result);
			}
		} catch (Exception ex) {
			throw new FtpClientException("Cannot get a FtpClientSession, general error", ex);
		}
		return result;
	}

	public void closeSession() throws FtpClientException {
		try {
			FtpClientSessionImpl session = sessionHolder.get();
			sessionHolder.set(null);
			if (session != null) {
				session.closed = true;
				if (session.delegate != null) {
					session.delegate.disconnect();
				}
			}
		} catch (Exception ex) {
			throw new FtpClientException("Cannot close FtpClientSession, general error", ex);
		}
	}

	public synchronized void close() throws FtpClientException {
		if (this.closed) {
			throw new FtpClientException("FtpClientSessionFactory was already closed");
		}
		//
		try {
			this.closed = true;
			// TODO sessionHolder 未清
			if (ftpClientConnectionFactory instanceof FtpClientConnectionFactoryImpl) {
				FtpClientConnectionFactoryImpl oldFactory = (FtpClientConnectionFactoryImpl) ftpClientConnectionFactory;
				ftpClientConnectionFactory = null;
				if (oldFactory != null) {
					oldFactory.close();
				}

			}
		} catch (Exception ex) {
			throw new FtpClientException("Cannot close FtpClientSessionFactory, general error", ex);
		}
	}

}
