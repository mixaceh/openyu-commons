package org.openyu.commons.commons.dbcp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BasicDataSourceFactoryBeanTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static BasicDataSource basicDataSource;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/commons/dbcp/testContext-dbcp.xml",//

		});
		basicDataSource = (BasicDataSource) applicationContext.getBean("basicDataSourceFactoryBean");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void basicDataSource() {
		System.out.println(basicDataSource);
		assertNotNull(basicDataSource);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(basicDataSource);
		assertNotNull(basicDataSource);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(basicDataSource);
		assertNotNull(basicDataSource);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}

//	/**
//	 * CreateInstanceTest
//	 */
//	public static class CreateInstanceTest extends BaseTestSupporter {
//
//		@Rule
//		public BenchmarkRule benchmarkRule = new BenchmarkRule();
//
//		@Test
//		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
//		public void createInstance() throws Exception {
//			BlankServiceFactoryBean<BlankService> factoryBean = new BlankServiceFactoryBean<BlankService>();
//			BlankService service = factoryBean.createBasicDataSource();
//			System.out.println(service);
//			assertNotNull(service);
//		}
//
//		@Test
//		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
//		public void shutdownInstance() throws Exception {
//			BlankServiceFactoryBean<BlankService> factoryBean = new BlankServiceFactoryBean<BlankService>();
//			factoryBean.start();
//			BlankService service = (BlankService) factoryBean.getObject();
//			System.out.println(service);
//			assertNotNull(service);
//			//
//			// service = factoryBean.shutdownInstance();
//			// 使用反射取protected方法
//			Method method = getDeclaredMethod(BlankServiceFactoryBean.class, "shutdownInstance");
//			service = (BlankService) method.invoke(factoryBean);
//			assertNull(service);
//			// 多次,不會丟出ex
//			// service = factoryBean.shutdownInstance();
//			service = (BlankService) method.invoke(factoryBean);
//			assertNull(service);
//		}
//
//		@Test
//		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
//		public void restartInstance() throws Exception {
//			BlankServiceFactoryBean<BlankService> factoryBean = new BlankServiceFactoryBean<BlankService>();
//			factoryBean.start();
//			BlankService service = (BlankService) factoryBean.getObject();
//			System.out.println(service);
//			assertNotNull(service);
//			//
//			// service = factoryBean.restartInstance();
//			// 使用反射取protected方法
//			Method method = getDeclaredMethod(BlankServiceFactoryBean.class, "restartInstance");
//			service = (BlankService) method.invoke(factoryBean);
//			assertNotNull(service);
//			// 多次,不會丟出ex
//			// service = factoryBean.restartInstance();
//			service = (BlankService) method.invoke(factoryBean);
//			assertNotNull(service);
//		}
//	}

}
