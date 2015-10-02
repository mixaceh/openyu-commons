package org.openyu.commons.security.impl;

import java.util.Properties;

import org.openyu.commons.security.AuthKeyService;
import org.openyu.commons.security.AuthKeyServiceFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.thread.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 認證碼服務工廠
 */
public class AuthKeyServiceFactoryImpl extends BaseServiceSupporter implements AuthKeyServiceFactory {

	private static final long serialVersionUID = -1141743296689645013L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(AuthKeyServiceFactoryImpl.class);

	@Autowired
	@Qualifier("threadService")
	private transient ThreadService threadService;

	private Resource configLocation;

	private Properties properties;

	public AuthKeyServiceFactoryImpl() {

	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public AuthKeyService createInstance() {
		AuthKeyServiceImpl result = null;
		try {
			// 接外部設定檔
			Properties props = new Properties();
			PropertiesLoaderUtils.fillProperties(props, this.configLocation);
			if (this.properties != null) {
				props.putAll(this.properties);
			}
			//
			result = new AuthKeyServiceImpl();
			// TODO set props
			result.setThreadService(threadService);
			result.setCreateInstance(true);
			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createInstance()").toString(), e);
			result = (AuthKeyServiceImpl) shutdownInstance(result);
		}
		return result;
	}

	public AuthKeyService shutdownInstance(AuthKeyService authKeyService) {
		try {
			if (authKeyService instanceof AuthKeyService) {
				AuthKeyService oldInstance = authKeyService;
				//
				oldInstance.shutdown();
				authKeyService = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownInstance(AuthKeyService)").toString(),
					e);
		}
		return authKeyService;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {

	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}
}
