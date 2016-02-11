package org.openyu.commons.bao.hbase;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.RowMutations;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;
import org.openyu.commons.bao.hbase.ex.HzTemplateException;
import org.openyu.commons.hbase.HzSession;
import org.openyu.commons.hbase.HzSessionFactory;
import org.openyu.commons.service.BaseService;

public interface HzTemplate extends BaseService {

	HzSessionFactory getHzSessionFactory();

	void setHzSessionFactory(
			HzSessionFactory hzSessionFactory);

	HzSession getSession();

	void closeSession();

	<T> T execute(HzCallback<T> action) throws HzTemplateException;

	// --------------------------------------------------

	boolean tableExists(String tableName) throws IOException;

	boolean tableExists(byte[] tableName) throws IOException;

	HTableDescriptor[] listTables() throws IOException;

	// --------------------------------------------------

	HTableInterface getTable(String tableName);

	void closeTable(String tableName);

	void closeTable(HTableInterface table);

	<T> T execute(String tableName, HzTableCallback<T> action)
			throws HzTemplateException;

	// --------------------------------------------------

	boolean exists(String tableName, Get paramGet) throws IOException;

	void batch(String tableName, List<? extends Row> paramList,
			Object[] paramArrayOfObject) throws IOException,
			InterruptedException;

	Object[] batch(String tableName, List<? extends Row> paramList)
			throws IOException, InterruptedException;

	Result get(String tableName, Get paramGet) throws IOException;

	Result[] get(String tableName, List<Get> paramList) throws IOException;

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
}
