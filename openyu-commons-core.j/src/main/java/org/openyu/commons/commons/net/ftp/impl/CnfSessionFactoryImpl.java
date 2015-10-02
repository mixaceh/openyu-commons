package org.openyu.commons.commons.net.ftp.impl;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.commons.net.ftp.CnfDataSource;
import org.openyu.commons.commons.net.ftp.CnfSession;
import org.openyu.commons.commons.net.ftp.CnfSessionFactory;
import org.openyu.commons.commons.net.ftp.ex.CnfException;

public class CnfSessionFactoryImpl extends BaseServiceSupporter implements
		CnfSessionFactory {

	private static final long serialVersionUID = 5691760949867661150L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(CnfSessionFactoryImpl.class);

	private CnfDataSource cnfDataSource;

	private ThreadLocal<CnfSessionImpl> cnfSessionHolder = new ThreadLocal<CnfSessionImpl>();

	private boolean closed = false;

	public CnfSessionFactoryImpl() {
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

	public CnfDataSource getCnfDataSource() {
		return cnfDataSource;
	}

	public void setCnfDataSource(CnfDataSource cnfDataSource) {
		this.cnfDataSource = cnfDataSource;
	}

	public CnfSession openSession() throws CnfException {
		CnfSessionImpl result = null;
		try {
			result = cnfSessionHolder.get();
			if (result == null) {
				FTPClient hConnection = cnfDataSource.getFTPClient();
				result = new CnfSessionImpl(this, hConnection);
				//
				cnfSessionHolder.set(result);
			}
		} catch (Exception ex) {
			throw new CnfException("Cannot get a CnfSession, general error", ex);
		}
		return result;
	}

	public void closeSession() throws CnfException {
		try {
			CnfSessionImpl cnfSession = cnfSessionHolder.get();
			cnfSessionHolder.set(null);
			if (cnfSession != null) {
				cnfSession.closed = true;
				if (cnfSession.delegate != null) {
					cnfSession.delegate.disconnect();
				}
			}
		} catch (Exception ex) {
			throw new CnfException("Cannot close CnfSession, general error", ex);
		}
	}

	public synchronized void close() throws CnfException {
		if (this.closed) {
			throw new CnfException("CnfSessionFactory was already closed");
		}
		//
		try {
			this.closed = true;
			// TODO cnfSessionHolder 未清
			if (cnfDataSource instanceof CnfDataSourceImpl) {
				CnfDataSourceImpl cnfds = (CnfDataSourceImpl) cnfDataSource;
				CnfDataSourceImpl oldcnfds = cnfds;
				cnfds = null;
				if (oldcnfds != null) {
					oldcnfds.close();
				}
			}
		} catch (Exception ex) {
			throw new CnfException(
					"Cannot close CnfSessionFactory, general error", ex);
		}
	}

}
