package org.openyu.commons.thread.supporter;

import java.util.concurrent.ExecutorService;

import org.openyu.commons.thread.BaseRunnableQueue;
import org.openyu.commons.thread.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseRunnableQueueSupporter<E> extends BaseRunnableSupporter implements BaseRunnableQueue<E> {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseRunnableQueueSupporter.class);

	public BaseRunnableQueueSupporter(ThreadService threadService) {
		super(threadService);
	}

	public BaseRunnableQueueSupporter(ExecutorService executorService) {
		super(executorService);
	}

}
