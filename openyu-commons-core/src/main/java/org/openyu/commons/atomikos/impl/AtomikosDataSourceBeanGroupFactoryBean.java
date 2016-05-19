package org.openyu.commons.atomikos.impl;

import org.openyu.commons.atomikos.supporter.AtomikosDataSourceBeanFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atomikos.jdbc.AtomikosDataSourceBean;

/**
 * AtomikosDataSourceBeanGroup工廠
 */
public final class AtomikosDataSourceBeanGroupFactoryBean
		extends AtomikosDataSourceBeanFactorySupporter<AtomikosDataSourceBean[]> {

	private static final long serialVersionUID = -4461697485043869301L;

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(AtomikosDataSourceBeanGroupFactoryBean.class);

	public final static String MAX_DATA_SOURCE_SIZE = "maxDataSourceSize";

	public static final int DEFAULT_MAX_DATA_SOURCE_SIZE = 1;

	private AtomikosDataSourceBean[] atomikosDataSourceBeans;

	public AtomikosDataSourceBeanGroupFactoryBean() {
	}

	public AtomikosDataSourceBean[] getAtomikosDataSourceBeans() {
		return atomikosDataSourceBeans;
	}

	public void setAtomikosDataSourceBeans(AtomikosDataSourceBean[] atomikosDataSourceBeans) {
		this.atomikosDataSourceBeans = atomikosDataSourceBeans;
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected AtomikosDataSourceBean[] createAtomikosDataSourceBeans() throws Exception {
		AtomikosDataSourceBean[] result = null;
		try {
			result = new AtomikosDataSourceBean[extendedProperties.getInt(MAX_DATA_SOURCE_SIZE,
					DEFAULT_MAX_DATA_SOURCE_SIZE)];
			//
			for (int i = 0; i < result.length; i++) {
				AtomikosDataSourceBean dataSource = createAtomikosDataSourceBean(i);
				result[i] = dataSource;
			}

		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createAtomikosDataSourceBeans()").toString(),
					e);
			try {
				result = (AtomikosDataSourceBean[]) shutdownAtomikosDataSourceBeans();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}

	protected AtomikosDataSourceBean shutdownAtomikosDataSourceBean() throws Exception {
		return null;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected AtomikosDataSourceBean[] shutdownAtomikosDataSourceBeans() throws Exception {
		try {
			if (this.atomikosDataSourceBeans != null) {
				for (int i = 0; i < this.atomikosDataSourceBeans.length; i++) {
					AtomikosDataSourceBean oldInstance = this.atomikosDataSourceBeans[i];
					oldInstance.close();
					this.atomikosDataSourceBeans[i] = null;
				}
				//
				this.atomikosDataSourceBeans = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownAtomikosDataSourceBeans()").toString(),
					e);
			throw e;
		}
		return this.atomikosDataSourceBeans;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected AtomikosDataSourceBean[] restartAtomikosDataSourceBeans() throws Exception {
		try {
			if (this.atomikosDataSourceBeans != null) {
				this.atomikosDataSourceBeans = shutdownAtomikosDataSourceBeans();
				this.atomikosDataSourceBeans = createAtomikosDataSourceBeans();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartAtomikosDataSourceBeans()").toString(),
					e);
			throw e;
		}
		return this.atomikosDataSourceBeans;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		if (this.atomikosDataSourceBeans != null) {
			LOGGER.info(new StringBuilder().append("Inject from setAtomikosDataSourceBeans()").toString());
		} else {
			LOGGER.info(new StringBuilder().append("Using createAtomikosDataSourceBeans()").toString());
			this.atomikosDataSourceBeans = createAtomikosDataSourceBeans();
		}
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.atomikosDataSourceBeans = shutdownAtomikosDataSourceBeans();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.atomikosDataSourceBeans = restartAtomikosDataSourceBeans();
	}

	@Override
	public AtomikosDataSourceBean[] getObject() throws Exception {
		return atomikosDataSourceBeans;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.atomikosDataSourceBeans != null) ? this.atomikosDataSourceBeans.getClass()
				: AtomikosDataSourceBean[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
