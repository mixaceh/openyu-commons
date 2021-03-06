package org.openyu.commons.lock.impl;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lock.MysqlLock;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class MysqlLockImplTest extends BaseTestSupporter {

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

	// 25, 15
	// round: 5.85 [+- 2.17], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 13.37, time.warmup: 0.02,
	// time.bench: 13.36

	// 50, 25
	// round: 1.78 [+- 0.64], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 4.33, time.warmup: 0.00,
	// time.bench: 4.33

	// 50, 25
	// round: 1.66 [+- 1.09], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 4.67, time.warmup: 0.01,
	// time.bench: 4.66
	@Test
	@BenchmarkOptions(benchmarkRounds = 50, warmupRounds = 0, concurrency = 25)
	public void lock() {
		// maxTotal=10
		MysqlLock lock = new MysqlLockImpl(commonDataSource, "aaa", 5);
		lock.lock();
		try {
			System.out.println(
					"T[" + Thread.currentThread().getId() + "], " + counter.incrementAndGet() + ": do something...");
			// ThreadHelper.sleep(60 * 60 * 1000);
			ThreadHelper.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 2)
	public void lockInterruptibly() {
		// maxTotal=10
		String lockName = "aaa";
		MysqlLock lock = new MysqlLockImpl(commonDataSource, lockName, 5);
		try {
			lock.lockInterruptibly();
			try {
				System.out.println("T[" + Thread.currentThread().getId() + "], " + counter.incrementAndGet()
						+ ": do something...");
				ThreadHelper.sleep(60 * 60 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
