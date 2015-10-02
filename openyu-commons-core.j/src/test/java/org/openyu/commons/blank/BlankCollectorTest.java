package org.openyu.commons.blank;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BlankCollectorTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@Deprecated
	/**
	 * 只是為了模擬用,有正式xml後,不應再使用,以免覆蓋掉正式的xml
	 */
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void writeToXml() {
		BlankCollector collector = BlankCollector.getInstance(false);
		String result = null;
		//
		result = CollectorHelper.writeToXml(BlankCollector.class, collector);
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void readFromXml() {
		BlankCollector result = null;
		//
		result = CollectorHelper.readFromXml(BlankCollector.class);
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * 常用的是這個
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void writeToSerFromXml() {
		String result = null;
		//
		result = CollectorHelper.writeToSerFromXml(BlankCollector.class);
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void writeToSer() {
		String result = null;
		//
		result = CollectorHelper.writeToSer(BlankCollector.class,
				BlankCollector.getInstance(false));
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void readFromSer() {
		BlankCollector result = null;
		//
		result = CollectorHelper.readFromSer(BlankCollector.class);
		//
		System.out.println(result);
		assertNotNull(result);
	}

	/**
	 * GetInstanceTest
	 */
	public static class GetInstanceTest extends BaseTestSupporter {
		@Test
		@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
		public void getInstance() {
			BlankCollector result = null;
			//
			result = BlankCollector.getInstance();
			//
			System.out.println(result);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void shutdownInstance() {
			BlankCollector instance = BlankCollector.getInstance();
			System.out.println(instance);
			assertNotNull(instance);
			//
			instance = BlankCollector.shutdownInstance();
			assertNull(instance);
			// 多次,不會丟出ex
			instance = BlankCollector.shutdownInstance();
			assertNull(instance);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void restartInstance() {
			BlankCollector instance = BlankCollector.getInstance();
			System.out.println(instance);
			assertNotNull(instance);
			//
			instance = BlankCollector.restartInstance();
			assertNotNull(instance);
			// 多次,不會丟出ex
			instance = BlankCollector.restartInstance();
			assertNotNull(instance);
		}
	}
}
