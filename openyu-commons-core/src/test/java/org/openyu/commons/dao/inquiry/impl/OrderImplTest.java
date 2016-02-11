package org.openyu.commons.dao.inquiry.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.dao.inquiry.Order;
import org.openyu.commons.dao.inquiry.Order.OrderType;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class OrderImplTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext-init.xml"//
				});
	}

	@Test
	public void writeToXml() {
		Order order = new OrderImpl();
		order.setId(OrderType.ASC);
		//
		order.addName(Locale.TRADITIONAL_CHINESE, "測試排序方向");
		order.addName(Locale.SIMPLIFIED_CHINESE, "测试排序方向");
		order.addName(Locale.US, "Test order");
		//
		String result = CollectorHelper.writeToXml(OrderImpl.class, order);
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void readFromXml() {
		Order result = CollectorHelper.readFromXml(OrderImpl.class);
		System.out.println(result);
		assertNotNull(result);
	}

}
