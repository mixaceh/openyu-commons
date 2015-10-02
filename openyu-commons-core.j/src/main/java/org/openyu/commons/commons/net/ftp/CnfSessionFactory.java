package org.openyu.commons.commons.net.ftp;

import org.openyu.commons.service.BaseService;
import org.openyu.commons.commons.net.ftp.ex.CnfException;

public interface CnfSessionFactory extends BaseService {

	CnfSession openSession() throws CnfException;

	void closeSession() throws CnfException;

	void close() throws CnfException;

}
