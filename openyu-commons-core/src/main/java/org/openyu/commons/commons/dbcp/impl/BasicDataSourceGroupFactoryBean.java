package org.openyu.commons.commons.dbcp.impl;

import org.apache.commons.dbcp.BasicDataSource;
import org.openyu.commons.commons.dbcp.supporter.BasicDataSourceFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BasicDataSourceGroup工廠
 */
public final class BasicDataSourceGroupFactoryBean extends BasicDataSourceFactorySupporter<BasicDataSource[]> {

	private static final long serialVersionUID = 5865182754049441001L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BasicDataSourceGroupFactoryBean.class);

	public final static String MAX_DATA_SOURCE_SIZE = "maxDataSourceSize";

	public static final int DEFAULT_MAX_DATA_SOURCE_SIZE = 1;

	private BasicDataSource[] basicDataSources;

	public BasicDataSourceGroupFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected BasicDataSource[] createBasicDataSources() throws Exception {
		BasicDataSource[] result = null;
		try {
			result = new BasicDataSource[extendedProperties.getInt(MAX_DATA_SOURCE_SIZE, DEFAULT_MAX_DATA_SOURCE_SIZE)];
			//
			for (int i = 0; i < result.length; i++) {
				BasicDataSource dataSource = createBasicDataSource(i);
				result[i] = dataSource;
			}

		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createBasicDataSources()").toString(), e);
			try {
				result = (BasicDataSource[]) shutdownBasicDataSources();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}

	protected BasicDataSource shutdownBasicDataSource() throws Exception {
		return null;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected BasicDataSource[] shutdownBasicDataSources() throws Exception {
		try {
			if (this.basicDataSources != null) {
				for (int i = 0; i < this.basicDataSources.length; i++) {
					BasicDataSource oldInstance = this.basicDataSources[i];
					oldInstance.close();
					this.basicDataSources[i] = null;
				}
				//
				this.basicDataSources = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownBasicDataSources()").toString(), e);
			throw e;
		}
		return this.basicDataSources;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected BasicDataSource[] restartBasicDataSources() throws Exception {
		try {
			if (this.basicDataSources != null) {
				this.basicDataSources = shutdownBasicDataSources();
				this.basicDataSources = createBasicDataSources();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartBasicDataSources()").toString(), e);
			throw e;
		}
		return this.basicDataSources;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.basicDataSources = createBasicDataSources();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.basicDataSources = shutdownBasicDataSources();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.basicDataSources = restartBasicDataSources();
	}

	@Override
	public BasicDataSource[] getObject() throws Exception {
		return basicDataSources;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.basicDataSources != null) ? this.basicDataSources.getClass() : BasicDataSource[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
