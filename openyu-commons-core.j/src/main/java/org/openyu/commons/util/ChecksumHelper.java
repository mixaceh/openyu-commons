package org.openyu.commons.util;

import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.openyu.commons.commons.pool.CacheCallback;
import org.openyu.commons.commons.pool.SoftReferenceCacheFactory;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.commons.pool.impl.SoftReferenceCacheFactoryFactoryBean;
import org.openyu.commons.commons.pool.supporter.CacheableObjectFactorySupporter;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.EncodingHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.util.impl.ChecksumProcessorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 檢查碼輔助類
 * 
 * 為了確保資料的正確性
 * 
 * @see CheckSumType
 * 
 *      1.CRC32
 * 
 *      2.ADLER32
 */
public final class ChecksumHelper extends BaseHelperSupporter {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(ChecksumHelper.class);

	public static final int BUFFER_LENGTH = 1024;

	/**
	 * 檢查碼處理器工廠
	 */
	private static SoftReferenceCacheFactoryFactoryBean<ChecksumProcessor, SoftReferenceCacheFactory<ChecksumProcessor>> checksumProcessorCacheFactoryFactoryBean;

	/**
	 * 檢查碼處理器
	 */
	private static SoftReferenceCacheFactory<ChecksumProcessor> checksumProcessorCacheFactory;

	static {
		new Static();
	}

	protected static class Static {
		@SuppressWarnings("unchecked")
		public Static() {
			try {
				checksumProcessorCacheFactoryFactoryBean = new SoftReferenceCacheFactoryFactoryBean<ChecksumProcessor, SoftReferenceCacheFactory<ChecksumProcessor>>();
				checksumProcessorCacheFactoryFactoryBean
						.setCacheableObjectFactory(new CacheableObjectFactorySupporter<ChecksumProcessor>() {

							private static final long serialVersionUID = -2745795176962911555L;

							public ChecksumProcessor makeObject() throws Exception {
								ChecksumProcessor obj = new ChecksumProcessorImpl();
								obj.setChecksum(ConfigHelper.isChecksum());
								obj.setChecksumType(ConfigHelper.getChecksumType());
								return obj;
							}

							public boolean validateObject(ChecksumProcessor obj) {
								return true;
							}

							public void activateObject(ChecksumProcessor obj) throws Exception {
								obj.setChecksum(ConfigHelper.isChecksum());
								obj.setChecksumType(ConfigHelper.getChecksumType());
							}

							public void passivateObject(ChecksumProcessor obj) throws Exception {
								obj.reset();
							}
						});
				checksumProcessorCacheFactoryFactoryBean.start();
				checksumProcessorCacheFactory = (SoftReferenceCacheFactory<ChecksumProcessor>) checksumProcessorCacheFactoryFactoryBean
						.getObject();
			} catch (Exception ex) {
				throw new HelperException("new Static() Initializing failed", ex);
			}
		}
	}

	private ChecksumHelper() {
		throw new HelperException(
				new StringBuilder().append(ChecksumHelper.class.getName()).append(" can not construct").toString());
	}

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
		Long retObj = (Long) checksumProcessorCacheFactory.execute(new CacheCallback<ChecksumProcessor>() {
			public Object doInAction(ChecksumProcessor obj) throws CacheException {
				return obj.checksum(value);
			}
		});
		result = NumberHelper.safeGet(retObj);
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
