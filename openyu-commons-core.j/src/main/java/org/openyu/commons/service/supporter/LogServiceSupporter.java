package org.openyu.commons.service.supporter;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.dao.CommonDao;
import org.openyu.commons.dao.anno.LogTx;
import org.openyu.commons.service.LogService;
import org.openyu.commons.service.ShutdownCallback;
import org.openyu.commons.service.StartCallback;
import org.openyu.commons.thread.RunnableQueueGroup;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.anno.DefaultThreadService;
import org.openyu.commons.thread.impl.RunnableQueueGroupImpl;
import org.openyu.commons.thread.supporter.LoopQueueSupporter;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.CollectionHelper;

/**
 * 日誌服務
 */
public abstract class LogServiceSupporter extends BaseServiceSupporter implements LogService {

	private static final long serialVersionUID = -730966546995347276L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(LogServiceSupporter.class);

	/**
	 * 線程服務
	 */
	@DefaultThreadService
	private transient ThreadService threadService;

	/**
	 * 通用Dao
	 */
	protected transient CommonDao commonDao;

	/**
	 * 最大佇列個數
	 */
	private static final int MAX_QUEUE_SIZE = 10;

	/**
	 * 新增佇列監聽毫秒
	 */
	private long insertQueueListenMills = 10L;

	/**
	 * 新增佇列是否開啟
	 */
	private boolean insertQueueEnabled;

	/**
	 * 新增佇列個數
	 */
	private int insertQueueSize;

	/**
	 * 新增佇列
	 */
	private transient RunnableQueueGroup<Object> insertQueueGroup;

	// ------------------------------------------------

	/**
	 * 修改佇列監聽毫秒
	 */
	private long updateQueueListenMills = 10L;

	/**
	 * 修改佇列是否開啟
	 */
	private boolean updateQueueEnabled;

	/**
	 * 修改佇列個數
	 */
	private int updateQueueSize;

	/**
	 * 修改佇列
	 */
	private transient RunnableQueueGroup<Object> updateQueueGroup;

	// ------------------------------------------------

	/**
	 * 刪除佇列監聽毫秒
	 */
	private long deleteQueueListenMills = 10L;

	/**
	 * 刪除佇列是否開啟
	 */
	private boolean deleteQueueEnabled;

	/**
	 * 刪除佇列個數
	 */
	private int deleteQueueSize;

	/**
	 * 刪除佇列
	 */
	private transient RunnableQueueGroup<Object> deleteQueueGroup;

	public LogServiceSupporter() {
		addServiceCallback("StartCallbacker", new StartCallbacker());
		addServiceCallback("ShutdownCallbacker", new ShutdownCallbacker());
	}

	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public long getInsertQueueListenMills() {
		return insertQueueListenMills;
	}

	public void setInsertQueueListenMills(long insertQueueListenMills) {
		this.insertQueueListenMills = insertQueueListenMills;
	}

	public boolean isInsertQueueEnabled() {
		return insertQueueEnabled;
	}

	public void setInsertQueueEnabled(boolean insertQueueEnabled) {
		this.insertQueueEnabled = insertQueueEnabled;
	}

	public int getInsertQueueSize() {
		return insertQueueSize;
	}

	public void setInsertQueueSize(int insertQueueSize) {
		this.insertQueueSize = insertQueueSize;
	}

	public long getUpdateQueueListenMills() {
		return updateQueueListenMills;
	}

	public void setUpdateQueueListenMills(long updateQueueListenMills) {
		this.updateQueueListenMills = updateQueueListenMills;
	}

	public boolean isUpdateQueueEnabled() {
		return updateQueueEnabled;
	}

	public void setUpdateQueueEnabled(boolean updateQueueEnabled) {
		this.updateQueueEnabled = updateQueueEnabled;
	}

	public int getUpdateQueueSize() {
		return updateQueueSize;
	}

	public void setUpdateQueueSize(int updateQueueSize) {
		this.updateQueueSize = updateQueueSize;
	}

	public long getDeleteQueueListenMills() {
		return deleteQueueListenMills;
	}

