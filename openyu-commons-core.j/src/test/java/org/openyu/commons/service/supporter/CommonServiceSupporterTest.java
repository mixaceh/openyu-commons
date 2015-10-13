package org.openyu.commons.service.supporter;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.Locale;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.openyu.commons.dao.supporter.CommonDaoSupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.CloneHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.vo.impl.CatImpl;
import org.openyu.commons.po.impl.CatPoImpl;
import org.openyu.commons.service.event.BeanListener;
import org.openyu.commons.service.event.impl.CommonBeanAdapter;

public class CommonServiceSupporterTest extends BaseTestSupporter {

	private static CommonServiceSupporter commonServiceSupporter;

	private static BeanListener serviceBeanListener;

	public static void createCommonServiceSupporter() {
		try {
			// service
			commonServiceSupporter = new CommonServiceSupporter();

			// dao
			CommonDaoSupporter commonDaoSupporter = new CommonDaoSupporter();

			// 建構HibernateTemplate,因HibernateDaoSupporter需要
			HibernateTemplate hibernateTemplate = new HibernateTemplate();
			Configuration config = new Configuration()
					.configure("hibernate.cfg.xml");
			// SessionFactory sessionFactory = config.buildSessionFactory();
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
					.applySettings(config.getProperties())
					.buildServiceRegistry();
			SessionFactory sessionFactory = config
					.buildSessionFactory(serviceRegistry);
			//System.out.println("sessionFactory: " + sessionFactory);
			//
			hibernateTemplate.setSessionFactory(sessionFactory);
			commonDaoSupporter.setHibernateTemplate(hibernateTemplate);
			//
			commonServiceSupporter.setCommonDao(commonDaoSupporter);

			// listener
			serviceBeanListener = new CommonBeanAdapter();
			commonServiceSupporter.addBeanListener(serviceBeanListener);
			assertNotNull(commonServiceSupporter);

			commonServiceSupporter.start();

			System.out.println("---------------------------");
			System.out.println("createCommonServiceSupporter [success]");
			System.out.println("---------------------------");
		} catch (Exception ex) {
			System.out.println("---------------------------");
			System.out.println("createCommonServiceSupporter [fail]");
			System.out.println("---------------------------");
			ex.printStackTrace();
		}
	}

	@Test
	public void commonServiceSupporter() {
		createCommonServiceSupporter();
		//
		System.out.println(commonServiceSupporter);
		assertNotNull(commonServiceSupporter);
	}

