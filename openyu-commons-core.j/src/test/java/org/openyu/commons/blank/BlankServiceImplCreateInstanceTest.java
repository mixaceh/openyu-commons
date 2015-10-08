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

public class BlankServiceImplCreateInstanceTest extends BaseTestSupporter {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static BlankServiceImplCreateInstance blankServiceImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				 "applicationContext-init.xml", //
				"org/openyu/commons/blank/testContext-blank.xml",//

		});

		blankServiceImpl = (BlankServiceImplCreateInstance) applicationContext.getBean("blankService");
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

	/**
	 * CreateInstanceTest
	 */
	public static class CreateInstanceTest extends BaseTestSupporter {

		@Rule
		public BenchmarkRule benchmarkRule = new BenchmarkRule();

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void newInstance() throws Exception {
			BlankServiceImplCreateInstance impl = new BlankServiceImplCreateInstance();
			// 啟動
			impl.start();
			System.out.println(impl);
			assertNotNull(impl);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void createInstance() {
			BlankService service = BlankServiceImplCreateInstance.createInstance();
			System.out.println(service);
			assertNotNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void shutdownInstance() {
			BlankService service = BlankServiceImplCreateInstance.createInstance();
			System.out.println(service);
			assertNotNull(service);
			//
			service = BlankServiceImplCreateInstance.shutdownInstance(service);
			assertNull(service);
			// 多次,不會丟出ex
			service = BlankServiceImplCreateInstance.shutdownInstance(service);
			assertNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void restartInstance() {
			BlankService service = BlankServiceImplCreateInstance.createInstance();
			System.out.println(service);
			assertNotNull(service);
			//
			service = BlankServiceImplCreateInstance.restartInstance(service);
			assertNotNull(service);
			// 多次,不會丟出ex
			service = BlankServiceImplCreateInstance.restartInstance(service);
			assertNotNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void startWithException() throws Exception {
			BlankServiceImplCreateInstance impl = new BlankServiceImplCreateInstance();
			// 多次,會丟出ex
			// java.lang.IllegalStateException: BlankServiceImpl 'blankService'
			// was already started
			impl.start();
			impl.start();
		}

		@Test()
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void shutdownWithException() throws Exception {
			BlankServiceImplCreateInstance impl = new BlankServiceImplCreateInstance();
			impl.start();
			System.out.println(impl);
			assertNotNull(impl);
			// 多次,會丟出ex
			// java.lang.IllegalStateException: BlankServiceImpl 'blankService'
			// was already shutdown
			impl.shutdown();
			impl.shutdown();
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void restartWithException() throws Exception {
			// 沒啟動直接調用重啟,會丟出ex
			BlankServiceImplCreateInstance impl = new BlankServiceImplCreateInstance();
			// java.lang.IllegalStateException: BlankServiceImpl (new) not
			// start. Call start()
			impl.restart();
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void checkStarted() {
			BlankServiceImplCreateInstance impl = new BlankServiceImplCreateInstance();
			// 一開始沒啟動,有可能是忘了call start()
			// impl.start();
			System.out.println(impl);
			assertNotNull(impl);
			// 會丟出ex
			// java.lang.IllegalStateException: BlankServiceImpl (new) not
			// start. Call start()
			impl.checkStarted();
		}
	}
}