	public void setDeleteQueueListenMills(long deleteQueueListenMills) {
		this.deleteQueueListenMills = deleteQueueListenMills;
	}

	public boolean isDeleteQueueEnabled() {
		return deleteQueueEnabled;
	}

	public void setDeleteQueueEnabled(boolean deleteQueueEnabled) {
		this.deleteQueueEnabled = deleteQueueEnabled;
	}

	public int getDeleteQueueSize() {
		return deleteQueueSize;
	}

	public void setDeleteQueueSize(int deleteQueueSize) {
		this.deleteQueueSize = deleteQueueSize;
	}

	/**
	 * 內部啟動
	 */
	protected class StartCallbacker implements StartCallback {

		@Override
		public void doInAction() throws Exception {
			// 檢查設置
			checkConfig();
			//
			// 建立新增佇列群
			createInsertQueueGroup();
			// 建立修改佇列群
			createUpdateQueueGroup();
			// 建立刪除佇列群
			createDeleteQueueGroup();
		}
	}

	/**
	 * 內部關閉
	 */
	protected class ShutdownCallbacker implements ShutdownCallback {

		@Override
		public void doInAction() throws Exception {
			if (insertQueueEnabled) {
				insertQueueGroup.shutdown();
			}
			if (updateQueueEnabled) {
				updateQueueGroup.shutdown();
			}
			if (deleteQueueEnabled) {
				deleteQueueGroup.shutdown();
			}
		}
	}

