package org.openyu.commons.io;

import java.io.*;
import java.net.*;
import java.util.*;

//#issue: jdk不支援中文檔名
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//#fix 改apache zip
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import javax.crypto.SecretKey;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.security.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHelper implements Supporter {

	/** The Constant LOGGER. */
	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(FileHelper.class);

	private FileHelper() {
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
		private static final FileHelper INSTANCE = new FileHelper();
	}

	/**
	 * Gets the single instance of ChecksumHelper.
	 *
	 * @return single instance of ChecksumHelper
	 */
	public static FileHelper getInstance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 目錄或檔案是否"存在",判斷null跟file.exists()
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isExist(String fileName) {
		if (fileName == null) {
			return false;
		}
		return isExist(new File(fileName));
	}

	/**
	 * 目錄或檔案是否"存在",判斷null跟file.exists()
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isExist(File file) {
		boolean result = false;
		//
		try {
			if (file != null) {
				result = file.exists();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 目錄或檔案是否"不存在"
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isNotExist(String fileName) {
		if (fileName == null) {
			return false;
		}
		return isNotExist(new File(fileName));
	}

	/**
	 * 目錄或檔案是否"不存在"
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isNotExist(File file) {
		boolean result = false;
		try {
			if (file == null || !file.exists()) {
				result = true;
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 轉成url
	 * 
	 * file:D:/dev/openyu/trunk/openyu-commons-core.j/src/main/java
	 * 
	 * @param fileName
	 * @return
	 */
	public static URL toUrl(String fileName) {
		if (fileName == null) {
			return null;
		}
		return toUrl(new File(fileName));
	}

	/**
	 * 轉成url
	 * 
	 * file:D:/dev/openyu/trunk/openyu-commons-core.j/src/main/java
	 * 
	 * @param file
	 * @return
	 */
	public static URL toUrl(File file) {
		URL result = null;
		try {
			if (isExist(file)) {
				// file.toURI().toURL();
				result = file.toURL(); // file:D:/dev/openyu/trunk/openyu-commons-core.j/src/main/java
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 轉成url
	 * 
	 * file:D:/dev/openyu/trunk/openyu-commons-core.j/src/main/java
	 * 
	 * @param files
	 * @return
	 */
	public static URL[] toUrls(File[] files) {
		URL[] result = new URL[0];
		//
		if (files != null) {
			result = new URL[files.length];
			for (int i = 0; i < files.length; i++) {
				result[i] = toUrl(files[i]);
			}
		}
		return result;
	}

	/**
	 * 轉成檔案
	 * 
	 * @param spec
	 * @return
	 */
	public static File toFile(String spec) {
		File result = null;
		try {
			if (spec != null) {
				URL url = new URL(spec);
				result = toFile(url);
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 轉成檔案
	 * 
	 * 可以轉成file,但file可能不存在
	 * 
	 * @param url
	 * @return
	 */
	public static File toFile(URL url) {
		File result = null;
		//
		if (url != null) {
			if (url.getProtocol().equals("file")) {
				result = new File(url.getFile().replace("/", File.separator));
			}
		}
		return result;
	}

	/**
	 * 轉成檔案
	 * 
	 * @param urls
	 * @return
	 */
	public static File[] toFiles(URL[] urls) {
		File[] result = new File[0];
		//
		if (urls != null) {
			result = new File[urls.length];
			for (int i = 0; i < urls.length; i++) {
				result[i] = toFile(urls[i]);
			}
		}
		return result;
	}

	/**
	 * 由class取得目錄
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getDirByClass(Class<?> clazz) {
		return getDirByClass((File) null, clazz);
	}

	/**
	 * 由class取得目錄
	 * 
	 * @param parentDir
	 * @param clazz
	 * @return
	 */
	public static String getDirByClass(String parentDir, Class<?> clazz) {
		return getDirByPackage(new File(parentDir), clazz.getPackage());
	}

	/**
	 * 由class取得目錄
	 * 
	 * @param parentDir
	 * @param clazz
	 * @return
	 */
	public static String getDirByClass(File parentDir, Class<?> clazz) {
		return getDirByPackage(parentDir, clazz.getPackage());
	}

	/**
	 * 由package取得目錄
	 * 
	 * @param packagz
	 * @return
	 */
	public static String getDirByPackage(Package packagz) {
		return getDirByPackage((File) null, packagz);
	}

	/**
	 * 由package取得目錄
	 * 
	 * @param parentDir
	 * @param packagz
	 * @return
	 */
	public static String getDirByPackage(String parentDir, Package packagz) {
		return getDirByPackage(new File(parentDir), packagz.getName());
	}

	/**
	 * 由package取得目錄
	 * 
	 * @param parentDir
	 * @param packagz
	 * @return
	 */
	public static String getDirByPackage(File parentDir, Package packagz) {
		return getDirByPackage(parentDir, packagz.getName());
	}

	/**
	 * 由package取得目錄
	 * 
	 * @param packageName
	 * @return
	 */
	public static String getDirByPackage(String packageName) {
		return getDirByPackage(null, packageName);
	}

	/**
	 * 由package取得目錄
	 * 
	 * @param parentDir
	 * @param packageName
	 * @return
	 */
	public static String getDirByPackage(File parentDir, String packageName) {
		if (packageName == null) {
			return null;
		}
		//
		StringTokenizer st = new StringTokenizer(packageName, ".");
		StringBuffer dir = new StringBuffer();
		if (parentDir == null) {
			dir.append(System.getProperty("user.dir"));
		} else {
			// 絕對路徑
			dir.append(parentDir.getAbsolutePath());

		}
		while (st.hasMoreTokens()) {
			// dir.append(System.getProperty("file.separator"));
			dir.append(File.separator);
			dir.append(st.nextToken());
		}
		return dir.toString();
	}

	/**
	 * 取得副檔名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtension(String fileName) {
		if (fileName == null) {
			return null;
		}
		return getExtension(new File(fileName));
	}

	/**
	 * 取得副檔名
	 * 
	 * @param file
	 * @return
	 */
	public static String getExtension(File file) {
		String result = null;
		if (file != null) {
			String fileName = file.getName();
			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length() - 1) {
				result = fileName.substring(i + 1);
			}
		}
		return result;
	}

	// -----------------------------------------------------
	// class 系統,資源
	// -----------------------------------------------------
	/**
	 * class 系統, 取得資源, /log4j.properties
	 * 
	 * file:D:/dev/openyu/trunk/openyu-commons-core.j/target/test-classes/log4j.properties
	 * 
	 * @param name
	 * @return
	 */
	public static URL getResource(String name) {
		if (name == null) {
			return null;
		}
		return FileHelper.class.getResource(name);
	}

	/**
	 * class 系統, 取得資源檔名, /log4j.properties
	 * 
	 * D:/dev/openyu/trunk/openyu-commons-core.j/target/test-classes/log4j.properties
	 * 
	 * @param name
	 * @return
	 */
	public static String getResourceFile(String name) {
		String result = null;
		//
		URL url = getResource(name);
		if (url != null) {
			result = url.getFile();
		}
		return result;
	}

	/**
	 * class 系統, 取得串流, /log4j.properties
	 * 
	 * java.io.BufferedInputStream@1103d94
	 * 
	 * @param name
	 * @return
	 */
	public static InputStream getResourceStream(String name) {
		if (name == null) {
			return null;
		}
		return FileHelper.class.getResourceAsStream(name);
	}

	/**
	 * class 系統, 取得資源轉為屬性內容, /log4j.properties
	 * 
	 * @param name
	 * @return
	 */
	public static Properties getProperties(String name) {
		// return new ResourceProperties(name).getProperties();
		Properties result = null;
		InputStream in = null;
		try {
			in = getResourceStream(name);
			if (in != null) {
				result = new Properties();
				result.load(in);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 關閉串流
			IoHelper.close(in);
		}
		return result;
	}

	// -----------------------------------------------------
	/**
	 * 建目錄
	 * 
	 * @param pathName
	 * @return
	 */
	public static boolean md(String pathName) {
		return md(pathName, false);
	}

	/**
	 * 建目錄,若已存在,則不建
	 * 
	 * 可建立不存在的子目錄
	 * 
	 * @param pathName
	 * @param includeFileName
	 *            目錄名稱是否包含檔名
	 * @return
	 */
	public static boolean md(String pathName, boolean includeFileName) {
		boolean result = false;
		//
		try {
			if (StringHelper.notEmpty(pathName)) {
				StringBuffer buff = new StringBuffer();
				// output/x/y/z/aaa.log
				if (includeFileName) {
					buff.append(StringHelper.excludeLast(pathName, "/"));
					// 判斷目錄分割字元為 / 或 \
					if (pathName.equals(buff.toString())) {
						buff = new StringBuffer();
						buff.append(StringHelper.excludeLast(pathName, "\\"));
					}
					if (buff.toString().equals(pathName)) {
						buff = new StringBuffer();
					}
					// System.out.println("dir:" + dir);
				} else {
					buff.append(pathName);
					// System.out.println("dir:" + name);
				}
				//
				if (buff.length() > 0) {
					result = md(new File(buff.toString()));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 建目錄,若已存在,則不建
	 * 
	 * @param file
	 * @return
	 */
	public static boolean md(File file) {
		boolean result = false;
		//
		try {
			if (isNotExist(file)) {
				result = file.mkdirs();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 讀取目錄檔案, 含子目錄/檔案
	 * 
	 * @param pathName
	 * @return
	 */
	public static File[] dir(String pathName) {
		return dir(pathName, null);
	}

	/**
	 * 讀取目錄檔案, 含子目錄/檔案
	 * 
	 * @param pathName
	 * @param filter
	 * @return
	 */
	public static File[] dir(String pathName, FileFilter filter) {
		if (pathName == null) {
			return null;
		}
		return dir(new File(pathName), filter);
	}

	/**
	 * 讀取目錄檔案, 含子目錄/檔案
	 * 
	 * @param file
	 * @return
	 */
	public static File[] dir(File file) {
		return dir(file, null);
	}

	/**
	 * 讀取目錄檔案, 含子目錄/檔案
	 * 
	 * @param file
	 * @param filter
	 * @return
	 */
	public static File[] dir(File file, FileFilter filter) {
		File[] result = new File[0];
		//
		try {
			if (isExist(file)) {
				if (filter != null) {
					result = file.listFiles(filter);
				} else {
					result = file.listFiles();
				}
				//
				if (result != null) {
					for (File entry : result) {
						result = ArrayHelper.addUnique(result,
								dir(entry, filter), File[].class);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	// -----------------------------------------------------
	// 加密
	// -----------------------------------------------------
	/**
	 * 加密目錄結果
	 */
	public static class EncryptDirResult extends BaseModelSupporter {

		private static final long serialVersionUID = -5076554124295350366L;

		private String baseDir;

		// origDir,destDirName
		private Map<File, String> dirs = new LinkedHashMap<File, String>();

		// origFile,destFileName
		private Map<File, String> files = new LinkedHashMap<File, String>();

		public EncryptDirResult() {
		}

		public String getBaseDir() {
			return baseDir;
		}

		public void setBaseDir(String baseDir) {
			this.baseDir = baseDir;
		}

		public Map<File, String> getDirs() {
			return dirs;
		}

		public void setDirs(Map<File, String> dirs) {
			this.dirs = dirs;
		}

		public Map<File, String> getFiles() {
			return files;
		}

		public void setFiles(Map<File, String> files) {
			this.files = files;
		}

	}

	/**
	 * 指定key,加密目錄
	 * 
	 * @param pathName
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static EncryptDirResult encryptDir(String pathName,
			String assignKey, String algorithm) {
		if (pathName == null) {
			return null;
		}
		return encryptDir(new File(pathName), assignKey, algorithm);
	}

	/**
	 * 指定key,加密目錄
	 * 
	 * @param file
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static EncryptDirResult encryptDir(File file, String assignKey,
			String algorithm) {
		EncryptDirResult result = new EncryptDirResult();
		//
		try {
			if (file != null) {
				// 若目錄不存在,則建立
				md(file);
				//
				File[] files = dir(file);// 含子目錄/檔案
				String encryptDir = encryptMd(file, assignKey, algorithm);
				result.setBaseDir(encryptDir);
				// 需有子目錄
				for (File entry : files) {
					// System.out.println(value+" :" +file);
					// 目錄
					if (entry.isDirectory()) {
						encryptDir = encryptMd(entry, assignKey, algorithm);
						result.getDirs().put(entry, encryptDir);
					}
					// 檔案
					else if (entry.isFile()) {
						// System.out.println(file);
						EncryptFileResult encryptFileResult = encryptFile(
								entry, assignKey, algorithm);
						result.getFiles().put(entry,
								encryptFileResult.getDestName());
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 指定key,加密建目錄
	 * 
	 * @param pathName
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static String encryptMd(String pathName, String assignKey,
			String algorithm) {
		if (pathName == null) {
			return null;
		}
		return encryptMd(new File(pathName), assignKey, algorithm);
	}

	/**
	 * 指定key,加密建目錄
	 * 
	 * @param file
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static String encryptMd(File file, String assignKey, String algorithm) {
		StringBuilder result = new StringBuilder();
		//
		try {
			if (file != null) {
				String[] names = StringUtils.splitPreserveAllTokens(
						file.getPath(), File.separator);

				// 指定key
				SecretKey secretKey = SecurityHelper.createSecretKey(assignKey,
						algorithm);
				// 目錄加密
				for (int i = 0; i < names.length; i++) {
					String encrypt = SecurityHelper.encryptHex(names[i],
							secretKey, algorithm);
					result.append(encrypt);
					//
					if (i < names.length - 1) {
						result.append(File.separator);
					}
				}

				// 建目錄
				if (isNotExist(result.toString())) {
					md(result.toString());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		return result.toString();
	}

	// -----------------------------------------------------
	// 解密
	// -----------------------------------------------------
	/**
	 * 解密目錄結果
	 */
	public static class DecryptDirResult extends EncryptDirResult {
		private static final long serialVersionUID = 9008760101269711796L;

		public DecryptDirResult() {
		}
	}

	/**
	 * 指定key,解密目錄
	 * 
	 * @param pathName
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static DecryptDirResult decryptDir(String pathName,
			String assignKey, String algorithm) {
		if (pathName == null) {
			return null;
		}
		return decryptDir(new File(pathName), assignKey, algorithm);
	}

	/**
	 * 指定key,解密目錄
	 * 
	 * @param file
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static DecryptDirResult decryptDir(File file, String assignKey,
			String algorithm) {
		DecryptDirResult result = new DecryptDirResult();
		//
		try {
			if (file != null) {
				File[] files = dir(file);// 含子目錄/檔案
				String decryptDir = decryptMd(file, assignKey, algorithm);
				result.setBaseDir(decryptDir);
				// 需有子目錄
				for (File entry : files) {
					// System.out.println(value+" :" +file);
					// 當目錄時
					if (entry.isDirectory()) {
						decryptDir = decryptMd(entry, assignKey, algorithm);
						if (StringHelper.notEmpty(decryptDir)) {
							result.getDirs().put(entry, decryptDir);
						}
					}
					// 當檔案時
					else if (entry.isFile()) {
						// System.out.println(file);
						DecryptFileResult decryptFileResult = decryptFile(
								entry, assignKey, algorithm);
						result.getFiles().put(entry,
								decryptFileResult.getDestName());
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 指定key,解密建目錄
	 * 
	 * @param pathName
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static String decryptMd(String pathName, String assignKey,
			String algorithm) {
		if (pathName == null) {
			return null;
		}
		return decryptMd(new File(pathName), assignKey, algorithm);
	}

	/**
	 * 指定key,解密建目錄
	 * 
	 * @param file
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static String decryptMd(File file, String assignKey, String algorithm) {
		StringBuilder result = new StringBuilder();
		if (file != null) {
			// 加個後綴,避免覆蓋原始目錄
			final String SUFFIX = "-decrypt";
			//
			String[] names = StringUtils.splitPreserveAllTokens(file.getPath(),
					File.separator);
			StringBuilder dir = new StringBuilder();
			// 指定key
			SecretKey secretKey = SecurityHelper.createSecretKey(assignKey,
					algorithm);
			// 目錄解密
			for (int i = 0; i < names.length; i++) {
				byte[] decrypt = SecurityHelper.decryptHex(names[i], secretKey,
						algorithm);
				dir.append(ByteHelper.toString(decrypt));
				if (i == 0) {
					dir.append(SUFFIX);
				}
				//
				if (i < names.length - 1) {
					dir.append(File.separator);
				}
			}
			result.append(dir);
			//
			// 建目錄
			if (isNotExist(dir.toString())) {
				md(dir.toString());
			}
		}
		return result.toString();
	}

	/**
	 * 加密檔案結果
	 */
	public static class EncryptFileResult extends BaseModelSupporter {

		private static final long serialVersionUID = -4818249687083928587L;

		private File origFile;

		private String destName;

		public EncryptFileResult() {
		}

		public File getOrigFile() {
			return origFile;
		}

		public void setOrigFile(File origFile) {
			this.origFile = origFile;
		}

		public String getDestName() {
			return destName;
		}

		public void setDestName(String destName) {
			this.destName = destName;
		}
	}

	/**
	 * 指定key,加密檔案,含目錄檔名,內容
	 * 
	 * @param fileName
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static EncryptFileResult encryptFile(String fileName,
			String assignKey, String algorithm) {
		if (fileName == null) {
			return null;
		}
		return encryptFile(new File(fileName), assignKey, algorithm);
	}

	/**
	 * 指定key,加密檔案,含目錄檔名,內容
	 * 
	 * @param file
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static EncryptFileResult encryptFile(File file, String assignKey,
			String algorithm) {
		EncryptFileResult result = new EncryptFileResult();
		if (isExist(file)) {
			// 指定key
			SecretKey secretKey = SecurityHelper.createSecretKey(assignKey,
					algorithm);

			// 目錄,encryptToHex
			String encryptDir = encryptMd(file.getParent(), assignKey,
					algorithm);
			// System.out.println("dir: " + encryptDir);

			// 檔名,encryptToHex
			String encryptFileName = SecurityHelper.encryptHex(file.getName(),
					secretKey, algorithm);
			// System.out.println("fileName: " + encryptFileName);

			// 內容,encryptToBase64
			byte[] contents = IoHelper.read(file);
			String encryptContent = SecurityHelper.encryptBase64(contents,
					secretKey, algorithm);
			// System.out.println("content: " + encryptContent);

			//
			Writer writer = IoHelper.createWriter(encryptDir + File.separator
					+ encryptFileName);
			try {
				IoHelper.write(writer, encryptContent);
				//
				result.setOrigFile(file);
				result.setDestName(encryptDir + File.separator
						+ encryptFileName);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				// 關閉串流
				IoHelper.close(writer);
			}
		}
		return result;
	}

	/**
	 * 解密檔案結果
	 */
	public static class DecryptFileResult extends EncryptFileResult {
		private static final long serialVersionUID = 9008760101269711796L;

		public DecryptFileResult() {
		}
	}

	/**
	 * 指定key,解密檔案,含目錄檔名,內容
	 * 
	 * @param fileName
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static DecryptFileResult decryptFile(String fileName,
			String assignKey, String algorithm) {
		if (fileName == null) {
			return null;
		}
		return decryptFile(new File(fileName), assignKey, algorithm);
	}

	/**
	 * 指定key,解密檔案,含目錄檔名,內容
	 * 
	 * @param file
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	public static DecryptFileResult decryptFile(File file, String assignKey,
			String algorithm) {
		DecryptFileResult result = new DecryptFileResult();
		OutputStream out = null;
		try {
			if (isExist(file)) {
				// 指定key
				SecretKey secretKey = SecurityHelper.createSecretKey(assignKey,
						algorithm);

				// 目錄,decryptFromHex
				String decryptDir = decryptMd(file.getParent(), assignKey,
						algorithm);
				// System.out.println("dir: " + decryptDir);

				// 檔名,decryptFromHex
				String decryptFileName = ByteHelper.toString(SecurityHelper
						.decryptHex(file.getName(), secretKey, algorithm));
				// System.out.println("fileName: " + decryptFileName);

				// 內容,decryptFromBase64
				byte[] contents = IoHelper.read(file);
				byte[] decryptContent = SecurityHelper.decryptBase64(contents,
						secretKey, algorithm);
				// System.out.println("content: " +
				// ByteHelper.toString(decryptContent));

				// System.out.println(decryptDir+" isExist: "+isExist(decryptDir));
				out = IoHelper.createOutputStream(decryptDir + File.separator
						+ decryptFileName);
				IoHelper.write(out, decryptContent);
				//
				result.setOrigFile(file);
				result.setDestName(decryptDir + File.separator
						+ decryptFileName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 關閉串流
			IoHelper.close(out);
		}
		return result;
	}

	// -----------------------------------------------------
	// 壓縮
	// -----------------------------------------------------
	/**
	 * 
	 * 壓縮,目錄以下的所有子目錄,空目錄,及檔案
	 * 
	 * @param pathName
	 * @param zipName
	 * @return
	 */
	public static File zipDir(String pathName, String zipName) {
		if (pathName == null) {
			return null;
		}
		return zipDir(new File(pathName), zipName);
	}

	/**
	 * 壓縮,目錄以下的所有子目錄,空目錄,及檔案
	 * 
	 * 支援中文目錄名,檔名
	 * 
	 * @param pathName
	 *            來源目錄
	 * @param zipName
	 *            目的zip檔
	 * @return
	 */
	public static File zipDir(File file, String zipName) {
		File result = null;
		//
		ZipOutputStream out = null;
		try {
			if (isExist(file)) {
				out = IoHelper.createZipOutputStream(zipName);
				//
				File[] files = dir(file);
				// 需有子目錄
				for (File entry : files) {
					// 目錄
					if (entry.isDirectory()) {
						// 含空目錄
						ZipEntry zipEntry = new ZipEntry(entry.getPath() + "/");
						out.putNextEntry(zipEntry);
					}
					// 檔案
					else {
						byte[] contents = IoHelper.read(entry);
						ZipEntry zipEntry = new ZipEntry(entry.getPath());
						out.putNextEntry(zipEntry);
						out.write(contents);
					}
				}
				result = new File(zipName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 關閉串流
			IoHelper.close(out);
		}
		return result;
	}

	/**
	 * 解壓縮
	 * 
	 * @param zipName
	 * @return
	 */
	public static String unzip(String zipName) {
		return unzip(zipName, null);
	}

	/**
	 * 解壓縮
	 * 
	 * @param zipName
	 * @param pathName
	 * @return
	 */
	public static String unzip(String zipName, String pathName) {
		if (pathName == null) {
			return null;
		}
		return unzip(new File(zipName), pathName);
	}

	/**
	 * 解壓縮
	 * 
	 * @param zipName
	 *            來源zip檔
	 * @param pathName
	 *            輸出目錄
	 * @return
	 */
	public static String unzip(File zipName, String pathName) {
		String result = null;
		if (isExist(zipName)) {
			try {
				ZipFile zipFile = new ZipFile(zipName);
				for (Enumeration<?> e = zipFile.getEntries(); e
						.hasMoreElements();) {
					ZipEntry zipEntry = (ZipEntry) e.nextElement();
					StringBuilder name = new StringBuilder();
					// 目錄
					if (zipEntry.isDirectory()) {
						if (StringHelper.notEmpty(pathName)) {
							name.append(pathName);
							name.append(File.separator);
						}
						name.append(zipEntry.getName().substring(0,
								zipEntry.getName().length() - 1));
						md(name.toString());
					}
					// 檔案
					else {
						if (StringHelper.notEmpty(pathName)) {
							name.append(pathName);
							name.append(File.separator);
						}
						name.append(zipEntry.getName());
						OutputStream out = IoHelper.createOutputStream(name
								.toString());
						md(name.toString(), true);
						//
						InputStream in = zipFile.getInputStream(zipEntry);
						try {
							byte[] contents = new byte[1024];
							int len;
							while ((len = in.read(contents)) != -1) {
								out.write(contents, 0, len);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
							// 關閉串流
							IoHelper.close(out);
							IoHelper.close(in);
						}
					}
					//
					if (result == null) {
						int pos = name.indexOf(File.separator);
						if (pos != -1) {
							result = name.substring(0, pos);
						} else {
							result = name.toString();
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 刪除檔案
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean delete(String fileName) {
		if (fileName == null) {
			return false;
		}
		return delete(new File(fileName));
	}

	/**
	 * 刪除檔案
	 * 
	 * @param file
	 * @return
	 */
	public static boolean delete(File file) {
		return FileUtils.deleteQuietly(file);
	}
}
