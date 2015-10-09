package org.openyu.commons.hbase.thrift;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.filter.FuzzyRowFilter;
import org.apache.hadoop.hbase.filter.MultipleColumnPrefixFilter;
import org.apache.hadoop.hbase.thrift.generated.Hbase;
import org.apache.hadoop.hbase.thrift.generated.Mutation;
import org.apache.hadoop.hbase.thrift.generated.TCell;
import org.apache.hadoop.hbase.thrift.generated.TRowResult;
import org.apache.hadoop.hbase.thrift.generated.TScan;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import org.openyu.commons.nio.ByteBufferHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;

public class HBaseThriftDMLTest extends HBaseThriftDDLTest {

	/**
	 * 列印TRowResult
	 *
	 * @param results
	 */
	public static void printlnResult(List<TRowResult> results) {
		for (TRowResult result : results) {
			printlnResult(result);
		}
	}

	/**
	 * 列印TRowResult
	 *
	 * @param result
	 */
	public static void printlnResult(TRowResult result) {
		StringBuilder buff = new StringBuilder();
		Map<ByteBuffer, TCell> columns = result.getColumns();
		int size = columns.size();
		//
		buff.append(ByteHelper.toString(result.getRow()) + ", ");// rowKey
		int i = 0;
		for (Map.Entry<ByteBuffer, TCell> entry : columns.entrySet()) {
			buff.append(ByteBufferHelper.toString(entry.getKey()));
			buff.append("=");
			buff.append(ByteHelper.toString((entry.getValue().getValue())));
			//
			if (i < size - 1) {
				buff.append(", ");
			}
			i++;
		}
		System.out.println(buff);
	}

	/**
	 * 陣列相加
	 *
	 * @param a
	 * @param b
	 */
	public static byte[] add(byte[] a, byte[] b) {
		return add(a, b, new byte[0]);
	}

	/**
	 * 陣列相加
	 *
	 * @param a
	 * @param b
	 * @param c
	 */
	public static byte[] add(byte[] a, byte[] b, byte[] c) {
		byte[] result = new byte[a.length + b.length + c.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		System.arraycopy(c, 0, result, a.length + b.length, c.length);
		return result;
	}

	/**
	 * get 讀取所有column
	 *
	 * @throws Exception
	 */
	@Test
	// 483 at mills.
	// 492 at mills.
	// 495 at mills.
	public void get() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";
		String rowKey = "1000|I200|A17P|AUDI15028071";
		//
		Map<ByteBuffer, ByteBuffer> attributes = new LinkedHashMap<ByteBuffer, ByteBuffer>();

		long beg = System.currentTimeMillis();
		List<TRowResult> results = client.getRow(
				ByteBufferHelper.toByteBuffer(TABLE_NAME),
				ByteBufferHelper.toByteBuffer(rowKey), attributes);// 可以attributes=null
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
		printlnResult(results);
	}

	/**
	 * get 讀取特定column
	 *
	 * @throws Exception
	 */
	@Test
	// 301 at mills.
	// 303 at mills.
	// 302 at mills.
	public void getOneColumn() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";
		String rowKey = "1000|I200|A17P|AUDI15028071";
		String cloumn = "CommonInfo:SellerID";
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		columns.add(ByteBufferHelper.toByteBuffer(cloumn));
		//
		Map<ByteBuffer, ByteBuffer> attributes = new LinkedHashMap<ByteBuffer, ByteBuffer>();

