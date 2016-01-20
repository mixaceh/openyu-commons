package org.openyu.commons.commons.net.ftp.impl;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.commons.net.ftp.FtpClientFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public class PoolableFtpClientFactory extends BaseServiceSupporter implements PoolableObjectFactory<FTPClient> {

	private static final long serialVersionUID = -732567508101509392L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(PoolableFtpClientFactory.class);

	protected volatile FtpClientFactory ftpClientFactory = null;

	protected volatile ObjectPool<FTPClient> pool = null;

	public PoolableFtpClientFactory(FtpClientFactory ftpClientFactory, ObjectPool<FTPClient> pool) {
		this.ftpClientFactory = ftpClientFactory;
		this.pool = pool;
		this.pool.setFactory(this);
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

	public void setFtpClientFactory(FtpClientFactory ftpClientFactory) {
		this.ftpClientFactory = ftpClientFactory;
	}

	public synchronized void setPool(ObjectPool<FTPClient> pool) {
		if ((null != this.pool) && (pool != this.pool))
			try {
				this.pool.close();
			} catch (Exception e) {
			}
		this.pool = pool;
	}

	public synchronized ObjectPool<FTPClient> getPool() {
		return this.pool;
	}

	public FTPClient makeObject() throws Exception {
		FTPClient result = ftpClientFactory.createFTPClient();
		if (result == null) {
			throw new IllegalStateException("FTPClient factory returned null from createFTPClient");
		}
		try {
			result = new PoolableFtpClient(result, this.pool);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = null;
		}
		return result;
	}

	public void destroyObject(FTPClient obj) throws Exception {
		if (obj != null) {
			if (obj instanceof PoolableFtpClient) {
				PoolableFtpClient conn = (PoolableFtpClient) obj;
				conn.reallyClose();
			}
		}
	}

	public boolean validateObject(FTPClient obj) {
		try {
			// 只要連過線,之後斷線還是true
			// if (obj.isConnected()) {
			// #fix
			if (obj.sendNoOp()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public void activateObject(FTPClient obj) throws Exception {
	}

	public void passivateObject(FTPClient obj) throws Exception {
	}

}
