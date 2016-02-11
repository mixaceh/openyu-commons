package org.openyu.commons.bean.supporter;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.openyu.commons.bean.AuditBean;
import org.openyu.commons.bean.IdBean;
import org.openyu.commons.bean.IdNamesBean;
import org.openyu.commons.bean.LocaleBean;
import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.bean.NameBean;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class BeanSupporterTest extends BaseTestSupporter {

	@Test
	// verified
	public void idBeanToXml() {
		IdBean bean = new IdBeanSupporter();
		bean.setId("TEST_ID");
		bean.setDataId("DATA_ID");
		//
		String result = CollectorHelper.writeToXml(IdBeanSupporter.class, bean);
		System.out.println(result);
		assertNotNull(result);
		//
		IdBean existBean = CollectorHelper.readFromXml(IdBeanSupporter.class);
		System.out.println(existBean);
		//
		assertEquals(bean.getId(), existBean.getId());
	}

	@Test
	// verified
	public void idNamesBeanToXml() {
		IdNamesBean bean = new IdNamesBeanSupporter();
		bean.setId("TEST_ID");
		bean.setDataId("DATA_ID");
		bean.addName(Locale.TRADITIONAL_CHINESE, "測試使用者");
		bean.addName(Locale.US, "Test user");
		//
		String result = CollectorHelper.writeToXml(IdNamesBeanSupporter.class,
				bean);
		System.out.println(result);
		assertNotNull(result);
		//
		IdNamesBean existBean = CollectorHelper
				.readFromXml(IdNamesBeanSupporter.class);
		System.out.println(existBean);
		//
		assertEquals(bean.getId(), existBean.getId());
		assertEquals(bean.getDataId(), existBean.getDataId());
		assertEquals(bean.getNames(), existBean.getNames());
	}

	@Test
	// verified
	public void auditBeanToXml() {
		AuditBean bean = new AuditBeanSupporter();
		bean.setCreateDate(new Date());
		bean.setCreateUser("sys");
		bean.setModifiedDate(new Date());
		bean.setModifiedUser("sys");
		//
		String result = CollectorHelper.writeToXml(AuditBeanSupporter.class,
				bean);
		System.out.println(result);
		assertNotNull(result);
		//
		AuditBean existBean = CollectorHelper
				.readFromXml(AuditBeanSupporter.class);
		System.out.println(existBean);
		//
		assertEquals(bean.getCreateDate(), existBean.getCreateDate());
		assertEquals(bean.getCreateUser(), existBean.getCreateUser());
		assertEquals(bean.getModifiedDate(), existBean.getModifiedDate());
		assertEquals(bean.getModifiedUser(), existBean.getModifiedUser());
	}

	@Test
	// verified
	public void localeBeanToXml() {
		LocaleBean bean = new LocaleBeanSupporter();
		bean.setLocale(Locale.TRADITIONAL_CHINESE);
		//
		String result = CollectorHelper.writeToXml(LocaleBeanSupporter.class,
				bean);
		System.out.println(result);
		assertNotNull(result);
		//
		LocaleBean existBean = CollectorHelper
				.readFromXml(LocaleBeanSupporter.class);
		System.out.println(existBean);
		//
		assertEquals(bean.getLocale(), existBean.getLocale());
	}

	@Test
	// verified
	public void nameBeanToXml() {
		NameBean bean = new NameBeanSupporter();
		bean.setName("TEST_NAME");
		//
		String result = CollectorHelper.writeToXml(NameBeanSupporter.class,
				bean);
		System.out.println(result);
		assertNotNull(result);
		//
		NameBean existBean = CollectorHelper
				.readFromXml(NameBeanSupporter.class);
		System.out.println(existBean);
		//
		assertEquals(bean.getName(), existBean.getName());
	}

	@Test
	public void localeNameBeanToXml() {
		LocaleNameBean bean = new LocaleNameBeanSupporter();
		bean.setLocale(Locale.TRADITIONAL_CHINESE);
		bean.setName("TEST_NAME");
		//
		String result = CollectorHelper.writeToXml(
				LocaleNameBeanSupporter.class, bean);
		System.out.println(result);
		assertNotNull(result);
		//
		LocaleNameBean existBean = CollectorHelper
				.readFromXml(LocaleNameBeanSupporter.class);
		System.out.println(existBean);
		//
		assertEquals(bean.getLocale(), existBean.getLocale());
		assertEquals(bean.getName(), existBean.getName());
	}

	@Test
	// verified
	public void namesBeanToXml() {
		NamesBean bean = new NamesBeanSupporter();
		bean.addName(Locale.TRADITIONAL_CHINESE, "測試使用者");
		bean.addName(Locale.US, "Test user");
		System.out.println(bean.getNames().size());
		//
		String result = CollectorHelper.writeToXml(NamesBeanSupporter.class,
				bean);
		System.out.println(result);
		assertNotNull(result);
		//
		NamesBean existBean = CollectorHelper
				.readFromXml(NamesBeanSupporter.class);
		System.out.println(existBean);
		//
		System.out.println(existBean.getNames().size());
		assertEquals(bean.getNames(), existBean.getNames());
	}

}
