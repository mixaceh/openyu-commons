package org.openyu.commons.bean.supporter;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.bean.SeqAuditBean;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class SeqAuditBeanSupporterTest extends BaseTestSupporter {

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
		SeqAuditBean bean = new SeqAuditBeanSupporter();
		bean.setSeq(1L);// 不寫出
		bean.setVersion(1);// 不寫出
		//
		bean.getAudit().setCreateDate(new Date());// 不寫出
		bean.getAudit().setCreateUser("sys");// 不寫出
		bean.getAudit().setModifiedDate(new Date());// 不寫出
		bean.getAudit().setModifiedUser("sys");// 不寫出
		//
		String result = CollectorHelper.writeToXml(SeqAuditBeanSupporter.class, bean);
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void readFromXml() {
		SeqAuditBean result = CollectorHelper
				.readFromXml(SeqAuditBeanSupporter.class);
		System.out.println(result);
		assertNotNull(result);
	}
}
