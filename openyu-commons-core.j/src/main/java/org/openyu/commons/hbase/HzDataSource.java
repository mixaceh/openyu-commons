package org.openyu.commons.hbase;

import org.openyu.commons.service.BaseService;

import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;

/**
 * HBase ZooKeeper DataSource
 */
public interface HzDataSource extends BaseService {

	/**
	 * 取得連線
	 * 
	 * @return
	 * @throws ZooKeeperConnectionException
	 */
	HConnection getHConnection() throws ZooKeeperConnectionException;
}
