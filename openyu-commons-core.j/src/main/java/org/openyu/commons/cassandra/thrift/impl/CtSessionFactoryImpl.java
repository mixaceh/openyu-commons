package org.openyu.commons.cassandra.thrift.impl;

import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.cassandra.thrift.CtDataSource;
import org.openyu.commons.cassandra.thrift.CtSession;
import org.openyu.commons.cassandra.thrift.CtSessionFactory;
import org.openyu.commons.cassandra.thrift.ex.CtException;
import org.openyu.commons.hbase.thrift.ex.HtException;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public class CtSessionFactoryImpl extends BaseServiceSupporter implements
		CtSessionFactory {

	private static final long serialVersionUID = -2668029567720936029L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(CtSessionFactoryImpl.class);

	private CtDataSource ctDataSource;

	private ThreadLocal<CtSessionImpl> ctSessionHolder = new ThreadLocal<CtSessionImpl>();

	private boolean closed = false;

	public CtSessionFactoryImpl() {
	}

	public CtDataSource getCtDataSource() {
		return ctDataSource;
	}

	public void setCtDataSource(CtDataSource ctDataSource) {
		this.ctDataSource = ctDataSource;
	}

	/**
	 * 內部初始
	 */
	@Override
	protected void doStart() throws Exception {

	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		close();
	}

	public CtSession openSession() throws CtException {
		CtSessionImpl result = null;
		try {
			result = ctSessionHolder.get();
			if (result == null) {
				TTransport ttransport = ctDataSource.getTTransport();
				result = new CtSessionImpl(this, ttransport);
				//
				ctSessionHolder.set(result);
			}
		} catch (Exception e) {
			throw new CtException("Cannot get a CtSession, general error", e);
		}
		return result;
	}

	public void closeSession() throws CtException {
		try {
			CtSessionImpl ctSession = ctSessionHolder.get();
			ctSessionHolder.set(null);
			if (ctSession != null) {
				ctSession.closed = true;
				if (ctSession.ttransport != null) {
					ctSession.ttransport.close();
				}
			}
		} catch (Exception e) {
			throw new CtException("Cannot close CtSession, general error", e);
		}
	}

	public synchronized void close() throws CtException {
		if (this.closed) {
			throw new CtException("CtSessionFactory was already closed");
		}
		//
		try {
			this.closed = true;
			// TODO htSessionHolder 未清
			if (ctDataSource instanceof CtDataSourceImpl) {
				CtDataSourceImpl htds = (CtDataSourceImpl) ctDataSource;
				CtDataSourceImpl oldhtds = htds;
				htds = null;
				if (oldhtds != null) {
					oldhtds.close();
				}
			}
		} catch (Exception ex) {
			throw new HtException(
					"Cannot close CtSessionFactory, general error", ex);
		}
	}
}
