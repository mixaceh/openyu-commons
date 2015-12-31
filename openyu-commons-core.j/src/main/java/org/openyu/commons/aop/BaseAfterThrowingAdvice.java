package org.openyu.commons.aop;

import org.openyu.commons.service.BaseService;
import org.springframework.aop.ThrowsAdvice;

/**
 * <aop:aspectj-autoproxy />	
 * 
 * @AspectJ support
 *          use @Before, @After, @AfterReturning, @AfterThrowing, @Around
 * 
 * @AfterThrowing – Run after the method throws an exception
 */

/**
 * Without @AspectJ support use
 * 
 * @AfterThrowing -> org.springframework.aop.ThrowsAdvice
 */
public interface BaseAfterThrowingAdvice extends BaseService, ThrowsAdvice {
}
