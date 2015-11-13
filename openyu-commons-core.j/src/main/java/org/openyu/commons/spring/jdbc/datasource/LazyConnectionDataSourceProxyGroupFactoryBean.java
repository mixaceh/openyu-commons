package org.openyu.commons.spring.jdbc.datasource;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

public class LazyConnectionDataSourceProxyGroupFactoryBean
		extends BaseFactorySupporter<LazyConnectionDataSourceProxy[]> {

	private static final long serialVersionUID = 1560497754167571755L;

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(LazyConnectionDataSourceProxyGroupFactoryBean.class);

	private DataSource[] targetDataSources;

	private LazyConnectionDataSourceProxy[] lazyConnectionDataSourceProxys;

	public LazyConnectionDataSourceProxyGroupFactoryBean(DataSource[] targetDataSources) {
		this.targetDataSources = targetDataSources;
	}

	public LazyConnectionDataSourceProxyGroupFactoryBean() {
	}

	public void setTargetDataSources(DataSource[] targetDataSources) {
		AssertHelper.notNull(targetDataSources, "The TargetDataSources must not be null");
		this.targetDataSources = targetDataSources;
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected LazyConnectionDataSourceProxy[] createLazyConnectionDataSourceProxys(DataSource[] dataSources)
			throws Exception {
		AssertHelper.notNull(dataSources, "The DataSources must not be null");
		//
		LazyConnectionDataSourceProxy[] result = null;
		try {
			//
			result = new LazyConnectionDataSourceProxy[dataSources.length];
			for (int i = 0; i < dataSources.length; i++) {
				DataSource targetDataSource = dataSources[i];
				LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy = new LazyConnectionDataSourceProxy(
						targetDataSource);
				result[i] = lazyConnectionDataSourceProxy;
			}
		} catch (Exception e) {
			LOGGER.error(
					new StringBuilder("Exception encountered during createLazyConnectionDataSourceProxys()").toString(),
					e);
			try {
				result = (LazyConnectionDataSourceProxy[]) shutdownLazyConnectionDataSourceProxys();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected LazyConnectionDataSourceProxy[] shutdownLazyConnectionDataSourceProxys() throws Exception {
		try {
			if (this.lazyConnectionDataSourceProxys != null) {
				for (int i = 0; i < this.lazyConnectionDataSourceProxys.length; i++) {
					LazyConnectionDataSourceProxy oldInstance = this.lazyConnectionDataSourceProxys[i];
					// oldInstance.close();
					this.lazyConnectionDataSourceProxys[i] = null;
				}
				//
				this.lazyConnectionDataSourceProxys = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownLazyConnectionDataSourceProxys()")
					.toString(), e);
			throw e;
		}
		return this.lazyConnectionDataSourceProxys;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected LazyConnectionDataSourceProxy[] restartLazyConnectionDataSourceProxys() throws Exception {
		try {
			if (this.lazyConnectionDataSourceProxys != null) {
				this.lazyConnectionDataSourceProxys = shutdownLazyConnectionDataSourceProxys();
				this.lazyConnectionDataSourceProxys = createLazyConnectionDataSourceProxys(this.targetDataSources);
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartLazyConnectionDataSourceProxys()")
					.toString(), e);
			throw e;
		}
		return this.lazyConnectionDataSourceProxys;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.lazyConnectionDataSourceProxys = createLazyConnectionDataSourceProxys(this.targetDataSources);
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.lazyConnectionDataSourceProxys = shutdownLazyConnectionDataSourceProxys();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.lazyConnectionDataSourceProxys = restartLazyConnectionDataSourceProxys();
	}

	@Override
	public LazyConnectionDataSourceProxy[] getObject() throws Exception {
		return lazyConnectionDataSourceProxys;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.lazyConnectionDataSourceProxys != null) ? this.lazyConnectionDataSourceProxys.getClass()
				: BasicDataSource[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
