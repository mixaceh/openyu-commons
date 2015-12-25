package org.openyu.commons.cat.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Test;
import org.openyu.commons.cat.CatTestSupporter;
import org.openyu.commons.cat.log.CatInsertLog;
import org.openyu.commons.cat.log.impl.CatInsertLogImpl;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

public class CatLogDaoImplTest extends CatTestSupporter {
	// --------------------------------------------------
	// CatInsertLog
	// --------------------------------------------------
	public static class CatInsertLogTest extends CatLogDaoImplTest {

		/**
		 * 隨機產生貓資料
		 * 
		 * @return
		 */
		public static CatInsertLog randomCatInsertLog() {
			final String CAT_ID = "TEST_CAT" + randomUnique();
			//
			CatInsertLog result = new CatInsertLogImpl();
			result.setCatId(CAT_ID);
			return result;
		}

		/**
		 * 檢核貓資料
		 * 
		 * @param expected
		 * @param actual
		 */
		public static void assertCatInsertLog(CatInsertLog expected, CatInsertLog actual) {
			assertNotNull(expected);
			assertNotNull(actual);
			//
			assertEquals(expected.getCatId(), actual.getCatId());
		}

		/**
		 * insert -> find -> delete
		 * 
		 * 當dao沒有tx,會回傳正確的值,但是
		 * 
		 * insert有時會寫,有時不會真正寫入db
		 * 
		 * update不會真正寫入db
		 * 
		 * delete不會真正寫入db
		 */
		@Test
		@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 1)
		// round: 0.08 [+- 0.14], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.76, time.warmup:
		// 0.00, time.bench: 0.76
		public void crud() {
			// 隨機
			CatInsertLog catInsertLog = randomCatInsertLog();
			// create
			Serializable pk = catLogDao.insert(catInsertLog);
			printInsert(pk);
			assertNotNull(pk);

			// retrieve
			CatInsertLog foundEntity = catLogDao.find(CatInsertLogImpl.class, catInsertLog.getSeq());
			printFind(foundEntity);
			assertCatInsertLog(catInsertLog, foundEntity);

			// update
			catInsertLog.setCatId("UPDATE_" + catInsertLog.getCatId());
			int updated = catLogDao.update(catInsertLog);
			printUpdate(updated);
			assertTrue(updated > 0);

			// delete
			CatInsertLogImpl deletedEntity = catLogDao.delete(CatInsertLogImpl.class, catInsertLog.getSeq());
			printDelete(deletedEntity);
			assertNotNull(deletedEntity);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 1)
		public void insert() {
			// 隨機
			CatInsertLog catInsertLog = randomCatInsertLog();
			//
			Serializable pk = catLogDao.insert(catInsertLog);
			printInsert(pk);
			assertNotNull(pk);

			CatInsertLog foundEntity = catLogDao.find(CatInsertLogImpl.class, catInsertLog.getSeq());
			assertCatInsertLog(catInsertLog, foundEntity);

			System.out.println(catInsertLog);
		}
	}
}
