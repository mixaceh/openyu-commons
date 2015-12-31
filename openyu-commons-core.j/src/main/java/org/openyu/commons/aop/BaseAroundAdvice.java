package org.openyu.commons.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.openyu.commons.service.BaseService;

/**
 * <aop:aspectj-autoproxy />	
 * 
 * @AspectJ support
 *          use @Before, @After, @AfterReturning, @AfterThrowing, @Around
 * 
 * @Before – Run before the method execution
 * 
 * @After – Run after the method returned a result
 * 
 * @AfterReturning – Run after the method returned a result, intercept the
 *                 returned result as well.
 *
 * @AfterThrowing – Run after the method throws an exception
 *
 * @Around – Run around the method execution, combine all three advices above.
 *         真正的邏輯在 methodInvocation.proceed()
 */

/**
 * Without @AspectJ support use
 * 
 * @Before -> org.springframework.aop.MethodBeforeAdvice
 * 
 * @After -> org.springframework.aop.AfterAdvice
 * 
 * @AfterReturning -> org.springframework.aop.AfterReturningAdvice
 * 
 * @AfterThrowing -> org.springframework.aop.ThrowsAdvice
 *
 * @Around -> org.aopalliance.intercept.MethodInterceptor
 */
public interface BaseAroundAdvice extends BaseService, MethodInterceptor {
}
