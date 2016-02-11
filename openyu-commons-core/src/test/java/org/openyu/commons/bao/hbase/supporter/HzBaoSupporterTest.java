package org.openyu.commons.bao.hbase.supporter;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.SystemHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HzBaoSupporterTest extends BaseTestSupporter {

	private static HzBaoSupporter hzBaoSupporter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"applicationContext-init.xml",//
				"META-INF/applicationContext-commons-core.xml",//
				"org/openyu/commons/thread/testContext-thread.xml", // 
				"applicationContext-hbase-zookeeper.xml",//
		});
		hzBaoSupporter = (HzBaoSupporter) applicationContext
				.getBean("hzBaoSupporter");
	}

	@Test
	// 50 times: 3109 mills.
	public void listTables() throws Exception {
		HTableDescriptor[] result = null;
		int count = 50;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = hzBaoSupporter.listTables();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(result);
	}

}
