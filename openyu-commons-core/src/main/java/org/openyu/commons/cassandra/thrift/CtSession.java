package org.openyu.commons.cassandra.thrift;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.thrift.transport.TTransport;
import org.openyu.commons.cassandra.thrift.ex.CtSessionException;
import org.openyu.commons.model.BaseModel;

public interface CtSession extends BaseModel, Cassandra.Iface {

	TTransport close() throws CtSessionException;

	boolean isClosed();

	boolean isConnected();
}
