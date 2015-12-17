package org.openyu.commons.util;

import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.openyu.commons.helper.supporter.BaseHelperSupporter;
//import org.openyu.commons.commons.pool.CacheCallback;
//import org.openyu.commons.commons.pool.SoftReferenceCacheFactory;
//import org.openyu.commons.commons.pool.ex.CacheException;
//import org.openyu.commons.commons.pool.impl.SoftReferenceCacheFactoryImpl;
//import org.openyu.commons.commons.pool.supporter.CacheableObjectFactorySupporter;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.EncodingHelper;
//import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.util.impl.ChecksumProcessorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 檢查碼 The Class ChecksumHelper.
 * 
 * 為了確保資料的正確性
 * 
 * @see CheckSumType
 * 
 *      1.CRC32
 * 
 *      2.ADLER32
 */
public final class ChecksumHelperWithoutPool extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static transient final Logger LOGGER = LoggerFactory.getLogger(ChecksumHelperWithoutPool.class);

	public static final int BUFFER_LENGTH = 1024;

	/**
	 * crc32
	 * 
	 * @param value
	 * @return
	 */
	public static long crc32(String value) {
		return crc32(value, EncodingHelper.UTF_8);
	}

	/**
	 * crc32
	 * 
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static long crc32(String value, String charsetName) {
		return crc32(ByteHelper.toByteArray(value, charsetName));
	}

	public static long crc32(final byte[] value) {
		long result = 0;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		Checksum checksum = new CRC32();
		checksum.update(value, 0, value.length);
		result = checksum.getValue();
		//
		return result;
	}

	/**
	 * crc32
	 * 
	 * @param value
	 * @return
	 */
	public static long ___crc32(final byte[] value) {
		long result = 0;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		// Long retObj = (Long) crc32CacheFactory
		// .execute(new CacheCallback<CRC32>() {
		// public Object doInAction(CRC32 obj) throws CacheException {
		// try {
		// obj.update(value, 0, value.length);
		// return new Long(obj.getValue());
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// return new Long(0);
		// }
		// });
		// result = retObj.longValue();

		//
		return result;
	}

	/**
	 * crc32
	 * 
	 * @param inputStream
	 * @return
	 */
	public static long crc32(InputStream inputStream) {
		long result = 0;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		//
		try {
			Checksum checksum = new CRC32();
			// int read = 0;
			// byte[] value = new byte[BUFFER_LENGTH];
			// while ((read = in.read(value, 0, BUFFER_LENGTH)) > -1) {
			// checksum.update(value, 0, read);
			// }
			// result = checksum.getValue();

			byte[] value = IoHelper.read(inputStream);
			checksum.update(value, 0, value.length);
			result = checksum.getValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * crc32
	 * 
	 * @param inputStream
	 * @return
	 */
	public static long ___crc32(final InputStream inputStream) {
		long result = 0;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		//
		// Long retObj = (Long) crc32CacheFactory
		// .execute(new CacheCallback<CRC32>() {
		// public Object doInAction(CRC32 obj) throws CacheException {
		// try {
		// byte[] value = IoHelper.read(inputStream);
		// obj.update(value, 0, value.length);
		// return new Long(obj.getValue());
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// return new Long(0);
		// }
		// });
		// result = retObj.longValue();
		//
		return result;
	}

	/**
	 * crc32 as a byte[]
	 * 
	 * @param values
	 * @return
	 */
	public static byte[] crc32AsBytes(byte[] values) {
		return ByteHelper.toByteArray(crc32(values));
	}

	/**
	 * crc32 as a byte[]
	 * 
	 * @param in
	 * @return
	 */
	public static byte[] crc32AsBytes(InputStream in) {
		return ByteHelper.toByteArray(crc32(in));
	}

	/**
	 * crc32 as a hex string
	 * 
	 * @param value
	 * @return
	 */
	public static String crc32AsHex(String value) {
		return EncodingHelper.encodeHex(ByteHelper.toByteArray(crc32(value)));
	}

	/**
	 * crc32 as a hex string
	 * 
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static String crc32AsHex(String value, String charsetName) {
		return EncodingHelper.encodeHex(ByteHelper.toByteArray(crc32(value, charsetName)));
	}

	/**
	 * crc32 as a hex string
	 * 
	 * @param values
	 * @return
	 */
	public static String crc32AsHex(byte[] values) {
		return EncodingHelper.encodeHex(ByteHelper.toByteArray(crc32(values)));
	}

	/**
	 * crc32 as a hex string
	 * 
	 * @param in
	 * @return
	 */
	public static String crc32AsHex(InputStream in) {
		return EncodingHelper.encodeHex(ByteHelper.toByteArray(crc32(in)));
	}

	/**
	 * adler32
	 * 
	 * @param value
	 * @return
	 */
	public static long adler32(String value) {
		return adler32(value, EncodingHelper.UTF_8);
	}

	/**
	 * adler32
	 * 
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static long adler32(String value, String charsetName) {
		return adler32(ByteHelper.toByteArray(value, charsetName));
	}

	/**
	 * adler32
	 * 
	 * @param value
	 * @return
	 */
	public static long adler32(byte[] value) {
		long result = 0;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		try {
			Checksum checksum = new Adler32();
			checksum.update(value, 0, value.length);
			result = checksum.getValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * adler32
	 * 
	 * @param value
	 * @return
	 */
	public static long ___adler32(final byte[] value) {
		long result = 0;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		// Long retObj = (Long) adler32CacheFactory
		// .execute(new CacheCallback<Adler32>() {
		// public Object doInAction(Adler32 obj) throws CacheException {
		// try {
		// obj.update(value, 0, value.length);
		// return new Long(obj.getValue());
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// return new Long(0);
		// }
		// });
		// result = retObj.longValue();
		//
		return result;
	}

	/**
	 * adler32
	 * 
	 * @param inputStream
	 * @return
	 */
	public static long adler32(InputStream inputStream) {
		long result = 0;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		try {
			Checksum checksum = new Adler32();
			// int read = 0;
			// byte[] buffer = new byte[BUFFER_LENGTH];
			// while ((read = inputStream.read(buffer, 0, BUFFER_LENGTH)) >
			// -1) {
			// checksum.update(buffer, 0, read);
			// }
			// result = checksum.getValue();

			byte[] buff = IoHelper.read(inputStream);
			checksum.update(buff, 0, buff.length);
			result = checksum.getValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * adler32
	 * 
	 * @param inputStream
	 * @return
	 */
	public static long ___adler32(final InputStream inputStream) {
		long result = 0;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		//
		// Long retObj = (Long) adler32CacheFactory
		// .execute(new CacheCallback<Adler32>() {
		// public Object doInAction(Adler32 obj) throws CacheException {
		// try {
		// byte[] value = IoHelper.read(inputStream);
		// obj.update(value, 0, value.length);
		// return new Long(obj.getValue());
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// return new Long(0);
		// }
		// });
		// result = retObj.longValue();
		//
		return result;
	}

	/**
	 * adler32 as a byte[]
	 * 
	 * @param values
	 * @return
	 */
	public static byte[] adler32AsBytes(byte[] values) {
		return ByteHelper.toByteArray(adler32(values));
	}

	/**
	 * adler32 as a byte[]
	 * 
	 * @param in
	 * @return
	 */
	public static byte[] adler32AsBytes(InputStream in) {
		return ByteHelper.toByteArray(adler32(in));
	}

	/**
	 * adler32 as a hex string
	 * 
	 * @param value
	 * @return
	 */
	public static String adler32AsHex(String value) {
		return EncodingHelper.encodeHex(ByteHelper.toByteArray(adler32(value)));
	}

	/**
	 * adler32 as a hex string
	 * 
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static String adler32AsHex(String value, String charsetName) {
		return EncodingHelper.encodeHex(ByteHelper.toByteArray(adler32(value, charsetName)));
	}

	/**
	 * adler32 as a hex string
	 * 
	 * @param values
	 * @return
	 */
	public static String adler32AsHex(byte[] values) {
		return EncodingHelper.encodeHex(ByteHelper.toByteArray(adler32(values)));
	}

	/**
	 * adler32 as a hex string
	 * 
	 * @param in
	 * @return
	 */
	public static String adler32AsHex(InputStream in) {
		return EncodingHelper.encodeHex(ByteHelper.toByteArray(adler32(in)));
	}

	public static long checksumWithProcessor(final byte[] value) {
		long result = 0;
		//
		// Long retObj = (Long) checksumProcessorCacheFactory
		// .execute(new CacheCallback<ChecksumProcessor>() {
		// public Object doInAction(ChecksumProcessor obj)
		// throws CacheException {
		// try {
		// return obj.checksum(value);
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });
		// result = NumberHelper.safeGet(retObj);

		ChecksumProcessor obj = new ChecksumProcessorImpl();
		obj.setChecksum(ConfigHelper.isChecksum());
		obj.setChecksumType(ConfigHelper.getChecksumType());
		result = obj.checksum(value);
		//
		return result;
	}

	// refactor to Checksumable
	// /**
	// * 檢查碼
	// *
	// * @param checksumTypeValue
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static long execute(String checksumTypeValue, byte[] values,
	// String assignKey) {
	// ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
	// checksumTypeValue);
	// return execute(checksumType, values, assignKey);
	// }
	//
	// /**
	// * 檢查碼
	// *
	// * @param checksumTypeValue
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static long execute(int checksumTypeValue, byte[] values,
	// String assignKey) {
	// ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
	// checksumTypeValue);
	// return execute(checksumType, values, assignKey);
	// }
	//
	// /**
	// * 檢查碼
	// *
	// * @param checksumType
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static long execute(ChecksumType checksumType, byte[] values,
	// String assignKey) {
	// long result = 0;
	// //
	// if (checksumType == null) {
	// throw new IllegalArgumentException(
	// "The ChecksumType must not be null");
	// }
	// //
	// switch (checksumType) {
	// case CRC32: {
	// byte[] buff = values;
	// if (assignKey != null) {
	// buff = ArrayHelper.add(buff, ByteHelper.toByteArray(assignKey));
	// }
	// result = crc32(buff);
	// break;
	//
	// }
	// case ADLER32: {
	// byte[] buff = values;
	// if (assignKey != null) {
	// buff = ArrayHelper.add(buff, ByteHelper.toByteArray(assignKey));
	// }
	// result = adler32(buff);
	// break;
	// }
	// default: {
	// throw new UnsupportedOperationException(
	// "The ChecksumType is not unsupported" + checksumType);
	// }
	// }
	// //
	// return result;
	// }
	//
	// /**
	// * as a byte[]
	// *
	// * @param checksumTypeValue
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static byte[] executeAsBytes(String checksumTypeValue,
	// byte[] values, String assignKey) {
	// ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
	// checksumTypeValue);
	// return ByteHelper.toByteArray(execute(checksumType, values, assignKey));
	// }
	//
	// /**
	// * as a byte[]
	// *
	// * @param checksumTypeValue
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static byte[] executeAsBytes(int checksumTypeValue, byte[] values,
	// String assignKey) {
	// ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
	// checksumTypeValue);
	// return ByteHelper.toByteArray(execute(checksumType, values, assignKey));
	// }
	//
	// /**
	// * as a byte[]
	// *
	// * @param checksumType
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static byte[] executeAsBytes(ChecksumType checksumType,
	// byte[] values, String assignKey) {
	// return ByteHelper.toByteArray(execute(checksumType, values, assignKey));
	// }
	//
	// /**
	// * as a hex string
	// *
	// * @param checksumTypeValue
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static String executeAsHex(String checksumTypeValue, byte[]
	// values,
	// String assignKey) {
	// ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
	// checksumTypeValue);
	// return EncodingHelper.encodeHex(ByteHelper.toByteArray(execute(
	// checksumType, values, assignKey)));
	// }
	//
	// /**
	// * as a hex string
	// *
	// * @param checksumTypeValue
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static String executeAsHex(int checksumTypeValue, byte[] values,
	// String assignKey) {
	// ChecksumType checksumType = EnumHelper.valueOf(ChecksumType.class,
	// checksumTypeValue);
	// return EncodingHelper.encodeHex(ByteHelper.toByteArray(execute(
	// checksumType, values, assignKey)));
	// }
	//
	// /**
	// * as a hex string
	// *
	// * @param checksumType
	// * 檢查碼類別
	// * @see ChecksumType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static String executeAsHex(ChecksumType checksumType, byte[]
	// values,
	// String assignKey) {
	// return EncodingHelper.encodeHex(ByteHelper.toByteArray(execute(
	// checksumType, values, assignKey)));
	// }
}
