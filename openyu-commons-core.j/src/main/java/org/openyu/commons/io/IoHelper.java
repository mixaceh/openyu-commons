package org.openyu.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.jar.JarFile;

//#issue: 不支援中文檔名
//import java.util.zip.ZipOutputStream;
//#fix 改apache zip
import org.apache.tools.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.EncodingHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.thread.ThreadHelper;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 將save/load -> 改成read/write
 */
public final class IoHelper extends BaseHelperSupporter implements Supporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(IoHelper.class);

	/**
	 * 預設讀取重試次數
	 */
	public static final int DEFAULT_READ_RETRY_NUMBER = 15;

	/**
	 * 預設讀取重試暫停毫秒
	 */
	public static final long DEFAULT_READ_RETRY_PAUSE_MILLS = 100L;

	/**
	 * 讀取重試補償
	 */
	public static final int[] READ_RETRY_BACK_OFF = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2 };

	/**
	 * 預試讀取區塊長度
	 */
	public static final int DEFALUT_READ_BLOCK_LENGTH = 1024;// k

	private IoHelper() {
		throw new HelperException(
				new StringBuilder().append(IoHelper.class.getName()).append(" can not construct").toString());
	}

	/**
	 * 建構輸入串流
	 * 
	 * BufferedInputStream
	 * 
	 * @param fileName
	 * @return
	 */
	public static InputStream createInputStream(String fileName) {
		InputStream result = null;
		if (StringHelper.notBlank(fileName)) {
			result = createInputStream(new File(fileName));
		}
		return result;
	}

	/**
	 * 建構輸入串流
	 * 
	 * BufferedInputStream
	 * 
	 * @param file
	 * @return
	 */
	public static InputStream createInputStream(File file) {
		InputStream result = null;
		//
		try {
			if (FileHelper.isExist(file)) {
				result = new BufferedInputStream(new FileInputStream(file));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	// BufferedOutputStream
	public static OutputStream createOutputStream(String fileName) {
		OutputStream result = null;
		if (StringHelper.notBlank(fileName)) {
			result = createOutputStream(new File(fileName));
		}
		return result;
	}

	// BufferedOutputStream
	public static OutputStream createOutputStream(File file) {
		OutputStream result = null;
		if (file != null) {
			// 建目錄
			FileHelper.md(file.getPath(), true);
			try {
				result = new BufferedOutputStream(new FileOutputStream(file));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static ZipOutputStream createZipOutputStream(String value) {
		ZipOutputStream result = null;
		if (StringHelper.notBlank(value)) {
			result = createZipOutputStream(new File(value));
		}
		return result;
	}

	public static ZipOutputStream createZipOutputStream(File value) {
		ZipOutputStream result = null;
		if (value != null) {
			try {
				OutputStream out = createOutputStream(value);
				result = new ZipOutputStream(out);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// BufferedReader
	public static Reader createReader(String value) {
		Reader result = null;
		if (StringHelper.notBlank(value)) {
			result = createReader(new File(value));
		}
		return result;
	}

	// BufferedReader
	public static Reader createReader(File value) {
		Reader result = null;
		if (FileHelper.isExist(value)) {
			try {
				result = new BufferedReader(new FileReader(value));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// BufferedWriter
	public static Writer createWriter(String value) {
		Writer result = null;
		if (StringHelper.notBlank(value)) {
			result = createWriter(new File(value));
		}
		return result;
	}

	// BufferedWriter
	public static Writer createWriter(File value) {
		Writer result = null;
		if (value != null) {
			FileHelper.md(value.getPath(), true);
			try {
				result = new BufferedWriter(new FileWriter(value, false));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static PrintWriter createPrintWriter(String value) {
		PrintWriter result = null;
		if (StringHelper.notBlank(value)) {
			result = createPrintWriter(new File(value));
		}
		return result;
	}

	public static PrintWriter createPrintWriter(File value) {
		PrintWriter result = null;
		if (value != null) {
			// 建目錄
			FileHelper.md(value.getPath(), true);
			try {
				result = new PrintWriter(new BufferedOutputStream(new FileOutputStream(value)));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// --------------------------------------------------
	// 20110815
	// --------------------------------------------------

	/**
	 * 關閉reader
	 * 
	 * @param reader
	 */
	public static void close(Reader reader) {

		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	/**
	 * 關閉writer
	 * 
	 * @param writer
	 */
	public static void close(Writer writer) {
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	/**
	 * 關閉in串流
	 * 
	 * @param in
	 */
	public static void close(InputStream in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	/**
	 * 關閉out串流
	 * 
	 * @param out
	 */
	public static void close(OutputStream out) {

		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	/**
	 * 關閉in
	 * 
	 * @param in
	 */
	public static void close(ObjectInput in) {

		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	/**
	 * 關閉out
	 * 
	 * @param out
	 */
	public static void close(ObjectOutput out) {

		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	// --------------------------------------------------
	// 20110920
	// --------------------------------------------------
	/**
	 * 
	 * OutputStream -> InputStream
	 * 
	 * @param out
	 * @return
	 */
	public static InputStream toInputStream(OutputStream out) {
		ByteArrayInputStream result = null;
		if (out instanceof ByteArrayOutputStream) {
			try {
				result = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// /**
	// * 不要直接用inputStream.available(),網路上會間斷
	// *
	// * @param in
	// * @return
	// */
	// public static int available(InputStream in) {
	// int result = 0;
	// //
	// if (in == null) {
	// return result;
	// }
	//
	// try {
	// // #issue: 網路上會間斷
	// // result = inputStream.available();
	// // #fix: 網路上會間斷
	//
	// // #issue: 若已經read後,則會available=0,讀第2次時會迴圈
	// // #fix: 讀一個byte,測試是否有效
	// in.mark(0);
	// boolean validRead = (in.read() != -1 ? true : false);
	// // System.out.println("read: "+inputStream.read());
	// in.reset();
	// //
	// while (validRead && result == 0) {
	// result = in.available();
	// // System.out.println("available: "+available);
	// }
	// } catch (IOException ex) {
	// // java.io.IOException: mark/reset not supported
	// // java.util.zip.InflaterInputStream
	// // org.apache.commons.httpclient.AutoCloseInputStream
	// result = 1024;// 預設 1024 byte
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// return result;

	// #issue 會造成無窮迴圈
	// int result = 0;
	// //
	// if (in == null) {
	// return result;
	// }
	// //
	// while (result == 0) {
	// try {
	// result = in.available();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// break;
	// }
	// }
	// return result;
	// }

	public static int available(InputStream in) {
		return available(in, DEFAULT_READ_RETRY_NUMBER, DEFAULT_READ_RETRY_PAUSE_MILLS);
	}

	/**
	 * 
	 * @param inputStream
	 * @param retryNumber
	 *            重試次數, 0=無限
	 * @param retryPauseMills
	 * @return
	 */
	public static int available(InputStream inputStream, int retryNumber, long retryPauseMills) {
		int result = 0;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		//
		try {
			// #issue:
			// 1.把資料讀完,再取available第2次會卡死
			// 2.AutoCloseInputStream會close, available=0,會卡死
			// while (result == 0) {
			// result = inputStream.available();
			// }

			// #fix 加個重試次數判斷
			int tries = 0;
			while (true) {
				result = inputStream.available();
				if (result != 0) {
					break;
				}
				tries++;
				// 預設100*10+200*5=2000共等待2秒
				if (retryNumber != 0 && tries >= retryNumber) {
					break;
				}
				// 讀取重試暫停毫秒
				long pauseMills = readRetryPause(tries, retryPauseMills);
				ThreadHelper.sleep(pauseMills);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		return result;
	}

	public static byte[] read(String fileName) {
		AssertHelper.notNull(fileName, "The FileName must not be null");
		return read(new File(fileName));
	}

	public static byte[] read(File file) {
		byte[] result = null;
		boolean exist = FileHelper.isExist(file);
		//
		AssertHelper.isTrue(exist, "The File is not exist");
		//
		InputStream in = null;
		try {
			in = createInputStream(file);
			result = read(in);
		} finally {
			close(in);
		}
		//
		return result;
	}

	// public static byte[] read(InputStream value)
	// {
	// // System.out.println(1);
	// int available = available(value);
	// // System.out.println("available: " + available);
	//
	// byte[] result = new byte[available];
	// // System.out.println(available);
	//
	// int already = 0; // 已經成功讀取的總byte的數
	// int read = 0;// 每次讀取的byte的數
	// while (already < available)
	// {
	// try
	// {
	// // #issue: inputStream.read(),效率會非常低
	// // #fix,ok
	// read = value.read(result, already, available - already);
	// // System.out.println("tempCount: " + tempCount);
	// already += read;
	// } catch (Exception ex)
	// {
	// ex.printStackTrace();
	// }
	// }
	// return result;
	// }

	// /**
	// * 不要使用read(), 效率會非常低
	// *
	// * 應用
	// *
	// * read(byte b[], int off, int len)
	// *
	// * read(byte b[])
	// *
	// * @param inputStream
	// * @return
	// */
	// public static byte[] read(InputStream inputStream, int tries,
	// long retryPauseMills) {
	// byte[] result = null;
	// //
	// AssertHelper.notNull(inputStream, "The InputStream must not be null");
	// //
	// ByteArrayOutputStream out = null;
	// try {
	// // 若http長連線,會卡死在這個的地方,改由用available判斷,讀取byte[]
	// // URLConnection.setRequestProperty("connection","Keep-Alive")
	// //
	// // 目前只有以下物件用while方式:
	// // 1.httpclient3 AutoCloseInputStream
	// // 2.httpclient4 EofSensorInputStream
	// //
	// // httpclient3
	// if ("org.apache.commons.httpclient.AutoCloseInputStream"
	// .equals(inputStream.getClass().getName())
	// // httpclient4
	// || "org.apache.http.conn.EofSensorInputStream"
	// .equals(inputStream.getClass().getName())) {
	// //
	// out = new ByteArrayOutputStream();
	// byte[] buff = new byte[4096];
	// //
	// int read = -1;
	// //
	// while ((read = inputStream.read(buff)) != -1) {
	// out.write(buff, 0, read);
	// }
	// result = out.toByteArray();
	// } else {
	// //
	// int length = available(inputStream, tries, retryPauseMills);
	// if (length < 1) {
	// return result;
	// }
	// result = new byte[length];
	// int readCount = 0;
	// while (readCount < length) {
	// readCount += inputStream.read(result, readCount, length
	// - readCount);
	// }
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// } finally {
	// if (out != null) {
	// close(out);
	// }
	// }
	// //
	// return result;
	// }

	public static byte[] read(InputStream inputStream) {
		return read(inputStream, DEFALUT_READ_BLOCK_LENGTH, DEFAULT_READ_RETRY_NUMBER, DEFAULT_READ_RETRY_PAUSE_MILLS,
				null);
	}

	/**
	 * 
	 * @param inputStream
	 * @param blockLength
	 *            每次讀取的長度
	 * @return
	 */
	public static byte[] read(InputStream inputStream, int blockLength) {
		return read(inputStream, blockLength, DEFAULT_READ_RETRY_NUMBER, DEFAULT_READ_RETRY_PAUSE_MILLS, null);
	}

	/**
	 * 
	 * @param inputStream
	 * @param blockLength
	 *            每次讀取的長度
	 * @param action
	 *            外接處理邏輯
	 * @return
	 */
	public static byte[] read(InputStream inputStream, int blockLength, InputStreamCallback action) {

		return read(inputStream, blockLength, DEFAULT_READ_RETRY_NUMBER, DEFAULT_READ_RETRY_PAUSE_MILLS, action);
	}

	/**
	 * 不要使用read(), 效率會非常低
	 * 
	 * 應該用以下兩種之一
	 * 
	 * 1.read(byte b[], int off, int len)
	 * 
	 * 2.read(byte b[])
	 * 
	 * @param inputStream
	 * @param blockLength
	 *            每次讀取的長度
	 * @param retryNumber
	 *            重試次數, 0=無限
	 * @param retryPauseMills
	 * @param action
	 *            外接處理邏輯
	 * @return
	 */
	public static byte[] read(InputStream inputStream, int blockLength, int retryNumber, long retryPauseMills,
			InputStreamCallback action) {
		byte[] result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		//
		ByteArrayOutputStream out = null;
		// defalut
		int buffLength = DEFALUT_READ_BLOCK_LENGTH;
		try {
			if (blockLength > 0) {
				buffLength = blockLength;
			}
			//
			out = new ByteArrayOutputStream();
			byte[] buff = new byte[buffLength];
			//
			int read = -1;
			// 重試次數判斷
			int tries = 0;
			//
			while ((read = inputStream.read(buff)) != -1) {
				// 當讀不到資料時
				if (read == 0) {
					tries++;
					// 預設100*10+200*5=2000共等待2秒
					if (retryNumber != 0 && tries >= retryNumber) {
						break;
					}
					// 讀取重試暫停毫秒
					long pauseMills = readRetryPause(tries, retryPauseMills);
					ThreadHelper.sleep(pauseMills);
				} else {
					// read > 0
					out.write(buff, 0, read);

					// action
					boolean next = true;
					if (action != null) {
						try {
							next = action.doInAction(buff);
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
							if (!next) {
								break;
							}
						}
					}
				}
			}
			//
			if (out.size() > 0) {
				result = out.toByteArray();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(out);
		}
		//
		return result;
	}

	/**
	 * 寫入writer
	 * 
	 * @param writer
	 * @param value
	 * @return
	 */
	public static boolean write(Writer writer, String value) {
		boolean result = false;
		//
		AssertHelper.notNull(writer, "The Writer must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		try {
			writer.write(value);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 寫入outputStream
	 * 
	 * @param outputStream
	 * @param value
	 * @return
	 */
	public static boolean write(OutputStream outputStream, String value) {
		return write(outputStream, value, EncodingHelper.UTF_8);
	}

	/**
	 * 寫入outputStream
	 * 
	 * @param outputStream
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static boolean write(OutputStream outputStream, String value, String charsetName) {
		return write(outputStream, ByteHelper.toByteArray(value, charsetName));
	}

	/**
	 * 寫入outputStream
	 * 
	 * @param outputStream
	 * @param value
	 */
	public static boolean write(OutputStream outputStream, byte[] value) {
		boolean result = false;
		//
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		try {
			outputStream.write(value);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		return result;
	}

	// --------------------------------------------------
	public static String writeToJson(Object value) {
		return writeToJson((Class<?>) null, value);
	}

	public static String writeToJson(Class<?> classFile, Object value) {
		String fileName = (classFile != null ? classFile.getSimpleName() : value.getClass().getSimpleName());
		return writeToJson(fileName, value);
	}

	/**
	 * object->json, use json to parse
	 * 
	 * data/json/xxx.json
	 * 
	 * @param outputStream
	 * @param value
	 */

	public static String writeToJson(String fileName, Object value) {
		String result = null;
		//
		OutputStream out = null;
		try {
			if (StringHelper.notBlank(fileName)) {
				StringBuilder buff = new StringBuilder();
				buff.append(ConfigHelper.getJsonDir());
				buff.append(File.separator);
				buff.append(fileName);
				buff.append(".json");
				//
				out = createOutputStream(buff.toString());
				boolean writed = writeToJson(out, value);
				if (writed) {
					result = buff.toString();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(out);
		}
		return result;
	}

	/**
	 * object->serial
	 * 
	 * @param outputStream
	 * @param value
	 *            , 一般物件/json物件均可
	 * @return
	 */
	public static boolean writeToJson(OutputStream outputStream, Object value) {

		// return JsonHelper.write(outputStream, value);
		throw new UnsupportedOperationException();
	}

	public static <T> T readFromJson(Class<?> clazz) {
		return readFromJson((Class<?>) null, clazz);
	}

	public static <T> T readFromJson(Class<?> classFile, Class<?> clazz) {
		String fileName = (classFile != null ? classFile.getSimpleName() : clazz.getSimpleName());
		return readFromJson(fileName, clazz);
	}

	public static <T> T readFromJson(String fileName, Class<?> clazz) {
		T result = null;
		//
		if (StringHelper.notBlank(fileName)) {
			StringBuilder buff = new StringBuilder();
			buff.append(ConfigHelper.getJsonDir());
			buff.append(File.separator);
			buff.append(fileName);
			buff.append(".json");
			//
			InputStream in = createInputStream(buff.toString());
			result = readFromJson(in, clazz);
		}
		return result;
	}

	/**
	 * json->object
	 * 
	 * data/json/xxx.json
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	public static <T> T readFromJson(InputStream inputStream, Class<?> clazz) {

		// return JsonHelper.read(inputStream, clazz);
		throw new UnsupportedOperationException();
	}

	public static JarFile readJar(String fileName) {
		if (fileName == null) {
			return null;
		}
		return readJar(new File(fileName));
	}

	public static JarFile readJar(File file) {
		JarFile result = null;
		//
		try {
			if (FileHelper.isExist(file)) {
				result = new JarFile(file);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 讀取檔案為字串
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readString(String fileName) {
		return readString(fileName, EncodingHelper.UTF_8);
	}

	/**
	 * 讀取檔案為字串
	 * 
	 * @param fileName
	 * @param encoding
	 * @return
	 */
	public static String readString(String fileName, String encoding) {
		if (fileName == null) {
			return null;
		}
		return readString(new File(fileName), encoding);
	}

	/**
	 * 讀取檔案為字串
	 * 
	 * @param file
	 * @return
	 */
	public static String readString(File file) {
		return readString(file, EncodingHelper.UTF_8);
	}

	/**
	 * 讀取檔案為字串
	 * 
	 * @param file
	 * @param encoding
	 * @return
	 */
	public static String readString(File file, String encoding) {
		String result = null;
		//
		try {
			result = FileUtils.readFileToString(file, encoding);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	// // --------------------------------------------------
	// // json -> ser
	// // --------------------------------------------------
	// public static String writeToSerFromJson(Class<?> clazz) {
	// String result = null;
	// Object value = readFromJson(clazz);// 有可能,沒有impements Serializable
	// if (value instanceof Serializable) {
	// result = writeToSer((Serializable) value);
	// }
	// return result;
	// }
	//
	// public static String writeToSerFromJson(Class<?> classFile, Class<?>
	// clazz) {
	// String result = null;
	// Object value = readFromXml(classFile, clazz);
	// if (value instanceof Serializable) {
	// result = writeToSer(classFile, (Serializable) value);
	// }
	// return result;
	// }
	//
	// /**
	// * json -> ser
	// *
	// * readFromJson, writeToSer
	// *
	// * @param fileName
	// * @param clazz
	// */
	// public static String writeToSerFromJson(String fileName, Class<?> clazz)
	// {
	// String result = null;
	// Object value = readFromXml(fileName, clazz);
	// if (value instanceof Serializable) {
	// result = writeToSer(fileName, (Serializable) value);
	// }
	// return result;
	// }
	//
	// // --------------------------------------------------
	// // ser -> json
	// // --------------------------------------------------
	// public static String writeToJsonFromSer(Class<?> classFile) {
	// Serializable value = readFromSer(classFile);
	// return writeToJson(classFile, value);
	// }
	//
	// /**
	// *
	// * ser -> json
	// *
	// * readFromSer,writeToJson
	// *
	// * @param fileName
	// * @param clazz
	// * @return
	// */
	// public static String writeToJsonFromSer(String fileName, Class<?> clazz)
	// {
	// Object value = readFromSer(fileName);
	// return writeToJson(fileName, value);
	// }

	/**
	 * 把字串寫出成檔案
	 * 
	 * @param fileName
	 * @param value
	 * @return
	 */
	public static boolean writeToString(String fileName, String value) {
		return writeToString(fileName, value, EncodingHelper.UTF_8);
	}

	/**
	 * 把字串寫出成檔案
	 * 
	 * @param fileName
	 * @param value
	 * @param encoding
	 * @return
	 */
	public static boolean writeToString(String fileName, String value, String encoding) {
		if (fileName == null) {
			return false;
		}
		return writeToString(new File(fileName), value, encoding);
	}

	/**
	 * 把字串寫出成檔案
	 * 
	 * @param file
	 * @param content
	 * @return
	 */
	public static boolean writeToString(File file, String content) {
		return writeToString(file, content, EncodingHelper.UTF_8);
	}

	/**
	 * 把字串寫出成檔案
	 * 
	 * @param file
	 * @param content
	 * @param encoding
	 * @return
	 */
	public static boolean writeToString(File file, String content, String encoding) {
		boolean result = false;
		//
		try {
			FileUtils.writeStringToFile(file, content, encoding);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 計算讀取重試暫停毫秒
	 *
	 * @param retryPauseMills
	 * @param tries
	 * @return
	 */
	public static long readRetryPause(int tries, long retryPauseMills) {
		int triesCount = tries;
		if (triesCount >= READ_RETRY_BACK_OFF.length) {
			triesCount = READ_RETRY_BACK_OFF.length - 1;
		}
		return (retryPauseMills * READ_RETRY_BACK_OFF[triesCount]);
	}

	public static void reset(InputStream in) {
		try {
			if (in != null && in.markSupported()) {
				in.reset();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}
}
