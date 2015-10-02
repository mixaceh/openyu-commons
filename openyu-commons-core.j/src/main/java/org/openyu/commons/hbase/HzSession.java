package org.openyu.commons.hbase;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Abortable;
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
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.SnapshotDescription;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.SnapshotDescription.Type;
import org.apache.hadoop.hbase.regionserver.compactions.CompactionRequest.CompactionState;
import org.apache.hadoop.hbase.regionserver.wal.FailedLogCloseException;
import org.apache.hadoop.hbase.snapshot.HBaseSnapshotException;
import org.apache.hadoop.hbase.snapshot.RestoreSnapshotException;
import org.apache.hadoop.hbase.snapshot.SnapshotCreationException;
import org.apache.hadoop.hbase.snapshot.UnknownSnapshotException;
import org.apache.hadoop.hbase.util.Pair;
import org.openyu.commons.hbase.ex.HzSessionException;
import org.openyu.commons.model.BaseModel;

public interface HzSession extends BaseModel, Abortable, Closeable {

	void close() throws HzSessionException;
	
	boolean isClosed();

	boolean isConnected();

	// --------------------------------------------------

	boolean tableExists(String tableName) throws IOException;

	boolean tableExists(byte[] tableName) throws IOException;

	HTableDescriptor[] listTables() throws IOException;

	HTableDescriptor[] listTables(Pattern pattern) throws IOException;

	HTableDescriptor[] listTables(String regex) throws IOException;

	HTableDescriptor getTableDescriptor(byte[] tableName)
			throws TableNotFoundException, IOException;

	void createTable(HTableDescriptor desc) throws IOException;

	void createTable(HTableDescriptor desc, byte[] startKey, byte[] endKey,
			int numRegions) throws IOException;

	void createTable(HTableDescriptor desc, byte[][] splitKeys)
			throws IOException;

	void createTableAsync(HTableDescriptor desc, byte[][] splitKeys)
			throws IOException;

	void deleteTable(String tableName) throws IOException;

	void deleteTable(byte[] tableName) throws IOException;

	HTableDescriptor[] deleteTables(String regex) throws IOException;

	HTableDescriptor[] deleteTables(Pattern pattern) throws IOException;

	void enableTable(String tableName) throws IOException;

	void enableTable(byte[] tableName) throws IOException;

	void enableTableAsync(String tableName) throws IOException;

	void enableTableAsync(byte[] tableName) throws IOException;

	HTableDescriptor[] enableTables(String regex) throws IOException;

	HTableDescriptor[] enableTables(Pattern pattern) throws IOException;

	void disableTableAsync(String tableName) throws IOException;

	void disableTableAsync(byte[] tableName) throws IOException;

	void disableTable(String tableName) throws IOException;

	void disableTable(byte[] tableName) throws IOException;

	HTableDescriptor[] disableTables(String regex) throws IOException;

	HTableDescriptor[] disableTables(Pattern pattern) throws IOException;

	boolean isTableEnabled(String tableName) throws IOException;

	boolean isTableEnabled(byte[] tableName) throws IOException;

	boolean isTableDisabled(String tableName) throws IOException;

	boolean isTableDisabled(byte[] tableName) throws IOException;

	boolean isTableAvailable(byte[] tableName) throws IOException;

	boolean isTableAvailable(String tableName) throws IOException;

	Pair<Integer, Integer> getAlterStatus(byte[] tableName) throws IOException;

	void addColumn(String tableName, HColumnDescriptor column)
			throws IOException;

	void addColumn(byte[] tableName, HColumnDescriptor column)
			throws IOException;

	void deleteColumn(String tableName, String columnName) throws IOException;

	void deleteColumn(byte[] tableName, byte[] columnName) throws IOException;

	void modifyColumn(String tableName, HColumnDescriptor descriptor)
			throws IOException;

	void modifyColumn(byte[] tableName, HColumnDescriptor descriptor)
			throws IOException;

	void closeRegion(String regionname, String serverName) throws IOException;

