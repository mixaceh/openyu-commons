package org.openyu.commons.bean.supporter;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.bean.SeqIdAuditNamesBean;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class SeqIdAuditNamesBeanSupporterTest extends BaseTestSupporter {

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
		SeqIdAuditNamesBean bean = new SeqIdAuditNamesBeanSupporter();
		bean.setSeq(1L);// 不寫出
		bean.setVersion(1);// 不寫出
		//
		bean.setId(randomString());
		//
		bean.getAudit().setCreateDate(new Date());// 不寫出
		bean.getAudit().setCreateUser("sys");// 不寫出
		bean.getAudit().setModifiedDate(new Date());// 不寫出
		bean.getAudit().setModifiedUser("sys");// 不寫出
		//
		bean.addName(Locale.TRADITIONAL_CHINESE, "測試名稱");
		bean.addName(Locale.SIMPLIFIED_CHINESE, "测试名称");
		bean.addName(Locale.US, "Test name");
		//
		String result = CollectorHelper.writeToXml(
				SeqIdAuditNamesBeanSupporter.class, bean);
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void readFromXml() {
		SeqIdAuditNamesBean result = CollectorHelper
				.readFromXml(SeqIdAuditNamesBeanSupporter.class);
		System.out.println(result);
		assertNotNull(result);
	}
}
