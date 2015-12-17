package org.openyu.commons.bootstrap;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.bootstrap.supporter.BootstrapSupporter;
import org.openyu.commons.collector.BaseCollector;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.collector.CollectorStarter;
import org.openyu.commons.collector.ex.CollectorException;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.CollectionHelper;

/**
 * collector啟動器
 * 
 * 1.xml -> serial, main(), 把宣告在applicationContext-*-collector.xml中的collector,
 * 轉換成ser (writeToSerFromXml)
 * 
 * 2.serial -> object, readFromSer(),
 * 讀取宣告在applicationContext-*-collector.xml中的collector, 轉換成object (readFromSer)
 * 
 * 3.META-INF/applicationContext-*-collector.xml
 */
public final class CollectorBootstrap extends BootstrapSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(CollectorBootstrap.class);

	/**
	 * 所有collector啟動器
	 */
	private static Map<String, CollectorStarter> collectorStarters = new LinkedHashMap<String, CollectorStarter>();

	/**
	 * 從main直接啟動,並不是在web下啟動.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		try {
			// 建構applicationContext
			buildApplicationContext(args);
			// 建構collector啟動器
			buildCollectorStarters();
			//
			doStart();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during main()").toString(), e);
			//結束
			System.exit(0);
		}
	}

	/**
	 * 建構applicationContext
	 * 
	 * @param configLocations
	 */
	protected static void buildApplicationContext(String[] configLocations) {
		applicationContext = new ClassPathXmlApplicationContext(configLocations);
		//
		AssertHelper.notNull(applicationContext,
				new StringBuilder().append("The ApplicationContext must not be null").toString());
	}

	/**
	 * 建構collector啟動器
	 */
	protected static void buildCollectorStarters() {
		collectorStarters = applicationContext.getBeansOfType(CollectorStarter.class);
		//
		AssertHelper.notEmpty(collectorStarters, "The CollectorStarters must not be null or empty");
	}

	/**
	 * 從web上啟動,或其他已經建構的applicationContext來啟動.
	 *
	 * @param applicationContext
	 *            the application context
	 */
	public static void start(ApplicationContext applicationContext) {
		AssertHelper.notNull(applicationContext, "The ApplicationContext must not be null");
		//
		try {
			CollectorBootstrap.applicationContext = applicationContext;
			// 建構collector啟動器
			buildCollectorStarters();
			//
			doStart();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during start()").toString(), e);
		}
	}

	/**
	 * 內部用啟動
	 * 
	 * @throws Exception
	 */
	protected static void doStart() throws Exception {
		// 總數
		int count = 0;
		// 執行數
		int runCount = 0;
		// 執行數花費的時間總計
		long sumTime = 0L;
		// 平均時間
		long avgTime = 0L;
		// 最佳時間
		long bestTime = Long.MAX_VALUE;
		//
		for (CollectorStarter collectorStarter : collectorStarters.values()) {
			List<String> collectorNames = collectorStarter.getNames();
			if (CollectionHelper.isEmpty(collectorNames)) {
				continue;
			}
			//
			for (String collectorName : collectorNames) {
				// xml -> serial
				String result = null;
				try {
					long begTime = System.currentTimeMillis();
					BaseCollector collector = checkBaseCollector(collectorName);
					if (collector == null) {
						continue;
					}
					//
					result = CollectorHelper.writeToSerFromXml(collector.getClass());
					if (result == null) {
						throw new CollectorException(collectorName + " Not write to ser");
					}
					long endTime = System.currentTimeMillis();
					long durTime = endTime - begTime;
					//
					runCount++;
					sumTime += durTime;
					bestTime = Math.min(bestTime, durTime);
					LOGGER.info("#." + (count + 1) + " Write to " + result + " success in " + durTime + " ms");

				} catch (NullPointerException ex) {
					LOGGER.info("#." + (count + 1) + " " + ex.getMessage());
				} catch (Exception ex) {
					LOGGER.info("#." + (count + 1) + " Write " + collectorName + " failed", ex);
				}
				count++;
			}
		}
		avgTime = (runCount == 0 ? 0 : sumTime / runCount);
		bestTime = (bestTime == Long.MAX_VALUE ? 0 : bestTime);
		//
		String msgPattern = "Total files is [{0}], [{1}] xml files had been successfully writed to ser file in {2} ms";
		StringBuilder msg = new StringBuilder(MessageFormat.format(msgPattern, count, runCount, sumTime));
		LOGGER.info(msg.toString());
		//
		msgPattern = "Best time: {0} ms, average time: {1} ms";
		msg = new StringBuilder(MessageFormat.format(msgPattern, bestTime, avgTime));
		LOGGER.info(msg.toString());
	}

	/**
	 * 檢查collector名稱, 是否可以建構
	 * 
	 * @param collectorName
	 * @return
	 */
	protected static BaseCollector checkBaseCollector(String collectorName) {
		BaseCollector result = null;
		if (StringHelper.isBlank(collectorName)) {
			LOGGER.error("[" + collectorName + "] Is null");
			return result;
		}
		Class<?> clazz = ClassHelper.forName(collectorName);
		if (clazz == null) {
			LOGGER.error("Can not forName(" + collectorName + ")");
			return result;
		}
		Object object = ClassHelper.newInstance(clazz);
		if (!(object instanceof BaseCollector)) {
			LOGGER.error("Can not newInstance(" + collectorName + "). It is not instance of BaseCollector");
			return result;
		}
		//
		result = (BaseCollector) object;
		return result;
	}

	/**
	 * ser -> collector
	 * 
	 * @param configLocations
	 * @return
	 */
	public static List<BaseCollector> readFromSer(String[] configLocations) {
		List<BaseCollector> result = new LinkedList<BaseCollector>();
		// 建構applicationContext
		buildApplicationContext(configLocations);
		// 建構collector啟動器
		buildCollectorStarters();

		// 總數
		int count = 0;
		// 執行數
		int runCount = 0;
		// 執行數花費的時間總計
		long sumTime = 0L;
		// 平均時間
		long avgTime = 0L;
		// 最佳時間
		long bestTime = Long.MAX_VALUE;
		//
		for (CollectorStarter collectorStarter : collectorStarters.values()) {
			List<String> collectorNames = collectorStarter.getNames();
			if (CollectionHelper.isEmpty(collectorNames)) {
				continue;
			}
			//
			for (String collectorName : collectorNames) {
				try {
					long begTime = System.currentTimeMillis();
					BaseCollector collector = checkBaseCollector(collectorName);
					if (collector == null) {
						continue;
					}
					// serial -> object
					collector = CollectorHelper.readFromSer(collector.getClass());
					if (collector == null) {
						throw new CollectorException(collectorName + " Not read from ser");
					}
					long endTime = System.currentTimeMillis();
					long durTime = endTime - begTime;
					//
					runCount++;
					sumTime += durTime;
					bestTime = Math.min(bestTime, durTime);
					result.add(collector);
					LOGGER.info("#." + (count + 1) + " Create " + collector.getClass().getSimpleName() + " success in "
							+ durTime + " ms");
				} catch (NullPointerException ex) {
					LOGGER.info("#." + (count + 1) + " " + ex.getMessage());
				} catch (Exception ex) {
					LOGGER.info("#." + (count + 1) + " Create " + collectorName + " failed", ex);
				}
				//
				count++;
			}
		}
		avgTime = (runCount == 0 ? 0 : sumTime / runCount);
		bestTime = (bestTime == Long.MAX_VALUE ? 0 : bestTime);
		//
		String msgPattern = "Total files is [{0}], [{1}] ser files had been successfully read to object in {2} ms";
		StringBuilder msg = new StringBuilder(MessageFormat.format(msgPattern, count, runCount, sumTime));
		LOGGER.info(msg.toString());
		//
		msgPattern = "Best time: {0} ms, average time: {1} ms";
		msg = new StringBuilder(MessageFormat.format(msgPattern, bestTime, avgTime));
		LOGGER.info(msg.toString());
		//
		return result;
	}
}
