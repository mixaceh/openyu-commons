package org.openyu.commons.dao.inquiry.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.dao.inquiry.Inquiry;
import org.openyu.commons.dao.inquiry.Order;
import org.openyu.commons.dao.inquiry.Pagination;
import org.openyu.commons.dao.inquiry.Sort;
import org.openyu.commons.dao.inquiry.Order.OrderType;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class InquiryImplTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext-init.xml"//
				});
	}

	@Test
	public void writeToXml() {
		Inquiry inquiry = new InquiryImpl();
		//
		Pagination pagination = new PaginationImpl();
		pagination.getPageSizes().add(10);
		pagination.getPageSizes().add(20);
		pagination.getPageSizes().add(50);
		pagination.getPageSizes().add(100);
		inquiry.setPagination(pagination);
		//
		Sort sort = new SortImpl();
		sort.setId("sort");
		//
		sort.addName(Locale.TRADITIONAL_CHINESE, "測試排序");
		sort.addName(Locale.SIMPLIFIED_CHINESE, "测试排序");
		sort.addName(Locale.US, "Test sort");
		inquiry.getSorts().add(sort);
		//
		Order order = new OrderImpl();
		order.setId(OrderType.ASC);
		//
		order.addName(Locale.TRADITIONAL_CHINESE, "測試排序方向");
		order.addName(Locale.SIMPLIFIED_CHINESE, "测试排序方向");
		order.addName(Locale.US, "Test order");
		inquiry.getOrders().add(order);
		//
		String result = CollectorHelper.writeToXml(InquiryImpl.class, inquiry);
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void readFromXml() {
		Inquiry result = CollectorHelper.readFromXml(InquiryImpl.class);
		System.out.println(result);
		assertNotNull(result);
	}

}
