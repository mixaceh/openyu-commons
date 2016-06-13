package org.openyu.commons.bao.hbase;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.RowMutations;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;
import org.openyu.commons.bao.BaseBao;

/**
 * Hbase Zookeper Big Data Access Object
 */
public interface HzBao extends BaseBao {

	boolean tableExists(String tableName);

	boolean tableExists(byte[] tableName);

	HTableDescriptor[] listTables();

	// --------------------------------------------------

	boolean exists(String tableName, Get paramGet);

	void batch(String tableName, List<? extends Row> paramList, Object[] paramArrayOfObject);

	Object[] batch(String tableName, List<? extends Row> paramList);

	Result get(String tableName, Get paramGet);

	Result[] get(String tableName, List<Get> paramList);

	ResultScanner getScanner(String tableName, Scan paramScan);

	ResultScanner getScanner(String tableName, byte[] paramArrayOfByte);

	ResultScanner getScanner(String tableName, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

	void put(String tableName, Put paramPut);

	void put(String tableName, List<Put> paramList);

	boolean checkAndPut(String tableName, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3,
			byte[] paramArrayOfByte4, Put paramPut);

	void delete(String tableName, Delete paramDelete);

	void delete(String tableName, List<Delete> paramList);

	boolean checkAndDelete(String tableName, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2,
			byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, Delete paramDelete);

	void mutateRow(String tableName, RowMutations paramRowMutations);

	<T extends CoprocessorProtocol> T coprocessorProxy(String tableName, Class<T> paramClass, byte[] paramArrayOfByte);

	<T extends CoprocessorProtocol, R> Map<byte[], R> coprocessorExec(String tableName, Class<T> paramClass,
			byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, Batch.Call<T, R> paramCall);

	<T extends CoprocessorProtocol, R> void coprocessorExec(String tableName, Class<T> paramClass,
			byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, Batch.Call<T, R> paramCall,
			Batch.Callback<R> paramCallback);
}
