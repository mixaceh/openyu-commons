package org.openyu.commons.blank;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BlankServiceImplTest extends BaseTestSupporter {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static BlankServiceImpl blankServiceImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				// "applicationContext-init.xml", //
				// "org/openyu/commons/thread/applicationContext-thread.xml", //
				"org/openyu/commons/service/applicationContext-service.xml", //
				"org/openyu/commons/blank/applicationContext-blank.xml",//

		});

		blankServiceImpl = (BlankServiceImpl) applicationContext.getBean("blankService");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void blankServiceImpl() {
		System.out.println(blankServiceImpl);
		assertNotNull(blankServiceImpl);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(blankServiceImpl);
		assertNotNull(blankServiceImpl);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(blankServiceImpl);
		assertNotNull(blankServiceImpl);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void toStates() {
		System.out.println(blankServiceImpl.toStates());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void startWithException() throws Exception {
		// 多次,會丟出ex
		// java.lang.IllegalStateException: BlankServiceImpl 'blankService' was
		// already started
		blankServiceImpl.start();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdownWithException() throws Exception {
		// 多次,會丟出ex
		// java.lang.IllegalStateException: BlankServiceImpl 'blankService' was
		// already shutdown
		blankServiceImpl.shutdown();
		blankServiceImpl.shutdown();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void restartWithException() throws Exception {
		// 關閉後調用重啟,會丟出ex
		blankServiceImpl.shutdown();
		// java.lang.IllegalStateException: BlankServiceImpl 'blankService' not
		// start. Call start()
		blankServiceImpl.restart();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void hex() throws Exception {
		System.out.println(0x00000001);// 1
		System.out.println(0x00000002);// 2
		System.out.println(0x00000004);// 4
		System.out.println(0x00000008);// 8
		//
		System.out.println(0x00000010);// 16
		System.out.println(0x00000020);// 32
		System.out.println(0x00000040);// 64
		System.out.println(0x00000080);// 128
		//
		System.out.println(0x00000100);// 256
		System.out.println(0x00000200);// 512
		System.out.println(0x00000400);// 1024
		System.out.println(0x00000800);// 2048
		//
		System.out.println(0x000000ff);// 255
		System.out.println(0xffffffff);// -1
		//
		System.out.println(1 << 0);// 1
		System.out.println(1 << 1);// 2
		System.out.println(1 << 2);// 4
		System.out.println(1 << 3);// 8
		//
		System.out.println(1 << 4);// 16
		System.out.println(1 << 5);// 32
		System.out.println(1 << 6);// 64
		System.out.println(1 << 7);// 128
		//
		System.out.println(1 << 8);// 256
		System.out.println(1 << 9);// 512
		//
		System.out.println(1 << 30);// 1073741824
		System.out.println(1 << 31);// -2147483648
		//
		System.out.println(Integer.MAX_VALUE);// 2147483647
		System.out.println(Integer.MIN_VALUE);// -2147483648
	}

	/**
	 * GetInstanceTest
	 */
	public static class GetInstanceTest extends BaseTestSupporter {

		@Rule
		public BenchmarkRule benchmarkRule = new BenchmarkRule();

		@Test
		@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
		public void getInstance() {
			BlankService instance = BlankServiceImpl.getInstance();
			System.out.println(instance);
			assertNotNull(instance);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void shutdownInstance() {
			BlankService instance = BlankServiceImpl.getInstance();
			System.out.println(instance);
			assertNotNull(instance);
			//
			instance = BlankServiceImpl.shutdownInstance();
			assertNull(instance);
			// 多次,不會丟出ex
			instance = BlankServiceImpl.shutdownInstance();
			assertNull(instance);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void restartInstance() {
			BlankService instance = BlankServiceImpl.getInstance();
			System.out.println(instance);
			assertNotNull(instance);
			//
			instance = BlankServiceImpl.restartInstance();
			assertNotNull(instance);
			// 多次,不會丟出ex
			instance = BlankServiceImpl.restartInstance();
			assertNotNull(instance);
		}
	}
}
