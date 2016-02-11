package org.openyu.commons.aop;

import org.openyu.commons.service.BaseService;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * <aop:aspectj-autoproxy />	
 * 
 * @AspectJ support
 *          use @Before, @After, @AfterReturning, @AfterThrowing, @Around
 *          
 * @Before – Run before the method execution
 */

/**
 * Without @AspectJ support use
 * 
 * @Before -> org.springframework.aop.MethodBeforeAdvice
 * 
 */
public interface BaseBeforeAdvice extends BaseService, MethodBeforeAdvice {
}
