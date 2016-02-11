package org.openyu.commons.bootstrap;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.bootstrap.supporter.BootstrapSupporter;
import org.openyu.commons.editor.BaseEditor;
import org.openyu.commons.editor.EditorStarter;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.CollectionHelper;

/**
 * editor啟動器
 * 
 * 1.excel -> xml, main(), 把宣告在applicationContext-*-editor.xml中的editor, 轉換成xml
 * (writeToXml)
 * 
 * 2.xml -> object, readFromXml(), 讀取宣告在applicationContext-*-editor.xml中的editor,
 * 轉換成object (readFromXml)
 * 
 * 3.META-INF/applicationContext-*-editor.xml
 */
public final class EditorBootstrap extends BootstrapSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(EditorBootstrap.class);

	/**
	 * 所有editor啟動器
	 */
	private static Map<String, EditorStarter> editorStarters = new LinkedHashMap<String, EditorStarter>();

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
			// 建構editor啟動器
			buildEditorStarters();
			//
			doStart();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during main()").toString(), e);
			// 結束
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
	protected static void buildEditorStarters() {
		editorStarters = applicationContext.getBeansOfType(EditorStarter.class);
		//
		AssertHelper.notEmpty(editorStarters, "The EditorStarters must not be null or empty");
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
			EditorBootstrap.applicationContext = applicationContext;
			// 建構editor啟動器
			buildEditorStarters();
			//
			doStart();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during start()").toString(), e);
		}
	}

	/**
	 * 內部用啟動.
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
		for (EditorStarter editorStarter : editorStarters.values()) {
			List<String> editorNames = editorStarter.getNames();
			if (CollectionHelper.isEmpty(editorNames)) {
				continue;
			}
			//
			for (String editorName : editorNames) {
				// excel -> xml
				String result = null;
				try {
					long begTime = System.currentTimeMillis();
					BaseEditor editor = checkBaseEditor(editorName);
					if (editor == null) {
						continue;
					}
					//
					result = editor.writeToXml();
					if (result == null) {
						throw new NullPointerException(editorName + " Not write to xml");
					}
					long endTime = System.currentTimeMillis();
					long durTime = endTime - begTime;
					//
					runCount++;
					sumTime += durTime;
					bestTime = Math.min(bestTime, durTime);
					LOGGER.info("[" + count + "] Write to " + result + " success in " + durTime + " ms");

				} catch (NullPointerException ex) {
					LOGGER.info("[" + count + "] " + ex.getMessage());
				} catch (Exception ex) {
					LOGGER.info("[" + count + "] Write " + editorName + " failed", ex);
				}
				count++;
			}
		}
		avgTime = (runCount == 0 ? 0 : sumTime / runCount);
		bestTime = (bestTime == Long.MAX_VALUE ? 0 : bestTime);
		//
		String msgPattern = "Total files is [{0}], [{1}] files had been successfully writed to xml file in {2} ms";
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
	 * @param editorName
	 * @return
	 */
	protected static BaseEditor checkBaseEditor(String editorName) {
		BaseEditor result = null;
		if (StringHelper.isBlank(editorName)) {
			LOGGER.error("[" + editorName + "] Is null");
			return result;
		}
		Class<?> clazz = ClassHelper.forName(editorName);
		if (clazz == null) {
			LOGGER.error("Can not forName(" + editorName + ")");
			return result;
		}
		Object object = ClassHelper.newInstance(clazz);
		if (!(object instanceof BaseEditor)) {
			LOGGER.error("Can not newInstance(" + editorName + "). It is not instance of BaseEditor");
			return result;
		}
		//
		result = (BaseEditor) object;
		return result;
	}
}
