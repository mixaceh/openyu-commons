package org.openyu.commons.service;

import org.openyu.commons.model.BaseModel;

/**
 * 服務類,以下3種建構方式
 * 
 * 1.spring 配置建構
 * 
 * 2.getInstance() 單例建構
 * 
 * 3.createInstance() 建構
 */
public interface BaseService extends BaseModel, ServiceLifecycle {

	// /**
	// * Gets the message.
	// *
	// * @param key
	// * the key
	// * @param locale
	// * the locale
	// * @return the message
	// */
	// String getMessage(String key, Locale locale);
	//
	// /**
	// * Gets the message.
	// *
	// * @param key
	// * the key
	// * @param params
	// * the params
	// * @param locale
	// * the locale
	// * @return the message
	// */
	// String getMessage(String key, Object[] params, Locale locale);

	// --------------------------------------------------
	// ServiceLifecycle
	// --------------------------------------------------
	/**
	 * 啟動
	 */
	void start() throws Exception;

	/**
	 * 是否啟動
	 * 
	 * @return
	 */
	boolean isStarted();

	/**
	 * 關閉
	 */
	void shutdown() throws Exception;

	/**
	 * 是否關閉
	 * 
	 * @return
	 */
	boolean isShutdown();

	/**
	 * 重啟
	 */
	void restart() throws Exception;
}
