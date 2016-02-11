package org.openyu.commons.thrift.impl;

import java.util.NoSuchElementException;

import org.apache.commons.pool.ObjectPool;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.thrift.ThriftDataSource;

public class PoolingThriftDataSource extends BaseServiceSupporter implements
		ThriftDataSource {

	private static final long serialVersionUID = 559316346864042639L;

	private ObjectPool<TTransport> pool;

	public PoolingThriftDataSource() {
		this(null);
	}

	public PoolingThriftDataSource(ObjectPool<TTransport> pool) {
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

	public void setPool(ObjectPool<TTransport> pool)
			throws IllegalStateException, NullPointerException {
		if (null != this.pool)
			throw new IllegalStateException("Pool already set");
		if (null == pool) {
			throw new NullPointerException("Pool must not be null.");
		}
		this.pool = pool;
	}

	public TTransport getTTransport() throws TTransportException {
		TTransport result = null;
		try {
			result = this.pool.borrowObject();
		} catch (TTransportException e) {
			throw e;
		} catch (NoSuchElementException e) {
			throw new TTransportException(
					"Cannot get a TTransport, pool error " + e.getMessage(), e);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new TTransportException(
					"Cannot get a TTransport, general error", e);
		}
		return result;
	}

}
