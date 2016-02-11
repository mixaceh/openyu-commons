package org.openyu.commons.cassandra.thrift.impl;

import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.cassandra.thrift.CtDataSource;
import org.openyu.commons.thrift.TTransportFactory;
import org.openyu.commons.thrift.supporter.ThriftDataSourceSupporter;

public class CtDataSourceImpl extends ThriftDataSourceSupporter
		implements CtDataSource {

	private static final long serialVersionUID = -1220445727844269856L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(CtDataSourceImpl.class);

	public CtDataSourceImpl() {
	}

	protected void createPoolableTTransportFactory(
			TTransportFactory ttransportFactory) throws TTransportException {
		PoolableCassandraTTransportFactory result = null;
		try {
			this.dataSourceType = "CtDataSource";
			//
			result = new PoolableCassandraTTransportFactory(ttransportFactory,
					this.objectPool, this.compactProtocol);
			validateConnectionFactory(result);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new TTransportException(
					new StringBuilder()
							.append("Cannot create PoolableCassandraTTransportFactory (")
							.append(e.getMessage()).append(")").toString(), e);
		}
	}

}
