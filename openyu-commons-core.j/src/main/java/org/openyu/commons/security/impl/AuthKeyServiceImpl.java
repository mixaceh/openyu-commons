package org.openyu.commons.security.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.EncodingHelper;
import org.openyu.commons.security.AuthKey;
import org.openyu.commons.security.AuthKeyService;
import org.openyu.commons.security.SecurityType;
import org.openyu.commons.security.SecurityProcessor;
import org.openyu.commons.service.ShutdownCallback;
import org.openyu.commons.service.StartCallback;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.thread.ThreadHelper;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.supporter.BaseRunnableSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 認證碼服務
 */
public class AuthKeyServiceImpl extends BaseServiceSupporter implements AuthKeyService {

	private static final long serialVersionUID = 2538406837266783323L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(AuthKeyServiceImpl.class);

	/**
	 * 線程服務
	 */
	@Autowired
	@Qualifier("threadService")
	private transient ThreadService threadService;

	/**
	 * 預設key存活毫秒, 3分鐘
	 */
	public static final long DEFAULT_ALIVE_MILLS = 180 * 1000L;
	/**
	 * key存活毫秒
	 */
	private long aliveMills = DEFAULT_ALIVE_MILLS;

	/**
	 * 預設監聽毫秒, 10秒
	 */
	public static final long DEFAULT_LISTEN_MILLS = 10 * 1000L;
	/**
	 * 監聽毫秒
	 */
	private long listenMills = DEFAULT_LISTEN_MILLS;

	/**
	 * 安全性處理器
	 */
	private transient SecurityProcessor securityProcessor = new SecurityProcessorImpl();

	private Map<String, AuthKey> authKeys = new ConcurrentHashMap<String, AuthKey>();

	/**
	 * 檢查到期
	 */
	private transient CheckExpiredRunner checkExpiredRunner;

	public AuthKeyServiceImpl() {
		// 2015/09/19 多加callback方式
		addServiceCallback("StartCallbacker", new StartCallbacker());
		addServiceCallback("ShutdownCallbacker", new ShutdownCallbacker());
	}

	public void setThreadService(ThreadService threadService) {
		this.threadService = threadService;
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

	/**
	 * 內部啟動
	 */
	protected class StartCallbacker implements StartCallback {

		@Override
		public void doInAction() throws Exception {
			// threadService.submit(checkExpiredRunner);
			checkExpiredRunner = new CheckExpiredRunner(threadService);
			checkExpiredRunner.start();
		}

	}

	/**
	 * 內部關閉
	 */
	protected class ShutdownCallbacker implements ShutdownCallback {

		@Override
		public void doInAction() throws Exception {
			checkExpiredRunner.shutdown();
			authKeys.clear();
		}
	}

	@Override
	public long getAliveMills() {
		return aliveMills;
	}

	@Override
	public void setAliveMills(long aliveMills) {
		this.aliveMills = aliveMills;
	}

	@Override
	public long getListenMills() {
		return listenMills;
	}

	@Override
	public void setListenMills(long listenMills) {
		this.listenMills = listenMills;
	}

	/**
	 * 是否加密
	 * 
	 * @return
	 */
	@Override
	public boolean isSecurity() {
		return securityProcessor.isSecurity();
	}

	@Override
	public void setSecurity(boolean security) {
		securityProcessor.setSecurity(security);
	}

	/**
	 * 加密類別
	 * 
	 * @return
	 */
	@Override
	public SecurityType getSecurityType() {
		return securityProcessor.getSecurityType();
	}

	@Override
	public void setSecurityType(SecurityType securityType) {
		securityProcessor.setSecurityType(securityType);
	}

	/**
	 * 加密key
	 * 
	 * @return
	 */
	@Override
	public String getSecurityKey() {
		return securityProcessor.getSecurityKey();
	}

	@Override
	public void setSecurityKey(String securityKey) {
		securityProcessor.setSecurityKey(securityKey);
	}

	@Override
	public Map<String, AuthKey> getAuthKeys() {
		return authKeys;
	}

	@Override
	public void setAuthKeys(Map<String, AuthKey> authKeys) {
		this.authKeys = authKeys;
	}

	@Override
	public AuthKey createAuthKey() {
		AuthKey result = null;
		StringBuilder uuid = new StringBuilder();
		uuid.append(UUID.randomUUID().toString());
		// 不加密,長度=36
		StringBuilder buff = new StringBuilder();
		//
		if (isSecurity()) {
			byte[] encrypt = securityProcessor.encrypt(ByteHelper.toByteArray(uuid.toString()));
			//
			// 加密後,長度=32
			buff.append(EncodingHelper.encodeHex((encrypt)));
		} else {
			buff.append(uuid);
		}
		result = new AuthKeyImpl(buff.toString(), this.aliveMills);
		return result;
	}

	// /**
	// * 隨機key
	// *
	// * a hex string
	// *
	// * @return
	// */
	// public String randomKey() {
	// String result = null;
	// //
	// byte[] random = ByteHelper.toByteArray(secureRandom.nextInt());
	// byte[] buff = null;
	// if (isSecurity()) {
	// buff = securityProcessor.encrypt(random);
	// } else {
	// buff = random;
	// }
	// result = EncodingHelper.encodeHex(buff);
	// //
	// return result;
	// }

	@Override
	public AuthKey addAuthKey(String id, AuthKey authKey) {
		AuthKey result = null;
		if (id != null) {
			result = authKeys.put(id, authKey);
		}
		return result;
	}

	@Override
	public AuthKey getAuthKey(String authKeyId) {
		AuthKey result = null;
		if (authKeyId != null) {
			result = authKeys.get(authKeyId);
		}
		return result;
	}

	@Override
	public AuthKey removeAuthKey(String authKeyId) {
		AuthKey result = null;
		if (authKeyId != null) {
			result = authKeys.remove(authKeyId);
		}
		return result;
	}

	@Override
	public AuthKey removeAuthKey(AuthKey authKey) {
		AuthKey result = null;
		if (authKey != null) {
			result = removeAuthKey(authKey.getId());
		}
		return result;
	}

	@Override
	public boolean containsAuthKey(String authKeyId) {
		boolean result = false;
		if (authKeyId != null) {
			result = authKeys.containsKey(authKeyId);
		}
		return result;
	}

	@Override
	public boolean containsAuthKey(AuthKey authKey) {
		boolean result = false;
		if (authKey != null) {
			result = containsAuthKey(authKey.getId());
		}
		return result;
	}

	/**
	 * 取得認證key的數量
	 */
	@Override
	public int sizeOfAuthKey() {
		return authKeys.size();
	}

	/**
	 * 監聽檢查到期
	 */
	protected class CheckExpiredRunner extends BaseRunnableSupporter {

		public CheckExpiredRunner(ThreadService threadService) {
			super(threadService);
		}

		@Override
		protected void doRun() throws Exception {
			while (true) {
				try {
					if (isShutdown()) {
						break;
					}
					//
					checkExpired();
					ThreadHelper.sleep(listenMills);
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * 檢查到期,若到期則從mem移除
	 */
	protected void checkExpired() {
		List<AuthKey> removes = new ArrayList<AuthKey>();
		for (AuthKey authKey : authKeys.values()) {
			if (authKey.isExpired()) {
				removes.add(authKey);
			}
		}
		//
		for (AuthKey authKey : removes) {
			removeAuthKey(authKey);
		}
	}

}
