package org.openyu.commons.security.impl;

import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.security.AuthKeyService;
import org.openyu.commons.security.SecurityType;
import org.openyu.commons.service.supporter.BaseFactorySupporter;
import org.openyu.commons.thread.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 認證碼服務工廠
 */
public final class AuthKeyFactoryBean<T> extends BaseFactorySupporter<AuthKeyService> {

	private static final long serialVersionUID = -5900541933254854765L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(AuthKeyFactoryBean.class);

	@Autowired
	@Qualifier("threadService")
	private transient ThreadService threadService;

	private AuthKeyService authKeyService;

	public AuthKeyFactoryBean() {
	}

	public void setThreadService(ThreadService threadService) {
		this.threadService = threadService;
	}

	/**
	 * 建構
	 * 
	 * @return
	 */
	protected AuthKeyService createInstance() throws Exception {
		AuthKeyServiceImpl result = null;
		try {
			result = new AuthKeyServiceImpl();
			result.setCreateInstance(true);

			// 1.extendedProperties
			result.setAliveMills(extendedProperties.getLong("org.openyu.commons.security.AuthKeyService.aliveMills",
					AuthKeyServiceImpl.DEFAULT_ALIVE_MILLS));
			result.setListenMills(extendedProperties.getLong("org.openyu.commons.security.AuthKeyService.listenMills",
					AuthKeyServiceImpl.DEFAULT_LISTEN_MILLS));
			result.setSecurity(extendedProperties.getBoolean("org.openyu.commons.security.AuthKeyService.security",
					SecurityProcessorImpl.DEFAULT_SECURITY));
			//
			SecurityType securityType = EnumHelper.valueOf(SecurityType.class,
					extendedProperties.getString("org.openyu.commons.security.AuthKeyService.securityType",
							SecurityProcessorImpl.DEFAULT_SECURITY_TYPE.getValue()));
			result.setSecurityType(securityType);
			result.setSecurityKey(extendedProperties.getString("org.openyu.commons.security.AuthKeyService.securityKey",
					SecurityProcessorImpl.DEFAULT_SECURITY_KEY));

			// 2. injectiion
			result.setThreadService(threadService);

			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createInstance()").toString(), e);
			try {
				result = (AuthKeyServiceImpl) shutdownInstance();
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
	protected AuthKeyService shutdownInstance() throws Exception {
		try {
			if (this.authKeyService != null) {
				AuthKeyService oldInstance = this.authKeyService;
				oldInstance.shutdown();
				this.authKeyService = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownInstance()").toString(), e);
			throw e;
		}
		return this.authKeyService;

	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected AuthKeyService restartInstance() throws Exception {
		try {
			if (this.authKeyService != null) {
				AuthKeyService oldInstance = this.authKeyService;
				oldInstance.restart();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartInstance()").toString(), e);
			throw e;
		}
		return this.authKeyService;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		this.authKeyService = createInstance();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.authKeyService = shutdownInstance();
	}

	@Override
	protected void doRestart() throws Exception {
		this.authKeyService = restartInstance();
	}

	@Override
	public AuthKeyService getObject() throws Exception {
		return authKeyService;
	}

	@Override
	public Class<? extends AuthKeyService> getObjectType() {
		return ((this.authKeyService != null) ? this.authKeyService.getClass() : AuthKeyService.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
