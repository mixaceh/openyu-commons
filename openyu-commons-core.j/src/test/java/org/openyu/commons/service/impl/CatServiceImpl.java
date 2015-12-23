package org.openyu.commons.service.impl;

import org.openyu.commons.service.supporter.CommonServiceSupporter;
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
}
