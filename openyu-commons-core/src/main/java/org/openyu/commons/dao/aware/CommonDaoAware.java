package org.openyu.commons.dao.aware;

import org.openyu.commons.dao.CommonDao;

public interface CommonDaoAware {

	CommonDao getCommonDao();

	void setCommonDao(CommonDao commonDao);
}