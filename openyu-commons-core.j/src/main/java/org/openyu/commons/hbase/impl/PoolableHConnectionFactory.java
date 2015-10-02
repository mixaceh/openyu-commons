package org.openyu.commons.hbase.impl;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.hbase.HConnectionFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public class PoolableHConnectionFactory extends BaseServiceSupporter implements
		PoolableObjectFactory<HConnection> {

	private static final long serialVersionUID = -9073293273204183872L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(PoolableHConnectionFactory.class);

	protected volatile HConnectionFactory hConnectionFactory = null;

	protected volatile ObjectPool<HConnection> pool = null;

	private Configuration configuration;

	public PoolableHConnectionFactory(HConnectionFactory hConnectionFactory,
			ObjectPool<HConnection> pool, Configuration configuration) {
		this.hConnectionFactory = hConnectionFactory;
		this.pool = pool;
		this.pool.setFactory(this);
		//
		this.configuration = configuration;
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

	public void setHConnectionFactory(HConnectionFactory hConnectionFactory) {
		this.hConnectionFactory = hConnectionFactory;
	}

	public synchronized void setPool(ObjectPool<HConnection> pool) {
		if ((null != this.pool) && (pool != this.pool))
			try {
				this.pool.close();
			} catch (Exception e) {
			}
		this.pool = pool;
	}

	public synchronized ObjectPool<HConnection> getPool() {
		return this.pool;
	}

	public HConnection makeObject() throws Exception {
		HConnection result = hConnectionFactory.createHConnection();
		if (result == null) {
			throw new IllegalStateException(
					"HConnection factory returned null from createHConnection");
		}
		try {
			result = new PoolableHConnection(result, this.pool);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = null;
		}
		return result;
	}

	public void destroyObject(HConnection obj) throws Exception {
		if (obj instanceof PoolableHConnection) {
			((PoolableHConnection) obj).reallyClose();
		}
	}

	public boolean validateObject(HConnection obj) {
		try {
			if (!(obj.isClosed())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public void activateObject(HConnection obj) throws Exception {
	}

	public void passivateObject(HConnection obj) throws Exception {
	}

}