	void closeRegion(byte[] regionname, String serverName) throws IOException;

	boolean closeRegionWithEncodedRegionName(String encodedRegionName,
			String serverName) throws IOException;

	void closeRegion(ServerName sn, HRegionInfo hri) throws IOException;

	void flush(String tableNameOrRegionName) throws IOException,
			InterruptedException;

	void flush(byte[] tableNameOrRegionName) throws IOException,
			InterruptedException;

	void compact(String tableNameOrRegionName) throws IOException,
			InterruptedException;

	void compact(byte[] tableNameOrRegionName) throws IOException,
			InterruptedException;

	void compact(String tableOrRegionName, String columnFamily)
			throws IOException, InterruptedException;

	void compact(byte[] tableNameOrRegionName, byte[] columnFamily)
			throws IOException, InterruptedException;

	void majorCompact(String tableNameOrRegionName) throws IOException,
			InterruptedException;

	void majorCompact(byte[] tableNameOrRegionName) throws IOException,
			InterruptedException;

	void majorCompact(String tableNameOrRegionName, String columnFamily)
			throws IOException, InterruptedException;

	void majorCompact(byte[] tableNameOrRegionName, byte[] columnFamily)
			throws IOException, InterruptedException;

	void move(byte[] encodedRegionName, byte[] destServerName)
			throws UnknownRegionException, MasterNotRunningException,
			ZooKeeperConnectionException;

	void assign(byte[] regionName) throws MasterNotRunningException,
			ZooKeeperConnectionException, IOException;

	void unassign(byte[] regionName, boolean force)
			throws MasterNotRunningException, ZooKeeperConnectionException,
			IOException;

	boolean balanceSwitch(boolean b) throws MasterNotRunningException,
			ZooKeeperConnectionException;

	boolean setBalancerRunning(boolean on, boolean synchronous)
			throws MasterNotRunningException, ZooKeeperConnectionException;

	boolean balancer() throws MasterNotRunningException,
			ZooKeeperConnectionException;

	void split(String tableNameOrRegionName) throws IOException,
			InterruptedException;

	void split(byte[] tableNameOrRegionName) throws IOException,
			InterruptedException;

	void split(String tableNameOrRegionName, String splitPoint)
			throws IOException, InterruptedException;

	void split(byte[] tableNameOrRegionName, byte[] splitPoint)
			throws IOException, InterruptedException;

	void modifyTable(byte[] tableName, HTableDescriptor htd) throws IOException;

	void shutdown() throws IOException;

	void stopMaster() throws IOException;

	void stopRegionServer(String hostnamePort) throws IOException;

	ClusterStatus getClusterStatus() throws IOException;

	Configuration getConfiguration();

	List<HRegionInfo> getTableRegions(byte[] tableName) throws IOException;

	HTableDescriptor[] getTableDescriptors(List<String> tableNames)
			throws IOException;

	byte[][] rollHLogWriter(String serverName) throws IOException,
			FailedLogCloseException;

	String[] getMasterCoprocessors();

	CompactionState getCompactionState(String tableNameOrRegionName)
			throws IOException, InterruptedException;

	CompactionState getCompactionState(byte[] tableNameOrRegionName)
			throws IOException, InterruptedException;

	<T extends CoprocessorProtocol> T coprocessorProxy(Class<T> protocol);

	void snapshot(String snapshotName, String tableName) throws IOException,
			SnapshotCreationException, IllegalArgumentException;

	void snapshot(byte[] snapshotName, byte[] tableName) throws IOException,
			SnapshotCreationException, IllegalArgumentException;

	void snapshot(String snapshotName, String tableName, Type type)
			throws IOException, SnapshotCreationException,
			IllegalArgumentException;

	void snapshot(SnapshotDescription snapshot) throws IOException,
			SnapshotCreationException, IllegalArgumentException;

	long takeSnapshotAsync(SnapshotDescription snapshot) throws IOException,
			SnapshotCreationException;

	boolean isSnapshotFinished(SnapshotDescription snapshot)
			throws IOException, HBaseSnapshotException,
			UnknownSnapshotException;

