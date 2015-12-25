package org.openyu.commons.cat;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.cat.dao.CatDao;
import org.openyu.commons.cat.dao.CatLogDao;
import org.openyu.commons.cat.service.CatLogService;
import org.openyu.commons.cat.service.CatService;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class CatTestSupporter extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	protected static CatDao catDao;

	protected static CatService catService;

	//
	protected static CatLogDao catLogDao;

	protected static CatLogService catLogService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"applicationContext-bean.xml", //
				"applicationContext-i18n.xml", //
				"applicationContext-database.xml", //
				"applicationContext-database-log.xml", //
				"org/openyu/commons/cat/testContext-cat.xml", //

		});
		// ---------------------------------------------------

		// ---------------------------------------------------
		catDao = applicationContext.getBean("catDao", CatDao.class);
		catService = applicationContext.getBean("catService", CatService.class);
		//
		catLogDao = applicationContext.getBean("catLogDao", CatLogDao.class);
		catLogService = applicationContext.getBean("catLogService", CatLogService.class);

	}

	// --------------------------------------------------

	public static class BeanTest extends CatTestSupporter {

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void catDao() {
			System.out.println(catDao);
			assertNotNull(catDao);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void catService() {
			System.out.println(catService);
			assertNotNull(catService);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void catLogDao() {
			System.out.println(catLogDao);
			assertNotNull(catLogDao);
		}

		@Test
		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		public void catLogService() {
			System.out.println(catLogService);
			assertNotNull(catLogService);
		}
	}
}
