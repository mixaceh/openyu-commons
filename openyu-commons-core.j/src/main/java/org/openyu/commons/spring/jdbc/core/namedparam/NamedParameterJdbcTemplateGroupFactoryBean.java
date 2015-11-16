package org.openyu.commons.spring.jdbc.core.namedparam;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class NamedParameterJdbcTemplateGroupFactoryBean
		extends BaseFactorySupporter<NamedParameterJdbcTemplate[]> {

	private static final long serialVersionUID = 1560497754167571755L;

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(NamedParameterJdbcTemplateGroupFactoryBean.class);

	private DataSource[] targetDataSources;

	private NamedParameterJdbcTemplate[] namedParameterJdbcTemplates;

	public NamedParameterJdbcTemplateGroupFactoryBean(DataSource[] targetDataSources) {
		this.targetDataSources = targetDataSources;
	}

	public NamedParameterJdbcTemplateGroupFactoryBean() {
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
	protected NamedParameterJdbcTemplate[] createNamedParameterJdbcTemplates(DataSource[] dataSources)
			throws Exception {
		AssertHelper.notNull(dataSources, "The DataSources must not be null");
		//
		NamedParameterJdbcTemplate[] result = null;
		try {
			//
			result = new NamedParameterJdbcTemplate[dataSources.length];
			for (int i = 0; i < dataSources.length; i++) {
				DataSource targetDataSource = dataSources[i];
				NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
						targetDataSource);
				result[i] = namedParameterJdbcTemplate;
			}
		} catch (Exception e) {
			LOGGER.error(
					new StringBuilder("Exception encountered during createNamedParameterJdbcTemplates()").toString(),
					e);
			try {
				result = (NamedParameterJdbcTemplate[]) shutdownNamedParameterJdbcTemplates();
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
	protected NamedParameterJdbcTemplate[] shutdownNamedParameterJdbcTemplates() throws Exception {
		try {
			if (this.namedParameterJdbcTemplates != null) {
				for (int i = 0; i < this.namedParameterJdbcTemplates.length; i++) {
					NamedParameterJdbcTemplate oldInstance = this.namedParameterJdbcTemplates[i];
					// oldInstance.close();
					this.namedParameterJdbcTemplates[i] = null;
				}
				//
				this.namedParameterJdbcTemplates = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownNamedParameterJdbcTemplates()")
					.toString(), e);
			throw e;
		}
		return this.namedParameterJdbcTemplates;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected NamedParameterJdbcTemplate[] restartNamedParameterJdbcTemplates() throws Exception {
		try {
			if (this.namedParameterJdbcTemplates != null) {
				this.namedParameterJdbcTemplates = shutdownNamedParameterJdbcTemplates();
				this.namedParameterJdbcTemplates = createNamedParameterJdbcTemplates(this.targetDataSources);
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartNamedParameterJdbcTemplates()")
					.toString(), e);
			throw e;
		}
		return this.namedParameterJdbcTemplates;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.namedParameterJdbcTemplates = createNamedParameterJdbcTemplates(this.targetDataSources);
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.namedParameterJdbcTemplates = shutdownNamedParameterJdbcTemplates();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.namedParameterJdbcTemplates = restartNamedParameterJdbcTemplates();
	}

	@Override
	public NamedParameterJdbcTemplate[] getObject() throws Exception {
		return namedParameterJdbcTemplates;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.namedParameterJdbcTemplates != null) ? this.namedParameterJdbcTemplates.getClass()
				: BasicDataSource[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
