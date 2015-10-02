package org.openyu.commons.dao.supporter;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.openyu.commons.dao.JdbcDao;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.thread.ThreadService;

public abstract class JdbcDaoSupporter extends BaseDaoSupporter implements
		JdbcDao, Supporter {

	private static transient final Logger log = LogManager
			.getLogger(JdbcDaoSupporter.class);

	// @Autowired
	// @Qualifier("threadService")
	protected transient ThreadService threadService;

	public JdbcDaoSupporter() {
	}

	public <E> List<E> find(String sqlString, String[] paramNames,
			Object[] values, String[] columnAliases, Object[] types) {
		// getSimpleJdbcTemplate().query(sqlString, rm, args);
		throw new UnsupportedOperationException();
	}

	public <E> List<E> find(String sqlString, String[] paramNames,
			Object[] values, Map<String, Object> scalars) {
		// getSimpleJdbcTemplate().query(sqlString, rm, args);
		throw new UnsupportedOperationException();
	}

	// insert into
	// CARDS(CARD_ID,CARD_NUMBER,CARD_NUMBER_PREFIX,CARD_NUMBER_SORT,CARD_NUMBER_SUFFIX,CARD_STATUS,CARD_TYPE,CARD_ISSUE_STATUS,CARD_ISSUE_TYPE,CARD_AVAILABILITY_START,CARD_AVAILABILITY_END,PATRON_ID,NOTES)
	// values(CARDS_AUTO.nextval,?,?,?,?,?,?,?,?,?,?,?,?)
	public int insert(String sqlString, String[] paramNames, Object[] values) {
		// return getSimpleJdbcTemplate().update(sqlString, values);
		return 0;
	}

	public int insert(String sqlString, String[] paramNames, Object[] values,
			String modifiedUser) {
		throw new UnsupportedOperationException();
	}

	// update CARDS set
	// CARD_NUMBER=?,CARD_NUMBER_PREFIX=?,CARD_NUMBER_SORT=?,CARD_NUMBER_SUFFIX=?,CARD_STATUS=?,CARD_TYPE=?,CARD_ISSUE_STATUS=?,CARD_ISSUE_TYPE=?,CARD_AVAILABILITY_START=?,CARD_AVAILABILITY_END=?,PATRON_ID=?,NOTES=?
	// where CARD_ID=?
	public int update(String sqlString, String[] paramNames, Object[] values) {
		// return getSimpleJdbcTemplate().update(sqlString, values);
		return 0;
	}

	public int update(String sqlString, String[] paramNames, Object[] values,
			String modifiedUser) {
		throw new UnsupportedOperationException();
	}

	public int delete(String sqlString, String[] paramNames, Object[] values) {
		// return getSimpleJdbcTemplate().update(sqlString, values);
		return 0;
	}

	public int delete(String sqlString, String[] paramNames, Object[] values,
			String modifiedUser) {
		throw new UnsupportedOperationException();
	}

	public Long rowCount(Class<?> entityClass) {
		throw new UnsupportedOperationException();
	}

	public <E> InputStream write(Collection<E> list) {
		throw new UnsupportedOperationException();
	}

	public <E> List<E> read(InputStream inputStream) {
		throw new UnsupportedOperationException();
	}

	//
	protected Integer intFrom(Boolean b) {
		if (b == null) {
			return null;
		}
		return (b) ? (1) : (0);
	}

	protected Boolean boolFrom(Integer i) {
		if (i == null) {
			return null;
		}
		return i != 0;
	}

	protected Boolean toBoolean(Integer i) {
		return i == null ? null : !i.equals(0);
	}

	protected void setLong(PreparedStatement ps, int index, Long x)
			throws SQLException {
		if (x == null) {
			ps.setNull(index, Types.NUMERIC);
		} else {
			ps.setLong(index, x);
		}
	}

	protected void setBigDecimal(PreparedStatement ps, int index, BigDecimal x)
			throws SQLException {
		if (x == null) {
			ps.setNull(index, Types.FLOAT);
		} else {
			ps.setBigDecimal(index, x);
		}
	}

	protected void setInteger(PreparedStatement ps, int index, Integer x)
			throws SQLException {
		if (x == null) {
			ps.setNull(index, Types.NUMERIC);
		} else {
			ps.setLong(index, x);
		}
	}

	protected void setString(PreparedStatement ps, int index, String x)
			throws SQLException {
		if (x == null) {
			ps.setNull(index, Types.VARCHAR);
		} else {
			ps.setString(index, x);
		}
	}

	protected void setDate(PreparedStatement ps, int index, Date x)
			throws SQLException {
		if (x == null) {
			ps.setNull(index, Types.DATE);
		} else {
			ps.setDate(index, new java.sql.Date(x.getTime()));
		}
	}

	protected void setTimestamp(PreparedStatement ps, int index, Timestamp x)
			throws SQLException {
		if (x == null) {
			ps.setNull(index, Types.TIMESTAMP);
		} else {
			ps.setTimestamp(index, x);
		}
	}

	protected void setTimestamp(PreparedStatement ps, int index, Date x)
			throws SQLException {
		if (x == null) {
			ps.setNull(index, Types.TIMESTAMP);
		} else {
			ps.setTimestamp(index, new Timestamp(x.getTime()));
		}
	}

	protected void setBytes(PreparedStatement ps, int index, byte[] x)
			throws SQLException {
		if (x == null) {
			ps.setNull(index, Types.BLOB);
		} else {
			ps.setBytes(index, x);
		}
	}

	//
	protected String getString(ResultSet resultSet, String columnName)
			throws SQLException {
		return resultSet.getString(columnName);
	}

	protected String getString(ResultSet resultSet, int index)
			throws SQLException {
		return resultSet.getString(index);
	}

	//
	protected Boolean getBoolean(ResultSet resultSet, String columnName)
			throws SQLException {
		return getBoolean(resultSet.getObject(columnName));
	}

	protected Boolean getBoolean(ResultSet resultSet, int index)
			throws SQLException {
		return getBoolean(resultSet.getObject(index));
	}

	//
	private Boolean getBoolean(Object obj) {
		Boolean result;
		if (obj == null) {
			result = null;
		} else if (obj instanceof Boolean) {
			result = ((Boolean) obj).booleanValue();
		} else {
			result = null;
			log.warn("can't handle: " + obj);
		}
		return result;
	}

	//
	protected Date getDate(ResultSet resultSet, String columnName)
			throws SQLException {
		return resultSet.getDate(columnName);
	}

	protected Date getDate(ResultSet resultSet, int index) throws SQLException {
		return resultSet.getDate(index);
	}

	//
	protected Timestamp getTimestamp(ResultSet resultSet, String columnName)
			throws SQLException {
		return resultSet.getTimestamp(columnName);
	}

	protected Timestamp getTimestamp(ResultSet resultSet, int index)
			throws SQLException {
		return resultSet.getTimestamp(index);
	}

	//
	protected Integer getInt(ResultSet resultSet, String columnName)
			throws SQLException {
		return getInteger(resultSet.getObject(columnName));
	}

	protected Integer getInt(ResultSet resultSet, int index)
			throws SQLException {
		return getInteger(resultSet.getObject(index));
	}

	private Integer getInteger(Object obj) {
		if (obj instanceof BigDecimal) {
			return (obj == null) ? null : ((BigDecimal) obj).intValue();
		}
		if (obj instanceof Long) {
			return (obj == null) ? null : ((Long) obj).intValue();
		} else {
			return (obj == null) ? null : (Integer) obj;
		}
	}

	//
	protected Long getLong(ResultSet resultSet, String columnName)
			throws SQLException {
		return getLong(resultSet.getObject(columnName));
	}

	protected Long getLong(ResultSet resultSet, int index) throws SQLException {
		return getLong(resultSet.getObject(index));
	}

	private Long getLong(Object obj) {
		Long result;
		if (obj == null) {
			result = null;
		} else if (obj instanceof Long) {
			result = (Long) obj;
		} else if (obj instanceof BigDecimal) {
			result = ((BigDecimal) obj).longValue();
		} else if (obj instanceof Integer) {
			result = ((Integer) obj).longValue();
		} else {
			result = null;
			log.warn("can't handle: " + obj);
		}
		return result;
	}

	//
	protected BigDecimal getBigDecimal(ResultSet resultSet, String columnName)
			throws SQLException {
		return resultSet.getBigDecimal(columnName);
	}

	protected BigDecimal getBigDecimal(ResultSet resultSet, int index)
			throws SQLException {
		return resultSet.getBigDecimal(index);
	}

}