		long beg = System.currentTimeMillis();
		List<TRowResult> results = client.getRowWithColumns(
				ByteBufferHelper.toByteBuffer(TABLE_NAME),
				ByteBufferHelper.toByteBuffer(rowKey), columns, attributes);// 可以attributes=null
		long end = System.currentTimeMillis();
		//
		System.out.println((end - beg) + " at mills.");
		//
		printlnResult(results);
	}

	/**
	 * scan 讀取所有column
	 *
	 * @throws Exception
	 */
	@Test
	// hbase.client.scanner.caching=200
	// 15172 at mills.
	// 15514 at mills.
	// 15037 at mills.
	public void scan() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		int rowCount = 0;
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			scannerId = client.scannerOpen(
					ByteBufferHelper.toByteBuffer(TABLE_NAME),
					ByteBufferHelper.toByteBuffer(new byte[] {}), columns,
					attributes);// 可以attributes=null
			// System.out.println(scannerId);
			while (true) {
				List<TRowResult> results = client.scannerGet(scannerId);
				if (results.isEmpty()) {
					break;
				}
				 printlnResult(results);
				rowCount++;

				// 太多筆會很慢,先算個50就好
				if (rowCount > 49) {
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scannerId >= 0) {
				client.scannerClose(scannerId);// 一定要關閉
			}
		}
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	/**
	 * scan 讀取特定column
	 *
	 * @throws Exception
	 */
	@Test
	// 1053 at mills.
	// 897 at mills.
	// 888 at mills.
	public void scanOneColumn() throws Exception {
		// String TABLE_NAME = "ItemCreation_Buffer_Item";
		// String rowKey =
		// "0016630555\\x0501\\x0501\\x05A1FS\\x05CreateItem\\x0516630555\\x050000116\\x05670744-086876166705";
		// String cloumn = "ItemInfo:SellerId";

		String TABLE_NAME = "UIH_OverallItemInfo";
		String rowKey = "1000|I200|A17P|AUDI15028071";
		String cloumn = "CommonInfo:SellerID";

		List<TRowResult> results = null;
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		columns.add(ByteBufferHelper.toByteBuffer(cloumn));
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		TScan scan = new TScan();
		scan.setCaching(200);
		scan.setStartRow(ByteBufferHelper.toByteBuffer(rowKey));
		// scan.setStopRow(ByteBufferHelper.toByteBuffer(rowKey));
		scan.setColumns(columns);
		//
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			scannerId = client.scannerOpenWithScan(
					ByteBufferHelper.toByteBuffer(TABLE_NAME), scan, attributes);// 可以attributes=null
			results = client.scannerGetList(scannerId, 10);// 讀取幾筆
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scannerId >= 0) {
				client.scannerClose(scannerId);// 一定要關閉
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		printlnResult(results);
	}

	@Test
	public void scanNotClose() throws Exception {
		String TABLE_NAME = "ItemCreation_Buffer_Item_B";
		String cloumn = "ItemInfo:ProcessResult";
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		columns.add(ByteBufferHelper.toByteBuffer(cloumn));
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:ProcessStatus"));
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		TScan scan = new TScan();
		scan.setCaching(200);
		//
		String filterString = "SingleColumnValueFilter('ItemInfo','ProcessResult',=,'substring:Test')";// substring:A17P
		filterString += " AND SingleColumnValueFilter('ItemInfo','ProcessStatus',=,'substring:Test')";
		scan.setFilterString(ByteBufferHelper.toByteBuffer(filterString));
		scan.setColumns(columns);
		//
		int scannerId = 0;
		int rowCount = 0;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			try {
				scannerId = client.scannerOpenWithScan(
						ByteBufferHelper.toByteBuffer(TABLE_NAME), scan,
						attributes);// 可以attributes=null
				// System.out.println(scannerId);
				while (true) {
					List<TRowResult> results = client.scannerGet(scannerId);
					if (results.isEmpty()) {
						break;
					}
					System.out.println(i + ", " + scannerId);
					// printlnResult(results);
					rowCount++;

					if (rowCount > 0) {
						break;
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (scannerId >= 0) {
					// 故意不關
					// client.scannerClose(scannerId);// 一定要關閉
				}
			}
		}
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
	}

	@Test
	public void singleColumnValueFilter() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";
		String cloumn = "CommonInfo:SellerID";

		List<TRowResult> results = null;
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		columns.add(ByteBufferHelper.toByteBuffer(cloumn));
		columns.add(ByteBufferHelper.toByteBuffer("CommonInfo:SellerItemNumber"));
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		TScan scan = new TScan();
		scan.setCaching(200);
		// SingleColumnValueFilter('<family>', '<qualifier>', <compare
		// operator>, '<comparator>', <filterIfColumnMissing_boolean>,
		// <latest_version_boolean>)

		// 注意: 只有當COLUMNS中包含SingleColumnValueFilter提到的欄位時,
		// 該SingleColumnValueFilter才有效的

		String filterString = "SingleColumnValueFilter('CommonInfo','SellerID',=,'binary:A17P')";// substring:A17P
		filterString += " AND SingleColumnValueFilter('CommonInfo','SellerItemNumber',=,'binary:AUDI15028005')";
		scan.setFilterString(ByteBufferHelper.toByteBuffer(filterString));
		scan.setColumns(columns);
		//
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			scannerId = client.scannerOpenWithScan(
					ByteBufferHelper.toByteBuffer(TABLE_NAME), scan, attributes);// 可以attributes=null
			results = client.scannerGetList(scannerId, 10);// 讀取幾筆
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scannerId >= 0) {
				client.scannerClose(scannerId);// 一定要關閉
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		printlnResult(results);
	}

	@Test
	public void rowFilter() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";

		List<TRowResult> results = null;
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		TScan scan = new TScan();
		scan.setCaching(200);
		//
		String filterString = "RowFilter(=, 'regexstring:00[1-3]00')";
		scan.setFilterString(ByteBufferHelper.toByteBuffer(filterString));
		scan.setColumns(columns);
		//
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			scannerId = client.scannerOpenWithScan(
					ByteBufferHelper.toByteBuffer(TABLE_NAME), scan, attributes);// 可以attributes=null
			results = client.scannerGetList(scannerId, 10);// 讀取幾筆
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scannerId >= 0) {
				client.scannerClose(scannerId);// 一定要關閉
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		printlnResult(results);
	}

	@Test
	public void prefixFilter() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";

		List<TRowResult> results = null;
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		TScan scan = new TScan();
		scan.setCaching(200);
		//
		String filterString = "PrefixFilter('1000|B101|A17P')";
		scan.setFilterString(ByteBufferHelper.toByteBuffer(filterString));
		scan.setColumns(columns);
		//
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			scannerId = client.scannerOpenWithScan(
					ByteBufferHelper.toByteBuffer(TABLE_NAME), scan, attributes);// 可以attributes=null
			results = client.scannerGetList(scannerId, 10);// 讀取幾筆
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scannerId >= 0) {
				client.scannerClose(scannerId);// 一定要關閉
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		printlnResult(results);
	}

	@Test
	public void pageFilter() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		final byte[] POSTFIX = new byte[] { 0x00 };
		byte[] lastRow = null;
		int rowCount = 0;
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			while (true) {
				//
				TScan scan = new TScan();
				scan.setCaching(200);
				//
				String filterString = "PageFilter(20)";
				scan.setFilterString(ByteBufferHelper.toByteBuffer(filterString));
				scan.setColumns(columns);
				if (lastRow != null) {
					// 注意這裡添加了POSTFIX操作，不然就死循環了
					ByteBuffer startRow = ByteBufferHelper.toByteBuffer(add(
							lastRow, POSTFIX));
					scan.setStartRow(startRow);
				}
				//
				int localRows = 0;
				try {
					scannerId = client.scannerOpenWithScan(
							ByteBufferHelper.toByteBuffer(TABLE_NAME), scan,
							attributes);// 可以attributes=null
					//
					while (true) {
						List<TRowResult> results = client.scannerGet(scannerId);
						if (results.isEmpty()) {
							break;
						}
						//
						localRows++;
						rowCount++;
						TRowResult result = results.get(0);
						lastRow = result.getRow();
						// printlnResult(results);

						// 太多筆會很慢,先算個50就好
						if (rowCount > 49) {
							localRows = 0;
							break;
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (scannerId >= 0) {
						client.scannerClose(scannerId);// 一定要關閉
					}
				}
				//
				if (localRows == 0) {
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	/**
	 * 只傳回key值,value屏蔽掉
	 *
	 * @throws Exception
	 */
	@Test
	public void keyOnlyFilter() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";
		List<TRowResult> results = null;
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		TScan scan = new TScan();
		scan.setCaching(200);
		// KeyOnlyFilter()

		String filterString = "KeyOnlyFilter()";
		scan.setFilterString(ByteBufferHelper.toByteBuffer(filterString));
		scan.setColumns(columns);
		//
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			scannerId = client.scannerOpenWithScan(
					ByteBufferHelper.toByteBuffer(TABLE_NAME), scan, attributes);// 可以attributes=null
			results = client.scannerGetList(scannerId, 10);// 讀取幾筆
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scannerId >= 0) {
				client.scannerClose(scannerId);// 一定要關閉
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		//
		printlnResult(results);
	}

	/**
	 * 只傳回第一個keyValue
	 *
	 * @throws Exception
	 */
	@Test
	public void firstKeyOnlyFilter() throws Exception {
		String TABLE_NAME = "UIH_OverallItemInfo";
		List<TRowResult> results = null;
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		TScan scan = new TScan();
		scan.setCaching(200);
		// FirstKeyOnlyFilter()

		String filterString = "FirstKeyOnlyFilter()";
		scan.setFilterString(ByteBufferHelper.toByteBuffer(filterString));
		scan.setColumns(columns);
		//
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			scannerId = client.scannerOpenWithScan(
					ByteBufferHelper.toByteBuffer(TABLE_NAME), scan, attributes);// 可以attributes=null
			results = client.scannerGetList(scannerId, 10);// 讀取幾筆
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scannerId >= 0) {
				client.scannerClose(scannerId);// 一定要關閉
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		//
		printlnResult(results);
	}

	@Test
	// #issue
	// 1.scan and update, 取所有欄位, 50筆timeout
	//
	// 2.scan and update, 只取第一個key,無value,撐比較久, 200筆timeout
	//
	// 3.scan 無 update, 只取第一個key,無value, 1400筆timeout
	//
	// #fix
	// scan and update, 改用 PageFilter每次取20筆, 取所有欄位, 無timeout
	public void mockUpdate() throws Exception {
		String TABLE_NAME = "ItemCreation_Buffer_Item_B";
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		// //
		TScan scan = new TScan();
		scan.setCaching(200);
		//
		String filterString = "FirstKeyOnlyFilter()";
		filterString += " AND KeyOnlyFilter()";
		scan.setFilterString(ByteBufferHelper.toByteBuffer(filterString));
		scan.setColumns(columns);
		//
		int rowCount = 0;
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			scannerId = client.scannerOpenWithScan(
					ByteBufferHelper.toByteBuffer(TABLE_NAME), scan, attributes);// 可以attributes=null
			// System.out.println(scannerId);
			while (true) {
				List<TRowResult> results = client.scannerGet(scannerId);
				if (results.isEmpty()) {
					break;
				}
				// printlnResult(results);
				rowCount++;
				//
				TRowResult result = results.get(0);
				//
				// 重複update次數
				for (int i = 0; i < 1; i++) {
					List<Mutation> mutations = new LinkedList<Mutation>();
					Mutation mutation = new Mutation();
					mutation.setColumn(ByteBufferHelper
							.toByteBuffer("ItemInfo:ProcessResult"));
					mutation.setValue(ByteBufferHelper.toByteBuffer("Error:Test"
							+ i));
					mutations.add(mutation);
					//
					mutation = new Mutation();
					mutation.setColumn(ByteBufferHelper
							.toByteBuffer("ItemInfo:ProcessStatus"));
					mutation.setValue(ByteBufferHelper.toByteBuffer("Test" + i));
					mutations.add(mutation);
					//
					mutation = new Mutation();
					mutation.setColumn(ByteBufferHelper
							.toByteBuffer("ItemInfo:SellerItemNumber"));
					mutation.setValue(ByteBufferHelper.toByteBuffer("Test" + i));
					mutations.add(mutation);
					//
					client.mutateRow(ByteBufferHelper.toByteBuffer(TABLE_NAME),
							ByteBufferHelper.toByteBuffer(result.getRow()),
							mutations, attributes);
				}
				//
				System.out.println(rowCount + " update: "
						+ ByteHelper.toString(result.getRow()));

				// 太多筆會很慢,先算個50就好
				if (rowCount > 299) {
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scannerId >= 0) {
				client.scannerClose(scannerId);// 一定要關閉
			}
		}
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	// #fix
	// scan and update, 改用 PageFilter每次取20筆, 取所有欄位, 無timeout
	public void mockUpdateFix() throws Exception {
		String TABLE_NAME = "ItemCreation_Buffer_Item_B";
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		final byte[] POSTFIX = new byte[] { 0x00 };
		byte[] lastRow = null;
		int rowCount = 0;
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			while (true) {
				//
				TScan scan = new TScan();
				scan.setCaching(200);
				//
				String filterString = "PageFilter(20)";
				scan.setFilterString(ByteBufferHelper.toByteBuffer(filterString));
				scan.setColumns(columns);
				if (lastRow != null) {
					// 注意這裡添加了POSTFIX操作，不然就死循環了
					ByteBuffer startRow = ByteBufferHelper.toByteBuffer(add(
							lastRow, POSTFIX));
					scan.setStartRow(startRow);
				}
				//
				int localRows = 0;
				try {
					scannerId = client.scannerOpenWithScan(
							ByteBufferHelper.toByteBuffer(TABLE_NAME), scan,
							attributes);// 可以attributes=null
					//
					while (true) {
						List<TRowResult> results = client.scannerGet(scannerId);
						if (results.isEmpty()) {
							break;
						}
						//
						localRows++;
						rowCount++;
						TRowResult result = results.get(0);
						lastRow = result.getRow();
						// printlnResult(results);

						// 重複update次數
						for (int i = 0; i < 5; i++) {
							List<Mutation> mutations = new LinkedList<Mutation>();
							Mutation mutation = new Mutation();
							mutation.setColumn(ByteBufferHelper
									.toByteBuffer("ItemInfo:ProcessResult"));
							mutation.setValue(ByteBufferHelper
									.toByteBuffer("Error:Test" + i));
							mutations.add(mutation);
							//
							mutation = new Mutation();
							mutation.setColumn(ByteBufferHelper
									.toByteBuffer("ItemInfo:ProcessStatus"));
							mutation.setValue(ByteBufferHelper
									.toByteBuffer("Test" + i));
							mutations.add(mutation);
							//
							mutation = new Mutation();
							mutation.setColumn(ByteBufferHelper
									.toByteBuffer("ItemInfo:SellerItemNumber"));
							mutation.setValue(ByteBufferHelper
									.toByteBuffer("Test" + i));
							mutations.add(mutation);
							//
							client.mutateRow(ByteBufferHelper
									.toByteBuffer(TABLE_NAME), ByteBufferHelper
									.toByteBuffer(result.getRow()), mutations,
									attributes);
						}
						//
						System.out.println(scannerId + " " + rowCount
								+ " update: "
								+ ByteHelper.toString(result.getRow()));

						// 太多筆會很慢,先算個50就好
						if (rowCount > 299) {
							localRows = 0;
							break;
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (scannerId >= 0) {
						client.scannerClose(scannerId);// 一定要關閉
					}
				}
				//
				if (localRows == 0) {
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println((end - beg) + " at mills.");
		System.out.println("rowCount: " + rowCount);
	}

	@Test
	public void mockFilterUpdate() throws Exception {
		TTransport transport = new TFramedTransport(new TSocket("172.16.18.25",
				9094));
		// TProtocol protocol = new TCompactProtocol(transport);
		// TProtocol protocol = new TBinaryProtocol(transport);

		// TTransport transport = new TSocket("172.16.18.25", 9095);
		// TProtocol protocol = new TBinaryProtocol(transport);

		Hbase.Client client = new Hbase.Client(tprotocol);
		transport.open();
		//
		String TABLE_NAME = "ItemCreation_Buffer_Item_B";
		List<TRowResult> results = null;
		//
		List<ByteBuffer> columns = new LinkedList<ByteBuffer>();
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:ProcessResult"));
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:ProcessStatus"));
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:SellerItemNumber"));
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:CustomerID"));
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:SellerId"));
		//
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:IsProcessed"));
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:FileVersion"));
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:IsSandBoxSeller"));
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:IsTestSeller"));
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:RunJobs"));
		columns.add(ByteBufferHelper.toByteBuffer("ItemInfo:CountryCode"));

		Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
		//
		TScan scan = new TScan();
		scan.setCaching(200);

		String filterString = "SingleColumnValueFilter('ItemInfo','ProcessResult',=,'substring:Error')";// substring:A17P
		filterString += " AND SingleColumnValueFilter('ItemInfo','ProcessStatus',=,'substring:Test')";
		filterString += " AND SingleColumnValueFilter('ItemInfo','SellerItemNumber',=,'substring:Test')";
		filterString += " AND SingleColumnValueFilter('ItemInfo','CustomerID',=,'substring:3600')";
		filterString += " AND SingleColumnValueFilter('ItemInfo','SellerId',=,'substring:A2')";
		//
		filterString += " OR SingleColumnValueFilter('ItemInfo','IsProcessed',=,'binary:0')";
		filterString += " OR SingleColumnValueFilter('ItemInfo','FileVersion',=,'binary:Update Item')";
		filterString += " OR SingleColumnValueFilter('ItemInfo','IsSandBoxSeller',=,'binary:0')";
		filterString += " OR SingleColumnValueFilter('ItemInfo','IsTestSeller',=,'binary:1')";
		filterString += " OR SingleColumnValueFilter('ItemInfo','RunJobs',=,'binary:0')";
		filterString += " OR SingleColumnValueFilter('ItemInfo','CountryCode',=,'binary:USA')";
		//
		scan.setFilterString(ByteBufferHelper.toByteBuffer(filterString));
		scan.setColumns(columns);
		//
		int scannerId = 0;
		long beg = System.currentTimeMillis();
		try {
			scannerId = client.scannerOpenWithScan(
					ByteBufferHelper.toByteBuffer(TABLE_NAME), scan, attributes);// 可以attributes=null
			results = client.scannerGetList(scannerId, 1);// 讀取幾筆
			if (results.isEmpty()) {
				return;
			}
			//
			TRowResult result = results.get(0);

			// 重複update次數
			for (int i = 0; i < 5; i++) {
				List<Mutation> mutations = new LinkedList<Mutation>();
				Mutation mutation = new Mutation();
				mutation.setColumn(ByteBufferHelper
						.toByteBuffer("ItemInfo:ProcessResult"));
				mutation.setValue(ByteBufferHelper.toByteBuffer("Error:Test" + i));
				mutations.add(mutation);
				//
				mutation = new Mutation();
				mutation.setColumn(ByteBufferHelper
						.toByteBuffer("ItemInfo:ProcessStatus"));
				mutation.setValue(ByteBufferHelper.toByteBuffer("Test" + i));
				mutations.add(mutation);
				//
				mutation = new Mutation();
				mutation.setColumn(ByteBufferHelper
						.toByteBuffer("ItemInfo:SellerItemNumber"));
				mutation.setValue(ByteBufferHelper.toByteBuffer("Test" + i));
				mutations.add(mutation);
				//
				client.mutateRow(ByteBufferHelper.toByteBuffer(TABLE_NAME),
						ByteBufferHelper.toByteBuffer(result.getRow()),
						mutations, attributes);
				//
				Thread.sleep(NumberHelper.randomInt(500));
			}
			//
			// System.out.println(scannerId + " update: "
			// + ByteHelper.toString(result.getRow()));

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (scannerId >= 0) {
				client.scannerClose(scannerId);// 一定要關閉
			}
		}
		long end = System.currentTimeMillis();
		// System.out.println((end - beg) + " at mills.");
		System.out.print("[" + Thread.currentThread().getName() + "] ");
		printlnResult(results);
		//
		Thread.sleep(1 * 60 * 60 * 1000);
		transport.close();
	}

	@Test
	public void mockFilterUpdateWithMultiThread() throws Exception {
		// blocking: 450-480
		// noblocking: 590-600
		for (int i = 0; i < 600; i++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						mockFilterUpdate();
					} catch (Exception ex) {
					}
				}
			});
			thread.setName("T-" + i);
			thread.start();
			Thread.sleep(NumberHelper.randomInt(100));
		}
		//
		Thread.sleep(1 * 60 * 60 * 1000);
	}

}
