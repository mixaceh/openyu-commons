package org.openyu.commons.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.openyu.commons.service.BaseService;

/**
 * <aop:aspectj-autoproxy />	
 * 
 * @AspectJ support
 *          use @Before, @After, @AfterReturning, @AfterThrowing, @Around
 * 
 * @Around – Run around the method execution, combine all three advices above.
 *         真正的邏輯在 methodInvocation.proceed()
 */

/**
 * Without @AspectJ support use
 * 
 * @Around -> org.aopalliance.intercept.MethodInterceptor
 */
public interface BaseAroundAdvice extends BaseService, MethodInterceptor {
}
