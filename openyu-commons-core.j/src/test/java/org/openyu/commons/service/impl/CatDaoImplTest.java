package org.openyu.commons.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Locale;

import org.junit.Test;
import org.openyu.commons.po.impl.CatPoImpl;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

public class CatDaoImplTest extends CatTestSupporter {

	/**
	 * 隨機產生貓資料
	 * 
	 * @return
	 */
	public static CatPoImpl randomCatPo() {
		final String UNIQUE = randomUnique();
		final String ID = "TEST_CAT" + UNIQUE;
		final String ZH_TW_NAME = "測試貓" + UNIQUE;
		final String EN_US_NAME = "Test Cat" + UNIQUE;
		//
		CatPoImpl result = new CatPoImpl();
		result.setId(ID);
		result.setValid(randomBoolean());
		result.addName(Locale.TRADITIONAL_CHINESE, ZH_TW_NAME);
		result.addName(Locale.US, EN_US_NAME);
		return result;
	}

	/**
	 * 檢核帳號資料
	 * 
	 * @param expected
	 * @param actual
	 */
	public static void assertCatPo(CatPoImpl expected, CatPoImpl actual) {
		assertNotNull(expected);
		assertNotNull(actual);
		//
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getValid(), actual.getValid());
		assertCollectionEquals(expected.getNames(), actual.getNames());
	}

	// insert -> find -> delete
	// 當dao沒有tx,會回傳正確的值,但是
	//
	// insert有時會寫,有時不會真正寫入db
	// update不會真正寫入db
	// delete不會真正寫入db
	@Test
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 1)
	// round: 0.05 [+- 0.12], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.49, time.warmup: 0.00,
	// time.bench: 0.49
	public void crud() {
		// 隨機
		CatPoImpl catPo = randomCatPo();
		// create
		Serializable pk = catDao.insert(catPo);
		printInsert(pk);
		assertNotNull(pk);

		// retrieve
		CatPoImpl foundEntity = catDao.find(CatPoImpl.class, catPo.getSeq());
		printFind(foundEntity);
		assertCatPo(catPo, foundEntity);

		// update
		catPo.setValid(true);
		int updated = catDao.update(catPo);
		printUpdate(updated);
		assertTrue(updated > 0);

		// delete
		CatPoImpl deletedEntity = catDao.delete(CatPoImpl.class, catPo.getSeq());
		printDelete(deletedEntity);
		assertNotNull(deletedEntity);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 1)
	public void insert() {
		// 隨機
		CatPoImpl catPo = randomCatPo();
		//
		Serializable pk = catDao.insert(catPo);
		printInsert(pk);
		assertNotNull(pk);

		CatPoImpl foundEntity = catDao.find(CatPoImpl.class, catPo.getSeq());
		assertCatPo(catPo, foundEntity);

		System.out.println(catPo);
	}
}
