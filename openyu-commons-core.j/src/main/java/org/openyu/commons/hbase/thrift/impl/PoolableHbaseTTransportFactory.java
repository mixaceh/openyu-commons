package org.openyu.commons.hbase.thrift.impl;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.thrift.TTransportFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public class PoolableHbaseTTransportFactory extends BaseServiceSupporter implements PoolableObjectFactory<TTransport> {

	private static final long serialVersionUID = -3233577852753579687L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(PoolableHbaseTTransportFactory.class);

	protected volatile TTransportFactory ttransportFactory = null;

	protected volatile ObjectPool<TTransport> pool = null;

	private boolean compactProtocol;

	public PoolableHbaseTTransportFactory(TTransportFactory ttransportFactory, ObjectPool<TTransport> pool,
			boolean compactProtocol) {
		this.ttransportFactory = ttransportFactory;
		this.pool = pool;
		this.pool.setFactory(this);
		//
		this.compactProtocol = compactProtocol;
	}

	public void setTTransportFactory(TTransportFactory ttransportFactory) {
		this.ttransportFactory = ttransportFactory;
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
			throw new IllegalStateException("TTransport factory returned null from createTTransport");
		}
		try {
			result = new PoolableHbaseTTransport(result, this.pool, this.compactProtocol);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = null;
		}
		return result;
	}

	public void destroyObject(TTransport obj) throws Exception {
		if (obj instanceof PoolableHbaseTTransport) {
			((PoolableHbaseTTransport) obj).reallyClose();
		}
	}

	public boolean validateObject(TTransport obj) {
		try {
			// TODO 只要連過線,之後斷線還是true
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
