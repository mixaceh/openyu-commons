package org.openyu.commons.jbossts;

import com.arjuna.ats.jdbc.TransactionalDriver;

import com.arjuna.ats.internal.jdbc.DynamicClass;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

import javax.sql.XADataSource;

import java.sql.SQLException;

/*
 * This is a wrapper class used to instantiate the XADataSource
 * for MySql database. Most of the code is from JBossTS source
 * with some minor modifications for MySQL related stuff.
 */

public class Mysql implements DynamicClass {
	private static final String driverName = "mysql:";
	private static final String semicolon = ";";

	public Mysql() {
	}

	public XADataSource getDataSource(String dbName) throws SQLException {
		return getDataSource(dbName, true);
	}

	public synchronized XADataSource getDataSource(String dbName, boolean create) throws SQLException {
		try {
			MysqlXADataSource xads = new MysqlXADataSource();
			int index1 = dbName.indexOf(Mysql.driverName);

			if (index1 == -1) {
				// throw new SQLException("Mysql.getDataSource -
				// "+jdbcLogger.logMesg.getString("com.arjuna.ats.internal.jdbc.drivers.invaliddb")+"
				// Mysql");
				throw new SQLException("Mysql.getDataSource - " + dbName);
			} else {
				/*
				 * Strip off any spurious parameters.
				 */

				int index2 = dbName.indexOf(Mysql.semicolon);
				String theDbName = null;

				if (index2 == -1) {
					theDbName = dbName.substring(index1 + Mysql.driverName.length());
				} else {
					theDbName = dbName.substring(index1 + Mysql.driverName.length(), index2);
				}

				System.out.println("URL->" + TransactionalDriver.jdbc + Mysql.driverName + theDbName);

				xads.setURL(TransactionalDriver.jdbc + Mysql.driverName + theDbName);

				return xads;
			}
		} catch (SQLException e1) {
			throw e1;
		} catch (Exception e2) {
			// throw new SQLException("Mysql
			// "+jdbcLogger.logMesg.getString("com.arjuna.ats.internal.jdbc.drivers.exception")+e2);
			throw new SQLException("Mysql " + e2);
		}
	}

	public synchronized void shutdownDataSource(XADataSource ds) throws SQLException {
		// Empty implmentation
	}

}
