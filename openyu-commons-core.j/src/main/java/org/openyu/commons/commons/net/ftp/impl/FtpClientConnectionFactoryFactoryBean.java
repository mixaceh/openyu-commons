package org.openyu.commons.commons.net.ftp.impl;

import org.openyu.commons.commons.net.ftp.FtpClientConnectionFactory;
import org.openyu.commons.service.supporter.BaseServiceFactoryBeanSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FtpClientConnectionFactory工廠
 */
public final class FtpClientConnectionFactoryFactoryBean<T extends FtpClientConnectionFactory>
		extends BaseServiceFactoryBeanSupporter<FtpClientConnectionFactory> {

	private static final long serialVersionUID = -1011527154648467009L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(FtpClientConnectionFactoryFactoryBean.class);

	/**
	 * 所有屬性
	 */
	public static final String[] ALL_PROPERTIES = {};

	public FtpClientConnectionFactoryFactoryBean() {
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected FtpClientConnectionFactory createService() throws Exception {
		FtpClientConnectionFactoryImpl result = null;
		try {
			result = new FtpClientConnectionFactoryImpl();
			//
			result.setApplicationContext(applicationContext);
			result.setBeanFactory(beanFactory);
			result.setResourceLoader(resourceLoader);
			//
			result.setCreateInstance(true);
			/**
			 * extendedProperties
			 */
			// TODO
			
			/**
			 * injectiion
			 */

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createService()").toString(), e);
			try {
				result = (FtpClientConnectionFactoryImpl) shutdownService();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}
}
