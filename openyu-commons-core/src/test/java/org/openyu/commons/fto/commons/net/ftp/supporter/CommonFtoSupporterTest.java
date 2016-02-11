package org.openyu.commons.fto.commons.net.ftp.supporter;

import org.apache.commons.net.ftp.FTPFile;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.fto.supporter.CommonFtoSupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.SystemHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class CommonFtoSupporterTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static CommonFtoSupporter commonFtoSupporter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"applicationContext-bean.xml", //
				"applicationContext-net-ftp.xml",//
		});
		commonFtoSupporter = (CommonFtoSupporter) applicationContext.getBean("commonFtoSupporter");
	}

	@Test
	// 10 times: 12252 mills.
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	// round: 1.75 [+- 0.97], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 3.51, time.warmup: 0.00,
	// time.bench: 3.51
	public void listFiles() throws Exception {
		FTPFile[] result = null;
		result = commonFtoSupporter.listFiles();
		//
		SystemHelper.println(result);
	}

	@Test
	// 10 times: 12061 mills.
	@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency = 10)
	public void listNames() throws Exception {
		String[] result = null;
		int count = 10;
		//
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = commonFtoSupporter.listNames();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(result);
	}
}