	void restoreSnapshot(byte[] snapshotName) throws IOException,
			RestoreSnapshotException;

	void restoreSnapshot(String snapshotName) throws IOException,
			RestoreSnapshotException;

	void cloneSnapshot(byte[] snapshotName, byte[] tableName)
			throws IOException, TableExistsException, RestoreSnapshotException,
			InterruptedException;

	void cloneSnapshot(String snapshotName, String tableName)
			throws IOException, TableExistsException, RestoreSnapshotException,
			InterruptedException;

	List<SnapshotDescription> listSnapshots() throws IOException;

	void deleteSnapshot(byte[] snapshotName) throws IOException;

	void deleteSnapshot(String snapshotName) throws IOException;

	// --------------------------------------------------

	HTableInterface getTable(String tableName) throws IOException;

	HTableInterface getTable(byte[] tableName) throws IOException;

	HTableDescriptor getTableDescriptor(String tableName) throws IOException;

	boolean exists(String tableName, Get paramGet) throws IOException;

	void batch(String tableName, List<? extends Row> paramList,
			Object[] paramArrayOfObject) throws IOException,
			InterruptedException;

	Object[] batch(String tableName, List<? extends Row> paramList)
			throws IOException, InterruptedException;

	Result get(String tableName, Get paramGet) throws IOException;

	Result[] get(String tableName, List<Get> paramList) throws IOException;

	/** @deprecated */
	Result getRowOrBefore(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) throws IOException;

	ResultScanner getScanner(String tableName, Scan paramScan)
			throws IOException;

	ResultScanner getScanner(String tableName, byte[] paramArrayOfByte)
			throws IOException;

	ResultScanner getScanner(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) throws IOException;

	void put(String tableName, Put paramPut) throws IOException;

	void put(String tableName, List<Put> paramList) throws IOException;

	boolean checkAndPut(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, byte[] paramArrayOfByte3,
			byte[] paramArrayOfByte4, Put paramPut) throws IOException;

	void delete(String tableName, Delete paramDelete) throws IOException;

	void delete(String tableName, List<Delete> paramList) throws IOException;

	boolean checkAndDelete(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, byte[] paramArrayOfByte3,
			byte[] paramArrayOfByte4, Delete paramDelete) throws IOException;

	void mutateRow(String tableName, RowMutations paramRowMutations)
			throws IOException;

	Result append(String tableName, Append paramAppend) throws IOException;

	Result increment(String tableName, Increment paramIncrement)
			throws IOException;

	long incrementColumnValue(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long paramLong)
			throws IOException;

	long incrementColumnValue(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long paramLong,
			boolean paramBoolean) throws IOException;

	boolean isAutoFlush(String tableName);

	void flushCommits(String tableName) throws IOException;

	void closeTable(String tableName) throws IOException;

	void closeTable(HTableInterface table) throws IOException;

	/** @deprecated */
	RowLock lockRow(String tableName, byte[] paramArrayOfByte)
			throws IOException;

	/** @deprecated */
	void unlockRow(String tableName, RowLock paramRowLock) throws IOException;

	<T extends CoprocessorProtocol> T coprocessorProxy(String tableName,
			Class<T> paramClass, byte[] paramArrayOfByte);

	<T extends CoprocessorProtocol, R> Map<byte[], R> coprocessorExec(
			String tableName, Class<T> paramClass, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, Batch.Call<T, R> paramCall)
			throws IOException, Throwable;

	<T extends CoprocessorProtocol, R> void coprocessorExec(String tableName,
			Class<T> paramClass, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, Batch.Call<T, R> paramCall,
			Batch.Callback<R> paramCallback) throws IOException, Throwable;

	void setAutoFlush(String tableName, boolean paramBoolean);

	void setAutoFlush(String tableName, boolean paramBoolean1,
			boolean paramBoolean2);

	long getWriteBufferSize(String tableName);

	void setWriteBufferSize(String tableName, long paramLong)
			throws IOException;
}
