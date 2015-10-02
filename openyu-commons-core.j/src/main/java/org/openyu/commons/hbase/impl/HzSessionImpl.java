package org.openyu.commons.hbase.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.UnknownRegionException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.RowLock;
import org.apache.hadoop.hbase.client.RowMutations;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.Batch.Call;
import org.apache.hadoop.hbase.client.coprocessor.Batch.Callback;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.SnapshotDescription;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.SnapshotDescription.Type;
import org.apache.hadoop.hbase.regionserver.compactions.CompactionRequest.CompactionState;
import org.apache.hadoop.hbase.regionserver.wal.FailedLogCloseException;
import org.apache.hadoop.hbase.snapshot.HBaseSnapshotException;
import org.apache.hadoop.hbase.snapshot.RestoreSnapshotException;
import org.apache.hadoop.hbase.snapshot.SnapshotCreationException;
import org.apache.hadoop.hbase.snapshot.UnknownSnapshotException;
import org.apache.hadoop.hbase.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.hbase.HzSession;
import org.openyu.commons.hbase.ex.HzSessionException;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.util.Delegateable;

public class HzSessionImpl extends BaseModelSupporter implements
		HzSession {

	private static final long serialVersionUID = 7538640140017769305L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(HzSessionImpl.class);

	protected boolean closed = false;

	protected HConnection hConnection;

	protected PoolableHConnection poolableHConnection;

	private transient HzSessionFactoryImpl factory;

	private HBaseAdmin delegate;

	public HzSessionImpl(HzSessionFactoryImpl factory,
			HConnection hConnection) {
		this.factory = factory;
		this.hConnection = hConnection;
		//
		if (hConnection instanceof Delegateable) {
			@SuppressWarnings("unchecked")
			Delegateable<HBaseAdmin> delegateable = (Delegateable<HBaseAdmin>) hConnection;
			this.delegate = delegateable.getDelegate();
		}
		//
		if (hConnection instanceof PoolableHConnection) {
			poolableHConnection = (PoolableHConnection) hConnection;
		}
	}

	public boolean isClosed() {
		return this.closed;
	}

	protected void errorIfClosed() {
		if (this.closed)
			throw new HzSessionException("HzSession was already closed");
	}

	public void close() throws HzSessionException {
		if (isClosed()) {
			throw new HzSessionException(
					"HzSession was already closed");
		}
		this.closed = true;
		//
		try {
			this.factory.closeSession();
		} catch (Exception ex) {
			throw new HzSessionException("Cannot close HzSession");
		}
	}

	public void abort(String paramString, Throwable paramThrowable) {
		errorIfClosed();
		try {
			this.delegate.abort(paramString, paramThrowable);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isAborted() {
		errorIfClosed();
		try {
			this.delegate.isAborted();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	public boolean isConnected() {
		return (!isClosed()) && (this.hConnection != null)
				&& (!this.hConnection.isClosed());
	}

	public boolean tableExists(String tableName) throws IOException {
		errorIfClosed();
		try {
			return this.delegate.tableExists(tableName);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean tableExists(byte[] tableName) throws IOException {
		errorIfClosed();
		try {
			return this.delegate.tableExists(tableName);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public HTableDescriptor[] listTables() throws IOException {
		errorIfClosed();
		try {
			return this.delegate.listTables();
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HTableDescriptor[] listTables(Pattern pattern) throws IOException {
		errorIfClosed();
		try {
			return this.delegate.listTables(pattern);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HTableDescriptor[] listTables(String regex) throws IOException {
		errorIfClosed();
		try {
			return this.delegate.listTables(regex);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HTableDescriptor getTableDescriptor(byte[] tableName)
			throws TableNotFoundException, IOException {
		errorIfClosed();
		try {
			return this.delegate.getTableDescriptor(tableName);
		} catch (TableNotFoundException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public void createTable(HTableDescriptor desc) throws IOException {
		errorIfClosed();
		try {
			delegate.createTable(desc);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void createTable(HTableDescriptor desc, byte[] startKey,
			byte[] endKey, int numRegions) throws IOException {
		errorIfClosed();
		try {
			delegate.createTable(desc, startKey, endKey, numRegions);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void createTable(HTableDescriptor desc, byte[][] splitKeys)
			throws IOException {
		errorIfClosed();
		try {
			delegate.createTable(desc, splitKeys);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void createTableAsync(HTableDescriptor desc, byte[][] splitKeys)
			throws IOException {
		errorIfClosed();
		try {
			delegate.createTableAsync(desc, splitKeys);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void deleteTable(String tableName) throws IOException {
		errorIfClosed();
		try {
			delegate.deleteTable(tableName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void deleteTable(byte[] tableName) throws IOException {
		errorIfClosed();
		try {
			delegate.deleteTable(tableName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public HTableDescriptor[] deleteTables(String regex) throws IOException {
		errorIfClosed();
		try {
			return delegate.deleteTables(regex);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HTableDescriptor[] deleteTables(Pattern pattern) throws IOException {
		errorIfClosed();
		try {
			return delegate.deleteTables(pattern);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public void enableTable(String tableName) throws IOException {
		errorIfClosed();
		try {
			delegate.enableTable(tableName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void enableTable(byte[] tableName) throws IOException {
		errorIfClosed();
		try {
			delegate.enableTable(tableName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void enableTableAsync(String tableName) throws IOException {
		errorIfClosed();
		try {
			delegate.enableTableAsync(tableName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void enableTableAsync(byte[] tableName) throws IOException {
		errorIfClosed();
		try {
			delegate.enableTableAsync(tableName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public HTableDescriptor[] enableTables(String regex) throws IOException {
		errorIfClosed();
		try {
			return delegate.enableTables(regex);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HTableDescriptor[] enableTables(Pattern pattern) throws IOException {
		errorIfClosed();
		try {
			return delegate.enableTables(pattern);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public void disableTableAsync(String tableName) throws IOException {
		errorIfClosed();
		try {
			delegate.disableTableAsync(tableName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void disableTableAsync(byte[] tableName) throws IOException {
		errorIfClosed();
		try {
			delegate.disableTableAsync(tableName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void disableTable(String tableName) throws IOException {
		errorIfClosed();
		try {
			delegate.disableTable(tableName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void disableTable(byte[] tableName) throws IOException {
		errorIfClosed();
		try {
			delegate.disableTable(tableName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public HTableDescriptor[] disableTables(String regex) throws IOException {
		errorIfClosed();
		try {
			return delegate.disableTables(regex);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HTableDescriptor[] disableTables(Pattern pattern) throws IOException {
		errorIfClosed();
		try {
			return delegate.disableTables(pattern);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public boolean isTableEnabled(String tableName) throws IOException {
		errorIfClosed();
		try {
			return delegate.isTableEnabled(tableName);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean isTableEnabled(byte[] tableName) throws IOException {
		errorIfClosed();
		try {
			return delegate.isTableEnabled(tableName);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean isTableDisabled(String tableName) throws IOException {
		errorIfClosed();
		try {
			return delegate.isTableDisabled(tableName);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean isTableDisabled(byte[] tableName) throws IOException {
		errorIfClosed();
		try {
			return delegate.isTableDisabled(tableName);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean isTableAvailable(byte[] tableName) throws IOException {
		errorIfClosed();
		try {
			return delegate.isTableAvailable(tableName);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean isTableAvailable(String tableName) throws IOException {
		errorIfClosed();
		try {
			return delegate.isTableAvailable(tableName);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public Pair<Integer, Integer> getAlterStatus(byte[] tableName)
			throws IOException {
		errorIfClosed();
		try {
			return delegate.getAlterStatus(tableName);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public void addColumn(String tableName, HColumnDescriptor column)
			throws IOException {
		errorIfClosed();
		try {
			delegate.addColumn(tableName, column);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void addColumn(byte[] tableName, HColumnDescriptor column)
			throws IOException {
		errorIfClosed();
		try {
			delegate.addColumn(tableName, column);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void deleteColumn(String tableName, String columnName)
			throws IOException {
		errorIfClosed();
		try {
			delegate.deleteColumn(tableName, columnName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void deleteColumn(byte[] tableName, byte[] columnName)
			throws IOException {
		errorIfClosed();
		try {
			delegate.deleteColumn(tableName, columnName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void modifyColumn(String tableName, HColumnDescriptor descriptor)
			throws IOException {
		errorIfClosed();
		try {
			delegate.modifyColumn(tableName, descriptor);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void modifyColumn(byte[] tableName, HColumnDescriptor descriptor)
			throws IOException {
		errorIfClosed();
		try {
			delegate.modifyColumn(tableName, descriptor);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void closeRegion(String regionname, String serverName)
			throws IOException {
		errorIfClosed();
		try {
			delegate.closeRegion(regionname, serverName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void closeRegion(byte[] regionname, String serverName)
			throws IOException {
		errorIfClosed();
		try {
			delegate.closeRegion(regionname, serverName);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public boolean closeRegionWithEncodedRegionName(String encodedRegionName,
			String serverName) throws IOException {
		errorIfClosed();
		try {
			return delegate.closeRegionWithEncodedRegionName(encodedRegionName,
					serverName);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public void closeRegion(ServerName sn, HRegionInfo hri) throws IOException {
		errorIfClosed();
		try {
			delegate.closeRegion(sn, hri);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void flush(String tableNameOrRegionName) throws IOException,
			InterruptedException {
		errorIfClosed();
		try {
			delegate.flush(tableNameOrRegionName);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public void flush(byte[] tableNameOrRegionName) throws IOException,
			InterruptedException {
		errorIfClosed();
		try {
			delegate.flush(tableNameOrRegionName);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public void compact(String tableNameOrRegionName) throws IOException,
			InterruptedException {
		errorIfClosed();
		try {
			delegate.compact(tableNameOrRegionName);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public void compact(byte[] tableNameOrRegionName) throws IOException,
			InterruptedException {
		errorIfClosed();
		try {
			delegate.compact(tableNameOrRegionName);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public void compact(String tableOrRegionName, String columnFamily)
			throws IOException, InterruptedException {
		errorIfClosed();
		try {
			delegate.compact(tableOrRegionName, columnFamily);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public void compact(byte[] tableNameOrRegionName, byte[] columnFamily)
			throws IOException, InterruptedException {
		errorIfClosed();
		try {
			delegate.compact(tableNameOrRegionName, columnFamily);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public void majorCompact(String tableNameOrRegionName) throws IOException,
			InterruptedException {
		errorIfClosed();
		try {
			delegate.majorCompact(tableNameOrRegionName);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public void majorCompact(byte[] tableNameOrRegionName) throws IOException,
			InterruptedException {
		errorIfClosed();
		try {
			delegate.majorCompact(tableNameOrRegionName);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public void majorCompact(String tableNameOrRegionName, String columnFamily)
			throws IOException, InterruptedException {
		errorIfClosed();
		try {
			delegate.majorCompact(tableNameOrRegionName, columnFamily);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public void majorCompact(byte[] tableNameOrRegionName, byte[] columnFamily)
			throws IOException, InterruptedException {
		errorIfClosed();
		try {
			delegate.majorCompact(tableNameOrRegionName, columnFamily);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public void move(byte[] encodedRegionName, byte[] destServerName)
			throws UnknownRegionException, MasterNotRunningException,
			ZooKeeperConnectionException {
		delegate.move(encodedRegionName, destServerName);
	}

	public void assign(byte[] regionName) throws MasterNotRunningException,
			ZooKeeperConnectionException, IOException {
		delegate.assign(regionName);
	}

	public void unassign(byte[] regionName, boolean force)
			throws MasterNotRunningException, ZooKeeperConnectionException,
			IOException {
		delegate.unassign(regionName, force);
	}

	public boolean balanceSwitch(boolean b) throws MasterNotRunningException,
			ZooKeeperConnectionException {
		return delegate.balanceSwitch(b);
	}

	public boolean setBalancerRunning(boolean on, boolean synchronous)
			throws MasterNotRunningException, ZooKeeperConnectionException {
		return delegate.setBalancerRunning(on, synchronous);
	}

	public boolean balancer() throws MasterNotRunningException,
			ZooKeeperConnectionException {
		return delegate.balancer();
	}

	public void split(String tableNameOrRegionName) throws IOException,
			InterruptedException {
		delegate.split(tableNameOrRegionName);
	}

	public void split(byte[] tableNameOrRegionName) throws IOException,
			InterruptedException {
		delegate.split(tableNameOrRegionName);
	}

	public void split(String tableNameOrRegionName, String splitPoint)
			throws IOException, InterruptedException {
		delegate.split(tableNameOrRegionName, splitPoint);
	}

	public void split(byte[] tableNameOrRegionName, byte[] splitPoint)
			throws IOException, InterruptedException {
		delegate.split(tableNameOrRegionName, splitPoint);
	}

	public void modifyTable(byte[] tableName, HTableDescriptor htd)
			throws IOException {
		delegate.modifyTable(tableName, htd);
	}

	public void shutdown() throws IOException {
		delegate.shutdown();
	}

	public void stopMaster() throws IOException {
		delegate.stopMaster();
	}

	public void stopRegionServer(String hostnamePort) throws IOException {
		delegate.stopRegionServer(hostnamePort);
	}

	public ClusterStatus getClusterStatus() throws IOException {
		return delegate.getClusterStatus();
	}

	public Configuration getConfiguration() {
		return delegate.getConfiguration();
	}

	public List<HRegionInfo> getTableRegions(byte[] tableName)
			throws IOException {
		return delegate.getTableRegions(tableName);
	}

	public HTableDescriptor[] getTableDescriptors(List<String> tableNames)
			throws IOException {
		return delegate.getTableDescriptors(tableNames);
	}

	public byte[][] rollHLogWriter(String serverName) throws IOException,
			FailedLogCloseException {
		return delegate.rollHLogWriter(serverName);
	}

	public String[] getMasterCoprocessors() {
		return delegate.getMasterCoprocessors();
	}

	public CompactionState getCompactionState(String tableNameOrRegionName)
			throws IOException, InterruptedException {
		return delegate.getCompactionState(tableNameOrRegionName);
	}

	public CompactionState getCompactionState(byte[] tableNameOrRegionName)
			throws IOException, InterruptedException {
		return delegate.getCompactionState(tableNameOrRegionName);
	}

	public <T extends CoprocessorProtocol> T coprocessorProxy(Class<T> protocol) {
		return delegate.coprocessorProxy(protocol);
	}

	public void snapshot(String snapshotName, String tableName)
			throws IOException, SnapshotCreationException,
			IllegalArgumentException {
		delegate.snapshot(snapshotName, tableName);
	}

	public void snapshot(byte[] snapshotName, byte[] tableName)
			throws IOException, SnapshotCreationException,
			IllegalArgumentException {
		delegate.snapshot(snapshotName, tableName);
	}

	public void snapshot(String snapshotName, String tableName, Type type)
			throws IOException, SnapshotCreationException,
			IllegalArgumentException {
		delegate.snapshot(snapshotName, tableName, type);
	}

	public void snapshot(SnapshotDescription snapshot) throws IOException,
			SnapshotCreationException, IllegalArgumentException {
		delegate.snapshot(snapshot);
	}

	public long takeSnapshotAsync(SnapshotDescription snapshot)
			throws IOException, SnapshotCreationException {
		return delegate.takeSnapshotAsync(snapshot);
	}

	public boolean isSnapshotFinished(SnapshotDescription snapshot)
			throws IOException, HBaseSnapshotException,
			UnknownSnapshotException {
		return delegate.isSnapshotFinished(snapshot);
	}

	public void restoreSnapshot(byte[] snapshotName) throws IOException,
			RestoreSnapshotException {
		delegate.restoreSnapshot(snapshotName);
	}

	public void restoreSnapshot(String snapshotName) throws IOException,
			RestoreSnapshotException {
		delegate.restoreSnapshot(snapshotName);
	}

	public void cloneSnapshot(byte[] snapshotName, byte[] tableName)
			throws IOException, TableExistsException, RestoreSnapshotException,
			InterruptedException {
		delegate.cloneSnapshot(snapshotName, tableName);
	}

	public void cloneSnapshot(String snapshotName, String tableName)
			throws IOException, TableExistsException, RestoreSnapshotException,
			InterruptedException {
		delegate.cloneSnapshot(snapshotName, tableName);
	}

	public List<SnapshotDescription> listSnapshots() throws IOException {
		return delegate.listSnapshots();
	}

	public void deleteSnapshot(byte[] snapshotName) throws IOException {
		delegate.deleteSnapshot(snapshotName);
	}

	public void deleteSnapshot(String snapshotName) throws IOException {
		delegate.deleteSnapshot(snapshotName);
	}

	// --------------------------------------------------

	public HTableInterface getTable(String tableName) throws IOException {
		return poolableHConnection.getTable(tableName);
	}

	public HTableInterface getTable(byte[] tableName) throws IOException {
		return poolableHConnection.getTable(tableName);
	}

	public boolean exists(String tableName, Get paramGet) throws IOException {
		return poolableHConnection.exists(tableName, paramGet);
	}

	public HTableDescriptor getTableDescriptor(String tableName)
			throws IOException {
		return poolableHConnection.getTableDescriptor(tableName);
	}

	public void batch(String tableName, List<? extends Row> paramList,
			Object[] paramArrayOfObject) throws IOException,
			InterruptedException {
		poolableHConnection.batch(tableName, paramList, paramArrayOfObject);
	}

	public Object[] batch(String tableName, List<? extends Row> paramList)
			throws IOException, InterruptedException {
		return poolableHConnection.batch(tableName, paramList);
	}

	public Result get(String tableName, Get paramGet) throws IOException {
		return poolableHConnection.get(tableName, paramGet);
	}

	public Result[] get(String tableName, List<Get> paramList)
			throws IOException {
		return poolableHConnection.get(tableName, paramList);
	}

	/** @deprecated */
	public Result getRowOrBefore(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) throws IOException {
		return poolableHConnection.getRowOrBefore(tableName, paramArrayOfByte1,
				paramArrayOfByte2);
	}

	public ResultScanner getScanner(String tableName, Scan paramScan)
			throws IOException {
		return poolableHConnection.getScanner(tableName, paramScan);
	}

	public ResultScanner getScanner(String tableName, byte[] paramArrayOfByte)
			throws IOException {
		return poolableHConnection.getScanner(tableName, paramArrayOfByte);
	}

	public ResultScanner getScanner(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) throws IOException {
		return poolableHConnection.getScanner(tableName, paramArrayOfByte1,
				paramArrayOfByte2);
	}

	public void put(String tableName, Put paramPut) throws IOException {
		poolableHConnection.put(tableName, paramPut);
	}

	public void put(String tableName, List<Put> paramList) throws IOException {
		poolableHConnection.put(tableName, paramList);
	}

	public boolean checkAndPut(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, byte[] paramArrayOfByte3,
			byte[] paramArrayOfByte4, Put paramPut) throws IOException {
		return poolableHConnection.checkAndPut(tableName, paramArrayOfByte1,
				paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4,
				paramPut);
	}

	public void delete(String tableName, Delete paramDelete) throws IOException {
		poolableHConnection.delete(tableName, paramDelete);
	}

	public void delete(String tableName, List<Delete> paramList)
			throws IOException {
		poolableHConnection.delete(tableName, paramList);
	}

	public boolean checkAndDelete(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, byte[] paramArrayOfByte3,
			byte[] paramArrayOfByte4, Delete paramDelete) throws IOException {
		return poolableHConnection.checkAndDelete(tableName, paramArrayOfByte1,
				paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4,
				paramDelete);
	}

	public void mutateRow(String tableName, RowMutations paramRowMutations)
			throws IOException {
		poolableHConnection.mutateRow(tableName, paramRowMutations);
	}

	public Result append(String tableName, Append paramAppend)
			throws IOException {
		return poolableHConnection.append(tableName, paramAppend);
	}

	public Result increment(String tableName, Increment paramIncrement)
			throws IOException {
		return poolableHConnection.increment(tableName, paramIncrement);
	}

	public long incrementColumnValue(String tableName,
			byte[] paramArrayOfByte1, byte[] paramArrayOfByte2,
			byte[] paramArrayOfByte3, long paramLong) throws IOException {
		return poolableHConnection.incrementColumnValue(tableName,
				paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3,
				paramLong);
	}

	public long incrementColumnValue(String tableName,
			byte[] paramArrayOfByte1, byte[] paramArrayOfByte2,
			byte[] paramArrayOfByte3, long paramLong, boolean paramBoolean)
			throws IOException {
		return poolableHConnection.incrementColumnValue(tableName,
				paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3,
				paramLong, paramBoolean);
	}

	public boolean isAutoFlush(String tableName) {
		return poolableHConnection.isAutoFlush(tableName);
	}

	public void flushCommits(String tableName) throws IOException {
		poolableHConnection.flushCommits(tableName);
	}

	public void closeTable(String tableName) throws IOException {
		poolableHConnection.closeTable(tableName);
	}

	public void closeTable(HTableInterface table) throws IOException {
		poolableHConnection.closeTable(table);
	}

	/** @deprecated */
	public RowLock lockRow(String tableName, byte[] paramArrayOfByte)
			throws IOException {
		return poolableHConnection.lockRow(tableName, paramArrayOfByte);
	}

	/** @deprecated */
	public void unlockRow(String tableName, RowLock paramRowLock)
			throws IOException {
		poolableHConnection.unlockRow(tableName, paramRowLock);
	}

	public <T extends CoprocessorProtocol> T coprocessorProxy(String tableName,
			Class<T> paramClass, byte[] paramArrayOfByte) {
		return poolableHConnection.coprocessorProxy(tableName, paramClass,
				paramArrayOfByte);
	}

	public <T extends CoprocessorProtocol, R> Map<byte[], R> coprocessorExec(
			String tableName, Class<T> paramClass, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, Call<T, R> paramCall) throws IOException,
			Throwable {
		return poolableHConnection.coprocessorExec(tableName, paramClass,
				paramArrayOfByte1, paramArrayOfByte2, paramCall);
	}

	public <T extends CoprocessorProtocol, R> void coprocessorExec(
			String tableName, Class<T> paramClass, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, Call<T, R> paramCall,
			Callback<R> paramCallback) throws IOException, Throwable {
		poolableHConnection.coprocessorExec(tableName, paramClass,
				paramArrayOfByte1, paramArrayOfByte2, paramCall, paramCallback);
	}

	public void setAutoFlush(String tableName, boolean paramBoolean) {
		poolableHConnection.setAutoFlush(tableName, paramBoolean);
	}

	public void setAutoFlush(String tableName, boolean paramBoolean1,
			boolean paramBoolean2) {
		poolableHConnection.setAutoFlush(tableName, paramBoolean1,
				paramBoolean2);
	}

	public long getWriteBufferSize(String tableName) {
		return poolableHConnection.getWriteBufferSize(tableName);
	}

	public void setWriteBufferSize(String tableName, long paramLong)
			throws IOException {
		poolableHConnection.setWriteBufferSize(tableName, paramLong);
	}

	// --------------------------------------------------

	protected void handleException(TableNotFoundException e)
			throws TableNotFoundException {
		throw e;
	}

	protected void handleException(IOException e) throws IOException {
		throw e;
	}

	protected void handleException(InterruptedException e)
			throws InterruptedException {
		throw e;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("closed", closed);
		builder.append("hConnection", hConnection);
		return builder.toString();
	}

}
