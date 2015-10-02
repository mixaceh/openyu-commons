package org.openyu.commons.commons.net.ftp.impl;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool.ObjectPool;
import org.openyu.commons.commons.net.ftp.CnfDataSource;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public class PoolingCnfDataSource extends BaseServiceSupporter implements
		CnfDataSource {

	private static final long serialVersionUID = 4671468693002418428L;

	private ObjectPool<FTPClient> pool;

	public PoolingCnfDataSource() {
		this(null);
	}

	public PoolingCnfDataSource(ObjectPool<FTPClient> pool) {
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

	public void setPool(ObjectPool<FTPClient> pool)
			throws IllegalStateException, NullPointerException {
		if (null != this.pool)
			throw new IllegalStateException("Pool already set");
		if (null == pool) {
			throw new NullPointerException("Pool must not be null.");
		}
		this.pool = pool;
	}

	public FTPClient getFTPClient() throws SocketException, IOException {
		FTPClient result = null;
		try {
			result = this.pool.borrowObject();
		} catch (SocketException e) {
			throw e;
		} catch (IOException e) {
			throw new IOException("Cannot get a FTPClient, pool error "
					+ e.getMessage(), e);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException("Cannot get a FTPClient, general error", e);
		}
		return result;
	}

}
