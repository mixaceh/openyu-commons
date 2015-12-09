package org.openyu.commons.security.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.security.AuthKey;
import org.openyu.commons.security.AuthKeyService;
import org.openyu.commons.security.impl.AuthKeyServiceImpl;
import org.openyu.commons.thread.ThreadHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class AuthKeyServiceImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static AuthKeyService authKeyService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/thread/testContext-thread.xml", //
				"org/openyu/commons/security/testContext-security.xml",//
		});
		authKeyService = (AuthKeyService) applicationContext.getBean("authKeyService");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void authKeyServiceImpl() {
		System.out.println(authKeyService);
		assertNotNull(authKeyService);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(authKeyService);
		assertNotNull(authKeyService);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(authKeyService);
		assertNotNull(authKeyService);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.08 [+- 0.01], round.block: 0.07 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 2, GC.time: 0.01, time.total: 0.10, time.warmup: 0.01,
	// time.bench: 0.09
	public void createAuthKey() {
		authKeyService.setSecurity(false);
		// aliveMills=180*1000,有效期限3分鐘
		AuthKey result = authKeyService.createAuthKey();
		// 16, df9a82e3-0a5e-45b7-887e-c6e1d40e64f8
		System.out.println(result.getId().length() + ", " + result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.27 [+- 0.03], round.block: 0.15 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 2, GC.time: 0.01, time.total: 0.29, time.warmup: 0.02,
	// time.bench: 0.28
	public void createAuthKeyWithSecurity() {
		authKeyService.setSecurity(true);
		// aliveMills=180*1000,有效期限3分鐘
		AuthKey result = authKeyService.createAuthKey();
		// 不定, df9a82e3-0a5e-45b7-887e-c6e1d40e64f8
		System.out.println(result.getId().length() + ", " + result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.71 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.71, time.warmup: 0.00,
	// time.bench: 0.71
	public void checkExpired() {
		// 建構key
		int count = 10;
		for (int i = 0; i < count; i++) {
			AuthKey authKey = authKeyService.createAuthKey();
			authKey.setAliveMills(0L);// 0=表都到期了
			// 加入到mem上
			authKeyService.addAuthKey(authKey.getId(), authKey);
			ThreadHelper.sleep(50);
		}
		int size = authKeyService.getAuthKeys().size();
		System.out.println(size);
		assertEquals(10, size);

		// 檢查key,應該都到期了
		((AuthKeyServiceImpl) authKeyService).checkExpired();
		//
		size = authKeyService.sizeOfAuthKey();
		System.out.println(size);
		assertEquals(0, size);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 100)
	// round: 0.07 [+- 0.01], round.block: 0.07 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 1, GC.time: 0.01, time.total: 0.10, time.warmup: 0.03,
	// time.bench: 0.07
	public void uuid() {
		// 716cf56a-8bff-4dcc-af1d-c1000bb7f494
		UUID result = UUID.randomUUID();
		System.out.println(result.toString());// length=36
		assertNotNull(result);
	}

}
