package org.openyu.commons.cat.service.impl;

import org.junit.Test;
import org.openyu.commons.cat.CatTestSupporter;
import org.openyu.commons.thread.ThreadHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

public class CatLogServiceImplTest extends CatTestSupporter {

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void deleteCatInsertLog() {
		final String CAT_ID = "TEST_CAT";
		//
		int result = catLogService.deleteCatInsertLog(CAT_ID);
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void recordInsert() {
		final String CAT_ID = "TEST_CAT";
		//
		catLogService.recordInsert(CAT_ID);
		ThreadHelper.sleep(3 * 1000);
	}
}
