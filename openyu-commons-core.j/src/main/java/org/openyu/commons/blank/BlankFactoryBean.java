package org.openyu.commons.blank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.service.supporter.BaseFactorySupporter;

/**
 * Blank服務
 */
public final class BlankFactoryBean extends BaseFactorySupporter<BlankService> {

	private static final long serialVersionUID = -1401366707657809071L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BlankFactoryBean.class);

	private BlankService blankService;

	public BlankFactoryBean() {
	}

	/**
	 * 建構
	 *
	 * @return
	 */
	public BlankService createInstance() {
		BlankServiceImpl result = null;
		try {
			result = new BlankServiceImpl();
			result.setCreateInstance(true);
			// 啟動
			result.start();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createInstance()").toString(), e);
			result = (BlankServiceImpl) shutdownInstance(result);
		}
		return result;
	}

	/**
	 * 關閉
	 * 
	 * @return
	 */
	public BlankService shutdownInstance(BlankService blankService) {
		try {
			if (blankService instanceof BlankService) {
				BlankService oldInstance = blankService;
				//
				oldInstance.shutdown();
				blankService = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownInstance(BlankService)").toString(),
					e);
		}
		return blankService;
	}

	/**
	 * 重啟
	 * 
	 * @return
	 */
	public BlankService restartInstance(BlankService blankService) {
		try {
			if (blankService instanceof BlankService) {
				BlankService oldInstance = blankService;
				oldInstance.restart();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartInstance(BlankService)").toString(), e);
		}
		return blankService;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		// blankService.start();
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		blankService.shutdown();
	}

	@Override
	public BlankService getObject() throws Exception {
		return blankService;
	}

	@Override
	public Class<? extends BlankService> getObjectType() {
		return ((this.blankService != null) ? this.blankService.getClass() : BlankService.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
