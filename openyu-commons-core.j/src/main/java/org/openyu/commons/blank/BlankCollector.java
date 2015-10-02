package org.openyu.commons.blank;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.collector.supporter.BaseCollectorSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Blank收集器
 */
// --------------------------------------------------
// jaxb
// --------------------------------------------------
@XmlRootElement(name = "blankCollector")
@XmlAccessorType(XmlAccessType.FIELD)
public final class BlankCollector extends BaseCollectorSupporter {

	private static final long serialVersionUID = 101853820339765181L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(BlankCollector.class);

	private static BlankCollector instance;

	// --------------------------------------------------
	// 此有系統值,只是為了轉出xml,並非給企劃編輯用
	// --------------------------------------------------
	
	public BlankCollector() {
		setId(BlankCollector.class.getName());
	}

	// --------------------------------------------------
	public synchronized static BlankCollector getInstance() {
		return getInstance(true);
	}

	/**
	 * 單例啟動
	 * 
	 * @return
	 */
	public synchronized static BlankCollector getInstance(boolean start) {
		if (instance == null) {
			instance = CollectorHelper.readFromSer(BlankCollector.class);
			// 此時有可能會沒有ser檔案,或舊的結構造成ex,只要再轉出一次ser,覆蓋原本ser即可
			if (instance == null) {
				instance = new BlankCollector();
			}
			//
			if (start) {
				// 啟動
				instance.start();
			}

			// 此有系統值,只是為了轉出xml,並非給企劃編輯用

		}
		return instance;
	}

	/**
	 * 單例關閉
	 * 
	 * @return
	 */
	public synchronized static BlankCollector shutdownInstance() {
		if (instance != null) {
			BlankCollector oldInstance = instance;
			//
			oldInstance.shutdown();
			instance = null;
		}
		return instance;
	}

	/**
	 * 單例重啟
	 * 
	 * @return
	 */
	public synchronized static BlankCollector restartInstance() {
		if (instance != null) {
			instance.restart();
		}
		return instance;
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
