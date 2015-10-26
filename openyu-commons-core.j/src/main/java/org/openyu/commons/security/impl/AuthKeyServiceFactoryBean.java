package org.openyu.commons.security.impl;

import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.security.AuthKeyService;
import org.openyu.commons.security.SecurityType;
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

	public final static String ALIVE_MILLS = "aliveMills";

	/**
	 * 預設key存活毫秒, 3分鐘
	 */
	public static final long DEFAULT_ALIVE_MILLS = 180 * 1000L;

	public final static String LISTEN_MILLS = "listenMills";
	/**
	 * 預設監聽毫秒, 10秒
	 */
	public static final long DEFAULT_LISTEN_MILLS = 10 * 1000L;

	/**
	 * security
	 */
	public final static String SECURITY = "security";

	/**
	 * 預設是否加密
	 */
	public static final boolean DEFAULT_SECURITY = true;

	public final static String SECURITY_TYPE = "securityType";

	/**
	 * 預設加密類型
	 */
	public static final SecurityType DEFAULT_SECURITY_TYPE = SecurityType.HmacSHA1;

	public final static String SECURITY_KEY = "securityKey";
	/**
	 * 預設加密key
	 */
	public static final String DEFAULT_SECURITY_KEY = "securityKey";

	/**
	 * 所有屬性
	 */
	public final static String[] ALL_PROPERTIES = { ALIVE_MILLS, LISTEN_MILLS, SECURITY, SECURITY_TYPE, SECURITY_KEY };

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
			//
			result.setApplicationContext(applicationContext);
			result.setBeanFactory(beanFactory);
			result.setResourceLoader(resourceLoader);
			//
			result.setCreateInstance(true);

			/**
			 * extendedProperties
			 */
			result.setAliveMills(extendedProperties.getLong(ALIVE_MILLS, DEFAULT_ALIVE_MILLS));
			result.setListenMills(extendedProperties.getLong(LISTEN_MILLS, DEFAULT_LISTEN_MILLS));
			// security
			result.setSecurity(extendedProperties.getBoolean(SECURITY, DEFAULT_SECURITY));
			String securityTypeValue = extendedProperties.getString(SECURITY_TYPE, DEFAULT_SECURITY_TYPE.getValue());
			SecurityType securityType = EnumHelper.valueOf(SecurityType.class, securityTypeValue);
			result.setSecurityType(securityType);
			result.setSecurityKey(extendedProperties.getString(SECURITY_KEY, DEFAULT_SECURITY_KEY));

			/**
			 * injectiion
			 */
			result.setThreadService(threadService);

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createService()").toString(), e);
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
