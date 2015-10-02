package org.openyu.commons.service.supporter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.openyu.commons.service.BaseLogService;
import org.openyu.commons.service.QueueService;

/**
 * 日誌服務
 */
public class BaseLogServiceSupporter extends BaseServiceSupporter implements
		BaseLogService {

	private static final long serialVersionUID = -730966546995347276L;

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(BaseLogServiceSupporter.class);

	/** 佇列服務. */
	@Autowired
	@Qualifier("logQueueService")
	protected transient QueueService queueService;

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
		return queueService.offerInsert(entity);
	}

	public <T> boolean offerUpdate(T entity) {
		return queueService.offerUpdate(entity);
	}

	public <T> boolean offerDelete(T entity) {
		return queueService.offerDelete(entity);
	}

	public boolean offerDelete(Class<?> entityClass, Serializable seq) {
		return queueService.offerDelete(entityClass, seq);
	}

	public List<Boolean> offerDelete(Class<?> entityClass,
			Collection<Serializable> seqs) {
		return queueService.offerDelete(entityClass, seqs);
	}

	// ------------------------------------

	public <T> Serializable insert(T entity) {
		return queueService.insert(entity);
	}

	public <T> int update(T entity) {
		return queueService.update(entity);
	}

	public <T> int delete(T entity) {
		return queueService.delete(entity);
	}

	public <T> T delete(Class<?> entityClass, Serializable seq) {
		return queueService.delete(entityClass, seq);
	}

	public <E> List<E> delete(Class<?> entityClass,
			Collection<Serializable> seqs) {
		return queueService.delete(entityClass, seqs);
	}

}
