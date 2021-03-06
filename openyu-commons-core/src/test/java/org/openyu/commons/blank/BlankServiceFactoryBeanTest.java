package org.openyu.commons.blank;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BlankServiceFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static BlankServiceImpl blankServiceImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/blank/testContext-blank.xml",//

		});
		blankServiceImpl = (BlankServiceImpl) applicationContext.getBean("blankServiceFactoryBean");
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
		public void createService() throws Exception {
			BlankServiceFactoryBean<BlankService> factoryBean = new BlankServiceFactoryBean<BlankService>();
			BlankService service = factoryBean.createService();
			System.out.println(service);
			assertNotNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void shutdownService() throws Exception {
			BlankServiceFactoryBean<BlankService> factoryBean = new BlankServiceFactoryBean<BlankService>();
			factoryBean.start();
			BlankService service = (BlankService) factoryBean.getObject();
			System.out.println(service);
			assertNotNull(service);
			//
			// service = factoryBean.shutdownService();
			// 使用反射取protected方法
			Method method = getDeclaredMethod(BlankServiceFactoryBean.class, "shutdownService");
			service = (BlankService) method.invoke(factoryBean);
			assertNull(service);
			// 多次,不會丟出ex
			// service = factoryBean.shutdownService();
			service = (BlankService) method.invoke(factoryBean);
			assertNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void restartService() throws Exception {
			BlankServiceFactoryBean<BlankService> factoryBean = new BlankServiceFactoryBean<BlankService>();
			factoryBean.start();
			BlankService service = (BlankService) factoryBean.getObject();
			System.out.println(service);
			assertNotNull(service);
			//
			// service = factoryBean.restartService();
			// 使用反射取protected方法
			Method method = getDeclaredMethod(BlankServiceFactoryBean.class, "restartService");
			service = (BlankService) method.invoke(factoryBean);
			assertNotNull(service);
			// 多次,不會丟出ex
			// service = factoryBean.restartService();
			service = (BlankService) method.invoke(factoryBean);
			assertNotNull(service);
		}
	}

}
