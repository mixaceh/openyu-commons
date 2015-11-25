package org.openyu.commons.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.openyu.commons.service.BaseService;

/**
 * 基底方法攔截器.
 */
public interface BaseMethodInterceptor extends BaseService, MethodInterceptor {

	/** The key. */
	String KEY = BaseMethodInterceptor.class.getName();

//	/**
//	 * 取消
//	 *
//	 * @return true, if is cancel
//	 */
//	boolean isCancel();
//
//	/**
//	 * Sets the cancel.
//	 *
//	 * @param cancel
//	 *            the new cancel
//	 */
//	void setCancel(boolean cancel);

	// /**
	// * 記錄.
	// *
	// * @return true, if is log enable
	// */
	// boolean isLogEnable();
	//
	// /**
	// * Sets the log enable.
	// *
	// * @param debug
	// * the new log enable
	// */
	// void setLogEnable(boolean debug);

	// /**
	// * 調用.
	// *
	// * @param methodInvocation
	// * the method invocation
	// * @param method
	// * 被攔截的方法
	// * @param paramTypes
	// * 方法參數類別
	// * @param args
	// * 方法參數值
	// * @return the object
	// */
	// Object invoke(MethodInvocation methodInvocation, Method method,
	// Class<?>[] paramTypes, Object[] args) throws Throwable;

}
