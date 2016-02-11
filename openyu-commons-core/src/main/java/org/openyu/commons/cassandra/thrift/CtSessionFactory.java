package org.openyu.commons.cassandra.thrift;

import org.openyu.commons.cassandra.thrift.ex.CtException;
import org.openyu.commons.service.BaseService;

public interface CtSessionFactory extends BaseService {

	CtSession openSession() throws CtException;

	void closeSession() throws CtException;

	void close() throws CtException;

}
