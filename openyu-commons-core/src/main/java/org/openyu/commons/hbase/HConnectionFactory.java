package org.openyu.commons.hbase;

import org.openyu.commons.service.BaseService;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;

/**
 * HBase ZooKeeper HConnection
 */
public interface HConnectionFactory extends BaseService {

	HConnection createHConnection() throws ZooKeeperConnectionException;
}
