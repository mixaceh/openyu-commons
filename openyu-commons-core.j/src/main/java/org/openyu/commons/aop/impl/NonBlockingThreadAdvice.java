package org.openyu.commons.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.openyu.commons.aop.supporter.BaseAroundAdviceSupporter;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.anno.DefaultThreadService;

/**
 * 非阻塞型多緒攔截器
 *
 * 不使用future.get(), 通常都回傳null, 所以不取傳回值
 *
 * 優點:可同時執行,但不取傳回值,keep in mem
 *
 * 缺點:無法有傳回值,無法有順序執行
 */
public class NonBlockingThreadAdvice extends BaseAroundAdviceSupporter {

	private static final long serialVersionUID = -2697604382610079339L;

	/** The Constant log. */
	private static transient final Logger LOGGER = LoggerFactory.getLogger(NonBlockingThreadAdvice.class);

	/**
	 * 線程服務
	 */
	// @Autowired
	// @Qualifier("threadService")
	@DefaultThreadService
	private transient ThreadService threadService;

	/**
	 * Instantiates a new non blocking thread interceptor.
	 */
	public NonBlockingThreadAdvice() {
	}

	protected Object doInvoke(final MethodInvocation invocation) throws Throwable {
		Object result = null;
		//
		threadService.submit(new Runnable() {
			public void run() {
				// --------------------------------------------------
				try {
					invocation.proceed();
				} catch (Throwable e) {
					LOGGER.error(new StringBuilder("Exception encountered during run()").toString(), e);
				}
				// --------------------------------------------------
			}
		});
		return result;
	}
}
