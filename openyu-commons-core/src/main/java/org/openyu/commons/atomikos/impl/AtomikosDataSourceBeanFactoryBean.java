package org.openyu.commons.atomikos.impl;

import org.openyu.commons.atomikos.supporter.AtomikosDataSourceBeanFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atomikos.jdbc.AtomikosDataSourceBean;

/**
 * AtomikosDataSourceBean工廠
 */
public final class AtomikosDataSourceBeanFactoryBean
		extends AtomikosDataSourceBeanFactorySupporter<AtomikosDataSourceBean> {

	private static final long serialVersionUID = 8946932683059948719L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(AtomikosDataSourceBeanFactoryBean.class);

	private AtomikosDataSourceBean atomikosDataSourceBean;

	public AtomikosDataSourceBeanFactoryBean() {
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected AtomikosDataSourceBean shutdownAtomikosDataSourceBean() throws Exception {
		try {
			if (this.atomikosDataSourceBean != null) {
				AtomikosDataSourceBean oldInstance = this.atomikosDataSourceBean;
				oldInstance.close();
				this.atomikosDataSourceBean = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownAtomikosDataSourceBean()").toString(),
					e);
			throw e;
		}
		return this.atomikosDataSourceBean;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected AtomikosDataSourceBean restartAtomikosDataSourceBean() throws Exception {
		try {
			if (this.atomikosDataSourceBean != null) {
				this.atomikosDataSourceBean = shutdownAtomikosDataSourceBean();
				this.atomikosDataSourceBean = createAtomikosDataSourceBean(0);
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartAtomikosDataSourceBean()").toString(),
					e);
			throw e;
		}
		return this.atomikosDataSourceBean;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.atomikosDataSourceBean = createAtomikosDataSourceBean(0);
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.atomikosDataSourceBean = shutdownAtomikosDataSourceBean();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.atomikosDataSourceBean = restartAtomikosDataSourceBean();
	}

	@Override
	public AtomikosDataSourceBean getObject() throws Exception {
		return atomikosDataSourceBean;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.atomikosDataSourceBean != null) ? this.atomikosDataSourceBean.getClass()
				: AtomikosDataSourceBean.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
