package org.openyu.commons.aop.supporter;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.aop.BaseAfterReturningAdvice;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * 可在proceed後, 處理其他邏輯
 */
public abstract class BaseAfterReturningAdviceSupporter extends BaseServiceSupporter
		implements BaseAfterReturningAdvice {

	private static final long serialVersionUID = -8066663886070767001L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseAfterReturningAdviceSupporter.class);

	public BaseAfterReturningAdviceSupporter() {
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
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		try {
			LOGGER.info(new StringBuilder().append("[After returing advice] afterReturning ").append(getDisplayName())
					.toString());
			doAfterReturning(returnValue, method, args, target);
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder("Exception encountered during [After returing advice] afterReturning()")
					.toString(), e);
		}
	}

	/**
	 * 內部觸發
	 */
	protected abstract Object doAfterReturning(Object returnValue, Method method, Object[] args, Object target)
			throws Throwable;

}
