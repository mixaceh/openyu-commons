package org.openyu.commons.security.impl;

import org.openyu.commons.security.AuthKeyService;

public class AuthKeyServiceConfigurer extends AuthKeyServiceFactoryImpl {

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
	}
}
