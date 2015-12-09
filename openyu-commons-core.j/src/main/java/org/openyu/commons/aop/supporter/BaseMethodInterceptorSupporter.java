package org.openyu.commons.aop.supporter;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.aop.BaseMethodInterceptor;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * 方法攔截器
 *
 * round advice,可在proceed前後,處理其他邏輯.
 */
public abstract class BaseMethodInterceptorSupporter extends BaseServiceSupporter
		implements BaseMethodInterceptor, Supporter {

	private static final long serialVersionUID = -8066663886070767001L;

	/** The Constant log. */
	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseMethodInterceptorSupporter.class);

	// /** 取消 */
	// private boolean cancel;

	// /** 記錄 */
	// private boolean logEnable;

	/**
	 * Instantiates a new base method interceptor supporter.
	 */
	public BaseMethodInterceptorSupporter() {
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

	// @Override
	// public boolean isCancel() {
	// return cancel;
	// }
	//
	// @Override
	// public void setCancel(boolean cancel) {
	// this.cancel = cancel;
	// }

	// public boolean isLogEnable() {
	// return logEnable;
	// }
	//
	// public void setLogEnable(boolean logEnable) {
	// this.logEnable = logEnable;
	// }

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object result = null;
		try {
			LOGGER.info(new StringBuilder().append("Invoking ").append(getDisplayName()).toString());
			result = doInvoke(methodInvocation);
		} catch (Throwable e) {
			LOGGER.error(new StringBuilder("Exception encountered during invoke()").toString(), e);
		}
		return result;
	}

	/**
	 * 內部觸發
	 */
	protected abstract Object doInvoke(MethodInvocation methodInvocation) throws Throwable;

	// /**
	// * Log.
	// *
	// * @param log
	// * the log
	// * @param method
	// * the method
	// */
	// protected void log(Logger log, Method method) {
	// log(log, method, null);
	// }
	//
	// /**
	// * Log.
	// *
	// * @param log
	// * the log
	// * @param method
	// * the method
	// * @param stopWatch
	// * the stop watch
	// */
	// protected void log(Logger log, Method method, StopWatch stopWatch) {
	// if (logEnable) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("[thread-");
	// sb.append(Thread.currentThread().getId());
	// sb.append("] ");
	// sb.append(method.getDeclaringClass().getSimpleName());
	// sb.append(" ");
	// sb.append(method.getReturnType().getSimpleName());
	// sb.append(" ");
	// sb.append(method.getName());
	// //
	// Class<?>[] paramTypes = ClassHelper.getParameterTypesAndCache(
	// method.getDeclaringClass(), method);
	// if (paramTypes != null) {
	// if (paramTypes.length == 0) {
	// sb.append("()");
	// } else {
	// sb.append("(");
	// for (int i = 0; i < paramTypes.length; i++) {
	// sb.append(paramTypes[i].getSimpleName());
	// if (i < paramTypes.length - 1) {
	// sb.append(StringHelper.COMMA + " ");
	// } else {
	// sb.append("),");
	// }
	// }
	// }
	// }
	// //
	// if (stopWatch != null) {
	// sb.append(" in ");
	// sb.append(stopWatch.getTotalTimeMillis());
	// sb.append(" mills.");
	// }
	// //
	// log.info(sb.toString());
	// }
	// }

	// /**
	// * Method equals.
	// *
	// * @param expected
	// * the expected
	// * @param actual
	// * the actual
	// * @return true, if successful
	// */
	// protected boolean methodEquals(Method expected, Method actual) {
	// boolean result = false;
	// if (expected != null
	// && expected.getName().equals(actual.getName())
	// && ObjectHelper.equals(expected.getParameterTypes(),
	// actual.getParameterTypes())) {
	// result = true;
	// }
	// return result;
	// }

}
