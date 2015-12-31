package org.openyu.commons.cat.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.openyu.commons.aop.supporter.BaseAroundAdviceSupporter;
import org.openyu.commons.cat.service.CatLogService;
import org.openyu.commons.cat.vo.impl.CatImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CatInsertAdvice extends BaseAroundAdviceSupporter {

	private static final long serialVersionUID = 7656845117510474167L;

	@Autowired
	@Qualifier("catLogService")
	private transient CatLogService catLogService;

	public CatInsertAdvice() {

	}

	protected Object doInvoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		CatImpl cat = (CatImpl) args[0];
		//
		System.out.println("Around before is running!");
		//
		Object result = invocation.proceed(); // continue on the intercepted
												// method
		//
		System.out.println("Around after is running!");
		//
		// log
		catLogService.recordInsert(cat.getId());
		return result;
	}
}
