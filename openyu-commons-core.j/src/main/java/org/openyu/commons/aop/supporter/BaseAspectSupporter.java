package org.openyu.commons.aop.supporter;

import org.openyu.commons.aop.BaseAspect;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1.此base類別不能宣告@Aspect
 * 
 * 2.不能是 super class 的方法
 * 
 * 3.pointcut可以是interface或impelemt class
 * 
 * @Around(
 * "execution(public * org.openyu.commons.cat.service.CatService.insertCat*(..))"
 * )
 * 
 * @Around(
 * "execution(public * org.openyu.commons.cat.service.impl.CatServiceImpl.insertCat*(..))"
 * )
 */
public class BaseAspectSupporter extends BaseServiceSupporter implements BaseAspect {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseAspectSupporter.class);

	public BaseAspectSupporter() {

	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {

	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}
}
