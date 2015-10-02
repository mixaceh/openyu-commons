package org.openyu.commons.misc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.ClassHelper;

import sun.misc.Unsafe;

/**
 * The Class UnsafeHelper.
 */
public class UnsafeHelper extends BaseHelperSupporter {

	private static Unsafe unsafe;

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(UnsafeHelper.class);

	/** Bytes used in a boolean */
	public static final int SIZE_OF_BOOLEAN = 1;

	/** Bytes used in a byte */
	public static final int SIZE_OF_BYTE = 1;

	/** Bytes used in a char */
	public static final int SIZE_OF_CHAR = 2;

	/** Bytes used in a short */
	public static final int SIZE_OF_SHORT = 2;

	/** Bytes used in a medium */
	public static final int SIZE_OF_MEDIUM = 3;

	/** Bytes used in an int */
	public static final int SIZE_OF_INT = 4;

	/** Bytes used in a float */
	public static final int SIZE_OF_FLOAT = 4;

	/** Bytes used in a long */
	public static final int SIZE_OF_LONG = 8;

	/** Bytes used in a double */
	public static final int SIZE_OF_DOUBLE = 8;

	/** Default number of bytes */
	public static final int DEFAULT_BYTES = 32;

	/** Offset of a byte array */
	public static long BYTE_ARRAY_OFFSET;// 16

	/** Offset of a long array */
	public static long LONG_ARRAY_OFFSET;// 16

	/** Offset of a double array */
	public static long DOUBLE_ARRAY_OFFSET;// 16

	/** Offset of a double array */
	public static long OBJECT_ARRAY_OFFSET;// 16

	static {
		new Static();

	}

	protected static class Static {
		public Static() {
			buildUnsafe();
			// newUnsafe() ;
		}
	}

	/**
	 * 使用Field來取得, 較省資源
	 */
	protected static void buildUnsafe() {
		try {
			// #issue
			// Field field = Unsafe.class.getDeclaredField("theUnsafe");

			// #fix
			Field field = ClassHelper.getDeclaredField(Unsafe.class,
					"theUnsafe");
			field.setAccessible(true);
			unsafe = (Unsafe) field.get(null);

			int boo = unsafe.arrayBaseOffset(byte[].class);
			// It seems not all Unsafe implementations implement the following
			// method.
			unsafe.copyMemory(new byte[1], boo, new byte[1], boo, 1);
			//
			BYTE_ARRAY_OFFSET = unsafe.arrayBaseOffset(byte[].class);
			LONG_ARRAY_OFFSET = unsafe.arrayBaseOffset(long[].class);
			DOUBLE_ARRAY_OFFSET = unsafe.arrayBaseOffset(double[].class);
			OBJECT_ARRAY_OFFSET = unsafe.arrayBaseOffset(Object[].class);
		} catch (Exception ex) {
			throw new RuntimeException(
					"UnsafeHelper Failed to " + "get unsafe", ex);
		}
	}

	/**
	 * 使用建構子建構unsafe, 消耗較多資源
	 */
	protected static void newUnsafe() {
		try {
			Constructor<Unsafe> constructor = Unsafe.class
					.getDeclaredConstructor();
			constructor.setAccessible(true);
			unsafe = constructor.newInstance();
			//
			BYTE_ARRAY_OFFSET = unsafe.arrayBaseOffset(byte[].class);
			LONG_ARRAY_OFFSET = unsafe.arrayBaseOffset(long[].class);
			DOUBLE_ARRAY_OFFSET = unsafe.arrayBaseOffset(double[].class);
			OBJECT_ARRAY_OFFSET = unsafe.arrayBaseOffset(Object[].class);
		} catch (Exception ex) {
			throw new RuntimeException(
					"UnsafeHelper Failed to " + "new unsafe", ex);

		}
	}

	/**
	 * Instantiates a new UnsafeHelper.
	 */
	private UnsafeHelper() {
		super();
		if (InstanceHolder.INSTANCE != null) {
			throw new UnsupportedOperationException("Can not construct.");
		}
	}

