package org.openyu.commons.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

/**
 * The Class SystemHelper.
 */
public final class SystemHelper extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(SystemHelper.class);

	/** The Constant FILE_ENCODING. */
	public static final String FILE_ENCODING = getProperty("file.encoding");

	/** The Constant FILE_SEPARATOR. */
	public static final String FILE_SEPARATOR = getProperty("file.separator");

	/** The Constant JAVA_CLASS_PATH. */
	public static final String JAVA_CLASS_PATH = getProperty("java.class.path");

	/** The Constant JAVA_CLASS_VERSION. */
	public static final String JAVA_CLASS_VERSION = getProperty("java.class.version");

	/** The Constant JAVA_COMPILER. */
	public static final String JAVA_COMPILER = getProperty("java.compiler");

	/** The Constant JAVA_EXT_DIRS. */
	public static final String JAVA_EXT_DIRS = getProperty("java.ext.dirs");

	/** The Constant JAVA_HOME. */
	public static final String JAVA_HOME = getProperty("java.home");

	/** The Constant JAVA_IO_TMPDIR. */
	public static final String JAVA_IO_TMPDIR = getProperty("java.io.tmpdir");

	/** The Constant JAVA_LIBRARY_PATH. */
	public static final String JAVA_LIBRARY_PATH = getProperty("java.library.path");

	/** The Constant JAVA_RUNTIME_NAME. */
	public static final String JAVA_RUNTIME_NAME = getProperty("java.runtime.name");

	/** The Constant JAVA_RUNTIME_VERSION. */
	public static final String JAVA_RUNTIME_VERSION = getProperty("java.runtime.version");

	/** The Constant JAVA_SPECIFICATION_NAME. */
	public static final String JAVA_SPECIFICATION_NAME = getProperty("java.specification.name");

	/** The Constant JAVA_SPECIFICATION_VENDOR. */
	public static final String JAVA_SPECIFICATION_VENDOR = getProperty("java.specification.vendor");

	/** The Constant JAVA_SPECIFICATION_VERSION. */
	public static final String JAVA_SPECIFICATION_VERSION = getProperty("java.specification.version");

	/** The Constant JAVA_VENDOR. */
	public static final String JAVA_VENDOR = getProperty("java.vendor");

	/** The Constant JAVA_VENDOR_URL. */
	public static final String JAVA_VENDOR_URL = getProperty("java.vendor.url");

	/** The Constant JAVA_VERSION. */
	public static final String JAVA_VERSION = getProperty("java.version");

	/** The Constant JAVA_VM_INFO. */
	public static final String JAVA_VM_INFO = getProperty("java.vm.info");

	/** The Constant JAVA_VM_NAME. */
	public static final String JAVA_VM_NAME = getProperty("java.vm.name");

	/** The Constant JAVA_VM_SPECIFICATION_NAME. */
	public static final String JAVA_VM_SPECIFICATION_NAME = getProperty("java.vm.specification.name");

	/** The Constant JAVA_VM_SPECIFICATION_VENDOR. */
	public static final String JAVA_VM_SPECIFICATION_VENDOR = getProperty("java.vm.specification.vendor");

	/** The Constant JAVA_VM_SPECIFICATION_VERSION. */
	public static final String JAVA_VM_SPECIFICATION_VERSION = getProperty("java.vm.specification.version");

	/** The Constant JAVA_VM_VENDOR. */
	public static final String JAVA_VM_VENDOR = getProperty("java.vm.vendor");

	/** The Constant JAVA_VM_VERSION. */
	public static final String JAVA_VM_VERSION = getProperty("java.vm.version");

	/** The Constant LINE_SEPARATOR. */
	public static final String LINE_SEPARATOR = getProperty("line.separator");

	/** The Constant OS_ARCH. */
	public static final String OS_ARCH = getProperty("os.arch");

	/** The Constant OS_NAME. */
	public static final String OS_NAME = getProperty("os.name");

	/** The Constant OS_VERSION. */
	public static final String OS_VERSION = getProperty("os.version");

	/** The Constant PATH_SEPARATOR. */
	public static final String PATH_SEPARATOR = getProperty("path.separator");

	/** The Constant USER_COUNTRY. */
	public static final String USER_COUNTRY = (getProperty("user.country") == null ? getProperty("user.region")
			: getProperty("user.country"));

	/** The Constant USER_DIR. */
	public static final String USER_DIR = getProperty("user.dir");

	/** The Constant USER_HOME. */
	public static final String USER_HOME = getProperty("user.home");

	/** The Constant USER_LANGUAGE. */
	public static final String USER_LANGUAGE = getProperty("user.language");

	/** The Constant USER_NAME. */
	public static final String USER_NAME = getProperty("user.name");

	/**
	 * Instantiates a new helper.
	 */
	public SystemHelper() {
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
		// private static final SystemHelper INSTANCE = new SystemHelper();
		private static SystemHelper INSTANCE = new SystemHelper();
	}

	/**
	 * Gets the single instance of SystemHelper.
	 *
	 * @return single instance of SystemHelper
	 */
	public synchronized static SystemHelper getInstance() {
		if (InstanceHolder.INSTANCE == null) {
			InstanceHolder.INSTANCE = new SystemHelper();
		}
		//
		if (!InstanceHolder.INSTANCE.isStarted()) {
			InstanceHolder.INSTANCE.setGetInstance(true);
			// 啟動
			InstanceHolder.INSTANCE.start();
		}
		return InstanceHolder.INSTANCE;
	}

	// -----------------------------------------------------------------------

	// Java version
	// 1.2f for JDK 1.2
	// 1.31f for JDK 1.3.1
	/** The Constant JAVA_VERSION_FLOAT. */
	public static final float JAVA_VERSION_FLOAT = getJavaVersionAsFloat();

	// 120 for JDK 1.2
	// 131 JDK 1.3.1
	/** The Constant JAVA_VERSION_INT. */
	public static final int JAVA_VERSION_INT = getJavaVersionAsInt();

	// Java version checks
	// -----------------------------------------------------------------------

	/** The Constant IS_JAVA_1_1. */
	public static final boolean IS_JAVA_1_1 = getJavaVersionMatches("1.1");

	/** The Constant IS_JAVA_1_2. */
	public static final boolean IS_JAVA_1_2 = getJavaVersionMatches("1.2");

	/** The Constant IS_JAVA_1_3. */
	public static final boolean IS_JAVA_1_3 = getJavaVersionMatches("1.3");

	/** The Constant IS_JAVA_1_4. */
	public static final boolean IS_JAVA_1_4 = getJavaVersionMatches("1.4");

	/** The Constant IS_JAVA_1_5. */
	public static final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");

	// Operating system checks
	// -----------------------------------------------------------------------
	/** The Constant IS_OS_AIX. */
	public static final boolean IS_OS_AIX = getOSMatches("AIX");

	/** The Constant IS_OS_HP_UX. */
	public static final boolean IS_OS_HP_UX = getOSMatches("HP-UX");

	/** The Constant IS_OS_IRIX. */
	public static final boolean IS_OS_IRIX = getOSMatches("Irix");

	/** The Constant IS_OS_LINUX. */
	public static final boolean IS_OS_LINUX = getOSMatches("Linux") || getOSMatches("LINUX");

	/** The Constant IS_OS_MAC. */
	public static final boolean IS_OS_MAC = getOSMatches("Mac");

	/** The Constant IS_OS_MAC_OSX. */
	public static final boolean IS_OS_MAC_OSX = getOSMatches("Mac OS X");

	/** The Constant IS_OS_OS2. */
	public static final boolean IS_OS_OS2 = getOSMatches("OS/2");

	/** The Constant IS_OS_SOLARIS. */
	public static final boolean IS_OS_SOLARIS = getOSMatches("Solaris");

	/** The Constant IS_OS_SUN_OS. */
	public static final boolean IS_OS_SUN_OS = getOSMatches("SunOS");

	/** The Constant IS_OS_WINDOWS. */
	public static final boolean IS_OS_WINDOWS = getOSMatches("Windows");

	/** The Constant IS_OS_WINDOWS_2000. */
	public static final boolean IS_OS_WINDOWS_2000 = getOSMatches("Windows", "5.0");

	/** The Constant IS_OS_WINDOWS_95. */
	public static final boolean IS_OS_WINDOWS_95 = getOSMatches("Windows 9", "4.0");

	/** The Constant IS_OS_WINDOWS_98. */
	public static final boolean IS_OS_WINDOWS_98 = getOSMatches("Windows 9", "4.1");

	/** The Constant IS_OS_WINDOWS_ME. */
	public static final boolean IS_OS_WINDOWS_ME = getOSMatches("Windows", "4.9");

	/** The Constant IS_OS_WINDOWS_NT. */
	public static final boolean IS_OS_WINDOWS_NT = getOSMatches("Windows NT");

	/** The Constant IS_OS_WINDOWS_XP. */
	public static final boolean IS_OS_WINDOWS_XP = getOSMatches("Windows", "5.1");

	/**
	 * Gets the java version.
	 *
	 * @return the java version
	 */
	public static float getJavaVersion() {
		return JAVA_VERSION_FLOAT;
	}

	/**
	 * Gets the java version as float.
	 *
	 * @return the java version as float
	 */
	private static float getJavaVersionAsFloat() {
		float result = 0f;
		//
		if (JAVA_VERSION != null) {
			StringBuilder buff = new StringBuilder();
			buff.append(JAVA_VERSION.substring(0, 3));
			if (JAVA_VERSION.length() >= 5) {
				buff.append(JAVA_VERSION.substring(4, 5));
			}
			result = Float.parseFloat(buff.toString());
		}
		return result;
	}

	/**
	 * Gets the java version as int.
	 *
	 * @return the java version as int
	 */
	private static int getJavaVersionAsInt() {
		int result = 0;
		//
		if (JAVA_VERSION != null) {
			StringBuilder buff = new StringBuilder();
			buff.append(JAVA_VERSION.substring(0, 1));
			buff.append(JAVA_VERSION.substring(2, 3));
			if (JAVA_VERSION.length() >= 5) {
				buff.append(JAVA_VERSION.substring(4, 5));
			} else {
				buff.append("0");
			}
			result = Integer.parseInt(buff.toString());
		}
		return result;
	}

	/**
	 * Gets the java version matches.
	 *
	 * @param versionPrefix
	 *            the version prefix
	 * @return the java version matches
	 */
	private static boolean getJavaVersionMatches(final String versionPrefix) {
		if (JAVA_VERSION == null) {
			return false;
		}
		return JAVA_VERSION.startsWith(versionPrefix);
	}

	/**
	 * Gets the oS matches.
	 *
	 * @param osNamePrefix
	 *            the os name prefix
	 * @return the oS matches
	 */
	private static boolean getOSMatches(final String osNamePrefix) {
		if (OS_NAME == null) {
			return false;
		}
		return OS_NAME.startsWith(osNamePrefix);
	}

	/**
	 * Gets the oS matches.
	 *
	 * @param osNamePrefix
	 *            the os name prefix
	 * @param osVersionPrefix
	 *            the os version prefix
	 * @return the oS matches
	 */
	private static boolean getOSMatches(final String osNamePrefix, final String osVersionPrefix) {
		if (OS_NAME == null || OS_VERSION == null) {
			return false;
		}
		return OS_NAME.startsWith(osNamePrefix) && OS_VERSION.startsWith(osVersionPrefix);
	}

	// -----------------------------------------------------------------------

	/**
	 * Gets the property.
	 *
	 * @param key
	 *            the key
	 * @return the property
	 */
	public static String getProperty(final String key) {
		return getProperty(key, null);
	}

	/**
	 * Gets the property.
	 *
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the property
	 */
	public static String getProperty(final String key, final String defaultValue) {
		String result = null;
		try {
			result = System.getProperty(key);
			if (StringHelper.isEmpty(result)) {
				result = defaultValue;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Sets the property.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String setProperty(final String key, final String value) {
		return System.setProperty(key, value);
	}

	/**
	 * Checks if is java version at least.
	 *
	 * @param requiredVersion
	 *            the required version
	 * @return true, if is java version at least
	 */
	public static boolean isJavaVersionAtLeast(final float requiredVersion) {
		return (JAVA_VERSION_FLOAT >= requiredVersion);
	}

	/**
	 * Checks if is java version at least.
	 *
	 * @param requiredVersion
	 *            the required version
	 * @return true, if is java version at least
	 */
	public static boolean isJavaVersionAtLeast(final int requiredVersion) {
		return (JAVA_VERSION_INT >= requiredVersion);
	}

	/**
	 * Println.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 */
	public static <T> void println(final T value) {
		System.out.println(ObjectHelper.toString(value));
	}

	/**
	 * Println.
	 *
	 * @param <T>
	 *            the generic type
	 * @param values
	 *            the values
	 */
	public static <T> void println(final T[] values) {
		println("", values);
	}

	public static <T> void println(String title, final T[] values) {
		System.out.print(title);
		System.out.println(ObjectHelper.toString(values));
	}

	public static void println(final byte[] values) {
		println("", values);
	}

	public static void println(String title, final byte[] values) {
		System.out.print(title);
		System.out.println(ObjectHelper.toString(values));
	}

}
