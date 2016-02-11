package org.openyu.commons.cassandra.thrift;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.Compression;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.CqlResult;
import org.apache.cassandra.thrift.CqlRow;
import org.apache.cassandra.thrift.Deletion;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.Mutation;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.junit.Test;

import org.openyu.commons.nio.ByteBufferHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;

public class CassandraThriftDMLTest extends CassandraThriftDDLTest {

	/**
	 * insert
	 *
	 * @throws Exception
	 */
	@Test
	public void insert() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		// column family
		ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY);
		//
		String ROW_KEY = "Jack";
		long timestamp = System.nanoTime();

		// column name
		Column column = new Column();
		column.setName(ByteBufferHelper.toByteBuffer("grad"));
		column.setValue(ByteBufferHelper.toByteBuffer("5"));
		column.setTimestamp(timestamp);
		//
		client.insert(ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
				column, ConsistencyLevel.ONE);

		// column name
		column = new Column();
		column.setName(ByteBufferHelper.toByteBuffer("math"));
		column.setValue(ByteBufferHelper.toByteBuffer("97"));
		column.setTimestamp(timestamp);
		//
		client.insert(ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
				column, ConsistencyLevel.ONE);

		// column name
		column = new Column();
		column.setName(ByteBufferHelper.toByteBuffer("art"));
		column.setValue(ByteBufferHelper.toByteBuffer("87"));
		column.setTimestamp(timestamp);
		//
		client.insert(ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
				column, ConsistencyLevel.ONE);
	}

	/**
	 * get 讀取1個column
	 *
	 * @throws Exception
	 */
	@Test
	public void get() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);

		// 讀取1個column
		String COLUMN_FAMILY = "student";
		ColumnPath columnPath = new ColumnPath(COLUMN_FAMILY);
		//
		String COLUMN = "grad";
		columnPath.setColumn(ByteBufferHelper.toByteBuffer(COLUMN));

		String ROW_KEY = "Jack";
		// key, column_path, consistency_level
		ColumnOrSuperColumn cos = client.get(
				ByteBufferHelper.toByteBuffer(ROW_KEY), columnPath,
				ConsistencyLevel.ONE);// NotFoundException

		Column column = cos.getColumn();
		System.out.println(ROW_KEY + ", "
				+ ByteHelper.toString(column.getName()) + ": "
				+ ByteHelper.toString(column.getValue()) + ", "
				+ column.getTimestamp());
		// Jack, grad: 5, 1380932164492000
	}

	/**
	 * get 讀取所有column
	 *
	 * @throws Exception
	 */
	@Test
	public void get2() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);

		// 讀取所有column
		String COLUMN_FAMILY = "student";
		ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY);

		// 術語
		SlicePredicate predicate = new SlicePredicate();

		// 範圍
		SliceRange sliceRange = new SliceRange();
		// sliceRange.setStart(ByteBufferHelper.toByteBuffer(new byte[0]));//開始
		sliceRange.setStart(new byte[0]);// 開始
		sliceRange.setFinish(new byte[0]);// 結束
		sliceRange.setCount(100);// 筆數
		//
		predicate.setSlice_range(sliceRange);

		String ROW_KEY = "Jack";
		// 結果
		// key, column_parent, predicate, consistency_level
		List<ColumnOrSuperColumn> results = client.get_slice(
				ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
				predicate, ConsistencyLevel.ONE);

		for (ColumnOrSuperColumn cos : results) {
			Column column = cos.getColumn();
			System.out.println(ROW_KEY + ", "
					+ ByteHelper.toString(column.getName()) + ": "
					+ ByteHelper.toString(column.getValue()) + ", "
					+ column.getTimestamp());
			// Jack, art, 87, 1380788003220
			// Jack, grad, 5, 1380788003203
			// Jack, math, 97, 1380788003214
		}
	}

	/**
	 * get
	 *
	 * @throws Exception
	 */
	@Test
	public void getByKey() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);

		String COLUMN_FAMILY = "student";
		// 讀取整筆
		ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY);
		// 術語
		SlicePredicate predicate = new SlicePredicate();

		// InvalidRequestException(why:predicate column_names and slice_range
		// may not both be present)

		// 範圍
		// SliceRange sliceRange = new SliceRange();
		// sliceRange.setStart(new byte[0]);// 開始
		// sliceRange.setFinish(new byte[0]);// 結束
		// predicate.setSlice_range(sliceRange);

		// 讀取1個column
		predicate.addToColumn_names(ByteBufferHelper.toByteBuffer("grad"));

		// key範圍
		KeyRange keyRange = new KeyRange();
		keyRange.setStart_key(new byte[0]);
		keyRange.setEnd_key(new byte[0]);
		keyRange.setCount(100);

		// 結果
		// column_parent, predicate, range, consistency_level
		List<KeySlice> results = client.get_range_slices(columnParent,
				predicate, keyRange, ConsistencyLevel.ONE);

		for (KeySlice keySlice : results) {

			for (ColumnOrSuperColumn cos : keySlice.getColumns()) {
				Column column = cos.column;
				System.out.println(ByteHelper.toString(keySlice.getKey())
						+ ", " + ByteHelper.toString(column.getName()) + ": "
						+ ByteHelper.toString(column.getValue()) + ", "
						+ column.getTimestamp());
				// Rose, grad, 4, 1380931646061000
				// Jack, art, 87, 1380933848350
				// Jack, grad, 5, 1380932164492000
				// Jack, math, 97, 1380933848305
			}
		}
	}

	/**
	 * getByCql
	 *
	 * @throws Exception
	 */
	@Test
	public void getByCql() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String CQL = "select * from student where KEY='Jack'";
		// query, compression
		CqlResult result = client.execute_cql_query(
				ByteBufferHelper.toByteBuffer(CQL), Compression.NONE);
		System.out.println(result);

		for (CqlRow cqlRow : result.getRows()) {
			for (Column column : cqlRow.getColumns()) {
				System.out.println(ByteHelper.toString(cqlRow.getKey()) + ", "
						+ ByteHelper.toString(column.getName()) + ": "
						+ ByteHelper.toString(column.getValue()) + ", "
						+ column.getTimestamp());
				// Jack, KEY: Jack, -1
				// Jack, art: 87, 1380933848350
				// Jack, grad: 5, 1380932164492000
				// Jack, math: 97, 1380933848305
			}
		}
	}

	/**
	 * remove
	 *
	 * 當remove後,key還會存在,所以再insert會無法新增
	 *
	 * @throws Exception
	 */
	@Test
	public void remove() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		ColumnPath columnPath = new ColumnPath(COLUMN_FAMILY);
		//
		String ROW_KEY = "Jack";
		// key, column_path, timestamp, consistency_level
		client.remove(ByteBufferHelper.toByteBuffer(ROW_KEY), columnPath,
				System.nanoTime(), ConsistencyLevel.ONE);
	}

	/**
	 * removeByCql
	 *
	 * @throws Exception
	 */
	@Test
	public void removeByCql() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String CQL = "delete from student where KEY='Mary'";
		CqlResult result = client.execute_cql_query(
				ByteBufferHelper.toByteBuffer(CQL), Compression.NONE);
		System.out.println(result);
	}

	/**
	 * update
	 *
	 * @throws Exception
	 */
	@Test
	public void update() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);

		List<Mutation> mutations = new LinkedList<Mutation>();
		// <columnFamily,mutations>
		Map<String, List<Mutation>> columnfamilyMutaions = new HashMap<String, List<Mutation>>();// keyMutations
		// <rowKey,keyMutations>
		Map<ByteBuffer, Map<String, List<Mutation>>> rowKeyMutations = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();

		long timestamp = System.nanoTime();
		//
		Column column = new Column();
		column.setName(ByteBufferHelper.toByteBuffer("grad"));
		column.setValue(ByteBufferHelper.toByteBuffer("9"));
		column.setTimestamp(timestamp);
		//
		ColumnOrSuperColumn cos = new ColumnOrSuperColumn();
		cos.setColumn(column);
		//
		Mutation mutation = new Mutation();
		mutation.setColumn_or_supercolumn(cos);
		mutations.add(mutation);

		String COLUMN_FAMILY = "student";
		columnfamilyMutaions.put(COLUMN_FAMILY, mutations);

		String ROW_KEY = "Jack";
		rowKeyMutations.put(ByteBufferHelper.toByteBuffer(ROW_KEY),
				columnfamilyMutaions);

		// mutation_map, consistency_level
		client.batch_mutate(rowKeyMutations, ConsistencyLevel.ONE);
	}

	/**
	 * delete
	 *
	 * @throws Exception
	 */
	@Test
	public void delete() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);

		List<Mutation> mutations = new ArrayList<Mutation>();
		// <columnFamily,mutations>
		Map<String, List<Mutation>> columnfamilyMutaions = new HashMap<String, List<Mutation>>();// keyMutations
		// <rowKey,keyMutations>
		Map<ByteBuffer, Map<String, List<Mutation>>> rowKeyMutations = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
		//
		List<ByteBuffer> columns = new ArrayList<ByteBuffer>();
		// Add as many supercolumns as you want here
		columns.add(ByteBufferHelper.toByteBuffer("grad"));
		columns.add(ByteBufferHelper.toByteBuffer("math"));
		//
		SlicePredicate predicate = new SlicePredicate();
		predicate.setColumn_names(columns);
		// delete
		Deletion deletion = new Deletion();
		deletion.setPredicate(predicate);
		// timestamp in microseconds
		long timestamp = System.nanoTime();
		deletion.setTimestamp(timestamp);

		Mutation mutation = new Mutation();
		mutation.setDeletion(deletion);
		mutations.add(mutation);

		String COLUMN_FAMILY = "student";
		columnfamilyMutaions.put(COLUMN_FAMILY, mutations);

		String ROW_KEY = "Jack";
		rowKeyMutations.put(ByteBufferHelper.toByteBuffer(ROW_KEY),
				columnfamilyMutaions);

		// mutation_map, consistency_level
		client.batch_mutate(rowKeyMutations, ConsistencyLevel.ONE);
	}

	@Test
	public void getTimestamp() {
		System.out.println(getTimestamp(System.currentTimeMillis()));
	}

	// 若已是毫微秒則不乘上1000
	// 若是毫秒則乘上1000
	public long getTimestamp(long time) {
		return time > (1L * Math.pow(10, 15)) ? time : time * 1000;
	}

	/**
	 * crud
	 *
	 * @throws Exception
	 */
	@Test
	public void crud() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		// column family
		ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY);

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			String ROW_KEY = "TEST_STUDENT_"
					+ NumberHelper.randomInt(count * count);
			long timestamp = System.nanoTime();

			// insert
			// column name
			Column column = new Column();
			column.setName(ByteBufferHelper.toByteBuffer("grad"));
			column.setValue(ByteBufferHelper.toByteBuffer(String
					.valueOf(NumberHelper.randomInt(10))));
			column.setTimestamp(timestamp);
			//
			client.insert(ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
					column, ConsistencyLevel.ONE);

			// column name
			column = new Column();
			column.setName(ByteBufferHelper.toByteBuffer("math"));
			column.setValue(ByteBufferHelper.toByteBuffer(String
					.valueOf(NumberHelper.randomInt(100))));
			column.setTimestamp(timestamp);
			//
			client.insert(ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
					column, ConsistencyLevel.ONE);

			// column name
			column = new Column();
			column.setName(ByteBufferHelper.toByteBuffer("art"));
			column.setValue(ByteBufferHelper.toByteBuffer(String
					.valueOf(NumberHelper.randomInt(100))));
			column.setTimestamp(timestamp);
			//
			client.insert(ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
					column, ConsistencyLevel.ONE);
			System.out.println("insert: " + ROW_KEY);

			// retrive
			// 術語
			SlicePredicate predicate = new SlicePredicate();

			// 範圍
			SliceRange sliceRange = new SliceRange();
			// sliceRange.setStart(ByteBufferHelper.toByteBuffer(new
			// byte[0]));//開始
			sliceRange.setStart(new byte[0]);// 開始
			sliceRange.setFinish(new byte[0]);// 結束
			sliceRange.setCount(100);// 筆數
			//
			predicate.setSlice_range(sliceRange);

			// 結果
			// key, column_parent, predicate, consistency_level
			List<ColumnOrSuperColumn> results = client.get_slice(
					ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
					predicate, ConsistencyLevel.ONE);

			for (ColumnOrSuperColumn cos : results) {
				column = cos.getColumn();
				System.out.println(ROW_KEY + ", "
						+ ByteHelper.toString(column.getName()) + ": "
						+ ByteHelper.toString(column.getValue()) + ", "
						+ column.getTimestamp());
				// TEST_STUDENT_0, art: 70, 21586763983507
				// TEST_STUDENT_0, grad: 2, 21586763983507
				// TEST_STUDENT_0, math: 99, 21586763983507
			}
			System.out.println("retrive: " + ROW_KEY);

			// update
			List<Mutation> mutations = new ArrayList<Mutation>();
			// <columnFamily,mutations>
			Map<String, List<Mutation>> columnfamilyMutaions = new HashMap<String, List<Mutation>>();// keyMutations
			// <rowKey,keyMutations>
			Map<ByteBuffer, Map<String, List<Mutation>>> rowKeyMutations = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();

			column = new Column();
			column.setName(ByteBufferHelper.toByteBuffer("grad"));
			column.setValue(ByteBufferHelper.toByteBuffer("999"));
			column.setTimestamp(timestamp);
			//
			ColumnOrSuperColumn cos = new ColumnOrSuperColumn();
			cos.setColumn(column);
			//
			Mutation mutation = new Mutation();
			mutation.setColumn_or_supercolumn(cos);
			mutations.add(mutation);

			columnfamilyMutaions.put(COLUMN_FAMILY, mutations);

			rowKeyMutations.put(ByteBufferHelper.toByteBuffer(ROW_KEY),
					columnfamilyMutaions);

			// mutation_map, consistency_level
			client.batch_mutate(rowKeyMutations, ConsistencyLevel.ONE);
			System.out.println("update: " + ROW_KEY);

			// delete
			List<ByteBuffer> columns = new ArrayList<ByteBuffer>();
			// Add as many supercolumns as you want here
			columns.add(ByteBufferHelper.toByteBuffer("grad"));
			//
			predicate = new SlicePredicate();
			predicate.setColumn_names(columns);
			// delete
			Deletion deletion = new Deletion();
			deletion.setPredicate(predicate);
			// timestamp in microseconds
			deletion.setTimestamp(timestamp);

			mutation = new Mutation();
			mutation.setDeletion(deletion);
			mutations.add(mutation);

			columnfamilyMutaions.put(COLUMN_FAMILY, mutations);

			rowKeyMutations.put(ByteBufferHelper.toByteBuffer(ROW_KEY),
					columnfamilyMutaions);

			// mutation_map, consistency_level
			client.batch_mutate(rowKeyMutations, ConsistencyLevel.ONE);
			System.out.println("delete: " + ROW_KEY);
		}
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
	}

	@Test
	public void mock() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		// column family
		ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY);

		int count = 10;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			String ROW_KEY = "TEST_STUDENT_"
					+ NumberHelper.randomInt(count * count);
			long timestamp = System.nanoTime();

			// insert
			// column name
			Column column = new Column();
			column.setName(ByteBufferHelper.toByteBuffer("grad"));
			column.setValue(ByteBufferHelper.toByteBuffer(String
					.valueOf(NumberHelper.randomInt(10))));
			column.setTimestamp(timestamp);
			//
			client.insert(ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
					column, ConsistencyLevel.ONE);

			// column name
			column = new Column();
			column.setName(ByteBufferHelper.toByteBuffer("math"));
			column.setValue(ByteBufferHelper.toByteBuffer(String
					.valueOf(NumberHelper.randomInt(100))));
			column.setTimestamp(timestamp);
			//
			client.insert(ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
					column, ConsistencyLevel.ONE);

			// column name
			column = new Column();
			column.setName(ByteBufferHelper.toByteBuffer("art"));
			column.setValue(ByteBufferHelper.toByteBuffer(String
					.valueOf(NumberHelper.randomInt(100))));
			column.setTimestamp(timestamp);
			//
			client.insert(ByteBufferHelper.toByteBuffer(ROW_KEY), columnParent,
					column, ConsistencyLevel.ONE);
			System.out.println("insert: " + ROW_KEY);
		}
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
	}
}
