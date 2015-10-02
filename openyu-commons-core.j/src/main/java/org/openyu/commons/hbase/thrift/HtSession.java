package org.openyu.commons.hbase.thrift;

import org.apache.hadoop.hbase.thrift.generated.Hbase;
import org.apache.thrift.transport.TTransport;
import org.openyu.commons.hbase.thrift.ex.HtSessionException;
import org.openyu.commons.model.BaseModel;

public interface HtSession extends BaseModel, Hbase.Iface {

	TTransport close() throws HtSessionException;

	boolean isClosed();

	boolean isConnected();
}
