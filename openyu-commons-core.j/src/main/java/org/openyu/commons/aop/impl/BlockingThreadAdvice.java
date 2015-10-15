package org.openyu.commons.aop.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.openyu.commons.aop.supporter.BaseMethodInterceptorSupporter;
import org.openyu.commons.thread.ThreadService;

/**
 * 阻塞型多緒攔截器
 *
 * 因有future.get(), 通常都回傳結果Object, 可以取傳回值
 *
 * 缺點:會block等待,執行結束後,才會繼續往下走
 */
public class BlockingThreadAdvice extends BaseMethodInterceptorSupporter {

	private static final long serialVersionUID = -6718879850739838005L;

	/** The Constant log. */
	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(BlockingThreadAdvice.class);

	/**
	 * 阻塞型線程服務
	 */
	@Autowired
	@Qualifier("blockingThreadService")
	private transient ThreadService threadService;

	/**
	 * Instantiates a new blocking thread interceptor.
	 */
	public BlockingThreadAdvice() {
	}

	@Override
	protected Object invokeInternal(final MethodInvocation methodInvocation)
			throws Throwable {
		Object result = null;
		//
		Future<?> future = threadService.submit(new Callable<Object>() {

			public Object call() {
				Object obj = null;
				try {
					// --------------------------------------------------
					obj = methodInvocation.proceed();
					// --------------------------------------------------
				} catch (Throwable ex) {
					LOGGER.error("Failed", ex);
				}
				return obj;
			}
		});
		// 會阻塞,等待執行完畢
		result = future.get();
		return result;
	}
}