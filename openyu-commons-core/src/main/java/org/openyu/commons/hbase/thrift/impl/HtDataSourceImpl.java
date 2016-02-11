package org.openyu.commons.hbase.thrift.impl;

import org.apache.thrift.transport.TTransportException;
import org.openyu.commons.hbase.thrift.HtDataSource;
import org.openyu.commons.thrift.TTransportFactory;
import org.openyu.commons.thrift.supporter.ThriftDataSourceSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtDataSourceImpl extends ThriftDataSourceSupporter implements
		HtDataSource {

	private static final long serialVersionUID = 3275237365919295880L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(HtDataSourceImpl.class);

	public HtDataSourceImpl() {
	}

	protected void createPoolableTTransportFactory(
			TTransportFactory ttransportFactory) throws TTransportException {
		PoolableHbaseTTransportFactory result = null;
		try {
			this.dataSourceType="HtDataSource";
			//
			result = new PoolableHbaseTTransportFactory(ttransportFactory,
					this.objectPool, this.compactProtocol);
			validateConnectionFactory(result);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new TTransportException(new StringBuilder()
					.append("Cannot create PoolableHbaseTTransportFactory (")
					.append(e.getMessage()).append(")").toString(), e);
		}
	}

}
