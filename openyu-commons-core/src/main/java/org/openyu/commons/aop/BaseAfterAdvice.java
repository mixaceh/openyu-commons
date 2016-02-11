package org.openyu.commons.aop;

import org.openyu.commons.service.BaseService;
import org.springframework.aop.AfterAdvice;

/**
 * <aop:aspectj-autoproxy />	
 * 
 * @AspectJ support
 *          use @Before, @After, @AfterReturning, @AfterThrowing, @Around
 * 
 * @After – Run after the method returned a result
 */

/**
 * Without @AspectJ support use
 * 
 * @After -> org.springframework.aop.AfterAdvice
 */
public interface BaseAfterAdvice extends BaseService, AfterAdvice {
}
