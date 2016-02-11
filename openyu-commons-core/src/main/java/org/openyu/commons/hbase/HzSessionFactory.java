package org.openyu.commons.hbase;

import org.openyu.commons.hbase.ex.HzException;
import org.openyu.commons.service.BaseService;

public interface HzSessionFactory extends BaseService {

	HzSession openSession() throws HzException;

	void closeSession() throws HzException;

	void close() throws HzException;

}
