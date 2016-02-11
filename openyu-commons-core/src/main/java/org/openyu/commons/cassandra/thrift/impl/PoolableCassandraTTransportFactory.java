package org.openyu.commons.cassandra.thrift.impl;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.thrift.TTransportFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public class PoolableCassandraTTransportFactory extends BaseServiceSupporter
		implements PoolableObjectFactory<TTransport> {

	private static final long serialVersionUID = 3395957731496515732L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(PoolableCassandraTTransportFactory.class);

	protected volatile TTransportFactory ttransportFactory = null;

	protected volatile ObjectPool<TTransport> pool = null;

	private boolean compactProtocol;

	public PoolableCassandraTTransportFactory(
			TTransportFactory ttransportFactory, ObjectPool<TTransport> pool,
			boolean compactProtocol) {
		this.ttransportFactory = ttransportFactory;
		this.pool = pool;
		this.pool.setFactory(this);
		//
		this.compactProtocol = compactProtocol;
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

	public void setTTransportFactory(TTransportFactory ttransportFactory) {
		this.ttransportFactory = ttransportFactory;
	}

	public synchronized void setPool(ObjectPool<TTransport> pool) {
		if ((null != this.pool) && (pool != this.pool))
			try {
				this.pool.close();
			} catch (Exception e) {
			}
		this.pool = pool;
	}

	public synchronized ObjectPool<TTransport> getPool() {
		return this.pool;
	}

	public TTransport makeObject() throws Exception {
		TTransport result = ttransportFactory.createTTransport();
		if (result == null) {
			throw new IllegalStateException(
					"TTransport factory returned null from createTTransport");
		}
		try {
			result = new PoolableCassandraTTransport(result, this.pool,
					this.compactProtocol);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = null;
		}
		return result;
	}

	public void destroyObject(TTransport obj) throws Exception {
		if (obj instanceof PoolableCassandraTTransport) {
			((PoolableCassandraTTransport) obj).reallyClose();
		}
	}

	public boolean validateObject(TTransport obj) {
		try {
			if (obj.isOpen()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public void activateObject(TTransport obj) throws Exception {
	}

	public void passivateObject(TTransport obj) throws Exception {
	}

}
