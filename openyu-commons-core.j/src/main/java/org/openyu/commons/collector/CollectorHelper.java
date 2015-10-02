package org.openyu.commons.collector;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.collector.ex.CollectorException;
import org.openyu.commons.enumz.LongEnum;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.io.FileHelper;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.jaxb.JaxbHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.mark.Magicer;
import org.openyu.commons.misc.UnsafeHelper;
import org.openyu.commons.security.SecurityHelper;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.ChecksumHelper;
import org.openyu.commons.util.CompressHelper;
import org.openyu.commons.util.ConfigHelper;
import org.openyu.commons.util.SerializeHelper;

/**
 * Collector輔助類
 */
public class CollectorHelper extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(CollectorHelper.class);

	/**
	 * Instantiates a new helper.
	 */
	// private CollectorHelper() {
	public CollectorHelper() {
		if (InstanceHolder.INSTANCE != null) {
			throw new HelperException(
					new StringBuilder().append(getDisplayName()).append(" can not construct").toString());
		}
	}

	/**
	 * The Class InstanceHolder.
	 */
	private static class InstanceHolder {

		/** The Constant INSTANCE. */
		// private final static CollectorHelper INSTANCE = new
		// CollectorHelper();
		private static CollectorHelper INSTANCE = new CollectorHelper();
	}

	/**
	 * Gets the single instance of CollectorHelper.
	 *
	 * @return single instance of CollectorHelper
	 */
	// public static CollectorHelper getInstance() {
	// return InstanceHolder.INSTANCE;
	// }
	public synchronized static CollectorHelper getInstance() {
		if (InstanceHolder.INSTANCE == null) {
			InstanceHolder.INSTANCE = new CollectorHelper();
		}
		//
		if (!InstanceHolder.INSTANCE.isStarted()) {
			InstanceHolder.INSTANCE.setGetInstance(true);
			// 啟動
			InstanceHolder.INSTANCE.start();
		}
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 單例關閉
	 * 
	 * @return
	 */
	public synchronized static CollectorHelper shutdownInstance() {
		if (InstanceHolder.INSTANCE != null) {
			CollectorHelper oldInstance = InstanceHolder.INSTANCE;
			InstanceHolder.INSTANCE = null;
			//
			if (oldInstance != null) {
				oldInstance.shutdown();
			}
		}
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 單例刷新
	 * 
	 * @return
	 */
	public synchronized static CollectorHelper restartInstance() {
		if (InstanceHolder.INSTANCE != null) {
			InstanceHolder.INSTANCE.restart();
		}
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {

	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}

	// --------------------------------------------------
	/*
	 * 轉換步驟 object -> xml -> serial
	 */
	// --------------------------------------------------

	// --------------------------------------------------
	/*
	 * object -> byte[] -> file
	 */
	// --------------------------------------------------
	/**
	 * 序列化到檔案
	 * 
	 * object -> byte[] -> file
	 * 
	 * @param value
	 * @return 輸出檔名
	 */
	public static String writeToSer(Serializable value) {
		return writeToSer((Class<?>) null, value);
	}

	/**
	 * 序列化到檔案
	 * 
	 * object -> byte[] -> file
	 * 
	 * @param classFile
	 * @param value
	 * @return 輸出檔名
	 */
	public static String writeToSer(Class<?> classFile, Serializable value) {
		String fileName = (classFile != null ? classFile.getSimpleName() : value.getClass().getSimpleName());
		return writeToSer(fileName, value);
	}

	/**
	 * 序列化到檔案
	 * 
	 * object -> byte[] -> file
	 * 
	 * data/ser/xxx.ser
	 * 
	 * @param name
	 * @param value
	 * @return 輸出檔名
	 */
	public static String writeToSer(String name, Serializable value) {
		String result = null;
		//
		if (name == null) {
			throw new IllegalArgumentException("The Name must not be null");
		}
		//
		OutputStream fileOut = null;
		// ByteArrayOutputStream baos = null;
		byte[] out = new byte[0];
		byte[] dataOut = new byte[0];
		try {
			// 1.fst 序列化
			// byte[] bytes = SerializeHelper.fst(value);

			// #issue 尚未找到原因, 所以先用jdk序列化
			// java.lang.OutOfMemoryError: Java heap space
			// at java.util.HashMap.resize(HashMap.java:462)

			// 1.序列化
			// #issue: 較耗資源
			// SerializeProcessor serialable = new SerializeProcessorImpl();
			// serialable.setSerialize(ConfigHelper.isSerialize());
			// serialable.setSerializeType(ConfigHelper.getSerializeType());
			// byte[] buf = serialable.serialize(value);

			int pos = 0;
			// #fix: 使用pool
			byte[] serBytes = SerializeHelper.serializeWithProcessor(value);
			if (ByteHelper.isEmpty(serBytes)) {
				// 1.check serializeProcessor.isSerialize must be true
				// 2.check config-op.xml
				throw new CollectorException("Byte array is empty");
			}

			// 2.檢查碼
			long checksum = ChecksumHelper.checksumWithProcessor(serBytes);
			// System.out.println("checksum: " + checksum);
			byte[] checksumBytes = ByteHelper.toByteArray(checksum);

			// 先放檢查碼
			dataOut = UnsafeHelper.putByteArray(dataOut, pos, checksumBytes);
			pos += 8;
			// SystemHelper.println("檢查: ", dataOut);

			// 再放序列化byte[]
			dataOut = UnsafeHelper.putByteArray(dataOut, pos, serBytes);
			pos += serBytes.length;
			// SystemHelper.println("序列: ", dataOut);

			byte[] buf = UnsafeHelper.getByteArray(dataOut, 0, pos);

			// 3.加密
			// #issue: 較耗資源
			// SecurityProcessor securityable = new SecurityProcessorImpl();
			// securityable.setSecurity(ConfigHelper.isSecurity());
			// securityable.setSecurityType(ConfigHelper.getSecurityType());
			// securityable.setSecurityKey(ConfigHelper.getSecurityKey());
			// buf = securityable.encrypt(buf);

			// #fix: 使用pool
			buf = SecurityHelper.encryptWithProcessor(buf);
			// SystemHelper.println("加密: ", buf);

			// 4.壓縮
			// #issue: 較耗資源
			// CompressProcessor compressable = new CompressProcessorImpl();
			// compressable.setCompress(ConfigHelper.isCompress());
			// compressable.setCompressType(ConfigHelper.getCompressType());
			// buf = compressable.compress(buf);

			// #fix: 使用pool
			buf = CompressHelper.compressWithProcessor(buf);
			// SystemHelper.println("壓縮: ", buf);

			//
			// baos = new ByteArrayOutputStream();
			// baos.write(MagicType.SER.getBytes());// length=4
			// baos.write(buf);
			//
			pos = 0;
			out = UnsafeHelper.putByteArray(out, pos, MagicType.SER.toByteArray());// length=4
			pos += 4;
			//
			out = UnsafeHelper.putByteArray(out, pos, buf);
			pos += buf.length;
			// SystemHelper.println("輸出: ", out);

			// data/ser/xxx.ser
			String serName = serName(name);
			fileOut = IoHelper.createOutputStream(serName);

			// boolean writed = IoHelper.write(out, baos.toByteArray());
			boolean writed = IoHelper.write(fileOut, out);
			if (writed) {
				result = serName;
			}
		} catch (Exception ex) {
			throw new CollectorException(ex);
		} finally {
			// IoHelper.close(baos);
			IoHelper.close(fileOut);
		}
		return result;
	}

	/**
	 * ser檔名
	 * 
	 * @param name
	 * @return
	 */
	protected static String serName(String name) {
		StringBuilder result = new StringBuilder();
		result.append(ConfigHelper.getSerDir());
		result.append(File.separator);
		result.append(name);
		result.append(".ser");
		return result.toString();
	}

	/**
	 * 從檔案反序列化
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T readFromSer(Class<?> clazz) {
		T result = null;
		//
		if (clazz != null) {
			result = readFromSer(clazz, clazz.getSimpleName());
		}
		return result;
	}

	/**
	 * 從檔案反序列化
	 * 
	 * @param name
	 * @return
	 */
	public static <T> T readFromSer(Class<?> clazz, String name) {
		T result = null;
		//
		if (clazz == null) {
			throw new IllegalArgumentException("The Clazz must not be null");
		}
		if (name == null) {
			throw new IllegalArgumentException("The Name must not be null");
		}
		//
		InputStream in = null;
		byte[] dataIn = new byte[0];
		try {
			String serName = serName(name);
			// 判斷檔案是否存在
			boolean exist = FileHelper.isExist(serName);
			if (!exist) {
				throw new CollectorException(serName + " File does not exist");
			}
			//
			in = IoHelper.createInputStream(serName);
			byte[] values = IoHelper.read(in);
			if (ByteHelper.isEmpty(values)) {
				throw new CollectorException("Byte array is empty");
			}
			// SystemHelper.println("輸入: ", values);
			int pos = 0;
			//
			byte[] magicTypeBytes = UnsafeHelper.getByteArray(values, pos, 4);
			long magicTypeValue = ByteHelper.fromIntLong(magicTypeBytes);
			pos += 4;

			// System.out.println("magicTypeValue: " + magicTypeValue);
			if (MagicType.SER.getValue() != magicTypeValue) {
				throw new CollectorException("Invalid ser file [" + serName + "]");
			}
			byte[] data = UnsafeHelper.getByteArray(values, pos, values.length - pos);
			// SystemHelper.println("解壓: ", data);

			// --------------------------------------------------
			// 搭配writeToSer的演算法,反解回來
			// --------------------------------------------------
			// 4.解壓
			// #issue: 較耗資源
			// CompressProcessor compressable = new CompressProcessorImpl();
			// compressable.setCompress(ConfigHelper.isCompress());
			// compressable.setCompressType(ConfigHelper.getCompressType());
			// data = compressable.uncompress(data);

			// #fix: 使用pool
			data = CompressHelper.decompressWithProcessor(data);
			// SystemHelper.println("解密: ", data);

			// 3.解密
			// #issue: 較耗資源
			// SecurityProcessor securityable = new SecurityProcessorImpl();
			// securityable.setSecurity(ConfigHelper.isSecurity());
			// securityable.setSecurityType(ConfigHelper.getSecurityType());
			// securityable.setSecurityKey(ConfigHelper.getSecurityKey());
			// data = securityable.decrypt(data);

			// #fix: 使用pool
			data = SecurityHelper.decryptWithProcessor(data);
			// SystemHelper.println("序列: ", data);
			//
			pos = 0;
			// 從遠端收到的checksum
			byte[] checksumBytes = UnsafeHelper.getByteArray(data, pos, 8);
			// SystemHelper.println("檢查: ", checksumBytes);
			pos += 8;
			AssertHelper.notNull(checksumBytes, "The Checksum must not be null");
			//
			long checksum = ByteHelper.toLong(checksumBytes);
			// System.out.println("checksum: " + checksum);

			// 2.檢查碼
			// 本地算出來的checksum
			dataIn = UnsafeHelper.getByteArray(data, pos, data.length - pos);
			long realChecksum = ChecksumHelper.checksumWithProcessor(dataIn);
			// System.out.println("realChecksum: " + realChecksum);

			AssertHelper.isTrue(realChecksum == checksum,
					"Checksum [" + realChecksum + "] not equal expected [" + checksum + "]");

			// 1.fst 反序列化
			// result = SerializeHelper.defst(bytes);

			// #issue 尚未找到原因, 所以先用jdk反序列化
			// java.lang.OutOfMemoryError: Java heap space
			// at java.util.HashMap.resize(HashMap.java:462)

			// 1.反序列化
			// #issue: 較耗資源
			// SerializeProcessor serialable = new SerializeProcessorImpl();
			// result = serialable.deserialize(ConfigHelper.getSerializeType(),
			// buf, clazz);

			// #fix 使用pool
			result = SerializeHelper.deserializeWithProcessor(dataIn, clazz);

			//
		} catch (Exception ex) {
			throw new CollectorException(ex);
		} finally {
			IoHelper.close(in);
		}
		return result;
	}

	// --------------------------------------------------
	// object -> xml
	// --------------------------------------------------
	/**
	 * object -> xml
	 * 
	 * @param clazz
	 *            實作class
	 * @param value
	 *            實作instance
	 * @return 輸出檔名
	 */
	public static String writeToXml(Class<?> clazz, Object value) {
		return writeToXml((Class<?>) null, clazz, value);
	}

	/**
	 * object -> xml
	 * 
	 * @param classFile
	 * @param clazz
	 * @param value
	 * @return 輸出檔名
	 */
	public static String writeToXml(Class<?> classFile, Class<?> clazz, Object value) {
		String result = null;
		String fileName = (classFile != null ? classFile.getSimpleName() : clazz.getSimpleName());
		result = writeToXml(fileName, clazz, value);
		return result;
	}

	/**
	 * object -> xml
	 * 
	 * data/xml/xxx.xml
	 * 
	 * @param name
	 * @param clazz
	 * @param value
	 * @return 輸出檔名
	 */
	public static String writeToXml(String name, Class<?> clazz, Object value) {
		String result = null;
		//
		if (name == null) {
			throw new IllegalArgumentException("The Name must not be null");
		}
		//
		OutputStream out = null;
		try {
			String xmlName = xmlName(name);
			//
			out = IoHelper.createOutputStream(xmlName);
			// jaxb
			boolean writed = JaxbHelper.marshal(value, out, clazz);
			if (writed) {
				result = xmlName;
			}
		} catch (Exception ex) {
			throw new CollectorException(ex);
		} finally {
			IoHelper.close(out);
		}
		return result;
	}

	/**
	 * xml檔名
	 * 
	 * @param name
	 * @return
	 */
	protected static String xmlName(String name) {
		StringBuilder result = new StringBuilder();
		result.append(ConfigHelper.getXmlDir());
		result.append(File.separator);
		result.append(name);
		result.append(".xml");
		return result.toString();
	}

	/**
	 * xml -> object
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T readFromXml(Class<?> clazz) {
		return readFromXml((Class<?>) null, clazz);
	}

	/**
	 * xml -> object
	 * 
	 * @param classFile
	 * @param clazz
	 * @return
	 */
	public static <T> T readFromXml(Class<?> classFile, Class<?> clazz) {
		String fileName = (classFile != null ? classFile.getSimpleName() : clazz.getSimpleName());
		return readFromXml(fileName, clazz);
	}

	/**
	 * xml -> object
	 * 
	 * @param name
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readFromXml(String name, Class<?> clazz) {
		T result = null;
		//
		if (name == null) {
			throw new IllegalArgumentException("The Name must not be null");
		}
		//
		InputStream in = null;
		try {
			String xmlName = xmlName(name);
			// 判斷檔案是否存在
			boolean exist = FileHelper.isExist(xmlName);
			if (!exist) {
				throw new CollectorException(xmlName + " File does not exist");
			}
			//
			in = IoHelper.createInputStream(xmlName);
			// jaxb
			result = (T) JaxbHelper.unmarshal(in, clazz);
		} catch (Exception ex) {
			throw new CollectorException(ex);
		} finally {
			IoHelper.close(in);
		}
		return result;
	}

	/**
	 * xml -> ser
	 * 
	 * @param clazz
	 * @return
	 */
	public static String writeToSerFromXml(Class<?> clazz) {
		String result = null;
		Serializable serializable = readFromXml(clazz);
		if (serializable != null) {
			result = writeToSer(serializable);
		}
		return result;
	}

	/**
	 * xml -> ser
	 * 
	 * @param classFile
	 * @param clazz
	 * @return
	 */
	public static String writeToSerFromXml(Class<?> classFile, Class<?> clazz) {
		String result = null;
		Serializable serializable = readFromXml(classFile, clazz);
		if (serializable != null) {
			result = writeToSer(classFile, serializable);
		}
		return result;
	}

	/**
	 * xml -> ser
	 * 
	 * readFromXml, writeToSer
	 * 
	 * @param name
	 * @param clazz
	 * @return 輸出檔名
	 */
	public static String writeToSerFromXml(String name, Class<?> clazz) {
		String result = null;
		Serializable serializable = readFromXml(name, clazz);
		if (serializable != null) {
			result = writeToSer(name, serializable);
		}
		return result;
	}

	/**
	 * ser -> xml
	 * 
	 * @param clazz
	 * @return
	 */
	public static String writeToXmlFromSer(Class<?> clazz) {
		String result = null;
		Serializable serializable = readFromSer(clazz);
		if (serializable != null) {
			result = writeToXml(clazz, serializable);
		}
		return result;
	}

	/**
	 * ser -> xml
	 * 
	 * @param classFile
	 * @param clazz
	 * @return
	 */
	public static String writeToXmlFromSer(Class<?> classFile, Class<?> clazz) {
		String result = null;
		Serializable serializable = readFromSer(classFile);
		if (serializable != null) {
			result = writeToXml(classFile, clazz, serializable);
		}
		return result;
	}

	/**
	 * 
	 * ser -> xml
	 * 
	 * readFromSer, writeToXml
	 * 
	 * @param name
	 * @param clazz
	 * @return
	 */
	public static String writeToXmlFromSer(String name, Class<?> clazz) {
		Serializable serializable = readFromSer(clazz, name);
		return writeToXml(name, clazz, serializable);
	}

	protected enum MagicType implements LongEnum,Magicer {
		/**
		 * 序列化用
		 * 
		 * long -> int
		 */
		SER(3155475134L) {
			public byte[] toByteArray() {
				if (byteArray == null) {
					byteArray = ByteHelper.toIntByteArray(getValue());
				}
				return byteArray;
			}
		},

		//
		;

		private final long value;

		protected byte[] byteArray;

		private MagicType(long value) {
			this.value = value;
		}

		public long getValue() {
			return value;
		}

		public abstract byte[] toByteArray();

	}
}
