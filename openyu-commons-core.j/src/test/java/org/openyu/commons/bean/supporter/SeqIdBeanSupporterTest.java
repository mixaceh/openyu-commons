package org.openyu.commons.bean.supporter;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.bean.SeqIdBean;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class SeqIdBeanSupporterTest extends BaseTestSupporter {

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
		SeqIdBean bean = new SeqIdBeanSupporter();
		bean.setSeq(1L);// 不寫出
		bean.setVersion(1);// 不寫出
		//
		bean.setId(randomString());
		//
		String result = CollectorHelper.writeToXml(SeqIdBeanSupporter.class,
				bean);
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void readFromXml() {
		SeqIdBean result = CollectorHelper
				.readFromXml(SeqIdBeanSupporter.class);
		System.out.println(result);
		assertNotNull(result);
	}
}
