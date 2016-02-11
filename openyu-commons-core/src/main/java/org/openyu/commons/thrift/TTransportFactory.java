package org.openyu.commons.thrift;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.openyu.commons.service.BaseService;

/**
 * Apache Thrift TTransport
 */
public interface TTransportFactory extends BaseService {

	TTransport createTTransport() throws TTransportException;
}
