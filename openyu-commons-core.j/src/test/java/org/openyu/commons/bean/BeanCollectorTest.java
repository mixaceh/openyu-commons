package org.openyu.commons.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.bean.BeanCollector;
import org.openyu.commons.bean.TrueFalseOption.TrueFalseType;
import org.openyu.commons.bean.WhetherOption.WhetherType;
import org.openyu.commons.bean.impl.TrueFalseOptionImpl;
import org.openyu.commons.bean.impl.WhetherOptionImpl;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.dao.inquiry.Inquiry;
import org.openyu.commons.dao.inquiry.Order;
import org.openyu.commons.dao.inquiry.Sort;
import org.openyu.commons.dao.inquiry.Order.OrderType;
import org.openyu.commons.dao.inquiry.Pagination;
import org.openyu.commons.dao.inquiry.impl.InquiryImpl;
import org.openyu.commons.dao.inquiry.impl.OrderImpl;
import org.openyu.commons.dao.inquiry.impl.PaginationImpl;
import org.openyu.commons.dao.inquiry.impl.SortImpl;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BeanCollectorTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@Deprecated
	/**
	 * 只是為了模擬用,有正式xml後,不應再使用,以免覆蓋掉正式的xml
	 */
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void writeToXml() {
		BeanCollector collector = BeanCollector.getInstance(false);
		String result = null;

		// 是否(true/false)選項
		TrueFalseOption trueFalseOption = new TrueFalseOptionImpl();
		trueFalseOption.setId(TrueFalseType.TRUE);
		trueFalseOption.addName(Locale.TRADITIONAL_CHINESE, "是");
		trueFalseOption.addName(Locale.SIMPLIFIED_CHINESE, "是");
		trueFalseOption.addName(Locale.US, "True");
		collector.getTrueFalseOptions().add(trueFalseOption);
		//
		trueFalseOption = new TrueFalseOptionImpl();
		trueFalseOption.setId(TrueFalseType.FALSE);
		trueFalseOption.addName(Locale.TRADITIONAL_CHINESE, "否");
		trueFalseOption.addName(Locale.SIMPLIFIED_CHINESE, "否");
		trueFalseOption.addName(Locale.US, "False");
		collector.getTrueFalseOptions().add(trueFalseOption);

		// 是否(""/"true"/"false")選項
		WhetherOption whetherOption = new WhetherOptionImpl();
		whetherOption.setId(WhetherType.ALL);
		whetherOption.addName(Locale.TRADITIONAL_CHINESE, "全部");
		whetherOption.addName(Locale.SIMPLIFIED_CHINESE, "全部");
		whetherOption.addName(Locale.US, "All");
		collector.getWhetherOptions().add(whetherOption);
		//
		whetherOption = new WhetherOptionImpl();
		whetherOption.setId(WhetherType.TRUE);
		whetherOption.addName(Locale.TRADITIONAL_CHINESE, "是");
		whetherOption.addName(Locale.SIMPLIFIED_CHINESE, "是");
		whetherOption.addName(Locale.US, "True");
		collector.getWhetherOptions().add(whetherOption);
		//
		whetherOption = new WhetherOptionImpl();
		whetherOption.setId(WhetherType.FALSE);
		whetherOption.addName(Locale.TRADITIONAL_CHINESE, "否");
		whetherOption.addName(Locale.SIMPLIFIED_CHINESE, "否");
		whetherOption.addName(Locale.US, "False");
		collector.getWhetherOptions().add(whetherOption);

		// 查詢條件
		Inquiry inquiry = new InquiryImpl();
		collector.setInquiry(inquiry);

		// 每頁筆數選項
		Pagination pagination = new PaginationImpl();
		pagination.getPageSizes().add(10);
		pagination.getPageSizes().add(20);
		pagination.getPageSizes().add(50);
		pagination.getPageSizes().add(100);
		collector.getInquiry().setPagination(pagination);

		// 排序欄位選項
		Sort sort = new SortImpl();
		sort.setId("seq");
		sort.addName(Locale.TRADITIONAL_CHINESE, "序號");
		sort.addName(Locale.SIMPLIFIED_CHINESE, "序号");
		sort.addName(Locale.US, "Sequence");
		collector.getInquiry().getSorts().add(sort);

		// 排序方向選項
		Order order = new OrderImpl();
		order.setId(OrderType.ASC);
		order.addName(Locale.TRADITIONAL_CHINESE, "升冪");
		order.addName(Locale.SIMPLIFIED_CHINESE, "升幂");
		order.addName(Locale.US, "Ascending");
		collector.getInquiry().getOrders().add(order);
		//
		order = new OrderImpl();
		order.setId(OrderType.DESC);
		order.addName(Locale.TRADITIONAL_CHINESE, "降冪");
		order.addName(Locale.SIMPLIFIED_CHINESE, "降幂");
		order.addName(Locale.US, "Descending");
		collector.getInquiry().getOrders().add(order);

		//
		// order = new OrderImpl();
		// order.setId(OrderType.NATURAL);
		// order.addName(Locale.TRADITIONAL_CHINESE, "自然");
		// order.addName(Locale.SIMPLIFIED_CHINESE, "自然");
		// order.addName(Locale.US, "Natural");
		// collector.getInquiry().getOrders().add(order);

		//
		result = CollectorHelper.writeToXml(BeanCollector.class, collector);
		//
		System.out.println(result);
		assertNotNull(result);
		// jaxb 2,769 bytes
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void readFromXml() {
		BeanCollector result = null;
		//
		result = CollectorHelper.readFromXml(BeanCollector.class);
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	/**
	 * 常用的是這個
	 */
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void writeToSerFromXml() {
		String result = null;
		//
		result = CollectorHelper.writeToSerFromXml(BeanCollector.class);
		//
		System.out.println(result);
		assertNotNull(result);

		// jdk 3,951 bytes
		// fst 1,597 bytes
		// fst-lz4 673 bytes
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void writeToSer() {
		String result = null;
		//
		result = CollectorHelper.writeToSer(BeanCollector.class, BeanCollector.getInstance(false));
		//
		System.out.println(result);
		assertNotNull(result);

		// jdk 929 bytes
		// fst 307 bytes
		// fst-lz4 208 bytes
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void readFromSer() {
		BeanCollector result = null;
		//
		result = CollectorHelper.readFromSer(BeanCollector.class);
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = 1)
	public void getInstance() {
		BeanCollector result = null;
		//
		result = BeanCollector.getInstance();
		//
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void shutdownInstance() {
		BeanCollector instance = BeanCollector.getInstance();
		System.out.println(instance);
		assertNotNull(instance);
		//
		instance = BeanCollector.shutdownInstance();
		assertNull(instance);
		// 多次,不會丟出ex
		instance = BeanCollector.shutdownInstance();
		assertNull(instance);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void restartInstance() {
		BeanCollector instance = BeanCollector.getInstance();
		System.out.println(instance);
		assertNotNull(instance);
		//
		instance = BeanCollector.restartInstance();
		assertNotNull(instance);
		// 多次,不會丟出ex
		instance = BeanCollector.restartInstance();
		assertNotNull(instance);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void getTrueFalseName() {
		BeanCollector collector = BeanCollector.getInstance();
		String result = null;
		//
		result = collector.getTrueFalseName(true, Locale.TRADITIONAL_CHINESE);
		//
		System.out.println(result);
		assertEquals("是", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void getWhetherName() {
		BeanCollector collector = BeanCollector.getInstance();
		String result = null;
		//
		result = collector.getWhetherName("all", Locale.TRADITIONAL_CHINESE);
		//
		System.out.println(result);
		assertEquals("全部", result);
		//
		result = collector.getWhetherName("not exist", Locale.TRADITIONAL_CHINESE);
		System.out.println(result);
		assertNull(result);
	}

}
