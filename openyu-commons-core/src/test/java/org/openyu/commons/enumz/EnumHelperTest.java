package org.openyu.commons.enumz;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.SystemHelper;

/**
 * The Class EnumHelperTest.
 */
public class EnumHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void EnumHelper() throws Exception {
		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.enumz.EnumHelper");
		//
		Object result = null;
		//
		result = constructor.newInstance();
		assertNull(result);
	}

	/**
	 * Value of.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.46 [+- 0.05], round.block: 0.05 [+- 0.01], round.gc: 0.00
	// [+- 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.47, time.warmup:
	// 0.00, time.bench: 0.47
	public void valueOf() {
		Object result = null;
		//
		result = EnumHelper.valueOf(IntType.class, 1);
		//
		// System.out.println(result);// BIG
		assertEquals(IntType.BIG, result);
		//
		//
		result = EnumHelper.valueOf(BooleanType.class, true);
		assertEquals(BooleanType.TRUE, result);
		//
		result = EnumHelper.valueOf(ByteType.class, (byte) 2);
		assertEquals(ByteType.X02, result);
		//
		result = EnumHelper.valueOf(DoubleType.class, 3d);
		assertEquals(DoubleType.GRAY, result);
		//
		result = EnumHelper.valueOf(StringType.class, "a");
		assertEquals(StringType.A, result);
	}

	/**
	 * Name of.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.12 [+- 0.01], round.block: 0.03 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.13, time.warmup: 0.00,
	// time.bench: 0.13
	public void nameOf() {
		Object result = null;
		//
		result = EnumHelper.nameOf(IntType.class, "BIG");
		//
		System.out.println(result);// BIG
		assertEquals(IntType.BIG, result);
		//
		result = EnumHelper.nameOf(StringType.class, "A");
		assertEquals(StringType.A, result);
	}

	/**
	 * Check unique.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.48 [+- 0.05], round.block: 0.05 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.49, time.warmup: 0.00,
	// time.bench: 0.49
	public void checkDuplicate() {
		List<?> result = null;
		//
		result = EnumHelper.checkDuplicate(IntType.class);
		System.out.println(result);// SUPER_BIG 重複
		assertTrue(result.size() > 0);
		//
		result = EnumHelper.checkDuplicate(DoubleType.class);// RED 重複
		assertTrue(result.size() > 0);
	}

	/**
	 * Sum of.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.47 [+- 0.05], round.block: 0.04 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.48, time.warmup: 0.00,
	// time.bench: 0.48
	public void sumOf() {
		List<IntType> list = new LinkedList<IntType>();
		list.add(IntType.BIG);
		list.add(IntType.MEDIUM);
		//
		int result = 0;
		//
		result = (int) EnumHelper.sumOf(list);
		System.out.println(result);// 3
		assertEquals(3, result);
		//
		result = (int) EnumHelper.sumOf((Collection<IntType>) null);
		assertEquals(0, result);
		//
		result = (int) EnumHelper.sumOf(IntType.class);
		assertEquals(9, result);
		//
		result = (int) EnumHelper.sumOf((Class<IntType>) null);
		assertEquals(0, result);
	}

	/**
	 * Sum of by double.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void sumOfByDouble() {
		List<DoubleType> list = new LinkedList<DoubleType>();

		list.add(DoubleType.BLACK);
		list.add(DoubleType.WHITE);

		double result = 0d;
		//
		final int COUNT = 1;
		for (int i = 0; i < COUNT; i++) {
			result = EnumHelper.sumOf(list);
		}
		System.out.println(result);// 3.3
		assertEquals(0, Double.compare(3.3d, result));
		//
		result = EnumHelper.sumOf(DoubleType.class);
		assertEquals(0, Double.compare(9.3d, result));
	}

	/**
	 * Random type.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void randomType() {
		IntType result = null;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = EnumHelper.randomType(IntType.class);
		}
		System.out.println(result);
		assertNotNull(result);
		//
		result = EnumHelper.randomType(IntType.class);
		assertNotNull(result);
		//
		result = EnumHelper.randomType(null);
		assertNull(result);
	}

	/**
	 * Values.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.06
	public void values() {
		IntType[] result = null;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = EnumHelper.values(IntType.class);
		}
		SystemHelper.println(result);
		assertNotNull(result);
		//
		result = EnumHelper.values(null);
		assertNull(result);

		// issue: enumType.getEnumConstants(), 會消耗很多資源

		// BIG, MEDIUM, SMALL, SUPER_BIG
		// [GC 52480K->1328K(124544K), 0.0028027 secs]
		// BIG, MEDIUM, SMALL, SUPER_BIG
		// [GC 53808K->2063K(124544K), 0.0009330 secs]
		// BIG, MEDIUM, SMALL, SUPER_BIG
		// BIG, MEDIUM, SMALL, SUPER_BIG
		// [GC 54543K->1825K(124544K), 0.0008707 secs]
		// BIG, MEDIUM, SMALL, SUPER_BIG
		// EnumHelperTest.values: [measured 3 out of 5 rounds, threads: 1
		// (sequential)]
		// round: 0.01 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 2, GC.time: 0.00, time.total: 0.15, time.warmup:
		// 0.12, time.bench: 0.03
		//
		//
		// #fix: ClassHelper.getEnumConstantsAndCache(enumType), 大幅減少資源消耗, 但稍微慢,
		// 在可接受程度內

		// BIG, MEDIUM, SMALL, SUPER_BIG
		// BIG, MEDIUM, SMALL, SUPER_BIG
		// BIG, MEDIUM, SMALL, SUPER_BIG
		// BIG, MEDIUM, SMALL, SUPER_BIG
		// BIG, MEDIUM, SMALL, SUPER_BIG
		// EnumHelperTest.values: [measured 3 out of 5 rounds, threads: 1
		// (sequential)]
		// round: 0.07 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.74, time.warmup:
		// 0.55, time.bench: 0.20
	}

	/**
	 * List values.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void valuesList() {
		List<IntType> result = null;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = EnumHelper.valuesList(IntType.class);
		}
		System.out.println(result);
		assertNotNull(result);
		//
		result = EnumHelper.valuesList(null);
		assertEquals(0, result.size());
	}

	/**
	 * Sets the values.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void valuesSet() {
		Set<IntType> result = null;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = EnumHelper.valuesSet(IntType.class);
		}
		System.out.println(result);
		assertNotNull(result);
		//
		result = EnumHelper.valuesSet(null);
		assertEquals(0, result.size());
	}

	/**
	 * Safe get.
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// round: 0.00
	public void safeGet() {
		boolean result = false;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = EnumHelper.safeGet(BooleanType.TRUE);
		}
		System.out.println(result);
		assertTrue(result);
		//
		result = EnumHelper.safeGet((BooleanEnum) null);
		assertFalse(result);
		//
		byte byteValue = EnumHelper.safeGet(ByteType.X01);
		assertEquals((byte) 1, byteValue);
		//
		byteValue = EnumHelper.safeGet((ByteEnum) null);
		assertEquals((byte) 0, byteValue);
	}

	// --------------------------------------------------

	/**
	 * The Enum BooleanType.
	 */
	public enum BooleanType implements BooleanEnum {

		/** The true. */
		TRUE(true),

		/** The false. */
		FALSE(false),

		//
		/** The value. */
		;

		private final boolean value;

		/**
		 * Instantiates a new boolean type.
		 *
		 * @param value
		 *            the value
		 */
		private BooleanType(boolean value) {
			this.value = value;
		}

		public boolean getValue() {
			return value;
		}
	}

	//
	/**
	 * The Enum ByteType.
	 */
	public enum ByteType implements ByteEnum {

		/** The X01. */
		X01((byte) 1),

		/** The X02. */
		X02((byte) 2),

		/** The X03. */
		X03((byte) 3),

		/** The X04. */
		X04((byte) 3),

		//
		/** The value. */
		;

		private final byte value;

		/**
		 * Instantiates a new byte type.
		 *
		 * @param value
		 *            the value
		 */
		private ByteType(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

	}

	//
	/**
	 * The Enum IntType.
	 */
	public enum IntType implements IntEnum {

		/** The big. */
		BIG(1),

		/** The medium. */
		MEDIUM(2),

		/** The small. */
		SMALL(3),

		/** The super big. */
		SUPER_BIG(3),

		//
		/** The value. */
		;

		private final int value;

		/**
		 * Instantiates a new int type.
		 *
		 * @param value
		 *            the value
		 */
		private IntType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	//
	/**
	 * The Enum DoubleType.
	 */
	public enum DoubleType implements DoubleEnum {

		/** The black. */
		BLACK(1.1),

		/** The white. */
		WHITE(2.2),

		/** The gray. */
		GRAY(3d),

		/** The red. */
		RED(3d),;

		/** The value. */
		private double value;

		/**
		 * Instantiates a new double type.
		 *
		 * @param value
		 *            the value
		 */
		private DoubleType(double value) {
			this.value = value;
		}

		public double getValue() {
			return value;
		}

	}

	//
	/**
	 * The Enum StringType.
	 */
	public enum StringType implements StringEnum {

		/** The a. */
		A("a"),

		/** The b. */
		B("b"),

		/** The c. */
		C("c"),

		/** The d. */
		D("d"),;

		/** The value. */
		private String value;

		/**
		 * Instantiates a new string type.
		 *
		 * @param value
		 *            the value
		 */
		private StringType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}
}
