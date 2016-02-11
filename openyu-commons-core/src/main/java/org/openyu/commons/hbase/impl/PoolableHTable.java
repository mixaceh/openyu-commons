package org.openyu.commons.hbase.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.RowLock;
import org.apache.hadoop.hbase.client.RowMutations;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;
import org.openyu.commons.util.Delegateable;

public class PoolableHTable implements HTableInterface,
		Delegateable<HTableInterface> {

	private PoolableHConnection poolableHConnection;

	private HTableInterface delegate;

	public PoolableHTable(PoolableHConnection poolableHConnection,
			HTableInterface table) {
		this.poolableHConnection = poolableHConnection;
		this.delegate = table;
	}

	public HTableInterface getDelegate() {
		return delegate;
	}

	public byte[] getTableName() {
		return this.delegate.getTableName();
	}

	public Configuration getConfiguration() {
		return this.delegate.getConfiguration();
	}

	public HTableDescriptor getTableDescriptor() throws IOException {
		return this.delegate.getTableDescriptor();
	}

	public boolean exists(Get get) throws IOException {
		return this.delegate.exists(get);
	}

	public void batch(List<? extends Row> actions, Object[] results)
			throws IOException, InterruptedException {
		this.delegate.batch(actions, results);
	}

	public Object[] batch(List<? extends Row> actions) throws IOException,
			InterruptedException {
		return this.delegate.batch(actions);
	}

	public Result get(Get get) throws IOException {
		return this.delegate.get(get);
	}

	public Result[] get(List<Get> gets) throws IOException {
		return this.delegate.get(gets);
	}

	public Result getRowOrBefore(byte[] row, byte[] family) throws IOException {
		return this.delegate.getRowOrBefore(row, family);
	}

	public ResultScanner getScanner(Scan scan) throws IOException {
		return this.delegate.getScanner(scan);
	}

	public ResultScanner getScanner(byte[] family) throws IOException {
		return this.delegate.getScanner(family);
	}

	public ResultScanner getScanner(byte[] family, byte[] qualifier)
			throws IOException {
		return this.delegate.getScanner(family, qualifier);
	}

	public void put(Put put) throws IOException {
		this.delegate.put(put);
	}

	public void put(List<Put> puts) throws IOException {
		this.delegate.put(puts);
	}

	public boolean checkAndPut(byte[] row, byte[] family, byte[] qualifier,
			byte[] value, Put put) throws IOException {
		return this.delegate.checkAndPut(row, family, qualifier, value, put);
	}

	public void delete(Delete delete) throws IOException {
		this.delegate.delete(delete);
	}

	public void delete(List<Delete> deletes) throws IOException {
		this.delegate.delete(deletes);
	}

	public boolean checkAndDelete(byte[] row, byte[] family, byte[] qualifier,
			byte[] value, Delete delete) throws IOException {
		return this.delegate
				.checkAndDelete(row, family, qualifier, value, delete);
	}

	public Result increment(Increment increment) throws IOException {
		return this.delegate.increment(increment);
	}

	public long incrementColumnValue(byte[] row, byte[] family,
			byte[] qualifier, long amount) throws IOException {
		return this.delegate.incrementColumnValue(row, family, qualifier, amount);
	}

	public long incrementColumnValue(byte[] row, byte[] family,
			byte[] qualifier, long amount, boolean writeToWAL)
			throws IOException {
		return this.delegate.incrementColumnValue(row, family, qualifier, amount,
				writeToWAL);
	}

	public boolean isAutoFlush() {
		return this.delegate.isAutoFlush();
	}

	public void flushCommits() throws IOException {
		this.delegate.flushCommits();
	}

	public void close() throws IOException {
		poolableHConnection.closeTable(this);
	}

	/** @deprecated */
	public RowLock lockRow(byte[] row) throws IOException {
		return this.delegate.lockRow(row);
	}

	/** @deprecated */
	public void unlockRow(RowLock rl) throws IOException {
		this.delegate.unlockRow(rl);
	}

	public <T extends CoprocessorProtocol> T coprocessorProxy(
			Class<T> protocol, byte[] row) {
		return this.delegate.coprocessorProxy(protocol, row);
	}

	public <T extends CoprocessorProtocol, R> Map<byte[], R> coprocessorExec(
			Class<T> protocol, byte[] startKey, byte[] endKey,
			Batch.Call<T, R> callable) throws IOException, Throwable {
		return this.delegate
				.coprocessorExec(protocol, startKey, endKey, callable);
	}

	public <T extends CoprocessorProtocol, R> void coprocessorExec(
			Class<T> protocol, byte[] startKey, byte[] endKey,
			Batch.Call<T, R> callable, Batch.Callback<R> callback)
			throws IOException, Throwable {
		this.delegate.coprocessorExec(protocol, startKey, endKey, callable,
				callback);
	}

	HTableInterface getWrappedTable() {
		return this.delegate;
	}

	public void mutateRow(RowMutations rm) throws IOException {
		this.delegate.mutateRow(rm);
	}

	public Result append(Append append) throws IOException {
		return this.delegate.append(append);
	}

	public void setAutoFlush(boolean autoFlush) {
		this.delegate.setAutoFlush(autoFlush);
	}

	public void setAutoFlush(boolean autoFlush, boolean clearBufferOnFail) {
		this.delegate.setAutoFlush(autoFlush, clearBufferOnFail);
	}

	public long getWriteBufferSize() {
		return this.delegate.getWriteBufferSize();
	}

	public void setWriteBufferSize(long writeBufferSize) throws IOException {
		this.delegate.setWriteBufferSize(writeBufferSize);
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("delegate", delegate);
		return builder.toString();
	}

}
