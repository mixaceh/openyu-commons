package org.openyu.commons.security.impl;

import org.openyu.commons.security.AuthKeyService;
import org.springframework.beans.factory.FactoryBean;

/**
 * 認證碼服務設定器
 */
public class AuthKeyServiceConfigurer extends AuthKeyServiceFactoryImpl  implements FactoryBean{

	private static final long serialVersionUID = 9107182237672400201L;

	private transient AuthKeyService authKeyService;

	public AuthKeyServiceConfigurer() {

	}

	public AuthKeyService getAuthKeyService() {
		return authKeyService;
	}

	public void setAuthKeyService(AuthKeyService authKeyService) {
		this.authKeyService = authKeyService;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		super.doStart();
		if (authKeyService == null) {
			this.authKeyService = createInstance();
		}
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		super.doShutdown();
		if (authKeyService != null) {
			shutdownInstance(authKeyService);
		}
	}

	@Override
	public Object getObject() throws Exception {
		// TODO Auto-generated method stub
		return authKeyService;
	}

	@Override
	public Class getObjectType() {
		// TODO Auto-generated method stub
		return AuthKeyService.class;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}
}
