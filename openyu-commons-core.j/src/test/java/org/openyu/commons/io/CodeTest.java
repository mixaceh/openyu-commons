package org.openyu.commons.io;

import java.net.URLDecoder;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class CodeTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	public void escapeHtml() throws Exception {
		// 中文資料庫
		// &#20013;&#25991;&#36039;&#26009;&#24235; //HTML Number

		String src = "&#20013;&#25991;&#36039;&#26009;&#24235;";

		// 解碼1
		String value = StringEscapeUtils.unescapeHtml(src);
		System.out.println(value); // 中文資料庫
		// 解碼2,fail
		// value = URLDecoder.decode("%20013%25991%36039%26009%24235");
		// System.out.println(value);

		// 編碼
		value = StringEscapeUtils.escapeHtml(value);
		System.out.println(value); // &#20013;&#25991;&#36039;&#26009;&#24235;

		// ---------------------------------------------------------
		// php,3個為一組
		src = "&#xE8;&#x91;&#x89;&#xE4;&#xBB;&#x81;&#xE5;&#x82;&#xB3;";
		//

		value = StringEscapeUtils.unescapeHtml(src);
		System.out.println(value);

		value = URLDecoder.decode("%E8%91%89%E4%BB%81%E5%82%B3");
		System.out.println(value);
	}
}
