package org.openyu.commons.commons.dbcp;

import org.apache.commons.dbcp.BasicDataSource;
import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BasicDataSource工廠
 */
public final class BasicDataSourceFactoryBean<T extends BasicDataSource> extends BaseFactorySupporter<BasicDataSource> {

	private static final long serialVersionUID = 5865182754049441001L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BasicDataSourceFactoryBean.class);

	private T basicDataSource;

	public BasicDataSourceFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T createBasicDataSource() throws Exception {
		BasicDataSource result = null;
		try {
			result = new BasicDataSource();

			/**
			 * extendedProperties
			 */
			// extendedProperties.getLong("aliveMills");

		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createBasicDataSource()").toString(), e);
			try {
				result = (BasicDataSource) shutdownBasicDataSource();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return (T) result;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected T shutdownBasicDataSource() throws Exception {
		try {
			if (this.basicDataSource != null) {
				T oldInstance = this.basicDataSource;
				oldInstance.close();
				this.basicDataSource = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownBasicDataSource()").toString(), e);
			throw e;
		}
		return this.basicDataSource;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected T restartBasicDataSource() throws Exception {
		try {
			if (this.basicDataSource != null) {
				this.basicDataSource = shutdownBasicDataSource();
				this.basicDataSource = createBasicDataSource();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartService()").toString(), e);
			throw e;
		}
		return this.basicDataSource;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.basicDataSource = createBasicDataSource();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.basicDataSource = shutdownBasicDataSource();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.basicDataSource = restartBasicDataSource();
	}

	@Override
	public BasicDataSource getObject() throws Exception {
		return basicDataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.basicDataSource != null) ? this.basicDataSource.getClass() : BasicDataSource.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
