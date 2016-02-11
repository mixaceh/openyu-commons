package org.openyu.commons.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.KeyPair;
import java.security.Provider;
import java.util.Arrays;

import javax.crypto.SecretKey;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.EncodingHelper;
import org.openyu.commons.lang.SystemHelper;
import org.openyu.commons.security.SecurityHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class SecurityHelperTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	public void provider() {
		for (Provider provider : java.security.Security.getProviders()) {
			System.out.println(provider.getName());
			for (String key : provider.stringPropertyNames())
				System.out.println("\t" + key + "\t"
						+ provider.getProperty(key));
		}
	}

	@Test
	// 1000000 times: 507 mills.
	// 1000000 times: 507 mills.
	// 1000000 times: 491 mills.
	// verified
	public void createSecretKey() {
		// String assignKey = "中文key123";
		// 24 byte
		// byte[] assignKey = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2,
		// 3, 4, 5, 6, 7, 8, 9, 0,
		// 1, 2, 3, 4 };
		// byte[] assignKey = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
		String assignKey = "abcdefgh"; // [8]
		SecretKey result = null;
		//
		int count = 1;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.createSecretKey(new byte[8], "DES");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.getAlgorithm() + " :"
				+ result.getEncoded().length);
		assertNotNull(result);
		//
		result = SecurityHelper.createSecretKey(new byte[24], "DESede");
		System.out.println(result.getAlgorithm() + " :"
				+ result.getEncoded().length);
		assertNotNull(result);
		//
		result = SecurityHelper.createSecretKey(new byte[16], "AES");
		System.out.println(result.getAlgorithm() + " :"
				+ result.getEncoded().length);
		assertNotNull(result);
		//
		result = SecurityHelper.createSecretKey(new byte[10], "HmacMD5");
		System.out.println(result.getAlgorithm() + " :"
				+ result.getEncoded().length);
		assertNotNull(result);
	}

	@Test
	// 1000000 times: 6007 mills.
	// 1000000 times: 5790 mills.
	// 1000000 times: 6157 mills.
	// verified
	public void randomSecretKey() {
		SecretKey result = null;
		//
		int count = 1000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.randomSecretKey("DES");
			System.out.println("[" + i + "] "
					+ ByteHelper.toString(result.getEncoded()));
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.getAlgorithm() + " :"
				+ ByteHelper.toString(result.getEncoded()));
		assertNotNull(result);
		//
		result = SecurityHelper.randomSecretKey("DESede");
		System.out.println(result.getAlgorithm() + " :"
				+ ByteHelper.toString(result.getEncoded()));
		assertNotNull(result);
		//
		result = SecurityHelper.randomSecretKey("HmacMD5");
		System.out.println(result.getAlgorithm() + " :"
				+ ByteHelper.toString(result.getEncoded()));
		assertNotNull(result);
		//
		result = SecurityHelper.randomSecretKey("AES");
		System.out.println(result.getAlgorithm() + " :"
				+ ByteHelper.toString(result.getEncoded()));
		assertNotNull(result);
	}

	@Test
	// 還蠻慢的
	// 10 times: 1615 mills.
	// 10 times: 1333 mills.
	// 10 times: 1547 mills.
	public void randomKeyPair() {
		KeyPair result = null;
		int count = 10;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// 1024 bit(128 byte)
			result = SecurityHelper.randomKeyPair(1024, "RSA");
			System.out.println("[" + i + "] " + result.getPublic());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("private: " + result.getPrivate());
		System.out.println("public: " + result.getPublic());
		assertNotNull(result);
		//
		result = SecurityHelper.randomKeyPair(1024, "DSA");
		System.out.println("private: " + result.getPrivate());
		System.out.println("public: " + result.getPublic());
		assertNotNull(result);
		//
		result = SecurityHelper.randomKeyPair(1024, "DH");
		System.out.println("private: " + result.getPrivate());
		System.out.println("public: " + result.getPublic());
		assertNotNull(result);
	}

	@Test
	// 1000000 times: 14767 mills.
	// 1000000 times: 13543 mills.
	// 1000000 times: 11782 mills.
	public void encrypt() {
		String value = "中文測試abcdef";

		String algorithm = "DES";
		// 隨機key
		SecretKey secretKey = SecurityHelper.randomSecretKey(algorithm);
		byte[] result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.encrypt(value, secretKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);
		assertEquals(24, result.length);
	}

	@Test
	// 1000000 times: 7901 mills.
	// 1000000 times: 7812 mills.
	// 1000000 times: 8108 mills.
	public void encryptHex() {
		String value = "中文測試abcdef";

		String algorithm = "DES";
		// 隨機key
		SecretKey secretKey = SecurityHelper.randomSecretKey(algorithm);
		String result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.encryptHex(value, secretKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);// 986985ea51dca188906abf8b2a1b8a4eed800729efb7b580
		assertEquals(48, result.length());
	}

	// @Test
	// // 1000000 times: 7901 mills.
	// // 1000000 times: 7812 mills.
	// // 1000000 times: 8108 mills.
	// public void encryptString() {
	// String value = "1qa2ws3ed";
	// String assignKey = "FarFarAway";
	// String algorithm = "DES";
	// //
	// String result = null;
	//
	// int count = 1;
	// long beg = System.currentTimeMillis();
	// for (int i = 0; i < count; i++) {
	// result = SecurityHelper.encryptString(value, EncodingHelper.UTF_8,
	// assignKey, algorithm);
	// }
	// long end = System.currentTimeMillis();
	// System.out.println(count + " times: " + (end - beg) + " mills. ");
	//
	// System.out.println(result);// a284a311c40885ed0df37642c78120a2
	// assertEquals(32, result.length());
	// //
	// result = SecurityHelper.encryptString("1234567890",
	// EncodingHelper.UTF_8, assignKey, algorithm);
	// System.out.println(result);// 84262249b424a94f95a6dde5d9761cfb
	// assertEquals(32, result.length());
	// //
	// result = SecurityHelper.encryptString(null, EncodingHelper.UTF_8,
	// assignKey, algorithm);
	// System.out.println(result);// null
	// //
	// result = SecurityHelper.encryptString("", EncodingHelper.UTF_8,
	// assignKey, algorithm);
	// System.out.println(result);// ""
	// }

	@Test
	// 1000000 times: 12067 mills.
	// 1000000 times: 12263 mills.
	// 1000000 times: 11782 mills.
	public void encryptBase64() {
		String value = "中文測試abcdef";

		String algorithm = "DES";
		// 隨機key
		SecretKey secretKey = SecurityHelper.randomSecretKey(algorithm);
		String result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.encryptBase64(value, secretKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);// At4nhIRyIZTbCVXpO+kz7NigWmiuz9Up
		assertEquals(32, result.length());
	}

	@Test
	// 1000000 times: 7901 mills.
	// 1000000 times: 7812 mills.
	// 1000000 times: 8108 mills.
	public void decryptHex() {
		String value = "中文測試abcdef";

		String algorithm = "DES";
		String assignKey = "abcdefgh01234567abcdefgh";
		// 隨機key
		SecretKey secretKey = SecurityHelper.randomSecretKey(algorithm);
		String encryptToHex = SecurityHelper.encryptHex(value, secretKey,
				algorithm);
		byte[] result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.decryptHex(encryptToHex, secretKey,
					algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);// -28, -72, -83, -26, -106, -121, -26,
										// -72, -84, -24, -87, -90, 97, 98, 99,
										// 100, 101, 102
		assertEquals(18, result.length);
		//
		String stringValue = ByteHelper.toString(result);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals(value, stringValue);
	}

	// @Test
	// // 1000000 times: 7901 mills.
	// // 1000000 times: 7812 mills.
	// // 1000000 times: 8108 mills.
	// public void decryptString() {
	// String value = "a284a311c40885ed0df37642c78120a2";
	// String assignKey = "FarFarAway";
	// String algorithm = "DES";
	// //
	// String result = null;
	//
	// int count = 1;
	// long beg = System.currentTimeMillis();
	// for (int i = 0; i < count; i++) {
	// result = SecurityHelper.decryptString(value, EncodingHelper.UTF_8,
	// assignKey, algorithm);
	// }
	// long end = System.currentTimeMillis();
	// System.out.println(count + " times: " + (end - beg) + " mills. ");
	//
	// System.out.println(result);// 1qa2ws3ed
	// assertEquals(9, result.length());
	// //
	// result = SecurityHelper.decryptString(null, EncodingHelper.UTF_8,
	// assignKey, algorithm);
	// System.out.println(result);// null
	// //
	// result = SecurityHelper.decryptString("", EncodingHelper.UTF_8,
	// assignKey, algorithm);
	// System.out.println(result);// ""
	// }

	@Test
	// 1000000 times: 7901 mills.
	// 1000000 times: 7812 mills.
	// 1000000 times: 8108 mills.
	public void decryptBase64() {
		String value = "中文測試abcdef";

		String algorithm = "DES";
		// 隨機key
		SecretKey secretKey = SecurityHelper.randomSecretKey(algorithm);
		String encryptToBase64 = SecurityHelper.encryptBase64(value, secretKey,
				algorithm);
		byte[] result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.decryptBase64(encryptToBase64, secretKey,
					algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);// -28, -72, -83, -26, -106, -121, -26,
										// -72, -84, -24, -87, -90, 97, 98, 99,
										// 100, 101, 102
		assertEquals(18, result.length);
		//
		String stringValue = ByteHelper.toString(result);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals(value, stringValue);
	}

	@Test
	// 隨機key
	// 1000000 times: 13034 mills.
	// 1000000 times: 13543 mills.
	// 1000000 times: 11782 mills.
	public void decrypt() {
		String value = "中文測試abcdef";

		String algorithm = "DES";
		SecretKey secretKey = SecurityHelper.randomSecretKey(algorithm);
		byte[] encrypt = SecurityHelper.encrypt(value, secretKey, algorithm);

		byte[] result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.decrypt(encrypt, secretKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);
		assertEquals(18, result.length);
		//
		String stringValue = ByteHelper.toString(result);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals(value, stringValue);
	}

	@Test
	// 指定key
	// 1000000 times: 14767 mills.
	// 1000000 times: 13543 mills.
	// 1000000 times: 11782 mills.
	public void encryptAssignKey() {
		String value = "中文測試abcdef";

		String algorithm = "DES";
		// byte[] assignKey = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
		// String assignKey="ABCDEFGH"; //[8]
		String assignKey = "abcdefgh"; // [8]
		SecretKey secretKey = SecurityHelper.createSecretKey(assignKey,
				algorithm);

		byte[] result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.encrypt(value, secretKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);// 119, -18, -62, 95, -17, 33, 105, 56, 72,
										// -20, 88, 99, -63, 106, -84, -88, 100,
										// 72, -70, -109, -58, 107, 24, 38
		assertEquals(24, result.length);
	}

	@Test
	// 指定key
	// 1000000 times: 13034 mills.
	// 1000000 times: 13543 mills.
	// 1000000 times: 11782 mills.
	public void decryptAssignKey() {
		String value = "中文測試abcdef";

		String algorithm = "DES";
		// byte[] assignKey = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
		// String assignKey="ABCDEFGH"; //[8]
		String assignKey = "abcdefgh"; // [8]
		SecretKey secretKey = SecurityHelper.createSecretKey(assignKey,
				algorithm);
		byte[] encrypt = SecurityHelper.encrypt(value, secretKey, algorithm);

		byte[] result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.decrypt(encrypt, secretKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);
		assertEquals(18, result.length);
		//
		String stringValue = ByteHelper.toString(result);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals(value, stringValue);
	}

	@Test
	// 1000000 times: 13034 mills.
	// 1000000 times: 13543 mills.
	// 1000000 times: 11782 mills.
	public void encryptDecryptHex() {
		String value = "中文測試abcdef";
		//
		String algorithm = "DES";
		SecretKey secretKey = SecurityHelper.randomSecretKey(algorithm);
		String keyFile = "encryptDecryptByHex";

		// 將key寫入檔案,給解密用
		String writeKeyFile = SecurityHelper.writeSecretKey(keyFile, secretKey);
		assertNotNull(writeKeyFile);
		//
		String encodeByHex = null;
		byte[] encrypt = null;
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			encrypt = SecurityHelper.encrypt(value, secretKey, algorithm);
			encodeByHex = EncodingHelper.encodeHex(encrypt);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(encodeByHex.length() + ", " + encodeByHex);// 48,
																		// b824f54aa6cc7c0003afea7627c5c308b5e6842fcf6481cc
		//
		byte[] decodeByHex = EncodingHelper.decodeHex(encodeByHex);

		// 從檔案讀取key
		secretKey = SecurityHelper.readSecretKey(keyFile);
		assertNotNull(secretKey);
		//
		byte[] decrypt = SecurityHelper.decrypt(decodeByHex, secretKey, "DES");
		//
		String stringValue = ByteHelper.toString(decrypt);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals(value, stringValue);
	}

	@Test
	// 1000000 times: 13034 mills.
	// 1000000 times: 13543 mills.
	// 1000000 times: 11782 mills.
	public void encryptDecryptByBase64() {
		String value = "中文測試abcdef";
		//
		String algorithm = "DES";
		SecretKey secretKey = SecurityHelper.randomSecretKey(algorithm);
		String keyFile = "encryptDecryptByBase64";

		// 將key寫入檔案,給解密用
		String writeKeyFile = SecurityHelper.writeSecretKey(keyFile, secretKey);
		assertNotNull(writeKeyFile);
		//
		String encodeByBase64 = null;
		byte[] encrypt = null;
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			encrypt = SecurityHelper.encrypt(value, secretKey, algorithm);
			encodeByBase64 = EncodingHelper.encodeBase64String(encrypt);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(encodeByBase64.length() + ", " + encodeByBase64);// 32,
																			// z1mXJdE7WYvMsde970GNk6XyyxY9a85i
		//
		byte[] decodeByBase64 = EncodingHelper.decodeBase64(encodeByBase64);

		// 從檔案讀取key
		secretKey = SecurityHelper.readSecretKey(keyFile);
		assertNotNull(secretKey);
		//
		byte[] decrypt = SecurityHelper.decrypt(decodeByBase64, secretKey,
				"DES");
		//
		String stringValue = ByteHelper.toString(decrypt);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals(value, stringValue);
	}

	@Test
	// 隨機key
	// 1000000 times: 14767 mills.
	// 1000000 times: 13543 mills.
	// 1000000 times: 11782 mills.
	public void encryptKeyPair() {
		String value = "中文測試abcdef";

		String algorithm = "RSA";
		KeyPair keyPair = SecurityHelper.randomKeyPair(1024, algorithm);
		byte[] result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.encrypt(value, keyPair, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);
		assertEquals(128, result.length);
	}

	@Test
	// 隨機key
	// 1000000 times: 13034 mills.
	// 1000000 times: 13543 mills.
	// 1000000 times: 11782 mills.
	public void decryptKeyPair() {
		String value = "中文測試abcdef";

		String algorithm = "RSA";
		KeyPair keyPair = SecurityHelper.randomKeyPair(1024, algorithm);
		byte[] encryptValue = SecurityHelper.encrypt(value, keyPair, algorithm);
		;

		byte[] result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// result = SecurityHelper.decrypt(encryptValue, keyPair,
			// "RSA/ECB/PKCS1Padding");
			result = SecurityHelper.decrypt(encryptValue, keyPair, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);
		assertEquals(18, result.length);
		//
		String stringValue = ByteHelper.toString(result);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals(value, stringValue);
	}

	@Test
	// 1000000 times: 3981 mills.
	// 1000000 times: 3787 mills.
	// 1000000 times: 3979 mills.
	public void mac() {
		// String value = "";
		String value = "中文測試abcdef";
		String algorithm = "HmacMD5";
		String assignKey = "中文key123";// HmacMD5 可中英文長度不拘
		SecretKey secretKey = SecurityHelper.createSecretKey(assignKey,
				algorithm);

		byte[] result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.mac(value, secretKey, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);
		assertEquals(16, result.length);
		//
		algorithm = "HmacSHA1";
		assignKey = "FarFarAway";
		secretKey = SecurityHelper.createSecretKey(assignKey, algorithm);
		result = SecurityHelper.mac(value, secretKey, algorithm);
		SystemHelper.println(result);
		assertEquals(20, result.length);
	}

	@Test
	// 1000000 times: 3981 mills.
	// 1000000 times: 3787 mills.
	// 1000000 times: 3979 mills.
	public void md() {
		String value = "中文測試abcdef";

		String algorithm = "MD5";
		byte[] result = null;

		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.md(value, algorithm);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);// 51, -46, 64, -103, -104, -110, -80, -9,
										// 12, -98, 17, 114, -4, -93, 90, 27
		assertEquals(16, result.length);
	}

	// @Test
	// //密碼加密,單向不可逆
	// //1000000 times: 2826 mills.
	// //1000000 times: 2758 mills.
	// //1000000 times: 2791 mills.
	// //verified
	// public void encryptPassword()
	// {
	// String value = "中文測試abcdef";
	// String result = null;
	//
	// int count = 1000000;
	//
	// long beg = System.currentTimeMillis();
	// for (int i = 0; i < count; i++)
	// {
	// result = SecurityHelper.encryptPassword(value);
	// }
	// long end = System.currentTimeMillis();
	// System.out.println(count + " times: " + (end - beg) + " mills. ");
	//
	// System.out.println(result.length() + ", " +
	// result);//12f64c3a212722f60a9c6d685c992a87
	// }

	// @Test
	// // 1000000 times: 2924 mills.
	// // 1000000 times: 2758 mills.
	// // 1000000 times: 2791 mills.
	// // verified
	// public void randomAuthKey() {
	// String result = null;
	// int count = 1000;
	// long beg = System.currentTimeMillis();
	// for (int i = 0; i < count; i++) {
	// result = SecurityHelper.randomAuthKey();
	// System.out.println("[" + i + "] " + result);
	// }
	// long end = System.currentTimeMillis();
	// System.out.println(count + " times: " + (end - beg) + " mills. ");
	//
	// System.out.println(result.length() + ", " + result);//
	// 12f64c3a212722f60a9c6d685c992a87
	// byte[] bytes = ByteHelper.toBytes(result);
	// System.out.print(bytes.length + ", ");
	// SystemHelper.println(bytes);
	// }

	@Test
	public void writeSecretKey() {
		SecretKey value = SecurityHelper.randomSecretKey("DES");

		int count = 1;
		long beg = System.currentTimeMillis();
		String result = null;
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.writeSecretKey("secretKey", value);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

	@Test
	public void writeKeyPair() {
		KeyPair keyPair = SecurityHelper.randomKeyPair(1024, "RSA");

		int count = 1;
		long beg = System.currentTimeMillis();
		String result = null;
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.writeKeyPair("keyPair", keyPair);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

	@Test
	public void readSecretKey() {
		SecretKey result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.readSecretKey("secretKey");
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

	@Test
	public void readKeyPair() {
		KeyPair result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = SecurityHelper.readKeyPair("keyPair");
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.59 [+- 0.06], round.block: 0.39 [+- 0.05], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.61, time.warmup: 0.00,
	// time.bench: 0.61
	public void encryptWithProcessor() {
		byte[] value = ByteHelper.toByteArray("中文測試abcdef");
		byte[] result = null;
		//
		result = SecurityHelper.encryptWithProcessor(value);
		//
		System.out.println(result.length + " ," + result);// 24
		assertEquals(24, result.length);
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.61 [+- 0.06], round.block: 0.41 [+- 0.05], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.64, time.warmup: 0.00,
	// time.bench: 0.63
	public void decryptWithProcessor() {
		byte[] value = ByteHelper.toByteArray("中文測試abcdef");
		byte[] encrypt = SecurityHelper.encryptWithProcessor(value);

		byte[] result = null;
		//
		result = SecurityHelper.decryptWithProcessor(encrypt);
		//
		System.out.println(result.length + " ," + result);// 18
		assertTrue(Arrays.equals(value, result));
	}
}
