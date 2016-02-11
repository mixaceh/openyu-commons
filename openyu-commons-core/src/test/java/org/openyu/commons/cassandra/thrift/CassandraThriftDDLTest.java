package org.openyu.commons.cassandra.thrift;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.locator.SimpleStrategy;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.ColumnDef;
import org.apache.cassandra.thrift.IndexType;
import org.apache.cassandra.thrift.KsDef;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.BeforeClass;
import org.junit.Test;

import org.openyu.commons.nio.ByteBufferHelper;
import org.openyu.commons.lang.ByteHelper;

//student
//name	grad	math	art
//Jack	5		97		87
//Rose	4		89		80
//Mary	3		89		80
public class CassandraThriftDDLTest {

	/**
	 * 客戶端
	 */
	protected static Cassandra.Client client = null;

	protected static TTransport ttransport = null;
	protected static TProtocol tprotocol = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ttransport = createTFramedTransport();
		tprotocol = createTBinaryProtocol(ttransport);
		client = new Cassandra.Client(tprotocol);
	}

	public static TTransport createTFramedTransport() throws Exception {
		TTransport result = new TFramedTransport(new TSocket("172.22.29.13",
				9160));// DEV
		result.open();
		return result;
	}

	public static TProtocol createTBinaryProtocol(TTransport ttransport)
			throws Exception {
		return new TBinaryProtocol(ttransport);
	}

	public static Cassandra.Client createClient(TProtocol tprotocol)
			throws Exception {
		return new Cassandra.Client(tprotocol);
	}

	/**
	 * create client
	 *
	 * @throws Exception
	 */
	@Test
	public void client() throws Exception {
		System.out.println(client);
		assertNotNull(client);
	}

	/**
	 * print cluster details
	 *
	 * @throws Exception
	 */
	@Test
	public void clusterDetails() throws Exception {
		System.out.println(client.describe_cluster_name());
	}

	// =================================================
	// keyspace
	// =================================================
	/**
	 * creating a keyspace
	 *
	 * @throws Exception
	 */
	@Test
	public void createKeyspace() throws Exception {
		// keyspace define
		KsDef kd = new KsDef();
		kd.setName("mock");
		// kd.setStrategy_class("org.apache.cassandra.locator.SimpleStrategy");
		kd.setStrategy_class(SimpleStrategy.class.getName());
		//
		Map<String, String> map = new HashMap<String, String>();
		map.put("replication_factor", String.valueOf(1));
		kd.setStrategy_options(map);
		kd.setCf_defs(new ArrayList<CfDef>());
		//
		client.system_add_keyspace(kd);// InvalidRequestException(why:Keyspace
										// names must be case-insensitively
										// unique ("mock" conflicts with
										// "mock"))
	}

	/**
	 * keyspace exist
	 *
	 * @throws Exception
	 */
	@Test
	public void keyspaceExists() throws Exception {
		String KEYSPACE = "mock";
		//
		boolean result = false;
		try {
			KsDef kd = client.describe_keyspace(KEYSPACE);// NotFoundException
			if (kd != null) {
				result = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(result);// true
	}

	/**
	 * getting a keyspace
	 *
	 * @throws Exception
	 */
	@Test
	public void getKeyspace() throws Exception {
		String KEYSPACE = "mock";
		//
		KsDef kd = client.describe_keyspace(KEYSPACE);// NotFoundException
		System.out.println(kd);// KsDef(name:mock,
								// strategy_class:org.apache.cassandra.locator.SimpleStrategy,
								// strategy_options:{replication_factor=1},
								// replication_factor:1, cf_defs:[],
								// durable_writes:true)
		assertNotNull(kd);
	}

	/**
	 * list all keyspaces
	 *
	 * @throws Exception
	 */
	@Test
	// 50 times: 982 mills.
	public void listKeyspaces() throws Exception {
		int count = 50;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			TTransport ttransport = createTFramedTransport();
			TProtocol tprotocol = createTBinaryProtocol(ttransport);
			Cassandra.Client client = createClient(tprotocol);
			System.out.println(client);
			//
			List<KsDef> kds = client.describe_keyspaces();
			for (KsDef kd : kds) {
				StringBuilder buffer = new StringBuilder();
				buffer.append("[");
				buffer.append(kd.getName());
				buffer.append("] ");
				List<CfDef> cds = kd.getCf_defs();
				int size = cds.size();
				for (int j = 0; j < size; j++) {
					buffer.append(cds.get(j).getName());
					if (j < size - 1) {
						buffer.append(", ");
					}
				}
				//
				System.out.println(buffer);
			}
			ttransport.close();

		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	/**
	 * modifying a keyspace
	 *
	 * @throws Exception
	 */
	@Test
	public void modyfyKeyspace() throws Exception {
		String KEYSPACE = "mock";
		//
		KsDef kd = client.describe_keyspace(KEYSPACE);// NotFoundException
		System.out.println(kd.getName());
		// 有column family 就無法修改
		client.system_update_keyspace(kd);// InvalidRequestException(why:Keyspace
											// update must not contain any
											// column family definitions.)

		System.out.println("modify keyspace");
	}

	/**
	 * deleting a keyspace
	 *
	 * @throws Exception
	 */
	@Test
	public void deleteKeyspace() throws Exception {
		String KEYSPACE = "mock";
		//
		client.system_drop_keyspace(KEYSPACE);// InvalidRequestException(why:Keyspace
												// does not exist.)
		System.out.println("delete keyspace");
	}

	// =================================================
	// column family
	// =================================================
	/**
	 * adding a column family to an existing keyspace
	 *
	 * @throws Exception
	 */
	@Test
	public void addColumnFamily() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		CfDef cd = new CfDef();
		cd.setKeyspace(KEYSPACE);
		cd.setName(COLUMN_FAMILY);
		// cd.setKey_alias(key_alias);
		//
		cd.setComparator_type("UTF8Type");
		cd.setKey_validation_class("UTF8Type");
		cd.setDefault_validation_class("UTF8Type");
		client.system_add_column_family(cd);// InvalidRequestException(why:student
											// already exists in keyspace mock)
		//
		System.out.println("add column family [" + COLUMN_FAMILY + "]");
	}

	/**
	 * column family exist
	 *
	 * @throws Exception
	 */
	@Test
	public void columnFamilyExists() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		boolean result = false;
		KsDef kd = client.describe_keyspace(KEYSPACE);
		for (CfDef entry : kd.getCf_defs()) {
			if (entry.getName().equals(COLUMN_FAMILY)) {
				result = true;
				break;
			}
		}
		//
		System.out.println(result);// true
	}

	/**
	 * getting a column family
	 *
	 * @throws Exception
	 */
	@Test
	public void getColumnFamily() throws Exception {
		// String KEYSPACE = "mock";
		// client.set_keyspace(KEYSPACE);
		// //
		// String COLUMN_FAMILY = "student";
		// KsDef kd = client.describe_keyspace(KEYSPACE);
		// CfDef result = null;
		// for (CfDef entry : kd.getCf_defs()) {
		// if (entry.getName().equals(COLUMN_FAMILY)) {
		// result = entry;
		// break;
		// }
		// }
		// System.out.println(result);

		String KEYSPACE = "mock";
		String COLUMN_FAMILY = "student";
		CfDef result = getColumnFamily(client, KEYSPACE, COLUMN_FAMILY);
		System.out.println(result);
	}

	/**
	 * getting a column family
	 *
	 * @param client
	 * @param keyspace
	 * @param columnFamily
	 * @return
	 */
	protected static CfDef getColumnFamily(Cassandra.Client client,
			String keyspace, String columnFamily) {
		CfDef result = null;
		//
		try {
			KsDef kd = client.describe_keyspace(keyspace);
			for (CfDef entry : kd.getCf_defs()) {
				if (entry.getName().equals(columnFamily)) {
					result = entry;
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * list all column familys
	 *
	 * @throws Exception
	 */
	@Test
	public void listColumnFamilys() throws Exception {
		long beg = System.currentTimeMillis();
		//
		String KEYSPACE = "UIH";
		client.set_keyspace(KEYSPACE);
		//
		KsDef kd = client.describe_keyspace(KEYSPACE);
		List<CfDef> result = kd.getCf_defs();
		for (CfDef cd : result) {
			StringBuilder buffer = new StringBuilder();
			buffer.append("[");
			buffer.append(cd.getName());
			buffer.append("] ");
			//
			List<ColumnDef> cols = cd.getColumn_metadata();
			int size = cols.size();
			int i = 0;
			for (ColumnDef column : cols) {
				buffer.append(ByteHelper.toString(column.getName()));
				if (i < size - 1) {
					buffer.append(", ");
				}
				i++;
			}
			//
			System.out.println(buffer);
		}
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
	}

	/**
	 * modifying a column family to an existing keyspace
	 *
	 * @throws Exception
	 */
	@Test
	public void modifyColumnFamily() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		KsDef kd = client.describe_keyspace(KEYSPACE);
		CfDef cd = null;
		for (CfDef entry : kd.getCf_defs()) {
			if (entry.getName().equals(COLUMN_FAMILY)) {
				cd = entry;
				break;
			}
		}
		System.out.println(cd.getName());
		client.system_update_column_family(cd);// InvalidRequestException(why:student
		// already exists in keyspace mock)

		System.out.println("modify column family [" + COLUMN_FAMILY + "]");
	}

	/**
	 * deleting a column family to an existing keyspace
	 *
	 * @throws Exception
	 */
	@Test
	public void deleteColumnFamily() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		client.system_drop_column_family(COLUMN_FAMILY);// InvalidRequestException(why:CF
														// is not defined in
														// that keyspace.)

		System.out.println("delete column family [" + COLUMN_FAMILY + "]");
	}

	// =================================================
	// column
	// =================================================

	/**
	 * adding a column to an column family
	 *
	 * @throws Exception
	 */
	@Test
	public void addColumn() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		CfDef cd = getColumnFamily(client, KEYSPACE, COLUMN_FAMILY);

		// column
		String COLUMN = "grad";
		ColumnDef columnDef = new ColumnDef(
				ByteBufferHelper.toByteBuffer(COLUMN), "UTF8Type");
		columnDef.index_type = IndexType.KEYS;
		columnDef.index_name = "ix_" + COLUMN_FAMILY + "_" + COLUMN;
		cd.addToColumn_metadata(columnDef);
		client.system_update_column_family(cd);
		//
		System.out.println("add column [" + COLUMN + "]");// InvalidRequestException(why:Duplicate
															// index name
															// student_grade_idx)

		// column
		COLUMN = "math";
		ColumnDef columnDef2 = new ColumnDef(
				ByteBufferHelper.toByteBuffer(COLUMN), "UTF8Type");
		columnDef2.index_type = IndexType.KEYS;
		columnDef2.index_name = "ix_" + COLUMN_FAMILY + "_" + COLUMN;
		cd.addToColumn_metadata(columnDef2);
		client.system_update_column_family(cd);
		//
		System.out.println("add column [" + COLUMN + "]");

		// column
		COLUMN = "art";
		ColumnDef columnDef3 = new ColumnDef(
				ByteBufferHelper.toByteBuffer(COLUMN), "UTF8Type");
		columnDef3.index_type = IndexType.KEYS;
		columnDef3.index_name = "ix_" + COLUMN_FAMILY + "_" + COLUMN;
		cd.addToColumn_metadata(columnDef3);
		client.system_update_column_family(cd);
		//
		System.out.println("add column [" + COLUMN + "]");
	}

	/**
	 * column exist
	 *
	 * @throws Exception
	 */
	@Test
	public void columnExists() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		String COLUMN = "grad";
		boolean result = false;
		KsDef kd = client.describe_keyspace(KEYSPACE);
		CfDef cd = null;
		for (CfDef entry : kd.getCf_defs()) {
			if (entry.getName().equals(COLUMN_FAMILY)) {
				cd = entry;
				break;
			}
		}
		//
		for (ColumnDef entry : cd.getColumn_metadata()) {
			if (new String(entry.getName()).equals(COLUMN)) {
				result = true;
				break;
			}
		}
		//
		System.out.println(result);// true
	}

	/**
	 * getting a column
	 *
	 * @throws Exception
	 */
	@Test
	public void getColumn() throws Exception {
		String KEYSPACE = "mock";
		String COLUMN_FAMILY = "student";
		String COLUMN = "grad";
		ColumnDef result = getColumn(client, KEYSPACE, COLUMN_FAMILY, COLUMN);
		System.out.println(result);
	}

	/**
	 * getting a column
	 *
	 * @param client
	 * @param keyspace
	 * @param columnFamily
	 * @return
	 */
	protected static ColumnDef getColumn(Cassandra.Client client,
			String keyspace, String columnFamily, String column) {
		ColumnDef result = null;
		//
		try {
			CfDef cd = getColumnFamily(client, keyspace, columnFamily);
			for (ColumnDef entry : cd.getColumn_metadata()) {
				if (new String(entry.getName()).equals(column)) {
					result = entry;
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * list all columns
	 *
	 * @throws Exception
	 */
	@Test
	public void listColumns() throws Exception {

		String KEYSPACE = "mock";
		String COLUMN_FAMILY = "student";
		CfDef cd = getColumnFamily(client, KEYSPACE, COLUMN_FAMILY);
		System.out.println(cd.getColumn_metadata());
	}

	/**
	 * modifying a column family to an existing keyspace
	 *
	 * @throws Exception
	 */
	@Test
	public void modifyColumn() throws Exception {
		String KEYSPACE = "mock";
		client.set_keyspace(KEYSPACE);
		//
		String COLUMN_FAMILY = "student";
		CfDef cd = getColumnFamily(client, KEYSPACE, COLUMN_FAMILY);
		System.out.println(cd.getName());
		//
		String COLUMN = "grad";
		ColumnDef columnDef = getColumn(client, KEYSPACE, COLUMN_FAMILY, COLUMN);
		System.out.println(new String(columnDef.getName()));

		// TODO

		System.out.println("modify column [" + COLUMN + "]");
	}

	/**
	 * deleting a column family to an existing keyspace
	 *
	 * @throws Exception
	 */
	@Test
	public void deleteColumn() throws Exception {
		// TODO
	}

}
