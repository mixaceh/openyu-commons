package org.openyu.commons.bean.supporter;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.bean.SeqBean;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class SeqBeanSupporterTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext-init.xml"//
				});
	}

	@Test
	// mock data
	// verification: ok
	public void writeToXml() {
		SeqBean bean = new SeqBeanSupporter();
		bean.setSeq(1L);// 不寫出
		bean.setVersion(1);// 不寫出
		//
		String result = CollectorHelper
				.writeToXml(SeqBeanSupporter.class, bean);
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void readFromXml() {
		SeqBean result = CollectorHelper.readFromXml(SeqBeanSupporter.class);
		System.out.println(result);
		assertNotNull(result);
	}
}
