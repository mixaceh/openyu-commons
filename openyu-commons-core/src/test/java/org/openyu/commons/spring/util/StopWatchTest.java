package org.openyu.commons.spring.util;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class StopWatchTest extends BaseTestSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(StopWatchTest.class);

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	public void start() {

	}

}
