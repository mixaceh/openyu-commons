package org.openyu.commons.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.dao.CommonDao;
import org.openyu.commons.dog.po.impl.DogPoImpl;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.NumberHelper;

public class CommonDaoImplTest extends BaseTestSupporter {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static CommonDao commonDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"org/openyu/commons/dao/testContext-dao.xml",//

		});
		commonDao = applicationContext.getBean("commonDao", CommonDao.class);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	// round: 0.02 [+- 0.02], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.03, time.warmup: 0.00,
	// time.bench: 0.03
	public void commonDao() {
		System.out.println(commonDao);
		assertNotNull(commonDao);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void close() {
		System.out.println(commonDao);
		assertNotNull(commonDao);
		applicationContext.close();
		// 多次,不會丟出ex
		applicationContext.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void refresh() {
		System.out.println(commonDao);
		assertNotNull(commonDao);
		applicationContext.refresh();
		// 多次,不會丟出ex
		applicationContext.refresh();
	}

	@Test
	// insert -> find -> delete
	//
	// 10 times: 1052 mills.
	// 10 times: 1172 mills.
	// 10 times: 1009 mills.
	//
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.39 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.39, time.warmup: 0.00,
	// time.bench: 0.39
	public void crud() {
		//
		final String ID = "TEST_DOG";
		// final String NAME = "TEST_NAME";

		int count = 10;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// random
			int randomNumber = NumberHelper.randomInt(0, 1000000);

			DogPoImpl dogPo = new DogPoImpl();
			String id = ID + randomNumber;
			dogPo.setId(id);

			// #deprecated, 改成 addName
			// LocaleNameEntity name = new LocaleNameEntitySupporter();
			// name.setLocale(Locale.TRADITIONAL_CHINESE);
			// name.setName("拉拉");
			// dogPo.getNames().add(name);
			// //
			// name = new LocaleNameEntitySupporter();
			// name.setLocale(Locale.US);
			// name.setName("LaLa");
			// dogPo.getNames().add(name);

			// #new 加入name
			dogPo.addName(Locale.TRADITIONAL_CHINESE, "拉拉");
			dogPo.addName(Locale.US, "LaLa");

			// create
			Serializable pk = commonDao.insert(dogPo);
			printInsert(i, pk);
			assertNotNull(pk);

			// retrieve
			DogPoImpl foundEntity = commonDao.find(DogPoImpl.class, dogPo.getSeq());
			printFind(i, foundEntity);
			assertEquals(id, foundEntity.getId());

			// update
			dogPo.setValid(true);
			int updated = commonDao.update(dogPo);
			printUpdate(i, updated);
			assertTrue(updated > 0);

			// delete
			DogPoImpl deletedEntity = commonDao.delete(DogPoImpl.class, dogPo.getSeq());
			printDelete(i, deletedEntity);
			assertNotNull(deletedEntity);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.30 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.30, time.warmup: 0.00,
	// time.bench: 0.30
	public void insert() {
		final String ID = "TEST_DOG";

		// random
		int randomNumber = NumberHelper.randomInt(0, 1000000);

		DogPoImpl dogPo = new DogPoImpl();
		dogPo.setId(ID + randomNumber);
		dogPo.setValid(true);
		// audit 已在EntityHelper.doAudit處理,故不需自行處理

		// AuditEntity audit = new AuditEntitySupporter();
		// audit.setCreateUser("sys");
		// audit.setCreateDate(DateHelper.today());
		// dogPo.setAudit(audit);

		// #deprecated, 改成 addName
		// LocaleNameEntity name = new LocaleNameEntitySupporter();
		// name.setLocale(Locale.TRADITIONAL_CHINESE);
		// name.setName("拉拉");
		// dogPo.getNames().add(name);
		// //
		// name = new LocaleNameEntitySupporter();
		// name.setLocale(Locale.US);
		// name.setName("LaLa");
		// dogPo.getNames().add(name);

		// #new 加入name
		dogPo.addName(Locale.TRADITIONAL_CHINESE, "拉拉");
		dogPo.addName(Locale.US, "LaLa");

		Serializable pk = commonDao.insert(dogPo);
		System.out.println("insert: " + (pk != null ? "[success]" : "[fail]"));
		assertNotNull(pk);

		DogPoImpl foundEntity = commonDao.find(DogPoImpl.class, dogPo.getSeq());
		assertEquals(dogPo.getId(), foundEntity.getId());
	}
}
