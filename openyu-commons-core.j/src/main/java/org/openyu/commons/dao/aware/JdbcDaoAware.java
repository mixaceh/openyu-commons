package org.openyu.commons.dao.aware;

import org.openyu.commons.dao.JdbcDao;

public interface JdbcDaoAware {

	JdbcDao getJdbcDao();

	void setJdbcDao(JdbcDao jdbcDao);
}