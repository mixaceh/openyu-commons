package org.openyu.commons.service.supporter;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.Locale;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.cat.po.impl.CatPoImpl;
import org.openyu.commons.cat.vo.impl.CatImpl;
import org.openyu.commons.dao.supporter.CommonDaoSupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.CloneHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.service.event.BeanListener;
import org.openyu.commons.service.event.impl.CommonBeanAdapter;

public class CommonServiceSupporterTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static CommonServiceSupporter commonServiceSupporter;

	private static BeanListener serviceBeanListener;

	/**
	 * 直接使用hibernate.cfg.xml, s並非由spring注入
	 */
	public static void createCommonServiceSupporter() {
		try {
			// service
			commonServiceSupporter = new CommonServiceSupporter();

			// dao
			CommonDaoSupporter commonDaoSupporter = new CommonDaoSupporter();

			// 建構HibernateTemplate,因HibernateDaoSupporter需要
			HibernateTemplate hibernateTemplate = new HibernateTemplate();
			Configuration config = new Configuration().configure("hibernate.cfg.xml");
			// SessionFactory sessionFactory = config.buildSessionFactory();
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties())
					.buildServiceRegistry();
			SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
			// System.out.println("sessionFactory: " + sessionFactory);
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
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// insert -> find -> delete

	// round: 3.30 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 3.30, time.warmup: 0.00,
	// time.bench: 3.30
	public void crud() {
		createCommonServiceSupporter();
		//
		final String ID = "TEST_CAT";
		// final String NAME = "TEST_NAME";

		int count = 10;
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
			printInsert(i, pk);
			assertNotNull(pk);

			// retrieve
			CatImpl foundEntity = commonServiceSupporter.find(CatImpl.class, cat.getSeq());
			printFind(i, foundEntity);
			assertEquals(id, foundEntity.getId());

			// update
			cat.setValid(true);
			int updated = commonServiceSupporter.update(cat);
			printUpdate(i, updated);
			assertTrue(updated > 0);

			// delete
			CatImpl deleteEntity = commonServiceSupporter.delete(CatImpl.class, cat.getSeq());
			printDelete(i, deleteEntity);
			assertNotNull(deleteEntity);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 3.38 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 3.38, time.warmup: 0.00,
	// time.bench: 3.38
	public void insert() {
		createCommonServiceSupporter();
		//
		final String ID = "TEST_CAT";
		int count = 10;
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
			printInsert(i, pk);
			assertNotNull(pk);

			CatImpl foundEntity = commonServiceSupporter.find(CatImpl.class, cat.getSeq());
			assertEquals(cat.getId(), foundEntity.getId());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 3.59 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 3.59, time.warmup: 0.00,
	// time.bench: 3.59
	public void insertByPo() {
		createCommonServiceSupporter();
		//
		final String ID = "TEST_CAT";
		int count = 10;
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
			printInsert(i, pk);
			assertNotNull(pk);

			CatImpl foundEntity = commonServiceSupporter.find(CatImpl.class, cat.getSeq());
			assertEquals(cat.getId(), foundEntity.getId());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void findCat() {
		createCommonServiceSupporter();
		//
		final Long SEQ = 1L;
		CatImpl result = null;

		int count = 10;
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
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void update() {
		createCommonServiceSupporter();
		//
		int result = 0;
		CatImpl cat = mockFindCat(1L);
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
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
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
			System.out.println("update: " + (update > 0 ? "[success]" : "[fail]"));
			System.out.println(update);
		} else {
			System.out.println("nothing to update: " + anotherCat);
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void delete() {
		createCommonServiceSupporter();
		//
		CatImpl result = commonServiceSupporter.delete(CatImpl.class, 1L);
		printDelete(0, result);
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void deleteByPo() {
		createCommonServiceSupporter();
		//
		CatPoImpl result = commonServiceSupporter.delete(CatPoImpl.class, 17L);
		printDelete(0, result);
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
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
			System.out.println("delete: " + (delete > 0 ? "[success]" : "[fail]"));
			System.out.println(delete);
		} else {
			System.out.println("nothing to delete: " + anotherCat);
		}

	}
}
