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

public class BlankFactoryBeanTest extends BaseTestSupporter {
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

		blankServiceImpl = (BlankServiceImpl) applicationContext.getBean("blankFactoryBean");
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
		public void createInstance() throws Exception {
			BlankFactoryBean<BlankService> factoryBean = new BlankFactoryBean<BlankService>();
			BlankService service = factoryBean.createInstance();
			System.out.println(service);
			assertNotNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void shutdownInstance() throws Exception {
			BlankFactoryBean<BlankService> factoryBean = new BlankFactoryBean<BlankService>();
			factoryBean.start();
			BlankService service = factoryBean.getObject();
			System.out.println(service);
			assertNotNull(service);
			//
			service = factoryBean.shutdownInstance();
			assertNull(service);
			// 多次,不會丟出ex
			service = factoryBean.shutdownInstance();
			assertNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void restartInstance() throws Exception {
			BlankFactoryBean<BlankService> factoryBean = new BlankFactoryBean<BlankService>();
			factoryBean.start();
			BlankService service = factoryBean.getObject();
			System.out.println(service);
			assertNotNull(service);
			//
			service = factoryBean.restartInstance();
			assertNotNull(service);
			// 多次,不會丟出ex
			service = factoryBean.restartInstance();
			assertNotNull(service);
		}
	}

}
