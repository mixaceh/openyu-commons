package org.openyu.commons.hbase.impl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.hbase.HConnectionFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * HBase ZooKeeper HConnection
 */
public class HConnectionFactoryImpl extends BaseServiceSupporter implements
		HConnectionFactory {

	private static final long serialVersionUID = 6177089661692484025L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(HConnectionFactoryImpl.class);

	private volatile Configuration configuration;

	public HConnectionFactoryImpl(Configuration configuration) {
		this.configuration = configuration;
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

	public HConnection createHConnection() throws ZooKeeperConnectionException {
		HConnection result = null;
		try {
			Configuration connConf = HBaseConfiguration.create(configuration);
			result = HConnectionManager.createConnection(connConf);
			result.getMaster();
		} catch (Exception ex) {
			throw new ZooKeeperConnectionException(ex.getMessage(), ex);
		}
		return result;
	}
}
