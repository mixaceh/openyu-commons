package org.openyu.commons.aop.supporter;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.aop.BaseBeforeAdvice;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * 可在proceed前, 處理其他邏輯
 */
public abstract class BaseBeforeAdviceSupporter extends BaseServiceSupporter implements BaseBeforeAdvice {

	private static final long serialVersionUID = 7452564549990187991L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseBeforeAdviceSupporter.class);

	public BaseBeforeAdviceSupporter() {
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
	public void before(Method method, Object[] args, Object target) throws Throwable {
		try {
			LOGGER.info(new StringBuilder().append("[Before advice] beforing ").append(getDisplayName()).toString());
			doBefore(method, args, target);
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder("Exception encountered during [Before advice] before()").toString(), e);
		}
	}

	/**
	 * 內部觸發
	 */
	protected abstract void doBefore(Method method, Object[] args, Object target) throws Throwable;

}
