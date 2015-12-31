package org.openyu.commons.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.aop.supporter.BaseAroundAdviceSupporter;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.anno.DefaultThreadService;
import org.openyu.commons.thread.supporter.BaseRunnableSupporter;

/**
 * 非阻塞型多緒攔截器
 *
 * 不使用future.get(), 通常都回傳null, 所以不取傳回值
 *
 * 優點:可同時執行,但不取傳回值,keep in mem
 *
 * 缺點:無法有傳回值,無法有順序執行
 */
public class ThreadAdvice extends BaseAroundAdviceSupporter {

	private static final long serialVersionUID = -2697604382610079339L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(ThreadAdvice.class);

	/**
	 * 線程服務
	 */
	@DefaultThreadService
	private transient ThreadService threadService;

	public ThreadAdvice() {
	}

	@Override
	protected Object doInvoke(final MethodInvocation invocation) throws Throwable {
		Object result = null;
		//
		// threadService.submit(new Runnable() {
		// public void run() {
		// // --------------------------------------------------
		// try {
		// invocation.proceed();
		// } catch (Throwable e) {
		// LOGGER.error(new StringBuilder("Exception encountered during
		// run()").toString(), e);
		// }
		// // --------------------------------------------------
		// }
		// });

		// #fix 2015/12/31
		ProceedRunner proceedRunner = new ProceedRunner(threadService, invocation);
		proceedRunner.start();
		//
		return result;
	}

	/**
	 * 處理原邏輯
	 */
	protected class ProceedRunner extends BaseRunnableSupporter {

		private MethodInvocation invocation;

		public ProceedRunner(ThreadService threadService, MethodInvocation invocation) {
			super(threadService);
			this.invocation = invocation;
		}

		@Override
		protected void doRun() throws Exception {
			try {
				proceed(invocation);
			} catch (Throwable e) {
				LOGGER.error(new StringBuilder("Exception encountered during doRun()").toString(), e);
			}
		}
	}

	/**
	 * 處理原邏輯
	 * 
	 * @param invocation
	 * @throws Throwable
	 */
	protected void proceed(MethodInvocation invocation) throws Throwable {
		invocation.proceed();
	}

}
