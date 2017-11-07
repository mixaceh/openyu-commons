package org.openyu.commons.spring.jdbc.core;

import javax.sql.DataSource;

import org.openyu.commons.service.supporter.BaseFactoryBeanSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateGroupFactoryBean extends BaseFactoryBeanSupporter<JdbcTemplate[]> {

	private static final long serialVersionUID = 1560497754167571755L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(JdbcTemplateGroupFactoryBean.class);

	private DataSource[] dataSources;

	private JdbcTemplate[] jdbcTemplates;

	public JdbcTemplateGroupFactoryBean(DataSource[] dataSources) {
		setDataSources( dataSources);
	}

	public JdbcTemplateGroupFactoryBean() {
	}

	public DataSource[] getDataSources() {
		return dataSources;
	}

	public void setDataSources(DataSource[] dataSources) {
		AssertHelper.notNull(dataSources, "The DataSources must not be null");
		//
		this.dataSources = dataSources;
	}

	public JdbcTemplate[] getJdbcTemplates() {
		return jdbcTemplates;
	}

	public void setJdbcTemplates(JdbcTemplate[] jdbcTemplates) {
		this.jdbcTemplates = jdbcTemplates;
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected JdbcTemplate[] createJdbcTemplates(DataSource[] dataSources) throws Exception {
		AssertHelper.notNull(dataSources, "The DataSources must not be null");
		//
		JdbcTemplate[] result = null;
		try {
			int size = dataSources.length;
			result = new JdbcTemplate[size];
			for (int i = 0; i < size; i++) {
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSources[i]);
				result[i] = jdbcTemplate;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createJdbcTemplates()").toString(), e);
			try {
				result = (JdbcTemplate[]) shutdownJdbcTemplates();
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
	protected JdbcTemplate[] shutdownJdbcTemplates() throws Exception {
		try {
			if (this.jdbcTemplates != null) {
				for (int i = 0; i < this.jdbcTemplates.length; i++) {
					JdbcTemplate oldInstance = this.jdbcTemplates[i];
					// oldInstance.close();
					this.jdbcTemplates[i] = null;
				}
				//
				this.jdbcTemplates = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownJdbcTemplates()").toString(), e);
			throw e;
		}
		return this.jdbcTemplates;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected JdbcTemplate[] restartJdbcTemplates() throws Exception {
		try {
			if (this.jdbcTemplates != null) {
				this.jdbcTemplates = shutdownJdbcTemplates();
				this.jdbcTemplates = createJdbcTemplates(this.dataSources);
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartJdbcTemplates()").toString(), e);
			throw e;
		}
		return this.jdbcTemplates;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		if (this.jdbcTemplates != null) {
			LOGGER.info(new StringBuilder().append("Inject from setJdbcTemplates()").toString());
		} else {
			AssertHelper.notNull(this.dataSources, "Property 'dataSources' is required");
			//
			LOGGER.info(new StringBuilder().append("Using createJdbcTemplates()").toString());
			this.jdbcTemplates = createJdbcTemplates(this.dataSources);
		}
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.jdbcTemplates = shutdownJdbcTemplates();
	}

	/**
	 * 內部重啟
	 */
	@Override
	protected void doRestart() throws Exception {
		this.jdbcTemplates = restartJdbcTemplates();
	}

	@Override
	public JdbcTemplate[] getObject() throws Exception {
		return jdbcTemplates;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.jdbcTemplates != null) ? this.jdbcTemplates.getClass() : JdbcTemplate[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
