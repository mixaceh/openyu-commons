package org.openyu.commons.security.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.security.AuthKeyService;
import org.openyu.commons.thread.impl.ThreadServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AuthKeyFactoryBeanTest extends BaseTestSupporter {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static AuthKeyServiceImpl authKeyServiceImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", // test目錄下
				"org/openyu/commons/thread/applicationContext-thread.xml", // test目錄下
				"org/openyu/commons/service/applicationContext-service.xml", //
				"org/openyu/commons/security/applicationContext-security.xml",// test目錄下

		});
		authKeyServiceImpl = (AuthKeyServiceImpl) applicationContext.getBean("authKeyFactoryBean");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void authKeyServiceImpl() {
		System.out.println(authKeyServiceImpl);
		assertNotNull(authKeyServiceImpl);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(authKeyServiceImpl);
		assertNotNull(authKeyServiceImpl);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(authKeyServiceImpl);
		assertNotNull(authKeyServiceImpl);
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
			AuthKeyFactoryBean<AuthKeyService> factoryBean = new AuthKeyFactoryBean<AuthKeyService>();
			factoryBean.setThreadService(ThreadServiceImpl.getInstance());
			//
			AuthKeyService service = factoryBean.createInstance();
			System.out.println(service);
			assertNotNull(service);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void shutdownInstance() throws Exception {
			AuthKeyFactoryBean<AuthKeyService> factoryBean = new AuthKeyFactoryBean<AuthKeyService>();
			factoryBean.setThreadService(ThreadServiceImpl.getInstance());
			//
			factoryBean.start();
			AuthKeyService service = factoryBean.getObject();
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
			AuthKeyFactoryBean<AuthKeyService> factoryBean = new AuthKeyFactoryBean<AuthKeyService>();
			factoryBean.setThreadService(ThreadServiceImpl.getInstance());
			//
			factoryBean.start();
			AuthKeyService service = factoryBean.getObject();
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
