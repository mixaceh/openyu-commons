package org.openyu.commons.commons.net.ftp.impl;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool.ObjectPool;
import org.openyu.commons.commons.net.ftp.FtpClientConnectionFactory;
import org.openyu.commons.commons.net.ftp.ex.FtpClientException;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public class PoolingFtpClientConnectionFactory extends BaseServiceSupporter implements FtpClientConnectionFactory {

	private static final long serialVersionUID = 4671468693002418428L;

	private ObjectPool<FTPClient> pool;

	public PoolingFtpClientConnectionFactory() {
		this(null);
	}

	public PoolingFtpClientConnectionFactory(ObjectPool<FTPClient> pool) {
		this.pool = pool;
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

	public void setPool(ObjectPool<FTPClient> pool) throws IllegalStateException, NullPointerException {
		if (null != this.pool)
			throw new IllegalStateException("Pool already set");
		if (null == pool) {
			throw new NullPointerException("Pool must not be null.");
		}
		this.pool = pool;
	}

	public FTPClient getFTPClient() throws FtpClientException {
		FTPClient result = null;
		try {
			result = this.pool.borrowObject();
		} catch (Exception e) {
			throw new FtpClientException("Cannot get a FTPClient, pool error " + e.getMessage(), e);
		}
		return result;
	}

}
