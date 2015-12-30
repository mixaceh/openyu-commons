package org.openyu.commons.cat.service.impl;

import org.openyu.commons.cat.dao.CatDao;
import org.openyu.commons.cat.service.CatService;
import org.openyu.commons.service.supporter.CommonServiceSupporter;
import org.openyu.commons.util.AssertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CatServiceImpl extends CommonServiceSupporter implements CatService {

	private static final long serialVersionUID = 4741632401699704385L;

	public CatServiceImpl() {

	}

	@Autowired
	@Qualifier("catDao")
	public void setCatDao(CatDao catDao) {
		setCommonDao(catDao);
	}

	/**
	 * 檢查設置
	 * 
	 * @throws Exception
	 */
	protected final void checkConfig() throws Exception {
		AssertHelper.notNull(this.commonDao, "The CommonDao is required");
	}

}
