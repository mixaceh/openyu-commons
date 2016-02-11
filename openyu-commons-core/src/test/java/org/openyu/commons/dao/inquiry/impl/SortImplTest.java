package org.openyu.commons.dao.inquiry.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.dao.inquiry.Sort;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class SortImplTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext-init.xml"//
				});
	}

	@Test
	public void writeToXml() {
		Sort sort = new SortImpl();
		sort.setId("sort");
		//
		sort.addName(Locale.TRADITIONAL_CHINESE, "測試排序");
		sort.addName(Locale.SIMPLIFIED_CHINESE, "测试排序");
		sort.addName(Locale.US, "Test sort");
		//
		String result = CollectorHelper.writeToXml(SortImpl.class, sort);
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void readFromXml() {
		Sort result = CollectorHelper.readFromXml(SortImpl.class);
		System.out.println(result);
		assertNotNull(result);
	}

}
