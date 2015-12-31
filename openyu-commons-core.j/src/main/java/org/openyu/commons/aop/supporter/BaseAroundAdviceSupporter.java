package org.openyu.commons.aop.supporter;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.aop.BaseAroundAdvice;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * 可在proceed前後, 處理其他邏輯
 */
public abstract class BaseAroundAdviceSupporter extends BaseServiceSupporter implements BaseAroundAdvice {

	private static final long serialVersionUID = -8066663886070767001L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseAroundAdviceSupporter.class);

	public BaseAroundAdviceSupporter() {
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
			LOGGER.info(new StringBuilder().append("[Around advice] invoking ").append(getDisplayName()).toString());
			// target物件
			// invocation.getThis();
			result = doInvoke(invocation);
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder("Exception encountered during [Around advice] invoke()").toString(), e);
		}
		return result;
	}

	/**
	 * 內部觸發
	 */
	protected abstract Object doInvoke(MethodInvocation invocation) throws Throwable;

}
