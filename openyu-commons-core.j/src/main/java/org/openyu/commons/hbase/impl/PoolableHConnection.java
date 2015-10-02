package org.openyu.commons.hbase.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.pool.ObjectPool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.HServerAddress;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.RowLock;
import org.apache.hadoop.hbase.client.RowMutations;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.ServerCallable;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.client.coprocessor.Batch.Call;
import org.apache.hadoop.hbase.client.coprocessor.Batch.Callback;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;
import org.apache.hadoop.hbase.ipc.HMasterInterface;
import org.apache.hadoop.hbase.ipc.HRegionInterface;
import org.apache.hadoop.hbase.util.Threads;
import org.apache.hadoop.hbase.zookeeper.ZooKeeperWatcher;
import org.openyu.commons.util.Delegateable;
import org.openyu.commons.lang.ByteHelper;

public class PoolableHConnection implements HConnection, Delegateable<HBaseAdmin> {

	private boolean closed = false;

	private HConnection hConnection = null;

	private ObjectPool<HConnection> pool = null;

	private HBaseAdmin delegate;

	private int maxThreads = 100;

	private long keepAliveTime = 60L;
	// --------------------------------------------------

	private Map<String, HTableInterface> tablePool = new ConcurrentHashMap<String, HTableInterface>();

	private Map<String, ExecutorService> tableExecutors = new ConcurrentHashMap<String, ExecutorService>();

	public PoolableHConnection(HConnection hConnection,
			ObjectPool<HConnection> pool) throws MasterNotRunningException,
			ZooKeeperConnectionException {
		this.hConnection = hConnection;
		this.pool = pool;
		//
		this.delegate = new HBaseAdmin(this.hConnection);
		this.maxThreads = getConfiguration().getInt("hbase.htable.threads.max",
				100);
		if (this.maxThreads == 0) {
			this.maxThreads = 1;
		}
		//
		this.keepAliveTime = getConfiguration().getLong(
				"hbase.htable.threads.keepalivetime", 60L);
	}

	public HBaseAdmin getDelegate() {
		return delegate;
	}

	protected void checkOpen() throws ZooKeeperConnectionException {
		if (this.closed) {
			if (null != this.hConnection) {
				String label = "";
				try {
					label = this.hConnection.toString();
				} catch (Exception ex) {
				}
				throw new ZooKeeperConnectionException("HConnection " + label
						+ " was already closed");
			}
			throw new ZooKeeperConnectionException("HConnection is null");
		}
	}

