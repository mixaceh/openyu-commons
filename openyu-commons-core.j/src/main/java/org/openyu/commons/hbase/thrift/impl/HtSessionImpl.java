package org.openyu.commons.hbase.thrift.impl;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.hadoop.hbase.thrift.generated.AlreadyExists;
import org.apache.hadoop.hbase.thrift.generated.BatchMutation;
import org.apache.hadoop.hbase.thrift.generated.ColumnDescriptor;
import org.apache.hadoop.hbase.thrift.generated.Hbase;
import org.apache.hadoop.hbase.thrift.generated.IOError;
import org.apache.hadoop.hbase.thrift.generated.IllegalArgument;
import org.apache.hadoop.hbase.thrift.generated.Mutation;
import org.apache.hadoop.hbase.thrift.generated.TCell;
import org.apache.hadoop.hbase.thrift.generated.TIncrement;
import org.apache.hadoop.hbase.thrift.generated.TRegionInfo;
import org.apache.hadoop.hbase.thrift.generated.TRowResult;
import org.apache.hadoop.hbase.thrift.generated.TScan;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.hbase.thrift.HtSession;
import org.openyu.commons.hbase.thrift.ex.HtSessionException;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.util.Delegateable;

public class HtSessionImpl extends BaseModelSupporter implements
		HtSession {

	private static final long serialVersionUID = -1092234835389273601L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(HtSessionImpl.class);

	protected boolean closed = false;

	protected TTransport ttransport;

	private transient HtSessionFactoryImpl factory;

	private Hbase.Client delegate;

	public HtSessionImpl(HtSessionFactoryImpl factory,
			TTransport ttransport) {
		this.factory = factory;
		this.ttransport = ttransport;
		//
		if (ttransport instanceof Delegateable) {
			@SuppressWarnings("unchecked")
			Delegateable<Hbase.Client> targetable = (Delegateable<Hbase.Client>) ttransport;
			this.delegate = targetable.getDelegate();
		}
	}

	public boolean isClosed() {
		return this.closed;
	}

	protected void errorIfClosed() {
		if (this.closed)
			throw new HtSessionException(
					"HtSession was already closed");
	}

	public TTransport close() throws HtSessionException {
		if (isClosed()) {
			throw new HtSessionException(
					"HtSession was already closed");
		}
		this.closed = true;
		//
		try {
			this.factory.closeSession();
			return ttransport;
		} catch (Exception ex) {
			throw new HtSessionException(
					"Cannot close HtSession");
		}
	}

	public boolean isConnected() {
		return (!isClosed()) && (this.ttransport != null)
				&& (this.ttransport.isOpen());
	}

	public void enableTable(ByteBuffer paramByteBuffer) throws IOError,
			TException {
		errorIfClosed();
		try {
			this.delegate.enableTable(paramByteBuffer);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void disableTable(ByteBuffer paramByteBuffer) throws IOError,
			TException {
		errorIfClosed();
		try {
			this.delegate.disableTable(paramByteBuffer);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public boolean isTableEnabled(ByteBuffer paramByteBuffer) throws IOError,
			TException {
		errorIfClosed();
		try {
			return this.delegate.isTableEnabled(paramByteBuffer);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return false;
	}

	public void compact(ByteBuffer paramByteBuffer) throws IOError, TException {
		errorIfClosed();
		try {
			this.delegate.compact(paramByteBuffer);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void majorCompact(ByteBuffer paramByteBuffer) throws IOError,
			TException {
		errorIfClosed();
		try {
			this.delegate.majorCompact(paramByteBuffer);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public List<ByteBuffer> getTableNames() throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getTableNames();
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public Map<ByteBuffer, ColumnDescriptor> getColumnDescriptors(
			ByteBuffer paramByteBuffer) throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getColumnDescriptors(paramByteBuffer);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TRegionInfo> getTableRegions(ByteBuffer paramByteBuffer)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getTableRegions(paramByteBuffer);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public void createTable(ByteBuffer paramByteBuffer,
			List<ColumnDescriptor> paramList) throws IOError, IllegalArgument,
			AlreadyExists, TException {
		errorIfClosed();
		try {
			this.delegate.createTable(paramByteBuffer, paramList);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		} catch (IllegalArgument e) {
			handleException(e);
		} catch (AlreadyExists e) {
			handleException(e);
		}
	}

	public void deleteTable(ByteBuffer paramByteBuffer) throws IOError,
			TException {
		errorIfClosed();
		try {
			this.delegate.deleteTable(paramByteBuffer);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public List<TCell> get(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.get(paramByteBuffer1, paramByteBuffer2,
					paramByteBuffer3, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TCell> getVer(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3,
			int paramInt, Map<ByteBuffer, ByteBuffer> paramMap) throws IOError,
			TException {
		errorIfClosed();
		try {
			return this.delegate.getVer(paramByteBuffer1, paramByteBuffer2,
					paramByteBuffer3, paramInt, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TCell> getVerTs(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3,
			long paramLong, int paramInt, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getVerTs(paramByteBuffer1, paramByteBuffer2,
					paramByteBuffer3, paramLong, paramInt, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TRowResult> getRow(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getRow(paramByteBuffer1, paramByteBuffer2,
					paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TRowResult> getRowWithColumns(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, List<ByteBuffer> paramList,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getRowWithColumns(paramByteBuffer1,
					paramByteBuffer2, paramList, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TRowResult> getRowTs(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, long paramLong,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getRowTs(paramByteBuffer1, paramByteBuffer2,
					paramLong, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TRowResult> getRowWithColumnsTs(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, List<ByteBuffer> paramList,
			long paramLong, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getRowWithColumnsTs(paramByteBuffer1,
					paramByteBuffer2, paramList, paramLong, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TRowResult> getRows(ByteBuffer paramByteBuffer,
			List<ByteBuffer> paramList, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getRows(paramByteBuffer, paramList, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TRowResult> getRowsWithColumns(ByteBuffer paramByteBuffer,
			List<ByteBuffer> paramList1, List<ByteBuffer> paramList2,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getRowsWithColumns(paramByteBuffer, paramList1,
					paramList2, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TRowResult> getRowsTs(ByteBuffer paramByteBuffer,
			List<ByteBuffer> paramList, long paramLong,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getRowsTs(paramByteBuffer, paramList, paramLong,
					paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TRowResult> getRowsWithColumnsTs(ByteBuffer paramByteBuffer,
			List<ByteBuffer> paramList1, List<ByteBuffer> paramList2,
			long paramLong, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getRowsWithColumnsTs(paramByteBuffer,
					paramList1, paramList2, paramLong, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public void mutateRow(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, List<Mutation> paramList,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError,
			IllegalArgument, TException {
		errorIfClosed();
		try {
			this.delegate.mutateRow(paramByteBuffer1, paramByteBuffer2,
					paramList, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (IllegalArgument e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void mutateRowTs(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, List<Mutation> paramList,
			long paramLong, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, IllegalArgument, TException {
		errorIfClosed();
		try {
			this.delegate.mutateRowTs(paramByteBuffer1, paramByteBuffer2,
					paramList, paramLong, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (IllegalArgument e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void mutateRows(ByteBuffer paramByteBuffer,
			List<BatchMutation> paramList, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, IllegalArgument, TException {
		errorIfClosed();
		try {
			this.delegate.mutateRows(paramByteBuffer, paramList, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (IllegalArgument e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void mutateRowsTs(ByteBuffer paramByteBuffer,
			List<BatchMutation> paramList, long paramLong,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError,
			IllegalArgument, TException {
		errorIfClosed();
		try {
			this.delegate.mutateRowsTs(paramByteBuffer, paramList, paramLong,
					paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (IllegalArgument e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public long atomicIncrement(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3,
			long paramLong) throws IOError, IllegalArgument, TException {
		errorIfClosed();
		try {
			return this.delegate.atomicIncrement(paramByteBuffer1,
					paramByteBuffer2, paramByteBuffer3, paramLong);
		} catch (IOError e) {
			handleException(e);
		} catch (IllegalArgument e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return 0;
	}

	public void deleteAll(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError, TException {
		errorIfClosed();
		try {
			this.delegate.deleteAll(paramByteBuffer1, paramByteBuffer2,
					paramByteBuffer3, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void deleteAllTs(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3,
			long paramLong, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, TException {
		errorIfClosed();
		try {
			this.delegate.deleteAllTs(paramByteBuffer1, paramByteBuffer2,
					paramByteBuffer3, paramLong, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void deleteAllRow(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, TException {
		errorIfClosed();
		try {
			this.delegate.deleteAllRow(paramByteBuffer1, paramByteBuffer2,
					paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void increment(TIncrement paramTIncrement) throws IOError,
			TException {
		errorIfClosed();
		try {
			this.delegate.increment(paramTIncrement);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void incrementRows(List<TIncrement> paramList) throws IOError,
			TException {
		errorIfClosed();
		try {
			this.delegate.incrementRows(paramList);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void deleteAllRowTs(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, long paramLong,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError, TException {
		errorIfClosed();
		try {
			this.delegate.deleteAllRowTs(paramByteBuffer1, paramByteBuffer2,
					paramLong, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public int scannerOpenWithScan(ByteBuffer paramByteBuffer,
			TScan paramTScan, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.scannerOpenWithScan(paramByteBuffer, paramTScan,
					paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return 0;
	}

	public int scannerOpen(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, List<ByteBuffer> paramList,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.scannerOpen(paramByteBuffer1, paramByteBuffer2,
					paramList, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return 0;
	}

	public int scannerOpenWithStop(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3,
			List<ByteBuffer> paramList, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.scannerOpenWithStop(paramByteBuffer1,
					paramByteBuffer2, paramByteBuffer3, paramList, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return 0;
	}

	public int scannerOpenWithPrefix(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, List<ByteBuffer> paramList,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.scannerOpenWithPrefix(paramByteBuffer1,
					paramByteBuffer2, paramList, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return 0;
	}

	public int scannerOpenTs(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, List<ByteBuffer> paramList,
			long paramLong, Map<ByteBuffer, ByteBuffer> paramMap)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.scannerOpenTs(paramByteBuffer1,
					paramByteBuffer2, paramList, paramLong, paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return 0;
	}

	public int scannerOpenWithStopTs(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3,
			List<ByteBuffer> paramList, long paramLong,
			Map<ByteBuffer, ByteBuffer> paramMap) throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.scannerOpenWithStopTs(paramByteBuffer1,
					paramByteBuffer2, paramByteBuffer3, paramList, paramLong,
					paramMap);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return 0;
	}

	public List<TRowResult> scannerGet(int paramInt) throws IOError,
			IllegalArgument, TException {
		errorIfClosed();
		try {
			return this.delegate.scannerGet(paramInt);
		} catch (IOError e) {
			handleException(e);
		} catch (IllegalArgument e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TRowResult> scannerGetList(int paramInt1, int paramInt2)
			throws IOError, IllegalArgument, TException {
		errorIfClosed();
		try {
			return this.delegate.scannerGetList(paramInt1, paramInt2);
		} catch (IOError e) {
			handleException(e);
		} catch (IllegalArgument e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public void scannerClose(int paramInt) throws IOError, IllegalArgument,
			TException {
		errorIfClosed();
		try {
			this.delegate.scannerClose(paramInt);
		} catch (IOError e) {
			handleException(e);
		} catch (IllegalArgument e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public List<TCell> getRowOrBefore(ByteBuffer paramByteBuffer1,
			ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getRowOrBefore(paramByteBuffer1,
					paramByteBuffer2, paramByteBuffer3);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public TRegionInfo getRegionInfo(ByteBuffer paramByteBuffer)
			throws IOError, TException {
		errorIfClosed();
		try {
			return this.delegate.getRegionInfo(paramByteBuffer);
		} catch (IOError e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	protected void handleException(IOError e) throws IOError {
		throw e;
	}

	protected void handleException(TException e) throws TException {
		throw e;
	}

	protected void handleException(IllegalArgument e) throws IllegalArgument {
		throw e;
	}

	protected void handleException(AlreadyExists e) throws AlreadyExists {
		throw e;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("closed", closed);
		builder.append("ttransport", ttransport);
		return builder.toString();
	}
}
