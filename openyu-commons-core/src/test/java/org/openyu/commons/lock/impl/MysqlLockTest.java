package org.openyu.commons.lock.impl;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lock.DistributedLock;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class MysqlLockTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static DataSource commonDataSource;
	private AtomicInteger counter = new AtomicInteger(0);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"applicationContext-bean.xml", //
				"applicationContext-database.xml",//
		});
		commonDataSource = applicationContext.getBean("commonDataSource", DataSource.class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void commonDataSource() throws Exception {
		System.out.println(commonDataSource);
		assertNotNull(commonDataSource);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 50)
	// 25, 15
	// round: 5.85 [+- 2.17], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 13.37, time.warmup: 0.02,
	// time.bench: 13.36

	// 50, 25
	// round: 9.91 [+- 3.75], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 25.96, time.warmup: 0.09,
	// time.bench: 25.87
	public void lock() {
		// maxActive=10
		DistributedLock lock = new MysqlLock(commonDataSource, "aaa", 5);
		//
		lock.lock();
		// boolean locked = lock.tryLock(5, TimeUnit.SECONDS);
		// System.out.println(locked);
		try {
			System.out.println(
					"T[" + Thread.currentThread().getId() + "] " + counter.incrementAndGet() + ": do something...");
			ThreadHelper.sleep(50);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
