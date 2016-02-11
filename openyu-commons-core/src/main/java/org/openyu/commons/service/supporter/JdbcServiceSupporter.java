package org.openyu.commons.service.supporter;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.openyu.commons.dao.JdbcDao;
import org.openyu.commons.dao.aware.JdbcDaoAware;
import org.openyu.commons.service.JdbcService;

public class JdbcServiceSupporter extends BaseServiceSupporter implements
		JdbcService, JdbcDaoAware {

	private static final long serialVersionUID = 3604369355366776944L;

	private transient JdbcDao jdbcDao;

	public JdbcServiceSupporter() {
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

	public JdbcDao getJdbcDao() {
		return jdbcDao;
	}

	public void setJdbcDao(JdbcDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}

	public <E> List<E> find(String sqlString, String[] paramNames,
			Object[] values, String[] columnAliases, Object[] types) {
		return jdbcDao
				.find(sqlString, paramNames, values, columnAliases, types);
	}

	public <E> List<E> find(String sqlString, String[] paramNames,
			Object[] values, Map<String, Object> scalars) {
		return jdbcDao.find(sqlString, paramNames, values, scalars);
	}

	public int insert(String sqlString, String[] paramNames, Object[] values) {
		return jdbcDao.insert(sqlString, paramNames, values);
	}

	public int insert(String sqlString, String[] paramNames, Object[] values,
			String modifiedUser) {
		return jdbcDao.insert(sqlString, paramNames, values, modifiedUser);
	}

	public int update(String sqlString, String[] paramNames, Object[] values) {
		return jdbcDao.update(sqlString, paramNames, values);
	}

	public int update(String sqlString, String[] paramNames, Object[] values,
			String modifiedUser) {
		return jdbcDao.update(sqlString, paramNames, values, modifiedUser);
	}

	public int delete(String sqlString, String[] paramNames, Object[] values) {
		return jdbcDao.delete(sqlString, paramNames, values);
	}

	public int delete(String sqlString, String[] paramNames, Object[] values,
			String modifiedUser) {
		return jdbcDao.delete(sqlString, paramNames, values, modifiedUser);
	}

	public <E> InputStream write(Collection<E> list) {
		return jdbcDao.write(list);
	}

	public <E> List<E> read(InputStream inputStream) {
		return jdbcDao.read(inputStream);
	}

	@Override
	public <E> List<E> find(String sqlString, Map<String, Object> params,
			Map<String, Object> scalars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(String sqlString, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(String sqlString, Map<String, Object> params,
			String modifiedUser) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String sqlString, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String sqlString, Map<String, Object> params,
			String modifiedUser) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String sqlString, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String sqlString, Map<String, Object> params,
			String modifiedUser) {
		// TODO Auto-generated method stub
		return 0;
	}

}
