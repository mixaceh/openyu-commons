package org.openyu.commons.fto.commons.net.ftp;

import org.openyu.commons.commons.net.ftp.CnfSession;
import org.openyu.commons.fto.commons.net.ftp.ex.CnfTemplateException;

public interface CnfCallback<T> {

	T doInAction(CnfSession session) throws CnfTemplateException;
}
