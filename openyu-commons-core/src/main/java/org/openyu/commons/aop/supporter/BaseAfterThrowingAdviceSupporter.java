package org.openyu.commons.aop.supporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openyu.commons.aop.BaseAfterAdvice;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * 可在proceed後, 處理其他邏輯
 * 
 * 利用Around advice, 使用MethodInterceptor來實作
 */
public abstract class BaseAfterThrowingAdviceSupporter extends BaseServiceSupporter
		implements BaseAfterAdvice, MethodInterceptor {

	private static final long serialVersionUID = -6387285760375785973L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseAfterThrowingAdviceSupporter.class);

	public BaseAfterThrowingAdviceSupporter() {
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

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = null;
		try {
			result = invocation.proceed();
			return result;
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder("Exception encountered during [After throwing advice] invoke()").toString(),
					e);
			//
			try {
				LOGGER.info(new StringBuilder().append("[After throwing advice] afterThrowing ")
						.append(getDisplayName()).toString());
				doAfterThrowing(invocation);
			} catch (Throwable e2) {
				LOGGER.error(new StringBuilder("Exception encountered during [After throwing advice] afterThrowing()")
						.toString(), e2);
			}
			//
			throw e;
		}
	}

	/**
	 * 內部觸發
	 */
	protected abstract void doAfterThrowing(MethodInvocation invocation) throws Throwable;

}
