package org.openyu.commons.service.impl;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.openyu.commons.dao.CommonDao;
import org.openyu.commons.dao.aware.CommonDaoAware;
import org.openyu.commons.dao.supporter.BaseDaoSupporter;
import org.openyu.commons.service.AsnycCommonService;
import org.openyu.commons.service.impl.QueueServiceImpl.DeleteQueue;
import org.openyu.commons.service.impl.QueueServiceImpl.InsertQueue;
import org.openyu.commons.service.impl.QueueServiceImpl.UpdateQueue;
import org.openyu.commons.thread.BaseRunnableQueue;
import org.openyu.commons.thread.LoopQueue;
import org.openyu.commons.thread.RunnableQueueGroup;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.anno.DefaultThreadService;
import org.openyu.commons.thread.impl.RunnableQueueGroupImpl;
import org.openyu.commons.thread.supporter.LoopQueueSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 異步 Dao
 */
public class AsnycCommonServiceImpl extends BaseDaoSupporter implements AsnycCommonService, CommonDaoAware {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(AsnycCommonServiceImpl.class);

	private transient CommonDao commonDao;

	/**
	 * 線程服務
	 */
	@DefaultThreadService
	private transient ThreadService threadService;

	/**
	 * 最大佇列個數
	 */
	private static final int MAX_QUEUE_SIZE = 10;

	/**
	 * 佇列監聽毫秒
	 */
	private long queueListenMills = 10L;

	/**
	 * 佇列是否開啟
	 */
	private boolean queueEnabled;

	/**
	 * 佇列個數
	 */
	private int queueSize;

	/**
	 * 新增佇列
	 */
	private transient RunnableQueueGroup<Object> insertQueueGroup;

	/**
	 * 修改佇列
	 */
	private transient RunnableQueueGroup<Object> updateQueueGroup;

	/**
	 * 刪除佇列
	 */
	private transient RunnableQueueGroup<Object> deleteQueueGroup;

	public AsnycCommonServiceImpl() {
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		// 建立新增佇列
		createInsertQueueGroup();
		// 建立修改佇列
		createUpdateQueueGroup();
		// 建立刪淑佇列
		createDeleteQueueGroup();

	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		if (this.queueEnabled) {
			insertQueueGroup.shutdown();
			updateQueueGroup.shutdown();
			deleteQueueGroup.shutdown();
		}
	}

	protected void createInsertQueueGroup() throws Exception {
		if (this.queueEnabled) {
			AssertHelper.isBetween(queueSize, 1, MAX_QUEUE_SIZE,
					"The queueSize is " + queueSize + " must be between 1 and " + MAX_QUEUE_SIZE);
			//
			@SuppressWarnings("unchecked")
			InsertQueue<Object> queues[] = new InsertQueue[this.queueSize];// thread數量
			for (int i = 0; i < queues.length; ++i) {
				InsertQueue<Object> queue = new InsertQueue<Object>(threadService);
				queue.setListenMills(this.queueListenMills);
				queues[i] = queue;
			}
			this.insertQueueGroup = new RunnableQueueGroupImpl<Object>(queues);
			this.insertQueueGroup.start();
			//
			LOGGER.info("InsertQueueGroup[" + this.queueSize + "] listen " + queueListenMills + " mills.");
		}
	}

	protected void createUpdateQueueGroup() {

	}

	protected void createDeleteQueueGroup() {

	}

	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public <E> InputStream write(Collection<E> list) {
		throw new UnsupportedOperationException();
	}

	public <E> List<E> read(InputStream inputStream) {
		throw new UnsupportedOperationException();
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
