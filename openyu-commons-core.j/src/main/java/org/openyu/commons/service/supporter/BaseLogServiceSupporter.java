package org.openyu.commons.service.supporter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.openyu.commons.service.AsyncCommonService;
import org.openyu.commons.service.BaseLogService;

/**
 * 日誌服務
 */
public class BaseLogServiceSupporter extends BaseServiceSupporter implements BaseLogService {

	private static final long serialVersionUID = -730966546995347276L;

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseLogServiceSupporter.class);

	/** 佇列服務. */
	@Autowired
	@Qualifier("logAsyncCommonService")
	protected transient AsyncCommonService asyncCommonService;

	/**
	 * Instantiates a new base log service supporter.
	 */
	public BaseLogServiceSupporter() {
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

	public <T> boolean offerInsert(T entity) {
		return asyncCommonService.offerInsert(entity);
	}

	public <T> boolean offerUpdate(T entity) {
		return asyncCommonService.offerUpdate(entity);
	}

	public <T> boolean offerDelete(T entity) {
		return asyncCommonService.offerDelete(entity);
	}

	public boolean offerDelete(Class<?> entityClass, Serializable seq) {
		return asyncCommonService.offerDelete(entityClass, seq);
	}

	public List<Boolean> offerDelete(Class<?> entityClass, Collection<Serializable> seqs) {
		return asyncCommonService.offerDelete(entityClass, seqs);
	}

}
