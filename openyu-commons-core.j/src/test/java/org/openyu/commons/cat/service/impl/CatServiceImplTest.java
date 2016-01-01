package org.openyu.commons.cat.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Locale;

import org.junit.Test;
import org.openyu.commons.cat.CatTestSupporter;
import org.openyu.commons.cat.vo.impl.CatImpl;
import org.openyu.commons.thread.ThreadHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

public class CatServiceImplTest extends CatTestSupporter {

	/**
	 * 隨機產生貓資料
	 * 
	 * @return
	 */
	public static CatImpl randomCat() {
		final String UNIQUE = randomUnique();
		final String ID = "TEST_CAT" + UNIQUE;
		final String ZH_TW_NAME = "測試貓" + UNIQUE;
		final String EN_US_NAME = "Test Cat" + UNIQUE;
		//
		CatImpl result = new CatImpl();
		result.setId(ID);
		result.setValid(randomBoolean());
		result.addName(Locale.TRADITIONAL_CHINESE, ZH_TW_NAME);
		result.addName(Locale.US, EN_US_NAME);
		return result;
	}

	/**
	 * 檢核貓資料
	 * 
	 * @param expected
	 * @param actual
	 */
	public static void assertCat(CatImpl expected, CatImpl actual) {
		assertNotNull(expected);
		assertNotNull(actual);
		//
		assertEquals(expected.getSeq(), actual.getSeq());
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getValid(), actual.getValid());
		assertEquals(expected.getAge(), actual.getAge());
		assertCollectionEquals(expected.getNames(), actual.getNames());
	}

	/**
	 * insert -> find -> delete
	 * 
	 * 當service有@CommonTx,會回傳正確的值
	 * 
	 * insert會真正寫入db
	 * 
	 * update會真正寫入db [ * delete會真正寫入db
	 * 
	 * 效率會比dao慢
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 1)
	// round: 0.26 [+- 0.09], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 1, GC.time: 0.01, time.total: 2.62, time.warmup: 0.00,
	// time.bench: 2.62
	public void crud() {
		// 隨機
		CatImpl cat = randomCat();
		// create
		Serializable pk = catService.insert(cat);
		printInsert(pk);
		assertNotNull(pk);

		// retrieve
		CatImpl foundEntity = catService.find(CatImpl.class, cat.getSeq());
		printFind(foundEntity);
		assertCat(cat, foundEntity);

		// update
		cat.setValid(true);
		int updated = catService.update(cat);
		printUpdate(updated);
		assertTrue(updated > 0);

		// delete
		CatImpl deletedEntity = catService.delete(CatImpl.class, cat.getSeq());
		printDelete(deletedEntity);
		assertNotNull(deletedEntity);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// no aop
	// round: 1.45 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.45, time.warmup: 0.00,
	// time.bench: 1.45
	public void insert() {
		// 隨機
		CatImpl cat = randomCat();
		//
		Serializable pk = catService.insert(cat);
		printInsert(pk);
		assertNotNull(pk);
		//
		// CatImpl foundEntity = catService.find(CatImpl.class, cat.getSeq());
		// printFind(foundEntity);
		// assertCat(cat, foundEntity);

		//
		ThreadHelper.sleep(3 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// @Aspect
	// pointcut=>class
	// round: 1.47 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 1, GC.time: 0.06, time.total: 1.47, time.warmup: 0.00,
	// time.bench: 1.47

	// pointcut=>insterface
	// round: 1.37 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 1, GC.time: 0.03, time.total: 1.37, time.warmup: 0.00,
	// time.bench: 1.37
	public void insertCat() {
		CatImpl cat = randomCat();
		// CatAspect處理log
		Serializable pk = catService.insertCat(cat);
		printInsert(pk);
		assertNotNull(pk);
		//
		ThreadHelper.sleep(3 * 1000);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// AroundAdvice
	// round: 1.56 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 1.56, time.warmup: 0.00,
	// time.bench: 1.56
	public void insertCat2() {
		CatImpl cat = randomCat();
		// CatInsertAdvice處理log
		Serializable pk = catService.insertCat2(cat);
		printInsert(pk);
		assertNotNull(pk);
		//
		ThreadHelper.sleep(3 * 1000);
	}
}
