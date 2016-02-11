package org.openyu.commons.thrift;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.openyu.commons.service.BaseService;

/**
 * Apache Thrift DataSource
 */
public interface ThriftDataSource extends BaseService {

	/**
	 * 取得連線
	 * 
	 * @return
	 * @throws TTransportException
	 */
	TTransport getTTransport() throws TTransportException;
}
