package org.openyu.commons.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.openyu.commons.dao.OjDao;
import org.openyu.commons.dao.aware.OjDaoAware;
import org.openyu.commons.service.QueueService;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.supporter.LoopQueueSupporter;
import org.openyu.commons.util.CollectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 資料庫佇列服務
 */
public class QueueServiceImpl extends BaseServiceSupporter implements
		QueueService, OjDaoAware {

	private static final long serialVersionUID = -436838930128568344L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(QueueServiceImpl.class);

	private transient OjDao ojDao;

	/**
	 * 線程服務
	 */
	@Autowired
	@Qualifier("threadService")
	private transient ThreadService threadService;

	/**
	 * 新增佇列
	 */
	private transient InsertQueue<Object> insertQueue;

	/**
	 * 修改佇列
	 */
	private transient UpdateQueue<Object> updateQueue;

	/**
	 * 刪除佇列
	 */
	private transient DeleteQueue<Object> deleteQueue;

	/**
	 * 監聽毫秒
	 */
	private static final long LISTEN_MILLS = 2 * 1000L;

	public QueueServiceImpl() {
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		insertQueue = new InsertQueue<Object>(threadService);
		insertQueue.setListenMills(LISTEN_MILLS);
		// threadService.submit(insertQueue);
		insertQueue.start();
		//
		updateQueue = new UpdateQueue<Object>(threadService);
		updateQueue.setListenMills(LISTEN_MILLS);
		// threadService.submit(updateQueue);
		updateQueue.start();
		//
		deleteQueue = new DeleteQueue<Object>(threadService);
		deleteQueue.setListenMills(LISTEN_MILLS);
//		threadService.submit(deleteQueue);
		deleteQueue.start();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		insertQueue.shutdown();
		updateQueue.shutdown();
		deleteQueue.shutdown();
	}

	public OjDao getOjDao() {
		return ojDao;
	}

	public void setOjDao(OjDao ojDao) {
		this.ojDao = ojDao;
	}

	/**
	 * 加到佇列新增
	 * 
	 * @param entity
	 */
	public <T> boolean offerInsert(T entity) {
		boolean result = false;
		//
		try {
			result = insertQueue.offer(entity);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 加到佇列儲存
	 * 
	 * @param entity
	 */
	public <T> boolean offerUpdate(T entity) {
		boolean result = false;
		//
		try {
			result = updateQueue.offer(entity);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 加到佇列刪除
	 * 
	 * @param entity
	 */
	public <T> boolean offerDelete(T entity) {
		boolean result = false;
		//
		try {
			result = deleteQueue.offer(entity);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 加到佇列刪除,會先用find找entity
	 * 
	 * @param entityClass
	 * @param seq
	 * @return
	 */
	public boolean offerDelete(Class<?> entityClass, Serializable seq) {
		boolean result = false;
		//
		try {
			// 搜尋entity
			Object entity = ojDao.find(entityClass, seq);
			if (entity != null) {
				result = offerDelete(entity);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 多筆加到佇列刪除,會先用find找entity
	 * 
	 * @param entityClass
	 * @param seqs
	 * @return
	 */
	public List<Boolean> offerDelete(Class<?> entityClass,
			Collection<Serializable> seqs) {
		List<Boolean> result = new LinkedList<Boolean>();
		//
		if (CollectionHelper.notEmpty(seqs)) {
			for (Serializable seq : seqs) {
				if (seq == null) {
					continue;
				}
				//
				boolean offerDelete = offerDelete(entityClass, seq);
				result.add(offerDelete);
			}
		}
		return result;
	}

	// ------------------------------------

	public <T> Serializable insert(T entity) {
		return ojDao.insert(entity);
	}

	public <T> int update(T entity) {
		return ojDao.update(entity);
	}

	public <T> int delete(T entity) {
		return ojDao.delete(entity);
	}

	public <T> T delete(Class<?> entityClass, Serializable seq) {
		return ojDao.delete(entityClass, seq);
	}

	public <E> List<E> delete(Class<?> entityClass,
			Collection<Serializable> seqs) {
		return ojDao.delete(entityClass, seqs);
	}

	/**
	 * 新增佇列
	 */
	protected class InsertQueue<E> extends LoopQueueSupporter<E> {

		public InsertQueue(ThreadService threadService) {
			super(threadService);
		}

		protected void doExecute(E e) throws Exception {
			insert(e);
		}
	}

	/**
	 * 修改佇列
	 */
	protected class UpdateQueue<E> extends LoopQueueSupporter<E> {

		public UpdateQueue(ThreadService threadService) {
			super(threadService);
		}

		protected void doExecute(E e) throws Exception {
			update(e);
		}
	}

	/**
	 * 刪除佇列
	 */
	protected class DeleteQueue<E> extends LoopQueueSupporter<E> {

		public DeleteQueue(ThreadService threadService) {
			super(threadService);
		}

		protected void doExecute(E e) throws Exception {
			delete(e);
		}
	}

}