	@Test
	// insert -> find -> delete
	//
	// 10 times: 1052 mills.
	// 10 times: 1172 mills.
	// 10 times: 1009 mills.
	//
	// verified: ok
	public void crud() {
		createCommonServiceSupporter();
		//
		final String ID = "TEST_CAT";
		// final String NAME = "TEST_NAME";

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// random
			int randomNumber = NumberHelper.randomInt(0, 1000000);

			CatImpl cat = new CatImpl();
			String id = ID + randomNumber;
			cat.setId(id);

			// #new 加入name
			cat.addName(Locale.TRADITIONAL_CHINESE, "金吉拉");
			cat.addName(Locale.US, "Gin Gi La");

			// create
			Serializable pk = commonServiceSupporter.insert(cat);
			System.out.println("#." + i + " insert: "
					+ (pk != null ? "[success]" : "[fail]"));
			assertNotNull(pk);

			// retrieve
			CatImpl existCat = commonServiceSupporter.find(CatImpl.class,
					cat.getSeq());
			System.out.println("#." + i + " find: "
					+ (existCat != null ? "[success]" : "[fail]"));
			assertEquals(id, existCat.getId());

			// update
			cat.setValid(true);
			int updated = commonServiceSupporter.update(cat);
			System.out.println("#." + i + " update: "
					+ (updated > 0 ? "[success]" : "[fail]"));
			assertTrue(updated > 0);

			// delete
			CatImpl deleteCatImpl = commonServiceSupporter.delete(CatImpl.class,
					cat.getSeq());
			System.out.println("#." + i + " delete: "
					+ (deleteCatImpl != null ? "[success]" : "[fail]"));
			assertNotNull(deleteCatImpl);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// verified: ok
	public void insert() {
		createCommonServiceSupporter();
		//
		final String ID = "TEST_CAT";
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// random
			int randomNumber = NumberHelper.randomInt(0, 1000000);

			CatImpl cat = new CatImpl();
			cat.setId(ID + randomNumber);
			cat.setValid(true);

			// #new 加入name
			cat.addName(Locale.TRADITIONAL_CHINESE, "金吉拉");
			cat.addName(Locale.US, "Gin Gi La");

			Serializable pk = commonServiceSupporter.insert(cat);
			printInsert(0, pk);
			assertNotNull(pk);

			CatImpl foundEntity = commonServiceSupporter.find(CatImpl.class,
					cat.getSeq());
			assertEquals(cat.getId(), foundEntity.getId());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// verified: ok
	public void insertByPo() {
		createCommonServiceSupporter();
		//
		final String ID = "TEST_CAT";
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// random
			int randomNumber = NumberHelper.randomInt(0, 1000000);

			CatPoImpl cat = new CatPoImpl();
			cat.setId(ID + randomNumber);
			cat.setValid(true);

			// #new 加入name
			cat.addName(Locale.TRADITIONAL_CHINESE, "金吉拉");
			cat.addName(Locale.US, "Gin Gi La");

			Serializable pk = commonServiceSupporter.insert(cat);
			printInsert(0, pk);
			assertNotNull(pk);

			CatImpl existCat = commonServiceSupporter.find(CatImpl.class,
					cat.getSeq());
			assertEquals(cat.getId(), existCat.getId());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// 1000 times: 982 mills.
	// 1000 times: 977 mills.
	// 1000 times: 978 mills.
	public void findCat() {
		createCommonServiceSupporter();
		//
		final Long SEQ = 10L;
		CatImpl result = null;

		int count = 1000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = mockFindCat(SEQ);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(SEQ, result.getSeq());
	}

	/**
	 * 測試用資料
	 * 
	 * @param seq
	 * @return
	 */
	private CatImpl mockFindCat(Long seq) {
		CatImpl result = null;
		result = commonServiceSupporter.find(CatImpl.class, seq);
		return result;
	}

	@Test
	public void update() {
		createCommonServiceSupporter();
		//
		int result = 0;
		CatImpl cat = mockFindCat(23L);
		CatImpl anotherCat = CloneHelper.clone(cat);
		if (anotherCat != null) {
			anotherCat.setSeq(cat.getSeq());
			System.out.println("anotherCat seq: " + anotherCat.getSeq());
			anotherCat.getNames().clear();
			//
			// org.hibernate.NonUniqueObjectException
			result = commonServiceSupporter.update(anotherCat);
			printUpdate(0, result);
			assertTrue(result > 0);
		} else {
			System.out.println("nothing to update: " + anotherCat);
		}
	}

	@Test
	public void updateNonUniqueObjectException() {
		createCommonServiceSupporter();
		//
		CatImpl cat = mockFindCat(3L);
		CatImpl anotherCat = CloneHelper.clone(cat);
		if (anotherCat != null) {
			anotherCat.setSeq(cat.getSeq());
			System.out.println("anotherCat seq: " + anotherCat.getSeq());
			anotherCat.getNames().clear();
			//
			// org.hibernate.NonUniqueObjectException
			int update = commonServiceSupporter.update(anotherCat);
			System.out.println("update: "
					+ (update > 0 ? "[success]" : "[fail]"));
			System.out.println(update);
		} else {
			System.out.println("nothing to update: " + anotherCat);
		}
	}

	@Test
	// verified: ok
	public void delete() {
		createCommonServiceSupporter();
		//
		CatImpl result = commonServiceSupporter.delete(CatImpl.class, 22L);
		printDelete(0, result);
		System.out.println(result);
	}

	@Test
	// verified: ok
	public void deleteByPo() {
		createCommonServiceSupporter();
		//
		CatPoImpl result = commonServiceSupporter.delete(CatPoImpl.class, 17L);
		printDelete(0, result);
		System.out.println(result);
	}

	@Test
	public void deleteNonUniqueObjectException() {
		createCommonServiceSupporter();
		//
		CatImpl cat = mockFindCat(19L);
		CatImpl anotherCat = CloneHelper.clone(cat);
		if (anotherCat != null) {
			anotherCat.setSeq(cat.getSeq());
			System.out.println("anotherCat seq: " + anotherCat.getSeq());
			//
			// org.hibernate.NonUniqueObjectException
			int delete = commonServiceSupporter.delete(anotherCat);
			System.out.println("delete: "
					+ (delete > 0 ? "[success]" : "[fail]"));
			System.out.println(delete);
		} else {
			System.out.println("nothing to delete: " + anotherCat);
		}

	}
}
