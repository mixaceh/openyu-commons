package org.openyu.commons.bao.hbase;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.openyu.commons.bao.hbase.ex.HzTemplateException;

public interface HzTableCallback<T> {

	T doInAction(HTableInterface table) throws HzTemplateException;
}