	/**
	 * The Class InstanceHolder.
	 */
	private static class InstanceHolder {

		/** The Constant INSTANCE. */
		private static final UnsafeHelper INSTANCE = new UnsafeHelper();
	}

	/**
	 * Gets the single instance of UnsafeHelper.
	 *
	 * @return single instance of UnsafeHelper
	 */
	public static UnsafeHelper getInstance() {
		return InstanceHolder.INSTANCE;
	}

	// 1.use Unsafe 加入參數 java -Xbootclasspath:/usr/jdk1.7.0/jre/lib/rt.jar:.
	// com.mishadoff.magic.UnsafeClient

	// 2.或是直接使用UnsafeHelper.getUnsafe()
	public static Unsafe getUnsafe() {
		return unsafe;
	}

	public static byte getByte(byte[] data, int offset) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 1 : offset;
		return unsafe.getByte(data, BYTE_ARRAY_OFFSET + offset);
	}

	public static short getUnsignedByte(byte[] data, int offset) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 1 : offset;

		return (short) (unsafe.getByte(data, BYTE_ARRAY_OFFSET + offset) & 0xff);
	}

	/**
	 * 讀取byte[], 從offset開始, 讀取長度length
	 * 
	 * 與ByteHelper.getByteArray() 效率一樣
	 * 
	 * @param data
	 * @param offset
	 * @param length
	 * @return
	 */
	public static byte[] getByteArray(byte[] data, int offset, int length) {
		assert offset >= 0 : offset;
		assert offset <= data.length - length : offset;

		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = unsafe.getByte(data, BYTE_ARRAY_OFFSET + offset + i);
		}
		return result;
	}

	public static short getShort(byte[] data, int offset) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 2 : offset;

		return unsafe.getShort(data, BYTE_ARRAY_OFFSET + offset);
	}

	public static int getUnsignedShort(byte[] data, int offset) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 2 : offset;

		return unsafe.getShort(data, BYTE_ARRAY_OFFSET + offset) & 0xffff;
	}

	public static int getInt(byte[] data, int offset) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 4 : offset;

		return unsafe.getInt(data, BYTE_ARRAY_OFFSET + offset);
	}

	public static long getUnsignedInt(byte[] data, int offset) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 4 : offset;

		return unsafe.getInt(data, BYTE_ARRAY_OFFSET + offset) & 0xffffffffL;
	}

	public static void putByte(byte[] data, int offset, byte value) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 1 : offset;

		unsafe.putByte(data, BYTE_ARRAY_OFFSET + offset, value);
	}

	public static void putUnsignedByte(byte[] data, int offset, short value) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 1 : offset;
		assert value >= 0 : value;
		assert value < 1 << 8 : value;

		unsafe.putByte(data, BYTE_ARRAY_OFFSET + offset, (byte) value);
	}

	/**
	 * 類似 ArrayHelper.add(byte[] x, byte[] y)
	 * 
	 * 但多了個offset可以指定啟始位置
	 * 
	 * @param data
	 * @param offset
	 * @param value
	 * @return
	 */
	public static byte[] putByteArray(byte[] data, int offset, byte[] value) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 1 : offset;
		assert value != null : value;
		assert data.length == 0 : offset;

		// 當超出原本的data的長度, 會自動增長byte[]
		byte[] result = data;
		if ((offset + value.length) > data.length) {
			result = new byte[offset + value.length];
			byteArraycopy(data, 0, result, 0, data.length);
		}
		//
		for (int i = 0; i < value.length; i++) {
			unsafe.putByte(result, BYTE_ARRAY_OFFSET + offset + i, value[i]);
		}
		return result;
	}

	public static void putShort(byte[] data, int offset, short value) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 2 : offset;

		unsafe.putShort(data, BYTE_ARRAY_OFFSET + offset, value);
	}

	public static void putUnsignedShort(byte[] data, int offset, int value) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 2 : offset;
		assert value >= 0 : value;
		assert value < 1 << 16 : value;

		unsafe.putShort(data, BYTE_ARRAY_OFFSET + offset, (short) value);
	}

	public static void putInt(byte[] data, int offset, int value) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 4 : offset;

		unsafe.putInt(data, BYTE_ARRAY_OFFSET + offset, value);
	}

	public static void putUnsignedInt(byte[] data, int offset, long value) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 4 : offset;
		assert value >= 0 : value;
		assert value < 1L << 32 : value;

		unsafe.putInt(data, BYTE_ARRAY_OFFSET + offset, (int) value);
	}

	public static void putLong(byte[] data, int offset, long value) {
		assert offset >= 0 : offset;
		assert offset <= data.length - 8 : offset;

		unsafe.putLong(data, BYTE_ARRAY_OFFSET + offset, value);
	}

	/**
	 * byte[] 複製
	 * 
	 * 與ByteHelper.byteArraycopy() 效率一樣
	 * 
	 * @param src
	 * @param srcPos
	 * @param dest
	 * @param destPos
	 * @param length
	 */
	public static void byteArraycopy(byte[] src, int srcPos, byte[] dest,
			int destPos, int length) {
		assert srcPos >= 0 : srcPos;
		assert srcPos <= src.length - length : srcPos;
		assert destPos >= 0 : destPos;
		assert destPos <= dest.length - length : destPos;

		unsafe.copyMemory(src, BYTE_ARRAY_OFFSET + srcPos, dest,
				BYTE_ARRAY_OFFSET + destPos, length);
	}

	/**
	 * 这个实现返回class的自身内存大小
	 * 
	 * @param clazz
	 * @return
	 */
	public static long sizeOf(Class<?> clazz) {
		long maximumOffset = 0;
		Field[] fields = ClassHelper.getDeclaredFieldsAndCache(clazz);
		// do {
		// for (Field f : clazz.getDeclaredFields()) {
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {
				// int=12, long=16
				maximumOffset = Math.max(maximumOffset,
						unsafe.objectFieldOffset(field));
			}
		}
		// } while ((clazz = clazz.getSuperclass()) != null);
		return maximumOffset == 0 ? 0 : maximumOffset + 8;
	}

	/**
	 * 这个实现返回对象的自身内存大小
	 * 
	 * @param obj
	 * @return
	 */
	public static long sizeOf(Object obj) {
		HashSet<Field> fields = new HashSet<Field>();
		Class<?> c = obj.getClass();
		// while (c != Object.class) {
		Field[] fs = ClassHelper.getDeclaredFieldsAndCache(c);
		// for (Field f : c.getDeclaredFields()) {
		for (Field f : fs) {
			if ((f.getModifiers() & Modifier.STATIC) == 0) {
				fields.add(f);
			}
		}
		// c = c.getSuperclass();
		// }

		// get offset
		long maxSize = 0;
		for (Field f : fields) {
			long offset = unsafe.objectFieldOffset(f);
			if (offset > maxSize) {
				maxSize = offset;
			}
		}

		return ((maxSize / 8) + 1) * 8; // padding
	}

	/**
	 * 为了正确内存地址使用，将有符号的int类型强制转换成无符号的long类型的方法
	 * 
	 * @param value
	 * @return
	 */
	public static long normalize(int value) {
		if (value >= 0)
			return value;
		return (~0L >>> 32) & value;
	}

	public static long toAddress(Object obj) {
		Object[] array = new Object[] { obj };
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
		return normalize(unsafe.getInt(array, baseOffset));
	}

	public static Object fromAddress(long address) {
		Object[] array = new Object[] { null };
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
		unsafe.putLong(array, baseOffset, address);
		return array[0];
	}

	@SuppressWarnings("unchecked")
	public static <T> T allocateInstance(Class<?> clazz) {
		T result = null;
		try {
			result = (T) unsafe.allocateInstance(clazz);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static long allocateMemory(long size) {
		return unsafe.allocateMemory(size);
	}

	public static void copyMemory(Object srcBase, long srcOffset,
			Object destBase, long destOffset, long bytes) {
		unsafe.copyMemory(srcBase, srcOffset, destBase, destOffset, bytes);
	}
}
