package org.openyu.commons.spring.jdbc.datasource.impl;

import org.openyu.commons.spring.jdbc.datasource.supporter.DriverManagerDataSourceFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DriverManagerDataSourceFactoryBean
		extends DriverManagerDataSourceFactorySupporter<DriverManagerDataSource> {

	private static final long serialVersionUID = -8584655900994814157L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(DriverManagerDataSourceFactoryBean.class);

	private DriverManagerDataSource driverManagerDataSource;

	public DriverManagerDataSourceFactoryBean() {

	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected DriverManagerDataSource shutdownDriverManagerDataSource() throws Exception {
		try {
			if (this.driverManagerDataSource != null) {
				DriverManagerDataSource oldInstance = this.driverManagerDataSource;
				// oldInstance.close();
				this.driverManagerDataSource = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownDriverManagerDataSource()").toString(),
					e);
			throw e;
		}
		return this.driverManagerDataSource;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected DriverManagerDataSource restartDriverManagerDataSource() throws Exception {
		try {
			if (this.driverManagerDataSource != null) {
				this.driverManagerDataSource = shutdownDriverManagerDataSource();
				this.driverManagerDataSource = createDriverManagerDataSource(0);
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartDriverManagerDataSource()").toString(),
					e);
			throw e;
		}
		return this.driverManagerDataSource;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.driverManagerDataSource = this.createDriverManagerDataSource(0);
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.driverManagerDataSource = shutdownDriverManagerDataSource();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.driverManagerDataSource = restartDriverManagerDataSource();
	}

	@Override
	public DriverManagerDataSource getObject() throws Exception {
		return driverManagerDataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.driverManagerDataSource != null) ? this.driverManagerDataSource.getClass()
				: DriverManagerDataSource.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
