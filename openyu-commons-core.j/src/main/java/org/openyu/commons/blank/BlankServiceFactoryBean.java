package org.openyu.commons.blank;

import org.openyu.commons.service.supporter.BaseServiceFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BlankService工廠
 */
public final class BlankServiceFactoryBean<T extends BlankService> extends BaseServiceFactorySupporter<BlankService> {

	private static final long serialVersionUID = -1401366707657809071L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankServiceFactoryBean.class);

	public BlankServiceFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected BlankService createService() throws Exception {
		BlankServiceImpl result = null;
		try {
			result = new BlankServiceImpl();
			//
			result.setApplicationContext(applicationContext);
			result.setBeanFactory(beanFactory);
			result.setResourceLoader(resourceLoader);
			//
			result.setCreateInstance(true);

			/**
			 * extendedProperties
			 */
			// extendedProperties.getLong("aliveMills");

			/**
			 * injectiion
			 */

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createService()").toString(), e);
			try {
				result = (BlankServiceImpl) shutdownService();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}
}
