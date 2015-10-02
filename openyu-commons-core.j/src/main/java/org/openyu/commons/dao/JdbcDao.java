package org.openyu.commons.dao;

import java.util.List;
import java.util.Map;

/**
 * JDBC Dao
 */
public interface JdbcDao extends BaseDao {

	// ------------------------------------
	// sql find(select)
	// ------------------------------------
	/**
	 * sql查詢多筆資料
	 * 
	 * @param sqlString
	 * @param paramNames
	 * @param values
	 * @param columnAliases
	 * @param types
	 */
	<E> List<E> find(String sqlString, String[] paramNames, Object[] values,
			String[] columnAliases, Object[] types);

	/**
	 * sql查詢多筆資料
	 * 
	 * sql find use map to set scalars
	 * 
	 * @param sqlString
	 * @param paramNames
	 * @param values
	 * @param scalars
	 */
	<E> List<E> find(String sqlString, String[] paramNames, Object[] values,
			Map<String, Object> scalars);

	/**
	 * sql查詢多筆資料
	 * 
	 * sql find use map to set params, scalars
	 * 
	 * @param sqlString
	 * @param params
	 * @param scalars
	 * @return
	 */
	<E> List<E> find(String sqlString, Map<String, Object> params,
			Map<String, Object> scalars);

	// ------------------------------------
	// sql insert
	// ------------------------------------
	int insert(String sqlString, String[] paramNames, Object[] values);

	int insert(String sqlString, String[] paramNames, Object[] values,
			String modifiedUser);

	int insert(String sqlString, Map<String, Object> params);

	int insert(String sqlString, Map<String, Object> params, String modifiedUser);

	// ------------------------------------
	// sql update
	// ------------------------------------
	int update(String sqlString, String[] paramNames, Object[] values);

	int update(String sqlString, String[] paramNames, Object[] values,
			String modifiedUser);

	int update(String sqlString, Map<String, Object> params);

	int update(String sqlString, Map<String, Object> params, String modifiedUser);

	// ------------------------------------
	// sql delete
	// ------------------------------------
	int delete(String sqlString, String[] paramNames, Object[] values);

	int delete(String sqlString, String[] paramNames, Object[] values,
			String modifiedUser);

	int delete(String sqlString, Map<String, Object> params);

	int delete(String sqlString, Map<String, Object> params, String modifiedUser);
}
