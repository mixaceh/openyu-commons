package org.openyu.commons.service.impl;

import org.openyu.commons.service.supporter.CommonServiceSupporter;
import org.openyu.commons.util.AssertHelper;

public class CommonServiceImpl extends CommonServiceSupporter {

	private static final long serialVersionUID = -2840063460022452697L;

	public CommonServiceImpl() {
	}

	/**
	 * 檢查設置
	 * 
	 * @throws Exception
	 */
	@Override
	protected final void checkConfig() throws Exception {
		AssertHelper.notNull(this.commonDao, "The CommonDao is required");
	}

}
