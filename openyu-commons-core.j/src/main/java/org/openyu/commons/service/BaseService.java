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
public interface BaseService extends BaseModel {

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

	/**
	 * 啟動
	 */
	void start() throws Exception;

	/**
	 * 關閉
	 */
	void shutdown() throws Exception;

	/**
	 * 重啟
	 */
	void restart() throws Exception;
}
