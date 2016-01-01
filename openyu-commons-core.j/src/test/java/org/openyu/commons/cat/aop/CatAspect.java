package org.openyu.commons.cat.aop;

import java.lang.reflect.Method;
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

	private static final long serialVersionUID = 2524265554035630063L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(CatAspect.class);

	@Autowired
	@Qualifier("catLogService")
	private transient CatLogService catLogService;

	public CatAspect() {

	}

	@Around("execution(public * org.openyu.commons.cat.service.CatService.insertCat(..))")
	public Object recordInsert(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		//
		String method = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		CatImpl cat = (CatImpl) args[0];
		//
		System.out.println("@Aspect is running!");
		System.out.println("method: " + method);
		System.out.println("arguments: " + Arrays.toString(args));

		System.out.println("Around before is running!");
		//
		result = joinPoint.proceed(); // continue on the intercepted
										// method
		//
		System.out.println("Around after is running!");
		// log
		catLogService.recordInsert(cat.getId());
		//
		return result;
	}

}
