package org.openyu.commons.security;

import java.util.Map;

import org.openyu.commons.service.BaseService;

/**
 * 認證碼服務
 */
public interface AuthKeyService extends BaseService {

	/**
	 * key存活毫秒
	 * 
	 * @return
	 */
	long getAliveMills();

	void setAliveMills(long aliveMills);

	/**
	 * 監聽毫秒
	 * 
	 * @return
	 */
	long getListenMills();

	void setListenMills(long listenMills);

	/**
	 * 是否加密
	 * 
	 * @return
	 */
	boolean isSecurity();

	void setSecurity(boolean security);

	/**
	 * 加密類別
	 * 
	 * @return
	 */
	SecurityType getSecurityType();

	void setSecurityType(SecurityType securityType);

	/**
	 * 加密key
	 * 
	 * @return
	 */
	String getSecurityKey();

	void setSecurityKey(String securityKey);

	/**
	 * 認證key
	 * 
	 * <8a1486892898e523745e6471c7653363,AuthKey> <id,AuthKey>
	 * 
	 * @return
	 */
	Map<String, AuthKey> getAuthKeys();

	void setAuthKeys(Map<String, AuthKey> authKeys);

	/**
	 * 建構認證key
	 * 
	 * @return
	 */
	AuthKey createAuthKey();

	/**
	 * 加認證key到mem
	 * 
	 * @param id
	 *            自行定義的id
	 * @param authKey
	 * @return
	 */
	AuthKey addAuthKey(String id, AuthKey authKey);

	/**
	 * 取得認證key
	 * 
	 * @param authKeyId
	 * @return
	 */
	AuthKey getAuthKey(String authKeyId);

	/**
	 * 移除認證key
	 * 
	 * @param authKeyId
	 * @return
	 */
	AuthKey removeAuthKey(String authKeyId);

	/**
	 * 移除認證key
	 * 
	 * @param authKey
	 * @return
	 */
	AuthKey removeAuthKey(AuthKey authKey);

	/**
	 * 認證key是否存在
	 * 
	 * @param authKeyId
	 * @return
	 */
	boolean containsAuthKey(String authKeyId);

	/**
	 * 認證key是否存在
	 * 
	 * @param authKey
	 * @return
	 */
	boolean containsAuthKey(AuthKey authKey);

	/**
	 * 取得認證key的數量
	 * 
	 * @return
	 */
	int sizeOfAuthKey();
}
