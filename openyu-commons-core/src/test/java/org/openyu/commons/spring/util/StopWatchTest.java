package org.openyu.commons.spring.util;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.thread.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class StopWatchTest extends BaseTestSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(StopWatchTest.class);

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void start() {
		StopWatch sw = new StopWatch();
		sw.start("A Task");
		ThreadHelper.sleep(1000);
		sw.stop();
		//
		sw.start("B Task");
		ThreadHelper.sleep(1500);
		sw.stop();
		//
		sw.start("C Task");
		ThreadHelper.sleep(2000);
		sw.stop();
		//
		sw.printResult();
		//
		LOGGER.info(sw.prettyPrint());
	}

}