	public synchronized void close() {
		if (this.closed) {
			return;
		}
		boolean isUnderlyingConectionClosed;
		try {
			isUnderlyingConectionClosed = this.hConnection.isClosed();
		} catch (Exception e) {
			try {
				this.pool.invalidateObject(this);
			} catch (IllegalStateException ise) {
				this.closed = true;
				reallyClose();
			} catch (Exception ie) {
			}
			throw new RuntimeException(
					"Cannot close HConnection (isClosed check failed)");
		}
		if (!(isUnderlyingConectionClosed)) {
			try {
				//
				for (HTableInterface table : tablePool.values()) {
					try {
						table.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				//
				this.pool.returnObject(this);
			} catch (IllegalStateException e) {
				this.closed = true;
				reallyClose();
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(
						"Cannot close HConnection (return to pool failed)");
			}
		} else {
			try {
				this.pool.invalidateObject(this);
			} catch (IllegalStateException e) {
				this.closed = true;
				reallyClose();
			} catch (Exception ie) {
			}
			throw new RuntimeException("Already closed.");
		}
	}

	public synchronized void reallyClose() {
		try {
			for (HTableInterface table : tablePool.values()) {
				try {
					table.flushCommits();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			tablePool.clear();
			//
			for (ExecutorService executor : tableExecutors.values()) {
				try {
					executor.shutdown();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			tableExecutors.clear();
			//
			hConnection.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public boolean isOpen() {
		return (!closed) || (!(this.hConnection.isClosed()));
	}

	public void abort(String paramString, Throwable paramThrowable) {
		try {
			checkOpen();
			this.hConnection.abort(paramString, paramThrowable);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isAborted() {
		try {
			checkOpen();
			return this.hConnection.isAborted();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Configuration getConfiguration() {
		try {
			checkOpen();
			return this.hConnection.getConfiguration();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ZooKeeperWatcher getZooKeeperWatcher() throws IOException {
		checkOpen();
		try {
			return this.hConnection.getZooKeeperWatcher();
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HMasterInterface getMaster() throws MasterNotRunningException,
			ZooKeeperConnectionException {
		checkOpen();
		try {
			return this.hConnection.getMaster();
		} catch (MasterNotRunningException e) {
			handleException(e);
		} catch (ZooKeeperConnectionException e) {
			handleException(e);
		}
		return null;
	}

	public boolean isMasterRunning() throws MasterNotRunningException,
			ZooKeeperConnectionException {
		checkOpen();
		try {
			return this.hConnection.isMasterRunning();
		} catch (MasterNotRunningException e) {
			handleException(e);
		} catch (ZooKeeperConnectionException e) {
			handleException(e);
		}
		return false;
	}

	public boolean isTableEnabled(byte[] paramArrayOfByte) throws IOException {
		checkOpen();
		try {
			return this.hConnection.isTableEnabled(paramArrayOfByte);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean isTableDisabled(byte[] paramArrayOfByte) throws IOException {
		checkOpen();
		try {
			return this.hConnection.isTableDisabled(paramArrayOfByte);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean isTableAvailable(byte[] paramArrayOfByte) throws IOException {
		checkOpen();
		try {
			return this.hConnection.isTableAvailable(paramArrayOfByte);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public HTableDescriptor[] listTables() throws IOException {
		checkOpen();
		try {
			return this.hConnection.listTables();
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HTableDescriptor getHTableDescriptor(byte[] paramArrayOfByte)
			throws IOException {
		checkOpen();
		try {
			return this.hConnection.getHTableDescriptor(paramArrayOfByte);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HRegionLocation locateRegion(byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) throws IOException {
		checkOpen();
		try {
			return this.hConnection.locateRegion(paramArrayOfByte1,
					paramArrayOfByte2);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public void clearRegionCache() {
		try {
			checkOpen();
			this.hConnection.clearRegionCache();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void clearRegionCache(byte[] paramArrayOfByte) {
		try {
			checkOpen();
			this.hConnection.clearRegionCache(paramArrayOfByte);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public HRegionLocation relocateRegion(byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) throws IOException {
		checkOpen();
		try {
			return this.hConnection.relocateRegion(paramArrayOfByte1,
					paramArrayOfByte2);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HRegionLocation locateRegion(byte[] paramArrayOfByte)
			throws IOException {
		checkOpen();
		try {
			return this.hConnection.locateRegion(paramArrayOfByte);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public List<HRegionLocation> locateRegions(byte[] paramArrayOfByte)
			throws IOException {
		checkOpen();
		try {
			return this.hConnection.locateRegions(paramArrayOfByte);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HRegionInterface getHRegionConnection(
			HServerAddress paramHServerAddress) throws IOException {
		checkOpen();
		try {
			return this.hConnection.getHRegionConnection(paramHServerAddress);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HRegionInterface getHRegionConnection(String paramString,
			int paramInt) throws IOException {
		checkOpen();
		try {
			return this.hConnection.getHRegionConnection(paramString, paramInt);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HRegionInterface getHRegionConnection(
			HServerAddress paramHServerAddress, boolean paramBoolean)
			throws IOException {
		checkOpen();
		try {
			return this.hConnection.getHRegionConnection(paramHServerAddress,
					paramBoolean);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HRegionInterface getHRegionConnection(String paramString,
			int paramInt, boolean paramBoolean) throws IOException {
		checkOpen();
		try {
			return this.hConnection.getHRegionConnection(paramString, paramInt,
					paramBoolean);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public HRegionLocation getRegionLocation(byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, boolean paramBoolean) throws IOException {
		checkOpen();
		try {
			return this.hConnection.getRegionLocation(paramArrayOfByte1,
					paramArrayOfByte2, paramBoolean);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public <T> T getRegionServerWithRetries(
			ServerCallable<T> paramServerCallable) throws IOException,
			RuntimeException {
		checkOpen();
		try {
			return this.hConnection
					.getRegionServerWithRetries(paramServerCallable);
		} catch (IOException e) {
			handleException(e);
		} catch (RuntimeException e) {
			handleException(e);
		}
		return null;
	}

	public <T> T getRegionServerWithoutRetries(
			ServerCallable<T> paramServerCallable) throws IOException,
			RuntimeException {
		checkOpen();
		try {
			return this.hConnection
					.getRegionServerWithoutRetries(paramServerCallable);
		} catch (IOException e) {
			handleException(e);
		} catch (RuntimeException e) {
			handleException(e);
		}
		return null;
	}

	public void processBatch(List<? extends Row> paramList,
			byte[] paramArrayOfByte, ExecutorService paramExecutorService,
			Object[] paramArrayOfObject) throws IOException,
			InterruptedException {
		checkOpen();
		try {
			this.hConnection.processBatch(paramList, paramArrayOfByte,
					paramExecutorService, paramArrayOfObject);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public <R> void processBatchCallback(List<? extends Row> paramList,
			byte[] paramArrayOfByte, ExecutorService paramExecutorService,
			Object[] paramArrayOfObject, Callback<R> paramCallback)
			throws IOException, InterruptedException {
		checkOpen();
		try {
			this.hConnection.processBatchCallback(paramList, paramArrayOfByte,
					paramExecutorService, paramArrayOfObject, paramCallback);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public <T extends CoprocessorProtocol, R> void processExecs(
			Class<T> paramClass, List<byte[]> paramList,
			byte[] paramArrayOfByte, ExecutorService paramExecutorService,
			Call<T, R> paramCall, Callback<R> paramCallback)
			throws IOException, Throwable {
		checkOpen();
		try {
			this.hConnection.processExecs(paramClass, paramList,
					paramArrayOfByte, paramExecutorService, paramCall,
					paramCallback);
		} catch (IOException e) {
			handleException(e);
		} catch (Throwable e) {
			handleException(e);
		}
	}

	public void setRegionCachePrefetch(byte[] paramArrayOfByte,
			boolean paramBoolean) {
		try {
			checkOpen();
			this.hConnection.setRegionCachePrefetch(paramArrayOfByte,
					paramBoolean);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean getRegionCachePrefetch(byte[] paramArrayOfByte) {
		try {
			checkOpen();
			return this.hConnection.getRegionCachePrefetch(paramArrayOfByte);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void prewarmRegionCache(byte[] paramArrayOfByte,
			Map<HRegionInfo, HServerAddress> paramMap) {
		try {
			checkOpen();
			this.hConnection.prewarmRegionCache(paramArrayOfByte, paramMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int getCurrentNrHRS() throws IOException {
		checkOpen();
		try {
			return this.hConnection.getCurrentNrHRS();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public HTableDescriptor[] getHTableDescriptors(List<String> paramList)
			throws IOException {
		checkOpen();
		try {
			return this.hConnection.getHTableDescriptors(paramList);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public boolean isClosed() {
		try {
			checkOpen();
			return this.hConnection.isClosed();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void clearCaches(String paramString) {
		try {
			checkOpen();
			this.hConnection.clearCaches(paramString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// --------------------------------------------------
	public HTableInterface getTable(byte[] tableName) throws IOException {
		return getTable(ByteHelper.toString(tableName));
	}

	public HTableInterface getTable(String tableName) throws IOException {
		checkOpen();
		try {
			HTableInterface result = tablePool.get(tableName);
			if (result == null) {
				result = createTable(tableName);
				result = new PoolableHTable(this, result);
			}
			return result;
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	protected HTableInterface createTable(byte[] tableName) throws IOException {
		return createTable(ByteHelper.toString(tableName));
	}

	protected HTableInterface createTable(String tableName) throws IOException {
		HTable result = null;
		//
		ExecutorService executor = new ThreadPoolExecutor(1, maxThreads,
				keepAliveTime, TimeUnit.SECONDS, new SynchronousQueue(),
				Threads.newDaemonThreadFactory("hbase-table"));
		this.tableExecutors.put(tableName, executor);
		//
		result = new HTable(ByteHelper.toByteArray(tableName), this.hConnection,
				executor);
		//
		return result;
	}

	public void closeTable(HTableInterface table) throws IOException {
		checkOpen();
		try {
			table.flushCommits();
			tablePool.put(ByteHelper.toString(table.getTableName()), table);
		} catch (IOException e) {
			handleException(e);
		}
	}

	// --------------------------------------------------

	public HTableDescriptor getTableDescriptor(String tableName)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.getTableDescriptor();
		} catch (IOException e) {
			handleException(e);
		}
		return null;

	}

	public boolean exists(String tableName, Get paramGet) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.exists(paramGet);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public void batch(String tableName, List<? extends Row> paramList,
			Object[] paramArrayOfObject) throws IOException,
			InterruptedException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.batch(paramList, paramArrayOfObject);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
	}

	public Object[] batch(String tableName, List<? extends Row> paramList)
			throws IOException, InterruptedException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.batch(paramList);
		} catch (IOException e) {
			handleException(e);
		} catch (InterruptedException e) {
			handleException(e);
		}
		return null;
	}

	public Result get(String tableName, Get paramGet) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.get(paramGet);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public Result[] get(String tableName, List<Get> paramList)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.get(paramList);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	/** @deprecated */
	public Result getRowOrBefore(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.getRowOrBefore(paramArrayOfByte1, paramArrayOfByte2);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public ResultScanner getScanner(String tableName, Scan paramScan)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.getScanner(paramScan);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public ResultScanner getScanner(String tableName, byte[] paramArrayOfByte)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.getScanner(paramArrayOfByte);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public ResultScanner getScanner(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.getScanner(paramArrayOfByte1, paramArrayOfByte2);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public void put(String tableName, Put paramPut) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.put(paramPut);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void put(String tableName, List<Put> paramList) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.put(paramList);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public boolean checkAndPut(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, byte[] paramArrayOfByte3,
			byte[] paramArrayOfByte4, Put paramPut) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.checkAndPut(paramArrayOfByte1, paramArrayOfByte2,
					paramArrayOfByte3, paramArrayOfByte4, paramPut);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public void delete(String tableName, Delete paramDelete) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.delete(paramDelete);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void delete(String tableName, List<Delete> paramList)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.delete(paramList);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public boolean checkAndDelete(String tableName, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, byte[] paramArrayOfByte3,
			byte[] paramArrayOfByte4, Delete paramDelete) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.checkAndDelete(paramArrayOfByte1, paramArrayOfByte2,
					paramArrayOfByte3, paramArrayOfByte4, paramDelete);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public void mutateRow(String tableName, RowMutations paramRowMutations)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.mutateRow(paramRowMutations);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public Result append(String tableName, Append paramAppend)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.append(paramAppend);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public Result increment(String tableName, Increment paramIncrement)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.increment(paramIncrement);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public long incrementColumnValue(String tableName,
			byte[] paramArrayOfByte1, byte[] paramArrayOfByte2,
			byte[] paramArrayOfByte3, long paramLong) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.incrementColumnValue(paramArrayOfByte1,
					paramArrayOfByte2, paramArrayOfByte3, paramLong);
		} catch (IOException e) {
			handleException(e);
		}
		return 0L;
	}

	public long incrementColumnValue(String tableName,
			byte[] paramArrayOfByte1, byte[] paramArrayOfByte2,
			byte[] paramArrayOfByte3, long paramLong, boolean paramBoolean)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.incrementColumnValue(paramArrayOfByte1,
					paramArrayOfByte2, paramArrayOfByte3, paramLong,
					paramBoolean);
		} catch (IOException e) {
			handleException(e);
		}
		return 0L;
	}

	public boolean isAutoFlush(String tableName) {
		try {
			checkOpen();
			HTableInterface table = getTable(tableName);
			return table.isAutoFlush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void flushCommits(String tableName) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.flushCommits();
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void closeTable(String tableName) throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.close();
		} catch (IOException e) {
			handleException(e);
		}
	}

	/** @deprecated */
	public RowLock lockRow(String tableName, byte[] paramArrayOfByte)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.lockRow(paramArrayOfByte);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	/** @deprecated */
	public void unlockRow(String tableName, RowLock paramRowLock)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.unlockRow(paramRowLock);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public <T extends CoprocessorProtocol> T coprocessorProxy(String tableName,
			Class<T> paramClass, byte[] paramArrayOfByte) {
		try {
			checkOpen();
			HTableInterface table = getTable(tableName);
			return table.coprocessorProxy(paramClass, paramArrayOfByte);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public <T extends CoprocessorProtocol, R> Map<byte[], R> coprocessorExec(
			String tableName, Class<T> paramClass, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, Batch.Call<T, R> paramCall)
			throws IOException, Throwable {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			return table.coprocessorExec(paramClass, paramArrayOfByte1,
					paramArrayOfByte2, paramCall);
		} catch (IOException e) {
			handleException(e);
		} catch (Throwable e) {
			handleException(e);
		}
		return null;
	}

	public <T extends CoprocessorProtocol, R> void coprocessorExec(
			String tableName, Class<T> paramClass, byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, Batch.Call<T, R> paramCall,
			Batch.Callback<R> paramCallback) throws IOException, Throwable {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.coprocessorExec(paramClass, paramArrayOfByte1,
					paramArrayOfByte2, paramCall, paramCallback);
		} catch (IOException e) {
			handleException(e);
		} catch (Throwable e) {
			handleException(e);
		}
	}

	public void setAutoFlush(String tableName, boolean paramBoolean) {
		try {
			checkOpen();
			HTableInterface table = getTable(tableName);
			table.setAutoFlush(paramBoolean);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setAutoFlush(String tableName, boolean paramBoolean1,
			boolean paramBoolean2) {
		try {
			checkOpen();
			HTableInterface table = getTable(tableName);
			table.setAutoFlush(paramBoolean1, paramBoolean2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public long getWriteBufferSize(String tableName) {
		try {
			checkOpen();
			HTableInterface table = getTable(tableName);
			return table.getWriteBufferSize();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setWriteBufferSize(String tableName, long paramLong)
			throws IOException {
		checkOpen();
		try {
			HTableInterface table = getTable(tableName);
			table.setWriteBufferSize(paramLong);
		} catch (IOException e) {
			handleException(e);
		}
	}

	// --------------------------------------------------

	protected void handleException(ZooKeeperConnectionException e)
			throws ZooKeeperConnectionException {
		throw e;
	}

	protected void handleException(IOException e) throws IOException {
		throw e;
	}

	protected void handleException(MasterNotRunningException e)
			throws MasterNotRunningException {
		throw e;
	}

	protected void handleException(RuntimeException e) throws RuntimeException {
		throw e;
	}

	protected void handleException(InterruptedException e)
			throws InterruptedException {
		throw e;
	}

	protected void handleException(Throwable e) throws Throwable {
		throw e;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("closed", closed);
		builder.append("delegate", delegate);
		builder.append("tablePool", tablePool);
		return builder.toString();
	}
}