	/**
	 * 檢查設置
	 * 
	 * @throws Exception
	 */
	protected abstract void checkConfig() throws Exception;

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		// move to StartCallbacker.doInAction()
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		// move to ShutdownCallbacker.doInAction()
	}

	/**
	 * 建立新增佇列群
	 * 
	 * @throws Exception
	 */
	protected void createInsertQueueGroup() throws Exception {
		if (this.insertQueueEnabled) {
			AssertHelper.isBetween(insertQueueSize, 1, MAX_QUEUE_SIZE,
					"The InsertQueueSize is " + insertQueueSize + " must be between 1 and " + MAX_QUEUE_SIZE);
			//
			@SuppressWarnings("unchecked")
			InsertQueue<Object> queues[] = new InsertQueue[this.insertQueueSize];// thread數量
			for (int i = 0; i < queues.length; ++i) {
				InsertQueue<Object> queue = new InsertQueue<Object>(threadService);
				queue.setListenMills(this.insertQueueListenMills);
				queues[i] = queue;
			}
			this.insertQueueGroup = new RunnableQueueGroupImpl<Object>(queues);
			this.insertQueueGroup.start();
			//
			LOGGER.info("InsertQueueGroup[" + this.insertQueueSize + "] listen " + insertQueueListenMills + " mills.");
		}
	}

	/**
	 * 建立修改佇列群
	 * 
	 * @throws Exception
	 */
	protected void createUpdateQueueGroup() throws Exception {
		if (this.updateQueueEnabled) {
			AssertHelper.isBetween(updateQueueSize, 1, MAX_QUEUE_SIZE,
					"The UpdateQueueSize is " + updateQueueSize + " must be between 1 and " + MAX_QUEUE_SIZE);
			//
			@SuppressWarnings("unchecked")
			UpdateQueue<Object> queues[] = new UpdateQueue[this.updateQueueSize];// thread數量
			for (int i = 0; i < queues.length; ++i) {
				UpdateQueue<Object> queue = new UpdateQueue<Object>(threadService);
				queue.setListenMills(this.updateQueueListenMills);
				queues[i] = queue;
			}
			this.updateQueueGroup = new RunnableQueueGroupImpl<Object>(queues);
			this.updateQueueGroup.start();
			//
			LOGGER.info("UpdateQueueGroup[" + this.updateQueueSize + "] listen " + updateQueueListenMills + " mills.");
		}
	}

	/**
	 * 建立刪除佇列群
	 * 
	 * @throws Exception
	 */
	protected void createDeleteQueueGroup() throws Exception {
		if (this.deleteQueueEnabled) {
			AssertHelper.isBetween(deleteQueueSize, 1, MAX_QUEUE_SIZE,
					"The DeleteQueueSize is " + deleteQueueSize + " must be between 1 and " + MAX_QUEUE_SIZE);
			//
			@SuppressWarnings("unchecked")
			DeleteQueue<Object> queues[] = new DeleteQueue[this.deleteQueueSize];// thread數量
			for (int i = 0; i < queues.length; ++i) {
				DeleteQueue<Object> queue = new DeleteQueue<Object>(threadService);
				queue.setListenMills(this.deleteQueueListenMills);
				queues[i] = queue;
			}
			this.deleteQueueGroup = new RunnableQueueGroupImpl<Object>(queues);
			this.deleteQueueGroup.start();
			//
			LOGGER.info("DeleteQueueGroup[" + this.deleteQueueSize + "] listen " + deleteQueueListenMills + " mills.");
		}
	}

	/**
	 * hdl查詢所有資料, = findAll
	 * 
	 * @param entityClass
	 */
	@Override
	public <E> List<E> find(Class<?> entityClass) {
		return commonDao.find(entityClass);
	}

	/**
	 * hql查詢單筆pk資料
	 * 
	 * @param entityClass
	 * @param seq
	 */
	@Override
	public <T> T find(Class<?> entityClass, Serializable seq) {
		return commonDao.find(entityClass, seq);
	}

	/**
	 * hql查詢多筆pk資料
	 * 
	 * @param entityClass
	 * @param seqs
	 */
	@Override
	public <E> List<E> find(Class<?> entityClass, Collection<Serializable> seqs) {
		return commonDao.find(entityClass, seqs);
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
	 * 新增
	 * 
	 * @param e
	 */
	@LogTx
	public <T> Serializable insert(T entity) {
		return commonDao.insert(entity);
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
	 * 修改
	 * 
	 * @param e
	 */
	@LogTx
	public <T> int update(T entity) {
		return commonDao.update(entity);
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

	/**
	 * 刪除
	 * 
	 * @param e
	 */
	@LogTx
	public <T> int delete(T entity) {
		return commonDao.delete(entity);
	}

	/**
	 * 加到佇列新增
	 * 
	 * @param entity
	 */
	@LogTx
	public <T> boolean offerInsert(T entity) {
		boolean result = false;
		//
		if (this.insertQueueEnabled) {
			result = insertQueueGroup.offer(entity);
		} else {
			Serializable pk = insert(entity);
			if (pk != null) {
				result = true;
			}
		}
		//
		return result;
	}

	/**
	 * 加到佇列儲存
	 * 
	 * @param entity
	 */
	@LogTx
	public <T> boolean offerUpdate(T entity) {
		boolean result = false;
		//
		if (this.updateQueueEnabled) {
			result = updateQueueGroup.offer(entity);
		} else {
			int updated = update(entity);
			if (updated > 0) {
				result = true;
			}
		}
		//
		return result;
	}

	/**
	 * 加到佇列刪除
	 * 
	 * @param entity
	 */
	@LogTx
	public <T> boolean offerDelete(T entity) {
		boolean result = false;
		//
		if (this.deleteQueueEnabled) {
			result = deleteQueueGroup.offer(entity);
		} else {
			int deleted = delete(entity);
			if (deleted > 0) {
				result = true;
			}
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
	@LogTx
	public boolean offerDelete(Class<?> entityClass, Serializable seq) {
		boolean result = false;
		//
		Object entity = commonDao.find(entityClass, seq);
		if (entity == null) {
			return result;
		}
		result = offerDelete(entity);
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
	@LogTx
	public List<Boolean> offerDelete(Class<?> entityClass, Collection<Serializable> seqs) {
		List<Boolean> result = new LinkedList<Boolean>();
		//
		if (CollectionHelper.notEmpty(seqs)) {
			for (Serializable seq : seqs) {
				if (seq == null) {
					continue;
				}
				//
				boolean deleted = offerDelete(entityClass, seq);
				result.add(deleted);
			}
		}
		return result;
	}
}
