package org.openyu.commons.dao.aware;

import org.openyu.commons.dao.OjDao;

public interface OjDaoAware {

	OjDao getOjDao();

	void setOjDao(OjDao ojDao);
}