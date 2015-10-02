package org.openyu.commons.hbase.thrift.impl;

import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.hbase.thrift.ex.HtException;
import org.openyu.commons.hbase.thrift.HtSession;
import org.openyu.commons.hbase.thrift.HtSessionFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.hbase.thrift.HtDataSource;

public class HtSessionFactoryImpl extends BaseServiceSupporter implements
		HtSessionFactory {

	private static final long serialVersionUID = -5668328627813319909L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(HtSessionFactoryImpl.class);

	private HtDataSource htDataSource;

	private ThreadLocal<HtSessionImpl> htSessionHolder = new ThreadLocal<HtSessionImpl>();

	private boolean closed = false;

	public HtSessionFactoryImpl() {
	}

	public HtDataSource getHtDataSource() {
		return htDataSource;
	}

	public void setHtDataSource(HtDataSource htDataSource) {
		this.htDataSource = htDataSource;
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
		close();
	}

	public HtSession openSession() throws HtException {
		HtSessionImpl result = null;
		try {
			result = htSessionHolder.get();
			if (result == null) {
				TTransport ttransport = htDataSource.getTTransport();
				result = new HtSessionImpl(this, ttransport);
				//
				htSessionHolder.set(result);
			}
		} catch (Exception e) {
			throw new HtException("Cannot get a HtSession, general error", e);
		}
		return result;
	}

	public void closeSession() throws HtException {
		try {
			HtSessionImpl htSession = htSessionHolder.get();
			htSessionHolder.set(null);
			if (htSession != null) {
				htSession.closed = true;
				if (htSession.ttransport != null) {
					htSession.ttransport.close();
				}
			}
		} catch (Exception e) {
			throw new HtException("Cannot close HtSession, general error", e);
		}
	}

	public synchronized void close() throws HtException {
		if (this.closed) {
			throw new HtException("HtSessionFactory was already closed");
		}
		//
		try {
			this.closed = true;
			// TODO htSessionHolder 未清
			if (htDataSource instanceof HtDataSourceImpl) {
				HtDataSourceImpl htds = (HtDataSourceImpl) htDataSource;
				HtDataSourceImpl oldhtds = htds;
				htds = null;
				if (oldhtds != null) {
					oldhtds.close();
				}
			}
		} catch (Exception ex) {
			throw new HtException(
					"Cannot close HtSessionFactory, general error", ex);
		}
	}
}
