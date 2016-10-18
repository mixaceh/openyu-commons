package org.openyu.commons.spring.jdbc.datasource.impl;

import org.openyu.commons.spring.jdbc.datasource.supporter.DriverManagerDataSourceFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DriverManagerDataSourceGroupFactoryBean
		extends DriverManagerDataSourceFactorySupporter<DriverManagerDataSource[]> {

	private static final long serialVersionUID = 9213551893607566215L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(DriverManagerDataSourceGroupFactoryBean.class);
	// props
	public static final String MAX_DATA_SOURCE_SIZE = "maxDataSourceSize";

	public static final int DEFAULT_MAX_DATA_SOURCE_SIZE = 1;

	private DriverManagerDataSource[] driverManagerDataSources;

	public DriverManagerDataSourceGroupFactoryBean() {

	}

	public DriverManagerDataSource[] getDriverManagerDataSources() {
		return driverManagerDataSources;
	}

	public void setDriverManagerDataSources(DriverManagerDataSource[] driverManagerDataSources) {
		this.driverManagerDataSources = driverManagerDataSources;
	}

	/**
	 * 建立多個DriverManagerDataSource
	 * 
	 * @return
	 * @throws Exception
	 */
	protected DriverManagerDataSource[] createDriverManagerDataSources() throws Exception {
		DriverManagerDataSource[] result = null;
		try {
			int maxDataSourceSize = extendedProperties.getInt(MAX_DATA_SOURCE_SIZE, DEFAULT_MAX_DATA_SOURCE_SIZE);
			result = new DriverManagerDataSource[maxDataSourceSize];
			//
			for (int i = 0; i < maxDataSourceSize; i++) {
				DriverManagerDataSource dataSource = createDriverManagerDataSource(i);
				result[i] = dataSource;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createDriverManagerDataSources()").toString(),
					e);
			try {
				result = (DriverManagerDataSource[]) shutdownDriverManagerDataSources();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}

	@Override
	protected DriverManagerDataSource shutdownDriverManagerDataSource() throws Exception {
		return null;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected DriverManagerDataSource[] shutdownDriverManagerDataSources() throws Exception {
		try {
			if (this.driverManagerDataSources != null) {
				for (int i = 0; i < this.driverManagerDataSources.length; i++) {
					DriverManagerDataSource oldInstance = this.driverManagerDataSources[i];
					// oldInstance.close();
					this.driverManagerDataSources[i] = null;
				}
				//
				this.driverManagerDataSources = null;
			}
		} catch (Exception e) {
			LOGGER.error(
					new StringBuilder("Exception encountered during shutdownDriverManagerDataSources()").toString(), e);
			throw e;
		}
		return this.driverManagerDataSources;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected DriverManagerDataSource[] restartDriverManagerDataSources() throws Exception {
		try {
			if (this.driverManagerDataSources != null) {
				this.driverManagerDataSources = shutdownDriverManagerDataSources();
				this.driverManagerDataSources = createDriverManagerDataSources();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartDriverManagerDataSources()").toString(),
					e);
			throw e;
		}
		return this.driverManagerDataSources;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		if (this.driverManagerDataSources != null) {
			LOGGER.info(new StringBuilder().append("Inject from setDriverManagerDataSources()").toString());
		} else {
			//
			LOGGER.info(new StringBuilder().append("Using createDriverManagerDataSources()").toString());
			this.driverManagerDataSources = createDriverManagerDataSources();
		}
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.driverManagerDataSources = shutdownDriverManagerDataSources();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.driverManagerDataSources = restartDriverManagerDataSources();
	}

	@Override
	public DriverManagerDataSource[] getObject() throws Exception {
		return driverManagerDataSources;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.driverManagerDataSources != null) ? this.driverManagerDataSources.getClass()
				: DriverManagerDataSource[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
