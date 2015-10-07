package org.openyu.commons.security.impl;

import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.security.AuthKeyService;
import org.openyu.commons.security.SecurityType;
import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.openyu.commons.service.supporter.BaseServiceFactorySupporter;
import org.openyu.commons.thread.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 認證碼服務工廠
 */
public final class AuthKeyServiceFactoryBean<T extends AuthKeyService>
		extends BaseServiceFactorySupporter<AuthKeyService> {

	private static final long serialVersionUID = -5900541933254854765L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(AuthKeyServiceFactoryBean.class);

	@Autowired
	@Qualifier("threadService")
	private transient ThreadService threadService;

	public AuthKeyServiceFactoryBean() {
	}

	public void setThreadService(ThreadService threadService) {
		this.threadService = threadService;
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected AuthKeyService createService() throws Exception {
		AuthKeyServiceImpl result = null;
		try {
			result = new AuthKeyServiceImpl();
			result.setCreateInstance(true);

			/**
			 * extendedProperties
			 */
			result.setAliveMills(extendedProperties.getLong("aliveMills", AuthKeyServiceImpl.DEFAULT_ALIVE_MILLS));
			result.setListenMills(extendedProperties.getLong("listenMills", AuthKeyServiceImpl.DEFAULT_LISTEN_MILLS));
			//
			result.setSecurity(extendedProperties.getBoolean("security", SecurityProcessorImpl.DEFAULT_SECURITY));
			//
			String securityTypeValue = extendedProperties.getString("securityType",
					SecurityProcessorImpl.DEFAULT_SECURITY_TYPE.getValue());
			SecurityType securityType = EnumHelper.valueOf(SecurityType.class, securityTypeValue);
			result.setSecurityType(securityType);
			//
			result.setSecurityKey(
					extendedProperties.getString("securityKey", SecurityProcessorImpl.DEFAULT_SECURITY_KEY));

			/**
			 * injectiion
			 */
			result.setThreadService(threadService);

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createInstance()").toString(), e);
			try {
				result = (AuthKeyServiceImpl) shutdownService();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}
}
