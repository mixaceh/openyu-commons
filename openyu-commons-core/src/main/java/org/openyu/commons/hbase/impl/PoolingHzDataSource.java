package org.openyu.commons.hbase.impl;

import java.util.NoSuchElementException;

import org.apache.commons.pool.ObjectPool;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.openyu.commons.hbase.HzDataSource;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoolingHzDataSource extends BaseServiceSupporter implements
		HzDataSource {

	private static final long serialVersionUID = -8322427494196434629L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(PoolingHzDataSource.class);

	private ObjectPool<HConnection> pool;

	public PoolingHzDataSource() {
		this(null);
	}

	public PoolingHzDataSource(ObjectPool<HConnection> pool) {
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

	public void setPool(ObjectPool<HConnection> pool)
			throws IllegalStateException, NullPointerException {
		if (null != this.pool)
			throw new IllegalStateException("Pool already set");
		if (null == pool) {
			throw new NullPointerException("Pool must not be null.");
		}
		this.pool = pool;
	}

	public HConnection getHConnection() throws ZooKeeperConnectionException {
		HConnection result = null;
		try {
			result = this.pool.borrowObject();
		} catch (ZooKeeperConnectionException e) {
			throw e;
		} catch (NoSuchElementException e) {
			throw new ZooKeeperConnectionException(
					"Cannot get a HConnection, pool error " + e.getMessage(), e);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new ZooKeeperConnectionException(
					"Cannot get a HConnection, general error", e);
		}
		return result;
	}

}
