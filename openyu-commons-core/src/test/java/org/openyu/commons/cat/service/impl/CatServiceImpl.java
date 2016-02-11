package org.openyu.commons.cat.service.impl;

import java.io.Serializable;

import org.openyu.commons.cat.dao.CatDao;
import org.openyu.commons.cat.service.CatService;
import org.openyu.commons.cat.vo.impl.CatImpl;
import org.openyu.commons.dao.anno.CommonTx;
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
		AssertHelper.notNull(this.commonDao, "The CatDao is required");
	}

	@CommonTx
	@Override
	public Serializable insertCat(CatImpl cat) {
		return insert(cat);
	}

	@CommonTx
	@Override
	public Serializable insertCat2(CatImpl cat) {
		return insert(cat);
	}

}
