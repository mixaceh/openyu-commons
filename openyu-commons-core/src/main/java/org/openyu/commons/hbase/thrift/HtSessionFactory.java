package org.openyu.commons.hbase.thrift;

import org.openyu.commons.hbase.thrift.ex.HtException;
import org.openyu.commons.service.BaseService;

public interface HtSessionFactory extends BaseService {

	HtSession openSession() throws HtException;

	void closeSession() throws HtException;

	void close() throws HtException;

}
