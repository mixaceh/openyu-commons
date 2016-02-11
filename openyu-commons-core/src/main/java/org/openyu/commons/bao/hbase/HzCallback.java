package org.openyu.commons.bao.hbase;

import org.openyu.commons.bao.hbase.ex.HzTemplateException;
import org.openyu.commons.hbase.HzSession;

public interface HzCallback<T> {

	T doInAction(HzSession session) throws HzTemplateException;
}
