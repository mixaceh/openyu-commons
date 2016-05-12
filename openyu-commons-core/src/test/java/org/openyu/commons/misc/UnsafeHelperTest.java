package org.openyu.commons.misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.SystemHelper;

import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

public class UnsafeHelperTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test(expected = SecurityException.class)
	public void singletonGetter() throws Exception {
		Unsafe.getUnsafe();
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.19, GC: 4
	public void buildUnsafe() throws Exception {
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			UnsafeHelper.buildUnsafe();
		}
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.15, GC: 7
	public void newUnsafe() throws Exception {
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			UnsafeHelper.newUnsafe();
		}
	}

	private static class ClassWithExpensiveConstructor {

		private final int value;

		private ClassWithExpensiveConstructor() {
			value = doExpensiveLookup();
		}

		private int doExpensiveLookup() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 1;
		}

		public int getValue() {
			return value;
		}
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	public void objectCreation() throws Exception {
		Unsafe unsafe = UnsafeHelper.getUnsafe();

		ClassWithExpensiveConstructor instance = (ClassWithExpensiveConstructor) unsafe
				.allocateInstance(ClassWithExpensiveConstructor.class);
		assertEquals(0, instance.getValue());
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	public void reflectionFactory() throws Exception {
		@SuppressWarnings("unchecked")
		Constructor<ClassWithExpensiveConstructor> silentConstructor = ReflectionFactory.getReflectionFactory()
				.newConstructorForSerialization(ClassWithExpensiveConstructor.class, Object.class.getConstructor());
		silentConstructor.setAccessible(true);
		assertEquals(0, silentConstructor.newInstance().getValue());
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// #issue: round: 0.51
	// #fix: round: 0.10, ClassHelper.getDeclaredField
	// #fix: round: 0.00, static {}
	public void getUnsafe() throws Exception {
		Unsafe result = null;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = UnsafeHelper.getUnsafe();
		}
		System.out.println(result);
	}

	@Test
	public void objectAllocation() throws Exception {
		Unsafe unsafe = UnsafeHelper.getUnsafe();

		long containerSize = UnsafeHelper.sizeOf(Container.class);
		System.out.println("sizeOf: " + containerSize);
		long address = unsafe.allocateMemory(containerSize);
		Container c1 = new Container(10, 1000L);
		Container c2 = new Container(5, -10L);
		place(c1, address);
		place(c2, address + containerSize);
		Container newC1 = (Container) read(Container.class, address);
		Container newC2 = (Container) read(Container.class, address + containerSize);
		assertEquals(c1, newC1);
		assertEquals(c2, newC2);
	}

	private static class OtherClass {

		private final int value;
		private final int unknownValue;

		private OtherClass() {
			System.out.println("test");
			this.value = 10;
			this.unknownValue = 20;
		}
	}

	@Test
	public void strangeReflectionFactory() throws Exception {
		@SuppressWarnings("unchecked")
		Constructor<ClassWithExpensiveConstructor> silentConstructor = ReflectionFactory.getReflectionFactory()
				.newConstructorForSerialization(ClassWithExpensiveConstructor.class,
						OtherClass.class.getDeclaredConstructor());
		silentConstructor.setAccessible(true);
		ClassWithExpensiveConstructor instance = silentConstructor.newInstance();
		assertEquals(10, instance.getValue());
		assertEquals(ClassWithExpensiveConstructor.class, instance.getClass());
		assertEquals(Object.class, instance.getClass().getSuperclass());
	}

	private static class SuperContainer {

		protected int i;

		private SuperContainer(int i) {
			this.i = i;
		}

		public int getI() {
			return i;
		}
	}

	private static class Container extends SuperContainer {

		private long l;

		private Container(int i, long l) {
			super(i);
			this.l = l;
		}

		public long getL() {
			return l;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Container container = (Container) o;
			return l == container.l && i == container.i;
		}
	}

	protected void place(Object o, long address) throws Exception {
		Unsafe unsafe = UnsafeHelper.getUnsafe();

		Class<?> clazz = o.getClass();
		do {
			for (Field f : clazz.getDeclaredFields()) {
				if (!Modifier.isStatic(f.getModifiers())) {
					long offset = unsafe.objectFieldOffset(f);
					if (f.getType() == long.class) {
						unsafe.putLong(address + offset, unsafe.getLong(o, offset));
					} else if (f.getType() == int.class) {
						unsafe.putInt(address + offset, unsafe.getInt(o, offset));
					} else {
						throw new UnsupportedOperationException();
					}
				}
			}
		} while ((clazz = clazz.getSuperclass()) != null);
	}

	protected static Object read(Class<?> clazz, long address) throws Exception {
		Unsafe unsafe = UnsafeHelper.getUnsafe();

		Object instance = unsafe.allocateInstance(clazz);
		do {
			for (Field f : clazz.getDeclaredFields()) {
				if (!Modifier.isStatic(f.getModifiers())) {
					long offset = unsafe.objectFieldOffset(f);
					if (f.getType() == long.class) {
						unsafe.putLong(instance, offset, unsafe.getLong(address + offset));
					} else if (f.getType() == int.class) {
						unsafe.putInt(instance, offset, unsafe.getInt(address + offset));
					} else {
						throw new UnsupportedOperationException();
					}
				}
			}
		} while ((clazz = clazz.getSuperclass()) != null);
		return instance;
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test(expected = Exception.class)
	public void throwChecked() throws Exception {
		throwException();
	}

	protected static void throwException() {
		Unsafe unsafe = UnsafeHelper.getUnsafe();
		unsafe.throwException(new Exception());
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	public void copyMemory() throws Exception {
		Unsafe unsafe = UnsafeHelper.getUnsafe();
		// System.out.println(unsafe);
		//
		long address = unsafe.allocateMemory(4L);
		System.out.println(address);
		unsafe.putInt(address, 100);
		//
		System.out.println(unsafe.getInt(address));// 100
		assertEquals(100, unsafe.getInt(address));
		//
		long otherAddress = unsafe.allocateMemory(4L);
		unsafe.copyMemory(address, otherAddress, 4L);
		System.out.println(unsafe.getInt(otherAddress));// 100
		assertEquals(100, unsafe.getInt(otherAddress));
		//
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	public void arrayBaseOffset() throws Exception {
		Unsafe unsafe = UnsafeHelper.getUnsafe();
		long address = unsafe.allocateMemory(78);
		System.out.println(address);
		//
		long BYTE_ARRAY_OFFSET = unsafe.arrayBaseOffset(byte[].class);
		System.out.println("BYTE_ARRAY_OFFSET: " + BYTE_ARRAY_OFFSET);// 16
		//
		long LONG_ARRAY_OFFSET = unsafe.arrayBaseOffset(long[].class);
		System.out.println("LONG_ARRAY_OFFSET: " + LONG_ARRAY_OFFSET);// 16
		//
		long DOUBLE_ARRAY_OFFSET = unsafe.arrayBaseOffset(double[].class);
		System.out.println("DOUBLE_ARRAY_OFFSET: " + DOUBLE_ARRAY_OFFSET);// 16
		//
		byte four_bytes[] = { 25, 25, 25, 25 };
		Object trash[] = new Object[] { four_bytes };
		long OBJECT_ARRAY_OFFSET = unsafe.arrayBaseOffset(Object[].class);
		System.out.println("OBJECT_ARRAY_OFFSET: " + OBJECT_ARRAY_OFFSET);// 16

		long four_bytes_address = unsafe.getLong(trash, OBJECT_ARRAY_OFFSET);
		System.out.println("four_bytes_address: " + four_bytes_address);// 4119923536
		//
		byte[] value = new byte[3];
		value[0] = 0;
		value[1] = 1;
		value[2] = 2;
		SystemHelper.println(value);
		//
		System.out.println(unsafe.getByte(value, BYTE_ARRAY_OFFSET + 0));
		System.out.println(unsafe.getByte(value, BYTE_ARRAY_OFFSET + 1));
		System.out.println(unsafe.getByte(value, BYTE_ARRAY_OFFSET + 2));
		//
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// 1000000 times: 79 mills.
	// round: 0.08, GC:11
	public void getByte() throws Exception {
		String value = "abcdef1234567890";
		byte result = 0;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = UnsafeHelper.getByte(value.getBytes(), 2);
		}
		//
		System.out.println(result);
		System.out.println(new String(new byte[] { result }));
		assertEquals(99, result);// c
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// 1000000 times: 79 mills.
	// round: 0.09, GC:13
	public void getByteArray() throws Exception {
		String value = "abcdef1234567890";
		byte[] result = null;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = UnsafeHelper.getByteArray(value.getBytes(), 2, 4);
		}
		//
		System.out.println(ByteHelper.toString(result));

		assertEquals("cdef", new String(result));
		assertEquals(4, result.length);
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// 1000000 times: 5 mills.
	// round: 0.01, GC: 0
	public void byteArraycopy() throws Exception {
		String value = "abcdef1234567890";
		byte[] buf = value.getBytes();
		byte[] result = new byte[buf.length];
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			UnsafeHelper.byteArraycopy(buf, 0, result, 0, buf.length);
		}
		//
		System.out.println(ByteHelper.toString(result));
		assertArrayEquals(buf, result);
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// 1000000 times: 12 mills.
	// round: 0.01, GC: 0
	public void putByteArray() throws Exception {
		String value = "abcdef1234567890";
		byte[] buf = value.getBytes();// length=16
		byte[] result = new byte[20];
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = UnsafeHelper.putByteArray(result, 2, buf);
		}
		//
		SystemHelper.println(result);
		//
		assertEquals(0, result[0]);
		assertEquals(0, result[1]);
		assertEquals(97, result[2]);
		assertEquals(98, result[3]);
		assertEquals(99, result[4]);
		//
		result = new byte[20];// data
		// 當超出原本的data的長度, 會自動增長byte[]
		result = UnsafeHelper.putByteArray(result, 6, buf);
		System.out.println("length: " + result.length);// 22
		SystemHelper.println(result);
		assertEquals(57, result[20]);
		assertEquals(48, result[21]);
		//
		result = new byte[0];// data
		result = UnsafeHelper.putByteArray(result, result.length, buf);
		System.out.println("length: " + result.length);// 16
		// 97, 98, 99, 100, 101, 102, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48
		SystemHelper.println(result);
		//
		buf = new byte[] { 0, 0, 1, 1 };
		result = UnsafeHelper.putByteArray(result, result.length, buf);
		System.out.println("length: " + result.length);// 20
		// 97, 98, 99, 100, 101, 102, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 0,
		// 0, 1, 1
		SystemHelper.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// 1000000 times: 12 mills.
	// round: 0.01, GC: 0
	public void putByteArraySameAdd() throws Exception {
		byte[] x = new byte[] { 1, 2, 3 };
		byte[] y = new byte[] { 4, 5, 6 };
		//
		byte[] result = new byte[0];
		//
		UnsafeHelper.putByteArray(result, 1, x);
		// UnsafeHelper.putByteArray(result, packBytes.length, lengthBytes);
		// UnsafeHelper.putByteArray(result, lengthBytes.length, bodyBytes);
		//
		SystemHelper.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// 1000000 times: 12 mills.
	// round: 0.01, GC: 0
	public void addByteArray() throws Exception {

		byte[] result = new byte[0];
		//
		byte[] packBytes = new byte[] { 0, 0, 0, 1 };
		byte[] lengthBytes = new byte[] { 0, 1, 0, 0 };
		byte[] bodyBytes = new byte[] { 1, 1, 1, 1 };
		//
		result = ArrayHelper.add(packBytes, lengthBytes);
		result = ArrayHelper.add(result, bodyBytes);
		//
		// 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1
		SystemHelper.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	// #issue:
	// 1000000 times: 227 mills.
	// round: 0.23, GC: 11
	//
	// #fix: use ClassHelper.getDeclaredFieldsAndCache
	// 1000000 times: 242 mills.
	// round: 0.23, GC: 8
	public void sizeOf() throws Exception {
		long result = 0;
		//
		final int COUNT = 1000;
		for (int i = 0; i < COUNT; i++) {
			result = UnsafeHelper.sizeOf(Container.class);
		}
		//
		System.out.println(result);
		assertEquals(24, result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// #issue:
	// 1000000 times: 287 mills.
	// round: 0.29, GC: 52
	//
	// #fix: use ClassHelper.getDeclaredFieldsAndCache
	// 1000000 times: 450 mills.
	// round: 0.37, GC: 47
	public void sizeOfObject() throws Exception {
		long result = 0;
		//
		result = UnsafeHelper.sizeOf(new Container(1, 1L));
		//
		System.out.println(result);
		assertEquals(24, result);
		//
		result = UnsafeHelper.sizeOf(new A());
		System.out.println(result);
		//
		result = UnsafeHelper.sizeOf(new B());
		System.out.println(result);

	}

	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	@Test
	public void allocateMemory() throws Exception {
		long address = UnsafeHelper.allocateMemory(78);
		System.out.println("address: " + address);
		System.out.println("0x" + Long.toHexString(address));
		//
		Unsafe unsafe = UnsafeHelper.getUnsafe();
		//
		unsafe.putInt(address, 12345);
		System.out.println(unsafe.getInt(address));
		//
		unsafe.putLong(address + 4, 67890L);
		System.out.println(unsafe.getLong(address + 4));
	}

	public class A {
	}
	
	public class B {
		private B b;
	}

}
