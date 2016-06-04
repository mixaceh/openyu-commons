package org.openyu.commons.bao.hbase.supporter;

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
import org.openyu.commons.bao.hbase.HzBao;
import org.openyu.commons.bao.hbase.HzTemplate;
import org.openyu.commons.bao.hbase.ex.HzBaoException;
import org.openyu.commons.bao.hbase.impl.HzTemplateImpl;
import org.openyu.commons.bao.supporter.BaseBaoSupporter;
import org.openyu.commons.hbase.HzSession;
import org.openyu.commons.hbase.HzSessionFactory;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HzBaoSupporter extends BaseBaoSupporter implements HzBao {

	private static final long serialVersionUID = 1099108265636854567L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(HzBaoSupporter.class);

	private HzTemplate hzTemplate;

	public HzBaoSupporter() {
	}

	/**
	 * 檢查設置
	 * 
	 * @throws Exception
	 */
	protected final void checkConfig() throws Exception {
		AssertHelper.notNull(hzTemplate, "The HzTemplate is required");
		AssertHelper.notNull(this.hzTemplate.getHzSessionFactory(), "The HzSessionFactory is required");
	}

	public final HzSessionFactory getHzSessionFactory() {
		return ((this.hzTemplate != null ? this.hzTemplate.getHzSessionFactory() : null));
	}

	public final void setHzSessionFactory(HzSessionFactory hzSessionFactory) {
		if ((this.hzTemplate == null) || (hzSessionFactory != this.hzTemplate.getHzSessionFactory()))
			this.hzTemplate = createHzTemplate(hzSessionFactory);
	}

	protected HzTemplate createHzTemplate(HzSessionFactory hzSessionFactory) {
		return new HzTemplateImpl(hzSessionFactory);
	}

	public final HzTemplate getHzTemplate() {
		return hzTemplate;
	}

	public final void setHzTemplate(HzTemplate hzTemplate) {
		this.hzTemplate = hzTemplate;
	}

	protected final HzSession getSession() {
		return hzTemplate.getSession();
	}

	protected final void closeSession() {
		hzTemplate.closeSession();
	}

	// --------------------------------------------------

	public boolean tableExists(String tableName) {
		try {
			return hzTemplate.tableExists(tableName);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public boolean tableExists(byte[] tableName) {
		try {
			return hzTemplate.tableExists(tableName);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public HTableDescriptor[] listTables() {
		try {
			return hzTemplate.listTables();
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	protected final HTableInterface getTable(String tableName) {
		try {
			return hzTemplate.getTable(tableName);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	protected final void closeTable(String tableName) {
		try {
			hzTemplate.closeTable(tableName);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	protected final void closeTable(HTableInterface table) {
		try {
			hzTemplate.closeTable(table);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public boolean exists(String tableName, Get paramGet) {
		try {
			return hzTemplate.exists(tableName, paramGet);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public void batch(String tableName, List<? extends Row> paramList, Object[] paramArrayOfObject) {
		try {
			hzTemplate.batch(tableName, paramList, paramArrayOfObject);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public Object[] batch(String tableName, List<? extends Row> paramList) {
		try {
			return hzTemplate.batch(tableName, paramList);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public Result get(String tableName, Get paramGet) {
		try {
			return hzTemplate.get(tableName, paramGet);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public Result[] get(String tableName, List<Get> paramList) {
		try {
			return hzTemplate.get(tableName, paramList);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public ResultScanner getScanner(String tableName, Scan paramScan) {
		try {
			return hzTemplate.getScanner(tableName, paramScan);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public ResultScanner getScanner(String tableName, byte[] paramArrayOfByte) {
		try {
			return hzTemplate.getScanner(tableName, paramArrayOfByte);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public ResultScanner getScanner(String tableName, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
		try {
			return hzTemplate.getScanner(tableName, paramArrayOfByte1, paramArrayOfByte2);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public void put(String tableName, Put paramPut) {
		try {
			hzTemplate.put(tableName, paramPut);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public void put(String tableName, List<Put> paramList) {
		try {
			hzTemplate.put(tableName, paramList);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public boolean checkAndPut(String tableName, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2,
			byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, Put paramPut) {
		try {
			return hzTemplate.checkAndPut(tableName, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3,
					paramArrayOfByte4, paramPut);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public void delete(String tableName, Delete paramDelete) {
		try {
			hzTemplate.delete(tableName, paramDelete);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public void delete(String tableName, List<Delete> paramList) {
		try {
			hzTemplate.delete(tableName, paramList);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public boolean checkAndDelete(String tableName, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2,
			byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, Delete paramDelete) {
		try {
			return hzTemplate.checkAndDelete(tableName, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3,
					paramArrayOfByte4, paramDelete);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public void mutateRow(String tableName, RowMutations paramRowMutations) {
		try {
			hzTemplate.mutateRow(tableName, paramRowMutations);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public <T extends CoprocessorProtocol> T coprocessorProxy(String tableName, Class<T> paramClass,
			byte[] paramArrayOfByte) {
		try {
			return hzTemplate.coprocessorProxy(tableName, paramClass, paramArrayOfByte);
		} catch (Exception ex) {
			throw new HzBaoException(ex);
		}
	}

	public <T extends CoprocessorProtocol, R> Map<byte[], R> coprocessorExec(String tableName, Class<T> paramClass,
			byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, Call<T, R> paramCall) {
		try {
			return hzTemplate.coprocessorExec(tableName, paramClass, paramArrayOfByte1, paramArrayOfByte2, paramCall);
		} catch (Throwable ex) {
			throw new HzBaoException(ex);
		}
	}

	public <T extends CoprocessorProtocol, R> void coprocessorExec(String tableName, Class<T> paramClass,
			byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, Call<T, R> paramCall, Callback<R> paramCallback) {
		try {
			hzTemplate.coprocessorExec(tableName, paramClass, paramArrayOfByte1, paramArrayOfByte2, paramCall,
					paramCallback);
		} catch (Throwable ex) {
			throw new HzBaoException(ex);
		}
	}

}
