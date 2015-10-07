package org.openyu.commons.bao.hbase.impl;

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
import org.apache.hadoop.hbase.client.coprocessor.Batch.Call;
import org.apache.hadoop.hbase.client.coprocessor.Batch.Callback;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;
import org.openyu.commons.bao.hbase.HzCallback;
import org.openyu.commons.bao.hbase.HzTableCallback;
import org.openyu.commons.bao.hbase.HzTemplate;
import org.openyu.commons.bao.hbase.ex.HzTemplateException;
import org.openyu.commons.hbase.HzSession;
import org.openyu.commons.hbase.HzSessionFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HzTemplateImpl extends BaseServiceSupporter implements HzTemplate {

	private static final long serialVersionUID = 5267122206605316626L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(HzTemplateImpl.class);

	private HzSessionFactory hzSessionFactory;

	public HzTemplateImpl(HzSessionFactory hzSessionFactory) {
		this.hzSessionFactory = hzSessionFactory;
	}

	public HzTemplateImpl() {
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		AssertHelper.notNull(hzSessionFactory, "The HzSessionFactory is required");
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}

	public HzSessionFactory getHzSessionFactory() {
		return hzSessionFactory;
	}

	public void setHzSessionFactory(HzSessionFactory hzSessionFactory) {
		this.hzSessionFactory = hzSessionFactory;
	}

	public HzSession getSession() {
		try {
			return hzSessionFactory.openSession();
		} catch (Exception ex) {
			throw new HzTemplateException("Could not open HzSession", ex);
		}
	}

	public void closeSession() {
		try {
			hzSessionFactory.closeSession();
		} catch (Exception ex) {
			throw new HzTemplateException("Could not close HzSession", ex);
		}
	}

	public <T> T execute(HzCallback<T> action) throws HzTemplateException {
		return doExecute(action);
	}

	protected <T> T doExecute(HzCallback<T> action) throws HzTemplateException {
		AssertHelper.notNull(action, "The HzCallback must not be null");
		//
		T result = null;
		HzSession session = null;
		try {
			session = getSession();
			result = action.doInAction(session);
			return result;
		} catch (Exception ex) {
			throw new HzTemplateException(ex);
		} finally {
			if (session != null) {
				closeSession();
			}
		}
	}

	// --------------------------------------------------

	public boolean tableExists(final String tableName) throws IOException {
		return execute(new HzCallback<Boolean>() {
			public Boolean doInAction(HzSession session)
					throws HzTemplateException {
				try {
					return session.tableExists(tableName);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public boolean tableExists(final byte[] tableName) throws IOException {
		return execute(new HzCallback<Boolean>() {
			public Boolean doInAction(HzSession session)
					throws HzTemplateException {
				try {
					return session.tableExists(tableName);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public HTableDescriptor[] listTables() throws IOException {
		return execute(new HzCallback<HTableDescriptor[]>() {
			public HTableDescriptor[] doInAction(HzSession session)
					throws HzTemplateException {
				try {
					return session.listTables();
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	// --------------------------------------------------

	public HTableInterface getTable(String tableName) {
		try {
			return getSession().getTable(tableName);
		} catch (Exception ex) {
			throw new HzTemplateException("Could not open HTableInterface", ex);
		}
	}

	public void closeTable(String tableName) {
		try {
			getSession().closeTable(tableName);
		} catch (Exception ex) {
			throw new HzTemplateException("Could not close HTableInterface", ex);
		}
	}

	public void closeTable(HTableInterface table) {
		try {
			getSession().closeTable(table);
		} catch (Exception ex) {
			throw new HzTemplateException("Could not close HTableInterface", ex);
		}
	}

	public <T> T execute(String tableName, HzTableCallback<T> action)
			throws HzTemplateException {
		return doExecute(tableName, action);
	}

	protected <T> T doExecute(String tableName, HzTableCallback<T> action)
			throws HzTemplateException {
		AssertHelper.notNull(action, "The HzTableCallback must not be null");
		//
		T result = null;
		HzSession session = null;
		HTableInterface table = null;
		try {
			session = getSession();
			table = session.getTable(tableName);
			result = action.doInAction(table);
			return result;
		} catch (Exception ex) {
			throw new HzTemplateException(ex);
		} finally {
			if (table != null) {
				closeTable(table);
			}
			if (session != null) {
				closeSession();
			}
		}
	}

	// --------------------------------------------------

	public boolean exists(String tableName, final Get paramGet)
			throws IOException {
		return execute(tableName, new HzTableCallback<Boolean>() {
			public Boolean doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.exists(paramGet);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public void batch(String tableName, final List<? extends Row> paramList,
			final Object[] paramArrayOfObject) throws IOException,
			InterruptedException {
		execute(tableName, new HzTableCallback<Object>() {
			public Object doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					table.batch(paramList, paramArrayOfObject);
					return null;
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public Object[] batch(String tableName, final List<? extends Row> paramList)
			throws IOException, InterruptedException {
		return execute(tableName, new HzTableCallback<Object[]>() {
			public Object[] doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.batch(paramList);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public Result get(String tableName, final Get paramGet) throws IOException {
		return execute(tableName, new HzTableCallback<Result>() {
			public Result doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.get(paramGet);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public Result[] get(String tableName, final List<Get> paramList)
			throws IOException {
		return execute(tableName, new HzTableCallback<Result[]>() {
			public Result[] doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.get(paramList);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public ResultScanner getScanner(String tableName, final Scan paramScan)
			throws IOException {
		return execute(tableName, new HzTableCallback<ResultScanner>() {
			public ResultScanner doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.getScanner(paramScan);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public ResultScanner getScanner(String tableName,
			final byte[] paramArrayOfByte) throws IOException {
		return execute(tableName, new HzTableCallback<ResultScanner>() {
			public ResultScanner doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.getScanner(paramArrayOfByte);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public ResultScanner getScanner(String tableName,
			final byte[] paramArrayOfByte1, final byte[] paramArrayOfByte2)
			throws IOException {
		return execute(tableName, new HzTableCallback<ResultScanner>() {
			public ResultScanner doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.getScanner(paramArrayOfByte1,
							paramArrayOfByte2);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public void put(String tableName, final Put paramPut) throws IOException {
		execute(tableName, new HzTableCallback<Object>() {
			public Object doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					table.put(paramPut);
					return null;
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public void put(String tableName, final List<Put> paramList)
			throws IOException {
		execute(tableName, new HzTableCallback<Object>() {
			public Object doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					table.put(paramList);
					return null;
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public boolean checkAndPut(String tableName,
			final byte[] paramArrayOfByte1, final byte[] paramArrayOfByte2,
			final byte[] paramArrayOfByte3, final byte[] paramArrayOfByte4,
			final Put paramPut) throws IOException {
		return execute(tableName, new HzTableCallback<Boolean>() {
			public Boolean doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.checkAndPut(paramArrayOfByte1,
							paramArrayOfByte2, paramArrayOfByte3,
							paramArrayOfByte4, paramPut);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public void delete(String tableName, final Delete paramDelete)
			throws IOException {
		execute(tableName, new HzTableCallback<Object>() {
			public Object doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					table.delete(paramDelete);
					return null;
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public void delete(String tableName, final List<Delete> paramList)
			throws IOException {
		execute(tableName, new HzTableCallback<Object>() {
			public Object doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					table.delete(paramList);
					return null;
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public boolean checkAndDelete(String tableName,
			final byte[] paramArrayOfByte1, final byte[] paramArrayOfByte2,
			final byte[] paramArrayOfByte3, final byte[] paramArrayOfByte4,
			final Delete paramDelete) throws IOException {
		return execute(tableName, new HzTableCallback<Boolean>() {
			public Boolean doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.checkAndDelete(paramArrayOfByte1,
							paramArrayOfByte2, paramArrayOfByte3,
							paramArrayOfByte4, paramDelete);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public void mutateRow(String tableName, final RowMutations paramRowMutations)
			throws IOException {
		execute(tableName, new HzTableCallback<Object>() {
			public Object doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					table.mutateRow(paramRowMutations);
					return null;
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public <T extends CoprocessorProtocol> T coprocessorProxy(String tableName,
			final Class<T> paramClass, final byte[] paramArrayOfByte) {
		return execute(tableName, new HzTableCallback<T>() {
			public T doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.coprocessorProxy(paramClass, paramArrayOfByte);
				} catch (Exception ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	@Override
	public <T extends CoprocessorProtocol, R> Map<byte[], R> coprocessorExec(
			String tableName, final Class<T> paramClass,
			final byte[] paramArrayOfByte1, final byte[] paramArrayOfByte2,
			final Call<T, R> paramCall) throws IOException, Throwable {
		return execute(tableName, new HzTableCallback<Map<byte[], R>>() {
			public Map<byte[], R> doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					return table.coprocessorExec(paramClass, paramArrayOfByte1,
							paramArrayOfByte2, paramCall);
				} catch (Throwable ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

	public <T extends CoprocessorProtocol, R> void coprocessorExec(
			String tableName, final Class<T> paramClass,
			final byte[] paramArrayOfByte1, final byte[] paramArrayOfByte2,
			final Call<T, R> paramCall, final Callback<R> paramCallback)
			throws IOException, Throwable {
		execute(tableName, new HzTableCallback<Object>() {
			public Object doInAction(HTableInterface table)
					throws HzTemplateException {
				try {
					table.coprocessorExec(paramClass, paramArrayOfByte1,
							paramArrayOfByte2, paramCall, paramCallback);
					return null;
				} catch (Throwable ex) {
					throw new HzTemplateException(ex);
				}
			}
		});
	}

}
