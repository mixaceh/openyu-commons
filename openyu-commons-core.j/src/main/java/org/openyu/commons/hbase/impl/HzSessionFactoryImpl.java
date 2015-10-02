package org.openyu.commons.hbase.impl;

import org.apache.hadoop.hbase.client.HConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.hbase.HzDataSource;
import org.openyu.commons.hbase.HzSession;
import org.openyu.commons.hbase.HzSessionFactory;
import org.openyu.commons.hbase.ex.HzException;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

public class HzSessionFactoryImpl extends BaseServiceSupporter implements
		HzSessionFactory {

	private static final long serialVersionUID = 4731628785810628545L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(HzSessionFactoryImpl.class);

	private HzDataSource hzDataSource;

	private ThreadLocal<HzSessionImpl> hzSessionHolder = new ThreadLocal<HzSessionImpl>();

	private boolean closed = false;

	public HzSessionFactoryImpl() {
	}

	public HzDataSource getHzDataSource() {
		return hzDataSource;
	}

	public void setHzDataSource(HzDataSource hzDataSource) {
		this.hzDataSource = hzDataSource;
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

	public HzSession openSession() throws HzException {
		HzSessionImpl result = null;
		try {
			result = hzSessionHolder.get();
			if (result == null) {
				HConnection hConnection = hzDataSource.getHConnection();
				result = new HzSessionImpl(this, hConnection);
				//
				hzSessionHolder.set(result);
			}
		} catch (Exception ex) {
			throw new HzException("Cannot get a HzSession, general error", ex);
		}
		return result;
	}

	public void closeSession() throws HzException {
		try {
			HzSessionImpl hzSession = hzSessionHolder.get();
			hzSessionHolder.set(null);
			if (hzSession != null) {
				hzSession.closed = true;
				if (hzSession.hConnection != null) {
					hzSession.hConnection.close();
				}
			}
		} catch (Exception ex) {
			throw new HzException("Cannot close HzSession, general error", ex);
		}
	}

	public synchronized void close() throws HzException {
		if (this.closed) {
			throw new HzException("HzSessionFactory was already closed");
		}
		//
		try {
			this.closed = true;
			// TODO hzSessionHolder 未清
			if (hzDataSource instanceof HzDataSourceImpl) {
				HzDataSourceImpl hzds = (HzDataSourceImpl) hzDataSource;
				HzDataSourceImpl oldhzds = hzds;
				hzds = null;
				if (oldhzds != null) {
					oldhzds.close();
				}
			}
		} catch (Exception ex) {
			throw new HzException(
					"Cannot close HzSessionFactory, general error", ex);
		}
	}
}
