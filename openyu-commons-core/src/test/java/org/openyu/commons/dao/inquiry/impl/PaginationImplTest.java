package org.openyu.commons.dao.inquiry.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.dao.inquiry.Pagination;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class PaginationImplTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext-init.xml"//
				});
	}

	@Test
	public void writeToXml() {
		Pagination pagination = new PaginationImpl();
		pagination.getPageSizes().add(10);
		pagination.getPageSizes().add(20);
		pagination.getPageSizes().add(50);
		pagination.getPageSizes().add(100);
		//
		String result = CollectorHelper.writeToXml(PaginationImpl.class,
				pagination);
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void readFromXml() {
		Pagination result = CollectorHelper.readFromXml(PaginationImpl.class);
		System.out.println(result);
		assertNotNull(result);
	}

}
