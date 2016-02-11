package org.openyu.commons.aop;

import org.openyu.commons.service.BaseService;
import org.springframework.aop.AfterReturningAdvice;

/**
 * <aop:aspectj-autoproxy />	
 * 
 * @AspectJ support
 *          use @Before, @After, @AfterReturning, @AfterThrowing, @Around
 * 
 * @AfterReturning – Run after the method returned a result, intercept the
 *                 returned result as well.
 *                 org.springframework.aop.AfterReturningAdvice
 */

/**
 * Without @AspectJ support use
 * 
 * @AfterReturning -> org.springframework.aop.AfterReturningAdvice
 */
public interface BaseAfterReturningAdvice extends BaseService, AfterReturningAdvice {
}
