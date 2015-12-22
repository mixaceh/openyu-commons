package org.openyu.commons.service.supporter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.openyu.commons.dao.anno.LogTx;
import org.openyu.commons.service.AsyncService;
import org.openyu.commons.service.LogService;

/**
 * 日誌服務
 */
public class LogServiceSupporter extends BaseServiceSupporter implements LogService {

	private static final long serialVersionUID = -730966546995347276L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(LogServiceSupporter.class);

	/**
	 * 異步服務
	 */
	@Autowired
	@Qualifier("logAsyncService")
	protected transient AsyncService asyncService;

	public LogServiceSupporter() {
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {

	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}

	@LogTx
	public <T> boolean offerInsert(T entity) {
		return asyncService.offerInsert(entity);
	}

	@LogTx
	public <T> boolean offerUpdate(T entity) {
		return asyncService.offerUpdate(entity);
	}

	@LogTx
	public <T> boolean offerDelete(T entity) {
		return asyncService.offerDelete(entity);
	}

	@LogTx
	public boolean offerDelete(Class<?> entityClass, Serializable seq) {
		return asyncService.offerDelete(entityClass, seq);
	}

	@LogTx
	public List<Boolean> offerDelete(Class<?> entityClass, Collection<Serializable> seqs) {
		return asyncService.offerDelete(entityClass, seqs);
	}

}
