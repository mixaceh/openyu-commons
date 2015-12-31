package org.openyu.commons.cat.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.openyu.commons.aop.supporter.BaseAspectSupporter;
import org.openyu.commons.cat.service.CatLogService;
import org.openyu.commons.cat.vo.impl.CatImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Aspect
public class CatAspect extends BaseAspectSupporter {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(CatAspect.class);

	@Autowired
	@Qualifier("catLogService")
	private transient CatLogService catLogService;

	public CatAspect() {

	}

	@Around("execution(public * org.openyu.commons.cat.service.CatService.insertCat(..))")
	public Object recordInsert(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("recordInsert() is running!");
		System.out.println("hijacked method : " + joinPoint.getSignature().getName());
		System.out.println("hijacked arguments : " + Arrays.toString(joinPoint.getArgs()));

		System.out.println("Around before is running!");
		//
		Object result = joinPoint.proceed(); // continue on the intercepted
												// method
		//
		System.out.println("Around after is running!");
		// log
		CatImpl cat = (CatImpl) joinPoint.getArgs()[0];
		catLogService.recordInsert(cat.getId());
		//
		return result;
	}

}
