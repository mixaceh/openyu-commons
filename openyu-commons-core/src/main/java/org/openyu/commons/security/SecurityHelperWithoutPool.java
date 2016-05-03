package org.openyu.commons.security;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.EncodingHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.security.SecurityProcessor;
import org.openyu.commons.security.impl.SecurityProcessorImpl;
import org.openyu.commons.util.ConfigHelper;
import org.openyu.commons.util.SerializeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * encrypt -> encode -> decode -> decrypt
 * 
 * 
 * 編解碼: encodeByHex(雙向)
 * 
 * 編解碼: encodeByBase64(雙向)
 * 
 * 有key對稱加密: encrypt(雙向)
 * 
 * 有key非對稱加密: encrypt(雙向)
 * 
 * 無key加密: encryptByMd(單向)
 * 
 * 有key加密: encryptByMac(單向)
 * 
 */

// 對稱加密算法用來對敏感數據等信息進行加密，常用的算法包括：
// DES（Data Encryption Standard）：數據加密標準，速度較快，適用於加密大量數據的場合。
// 3DES（Triple DES）：是基於DES，對一塊數據用三個不同的密鑰進行三次加密，強度更高。
// AES（Advanced Encryption Standard)：高級加密標準，什麼下一代的加密算法標準，速度快，安全級別高;

// 常見的非對稱加密演算法如下：
// RSA：由 RSA 公司發明，是一個支持變長金鑰的公共金鑰演算法，需要加密的檔塊的長度也是可變的；
// DSA（Digital Signature Algorithm）：數位簽章演算法，是一種標準的 DSS（數位簽章標準）；
// ECC（Elliptic Curves Cryptography）：橢圓曲線密碼編碼學。

// AlgorithmParameters DSA
// CertificateFactory X.509
// CertPathBuilder PKIX
// CertPathValidator PKIX
// CertStore Collection
// Cipher (the algorithms are specified as transformations). Implementations
// must support up to the key size in parentheses.

// AES/CBC/NoPadding (128)
// AES/CBC/PKCS5Padding (128)
// AES/ECB/NoPadding (128)
// AES/ECB/PKCS5Padding (128)

// DES/CBC/NoPadding (56)
// DES/CBC/PKCS5Padding (56)
// DES/ECB/NoPadding (56)
// DES/ECB/PKCS5Padding (56)

// DESede/CBC/NoPadding (168)
// DESede/CBC/PKCS5Padding (168)
// DESede/ECB/NoPadding (168)
// DESede/ECB/PKCS5Padding (168)

// RSA/ECB/PKCS1Padding (2048)
// RSA/ECB/OAEPPadding (2048)
//
// KeyFactory (implementations must support up to the key size in parentheses)
// DSA (1024)
// RSA (2048)
//
// KeyGenerator (implementations must support up to the key size in parentheses)
// AES (128)
// DES (56)
// DESede (168)
// HmacMD5 (128)
// HmacSHA1 (160)
// HmacSHA256 (160)
//
// KeyPairGenerator (implementations must support up to the key size in
// parentheses)
// DSA (1024)
// RSA (2048)
//
// KeyStore
// jks
// pkcs12
//
// Mac (implementations must support up to the key size in parentheses)
// HmacMD5 (128)
// HmacSHA1 (160)
// HmacSHA256 (160)
//
// MessageDigest
// MD5
// SHA-1
// SHA-256
//
// SecretKeyFactory (implementations must support up to the key size in
// parentheses)
// AES (128)
// DES (56)
// DESede (168)
//
// Signature
// MD5withRSA
// SHA1withDSA
// SHA1withRSA
// SHA256withRSA

/**
 * 安全性輔助類
 */
public final class SecurityHelperWithoutPool extends BaseHelperSupporter {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(SecurityHelperWithoutPool.class);

	// --------------------------------------------------------
	// SecureRandom Number Generation (RNG) Algorithms
	// --------------------------------------------------------
	// SHA1PRNG The name of the pseudo-random number generation (PRNG) algorithm
	// supplied by the SUN provider. This algorithm uses SHA-1 as the foundation
	// of the PRNG. It computes the SHA-1 hash over a true-random seed value
	// concatenated with a 64-bit counter which is incremented by 1 for each
	// operation. From the 160-bit SHA-1 output, only 64 bits are used.
	private static SecureRandom secureRandom;

	// // appConfig-op.xml
	// public final static String ASSIGN_KEY = "securityHelper.assignKey";
	//
	// private static String assignKey;
	//
	// //
	// public final static String ALGORITHM = "securityHelper.algorithm";
	//
	// private static String algorithm;
	//
	// //
	// public final static String RANDOM_AUTH_KEY =
	// "securityHelper.randomAuthKey";
	//
	// private static String randomAuthKey;

	// 2014/11/24, 會很耗mem, 先不使用
	// /** 安全性處理器 */
	// private static SoftReferenceCacheFactory<SecurityProcessor>
	// securityProcessorCacheFactory;

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			try {
				secureRandom = SecureRandom.getInstance("SHA1PRNG");
				secureRandom.setSeed(System.nanoTime());

			} catch (Exception ex) {
				throw new HelperException("new Static() Initializing failed", ex);
			}
		}
	}

	private SecurityHelperWithoutPool() {
		throw new HelperException(new StringBuilder().append(SecurityHelperWithoutPool.class.getName())
				.append(" can not construct").toString());
	}

	/**
	 * 
	 * @param assignKey
	 *            指定 key
	 * @param algorithm
	 * @return
	 */
	public static SecretKey createSecretKey(String assignKey, String algorithm) {
		return createSecretKey(assignKey, EncodingHelper.UTF_8, algorithm);
	}

	/**
	 * 
	 * @param assignKey
	 *            指定 key
	 * @param charsetName
	 * @param algorithm
	 * @return
	 */
	public static SecretKey createSecretKey(String assignKey, String charsetName, String algorithm) {
		byte[] keysValue = ByteHelper.toByteArray(assignKey, charsetName);
		return createSecretKey(keysValue, algorithm);
	}

	/**
	 * 
	 * Key
	 * 
	 * +-SecretKey <- SecretKeySpec <- KerberosKey
	 * 
	 * KeySpec <- SecretKeySpec <- RSAPublicKeySpec
	 * 
	 * 建構指定key的SecretKey, 提供string,byte[]兩種型別建構
	 * 
	 * @param assignKey
	 * @param algorithm
	 * @return
	 */
	// assignKey長度,目前已ok的有
	// byte[8] ,DES
	// byte[24] ,DESede
	// byte[16] ,AES
	// 不限,HmacMD5
	// 不限,HmacSHA1
	public static SecretKey createSecretKey(byte[] assignKey, String algorithm) {
		SecretKey result = null;
		if (ByteHelper.notEmpty(assignKey) && StringHelper.notEmpty(algorithm)) {
			try {
				// new byte[8]
				if ("DES".equals(algorithm)) {
					DESKeySpec desKeySpec = new DESKeySpec(assignKey);
					SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
					result = factory.generateSecret(desKeySpec);
				}
				// new byte[24]
				else if ("DESede".equals(algorithm)) {
					DESedeKeySpec desedeKeySpec = new DESedeKeySpec(assignKey);
					SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
					result = factory.generateSecret(desedeKeySpec);
					// new byte[16]
				} else if ("AES".equals(algorithm)) {
					// byte[] buff = assignKey;
					// MessageDigest sha = MessageDigest.getInstance("SHA-1");
					// buff = sha.digest(buff);
					// buff = Arrays.copyOf(buff, 16); // use only first 128 bit

					byte[] buff = md(assignKey, "MD5");// MD5/SHA-1
					// buff = Arrays.copyOf(buff, 16); // use only first 128 bit
					buff = ByteHelper.getByteArray(buff, 0, 16);
					result = new SecretKeySpec(buff, algorithm);
				} else {
					// HmacMD5
					// HmacSHA1
					result = new SecretKeySpec(assignKey, algorithm);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// --------------------------------------------------------
	// KeyGenerator algorithm
	// --------------------------------------------------------
	// AES Key generator for use with the AES algorithm.
	// ARCFOUR Key generator for use with the ARCFOUR (RC4) algorithm.
	// Blowfish Key generator for use with the Blowfish algorithm.
	// DES Key generator for use with the DES algorithm. keysize=56
	// DESede Key generator for use with the DESede (triple-DES) algorithm.
	// keysize=112-168
	// HmacMD5 Key generator for use with the HmacMD5 algorithm.
	//
	// HmacSHA1
	// HmacSHA256
	// HmacSHA384
	// HmacSHA512 Keys generator for use with the various flavors of the HmacSHA
	// algorithms.
	//
	// RC2 Key generator for use with the RC2 algorithm.

	/**
	 * 隨機 SecretKey
	 * 
	 * @param algorithm
	 * @return
	 */
	public static SecretKey randomSecretKey(String algorithm) {
		SecretKey result = null;
		if (StringHelper.notEmpty(algorithm)) {
			try {
				// 換掉provider,BouncyCastleProvider會比較慢
				// KeyGenerator generator = KeyGenerator.getInstance(algorithm,
				// new BouncyCastleProvider());

				KeyGenerator generator = KeyGenerator.getInstance(algorithm);
				generator.init(secureRandom);
				result = generator.generateKey();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// --------------------------------------------------------
	// KeyPairGenerator algorithm
	// --------------------------------------------------------
	// DiffieHellman(DH) Generates keypairs for the Diffie-Hellman KeyAgreement
	// algorithm.
	// Note: key.getAlgorithm() will return "DH" instead of "DiffieHellman".
	//
	// DSA Generates keypairs for the Digital Signature Algorithm.
	// RSA Generates keypairs for the RSA algorithm (Signature/Cipher).
	// EC Generates keypairs for the Elliptic Curve algorithm.
	/**
	 * 隨機 KeyPair
	 * 
	 * @param keysize
	 *            建議 1024 bit(128 byte)
	 * @algorithm
	 * @return
	 */
	public static KeyPair randomKeyPair(int keysize, String algorithm) {
		KeyPair keyPair = null;
		if (keysize > 0 && StringHelper.notEmpty(algorithm)) {
			try {
				// KeyPairGenerator generator =
				// KeyPairGenerator.getInstance(algorithm);
				// // key size 2048 bit=256 byte
				// generator.initialize(new RSAKeyGenParameterSpec(keyize,
				// publicExponent));
				// keyPair = generator.generateKeyPair();

				// 換掉provider,BouncyCastleProvider會比較慢
				// KeyPairGenerator generator =
				// KeyPairGenerator.getInstance(algorithm,
				// new BouncyCastleProvider());

				KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
				generator.initialize(keysize, secureRandom);
				keyPair = generator.generateKeyPair();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return keyPair;
	}

	// --------------------------------------------------------
	// 對稱加解密
	// --------------------------------------------------------
	public static byte[] encrypt(String value, SecretKey secretKey, String algorithm) {
		return encrypt(value, EncodingHelper.UTF_8, secretKey, algorithm);
	}

	public static byte[] encrypt(String value, String charsetName, SecretKey secretKey, String algorithm) {
		byte[] buffs = ByteHelper.toByteArray(value, charsetName);
		return encrypt(buffs, secretKey, algorithm);
	}

	// --------------------------------------------------------
	// Cipher algorithm
	// --------------------------------------------------------
	// AES Advanced Encryption Standard as specified by NIST in FIPS 197. Also
	// known as the Rijndael algorithm by Joan Daemen and Vincent Rijmen, AES is
	// a 128-bit block cipher supporting keys of 128, 192, and 256 bits.
	// AESWrap The AES key wrapping algorithm as described in RFC 3394.
	// ARCFOUR A stream cipher believed to be fully interoperable with the RC4
	// cipher developed by Ron Rivest. For more information, see K. Kaukonen and
	// R. Thayer, "A Stream Cipher Encryption Algorithm 'Arcfour'", Internet
	// Draft (expired), draft-kaukonen-cipher-arcfour-03.txt.
	// Blowfish The Blowfish block cipher designed by Bruce Schneier.
	// DES The Digital Encryption Standard as described in FIPS PUB 46-3.
	// DESede Triple DES Encryption (also known as DES-EDE, 3DES, or
	// Triple-DES). Data is encrypted using the DES algorithm three separate
	// times. It is first encrypted using the first subkey, then decrypted with
	// the second subkey, and encrypted with the third subkey.
	// DESedeWrap The DESede key wrapping algorithm as described in RFC 3217 .
	// ECIES Elliptic Curve Integrated Encryption Scheme
	// PBEWith<digest>And<encryption> PBEWith<prf>And<encryption> The
	// password-based encryption algorithm found in (PKCS5), using the specified
	// message digest (<digest>) or pseudo-random function (<prf>) and
	// encryption algorithm (<encryption>). Examples:
	// PBEWithMD5AndDES: The password-based encryption algorithm as defined in
	// RSA Laboratories, "PKCS5: Password-Based Encryption Standard," version
	// 1.5, Nov 1993. Note that this algorithm implies CBC as the cipher mode
	// and PKCS5Padding as the padding scheme and cannot be used with any other
	// cipher modes or padding schemes.
	// PBEWithHmacSHA1AndDESede: The password-based encryption algorithm as
	// defined in RSA Laboratories,
	// "PKCS5: Password-Based Cryptography Standard," version 2.0, March 1999.
	//
	// RC2 Variable-key-size encryption algorithms developed by Ron Rivest for
	// RSA Data Security, Inc.
	// RC4 Variable-key-size encryption algorithms developed by Ron Rivest for
	// RSA Data Security, Inc. (See note above for ARCFOUR.)
	// RC5 Variable-key-size encryption algorithms developed by Ron Rivest for
	// RSA Data Security, Inc.
	// RSA The RSA encryption algorithm as defined in PKCS1
	/**
	 * 對稱加密
	 * 
	 * @param values
	 * @param secretKey
	 * @param algorithm
	 * @return
	 */
	public static byte[] encrypt(byte[] values, SecretKey secretKey, String algorithm) {
		byte[] result = new byte[0];
		if (ByteHelper.notEmpty(values) && secretKey != null && StringHelper.notEmpty(algorithm)) {
			try {
				Cipher cipher = Cipher.getInstance(algorithm);
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				result = cipher.doFinal(values);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// assignKey
	public static String encryptHex(String value, String assignKey, String algorithm) {
		return encryptHex(value, EncodingHelper.UTF_8, assignKey, algorithm);
	}

	public static String encryptHex(String value, String charsetName, String assignKey, String algorithm) {
		// 指定key
		SecretKey secretKey = createSecretKey(assignKey, algorithm);
		return encryptHex(value, charsetName, secretKey, algorithm);
	}

	// secretKey
	public static String encryptHex(String value, SecretKey secretKey, String algorithm) {
		return encryptHex(value, EncodingHelper.UTF_8, secretKey, algorithm);
	}

	public static String encryptHex(String value, String charsetName, SecretKey secretKey, String algorithm) {
		byte[] buffs = encrypt(value, charsetName, secretKey, algorithm);
		return EncodingHelper.encodeHex(buffs);
	}

	/**
	 * 對稱加密,傳回hex
	 * 
	 * @param value
	 * @param secretKey
	 * @param algorithm
	 * @return
	 */
	public static String encryptHex(byte[] values, SecretKey secretKey, String algorithm) {
		byte[] buffs = encrypt(values, secretKey, algorithm);
		return EncodingHelper.encodeHex(buffs);
	}

	public static String encryptBase64(String value, SecretKey secretKey, String algorithm) {
		byte[] buffs = encrypt(value, secretKey, algorithm);
		return EncodingHelper.encodeBase64String(buffs);
	}

	/**
	 * 對稱加密,傳回Base64
	 * 
	 * @param value
	 * @param secretKey
	 * @param algorithm
	 * @return
	 */
	public static String encryptBase64(byte[] values, SecretKey secretKey, String algorithm) {
		byte[] buffs = encrypt(values, secretKey, algorithm);
		return EncodingHelper.encodeBase64String(buffs);
	}

	/**
	 * 對稱解密
	 * 
	 * @param values
	 * @param secretKey
	 * @param algorithm
	 * @return
	 */
	public static byte[] decrypt(byte[] values, SecretKey secretKey, String algorithm) {
		byte[] result = new byte[0];
		if (ByteHelper.notEmpty(values) && secretKey != null && StringHelper.notEmpty(algorithm)) {
			try {
				Cipher cipher = Cipher.getInstance(algorithm);
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				result = cipher.doFinal(values);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static byte[] decryptHex(String value, String assignKey, String algorithm) {
		// 指定key
		SecretKey secretKey = SecurityHelperWithoutPool.createSecretKey(assignKey, algorithm);
		return decryptHex(value, secretKey, algorithm);
	}

	public static byte[] decryptHex(String value, SecretKey secretKey, String algorithm) {
		byte[] buffs = EncodingHelper.decodeHex(value);
		return decrypt(buffs, secretKey, algorithm);
	}

	/**
	 * 對稱解密,從hex解密
	 * 
	 * @param value
	 * @param secretKey
	 * @param algorithm
	 * @return
	 */
	public static byte[] decryptHex(byte[] values, SecretKey secretKey, String algorithm) {
		byte[] buffs = EncodingHelper.decodeHex(values);
		return decrypt(buffs, secretKey, algorithm);
	}

	public static byte[] decryptBase64(String value, SecretKey secretKey, String algorithm) {
		byte[] buffs = EncodingHelper.decodeBase64(value);
		return decrypt(buffs, secretKey, algorithm);
	}

	/**
	 * 對稱解密,從Base64解密
	 * 
	 * @param value
	 * @param secretKey
	 * @param algorithm
	 * @return
	 */
	public static byte[] decryptBase64(byte[] values, SecretKey secretKey, String algorithm) {
		byte[] buffs = EncodingHelper.decodeBase64(values);
		return decrypt(buffs, secretKey, algorithm);
	}

	// --------------------------------------------------------
	// 非對稱加解密
	// --------------------------------------------------------
	public static byte[] encrypt(String value, KeyPair keyPair, String algorithm) {
		return encrypt(value, EncodingHelper.UTF_8, keyPair, algorithm);
	}

	public static byte[] encrypt(String value, String charsetName, KeyPair keyPair, String algorithm) {
		byte[] buffs = ByteHelper.toByteArray(value, charsetName);
		return encrypt(buffs, keyPair, algorithm);
	}

	/**
	 * 非對稱加密, RSA, DSA, ECC
	 * 
	 * @param values
	 * @param keyPair
	 * @param algorithm
	 * @return
	 */
	public static byte[] encrypt(byte[] values, KeyPair keyPair, String algorithm) {
		byte[] result = new byte[0];
		if (ByteHelper.notEmpty(values) && keyPair != null && StringHelper.notEmpty(algorithm)) {
			try {
				Cipher cipher = Cipher.getInstance(algorithm);
				cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
				result = cipher.doFinal(values);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 非對稱解密, RSA, DSA, ECC
	 * 
	 * @param values
	 * @param keyPair
	 * @param algorithm
	 * @return
	 */
	public static byte[] decrypt(byte[] values, KeyPair keyPair, String algorithm) {
		byte[] result = new byte[0];
		if (ByteHelper.notEmpty(values) && keyPair != null && StringHelper.notEmpty(algorithm)) {
			try {
				Cipher cipher = Cipher.getInstance(algorithm);
				cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
				result = cipher.doFinal(values);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// --------------------------------------------------------
	// Mac Algorithms
	// --------------------------------------------------------
	// HmacMD5 The HMAC-MD5 keyed-hashing algorithm as defined in RFC 2104
	// "HMAC: Keyed-Hashing for Message Authentication" (February 1997).
	// HmacSHA1
	// HmacSHA256
	// HmacSHA384
	// HmacSHA512
	// The HmacSHA* algorithms as defined in RFC 2104
	// "HMAC: Keyed-Hashing for Message Authentication" (February 1997) with
	// SHA-* as the message digest algorithm.
	// PBEWith<mac> Mac for use with the PKCS5 v 2.0 password-based message
	// authentication standard, where <mac> is a Message Authentication Code
	// algorithm name. Example: PBEWithHmacSHA1.

	public static byte[] mac(String value, SecretKey secretKey, String algorithm) {
		return mac(value, EncodingHelper.UTF_8, secretKey, algorithm);
	}

	public static byte[] mac(String value, String charsetName, SecretKey secretKey, String algorithm) {
		byte[] buffs = ByteHelper.toByteArray(value, charsetName);
		return mac(buffs, secretKey, algorithm);
	}

	/**
	 * 有key, 單向不可逆加密,如:驗證碼加密
	 * 
	 * @param value
	 * @param key
	 * @param algorithm
	 * @return
	 */
	public static byte[] mac(byte[] values, SecretKey secretKey, String algorithm) {
		byte[] result = new byte[0];
		if (ByteHelper.notEmpty(values) && secretKey != null && StringHelper.notEmpty(algorithm)) {
			try {
				// 每次都會建構一個新的Mac
				Mac mac = Mac.getInstance(algorithm);
				// System.out.println("mac:"+mac);
				if (mac != null) {
					mac.init(secretKey);
					result = mac.doFinal(values);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// --------------------------------------------------------
	// MessageDigest Algorithms
	// --------------------------------------------------------
	// MD2 The MD2 message digest algorithm as defined in RFC 1319.
	// MD5 The MD5 message digest algorithm as defined in RFC 1321.
	// SHA-1
	// SHA-256
	// SHA-384
	// SHA-512
	// Hash algorithms defined in the FIPS PUB 180-2.
	//
	// SHA-256 is a 256-bit hash function intended to provide 128 bits of
	// security against collision attacks, while SHA-512 is a 512-bit hash
	// function intended to provide 256 bits of security. A 384-bit hash may be
	// obtained by truncating the SHA-512 output.

	public static byte[] md(String value) {
		return md(value, "MD5");
	}

	public static byte[] md(String value, String algorithm) {
		return md(value, EncodingHelper.UTF_8, algorithm);
	}

	public static byte[] md(String value, String charsetName, String algorithm) {
		byte[] buffs = ByteHelper.toByteArray(value, charsetName);
		return md(buffs, algorithm);
	}

	/**
	 * 無key, 單向不可逆加密,如:個人密碼加密
	 * 
	 * @param value
	 * @param algorithm
	 * @return
	 */
	public static byte[] md(byte[] values, String algorithm) {
		byte[] result = new byte[0];
		if (ByteHelper.notEmpty(values) && StringHelper.notEmpty(algorithm)) {
			try {
				MessageDigest md = MessageDigest.getInstance(algorithm);
				if (md != null) {
					// md.reset();
					// md.update(value);
					// result = md.digest();
					result = md.digest(values);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// --------------------------------------------------------
	// 常用加解密
	// --------------------------------------------------------
	// public static String encryptPassword(String value)
	// {
	// return encryptPassword(value, EncodingHelper.UTF_8);
	// }
	//
	// /**
	// * 指定key加密,密碼加密,單向不可逆
	// *
	// * @param value
	// * @return
	// */
	// public static String encryptPassword(String value, String charsetName)
	// {
	// String result = null;
	// try
	// {
	// //assignKey,HmacMD5
	// SecretKey secretKey = SecurityHelper.createSecretKey(assignKey,
	// algorithm);
	// byte[] buffs = encryptMac(value, charsetName, secretKey, algorithm);
	// result = encodeHex(buffs);
	// }
	// catch (Exception ex)
	// {
	// //ex.printStackTrace();
	// }
	// return result;
	// }

	// public static String encryptString(String value, String algorithm) {
	// return encryptString(value, EncodingHelper.UTF_8, algorithm);
	// }
	//
	// public static String encryptString(String value, String charsetName,
	// String algorithm) {
	// return encryptString(value, charsetName, assignKey, algorithm);
	// }
	//
	// /**
	// * 對稱加密,傳回hex
	// *
	// * @param value
	// * @return
	// */
	// public static String encryptString(String value, String charsetName,
	// String assignKey, String algorithm) {
	// String result = null;
	// if (value != null) {
	// result = encryptHex(value, charsetName, assignKey, algorithm);
	// }
	// return result;
	// }
	//
	// public static String decryptString(String value, String algorithm) {
	// return decryptString(value, EncodingHelper.UTF_8, algorithm);
	// }
	//
	// public static String decryptString(String value, String charsetName,
	// String algorithm) {
	// return decryptString(value, charsetName, assignKey, algorithm);
	// }
	//
	// /**
	// * 對稱解密,從hex解密
	// *
	// * @param value
	// * @return
	// */
	// public static String decryptString(String value, String charsetName,
	// String assignKey, String algorithm) {
	// String result = null;
	// if (value != null) {
	// byte[] buffs = decryptHex(value, assignKey, algorithm);
	// result = ByteHelper.toString(buffs, charsetName);
	// }
	// return result;
	// }

	// /**
	// * 隨機驗證碼
	// *
	// * @return
	// */
	// public static String randomAuthKey() {
	// String result = null;
	// try {
	// // MD5
	// byte[] buffs = encryptMd(String.valueOf(secureRandom.nextInt()),
	// randomAuthKey);
	// result = EncodingHelper.encodeHex(buffs);
	// } catch (Exception ex) {
	// // ex.printStackTrace();
	// }
	// return result;
	// }

	public static String writeSecretKey(String fileName, SecretKey value) {
		String result = null;
		//
		if (fileName == null) {
			throw new IllegalArgumentException("The FileName must not be null");
		}
		//
		OutputStream out = null;
		try {
			// data/key/xxx.key
			StringBuilder buff = new StringBuilder();
			buff.append(ConfigHelper.getKeyDir());
			buff.append(File.separator);
			buff.append(fileName);
			buff.append(".key");
			//
			out = IoHelper.createOutputStream(buff.toString());
			boolean serialized = SerializeHelper.jdk(value, out);
			if (serialized) {
				result = buff.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(out);
		}
		return result;
	}

	public static String writeKeyPair(String fileName, KeyPair value) {
		String result = null;
		//
		if (fileName == null) {
			throw new IllegalArgumentException("The FileName must not be null");
		}
		//
		OutputStream out = null;
		try {
			// data/key/xxx.key
			StringBuilder buff = new StringBuilder();
			buff.append(ConfigHelper.getKeyDir());
			buff.append(File.separator);
			buff.append(fileName);
			buff.append(".key");
			//
			out = IoHelper.createOutputStream(buff.toString());
			boolean serialized = SerializeHelper.jdk(value, out);
			if (serialized) {
				result = buff.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(out);
		}
		return result;
	}

	public static SecretKey readSecretKey(String fileName) {
		SecretKey result = null;
		//
		if (fileName == null) {
			throw new IllegalArgumentException("The FileName must not be null");
		}
		//
		InputStream in = null;
		try {
			StringBuilder buff = new StringBuilder();
			buff.append(ConfigHelper.getKeyDir());
			buff.append(File.separator);
			buff.append(fileName);
			buff.append(".key");
			//
			in = IoHelper.createInputStream(buff.toString());
			result = SerializeHelper.dejdk(in);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(in);
		}
		return result;
	}

	public static KeyPair readKeyPair(String fileName) {
		KeyPair result = null;
		//
		if (fileName == null) {
			throw new IllegalArgumentException("The FileName must not be null");
		}
		//
		InputStream in = null;
		try {
			StringBuilder buff = new StringBuilder();
			buff.append(ConfigHelper.getKeyDir());
			buff.append(File.separator);
			buff.append(fileName);
			buff.append(".key");
			//
			in = IoHelper.createInputStream(buff.toString());
			result = SerializeHelper.dejdk(in);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(in);
		}
		return result;
	}

	// public static SoftReferenceCacheFactory<SecurityProcessor>
	// getSecurityProcessorCacheFactory() {
	// return securityProcessorCacheFactory;
	// }
	//
	// public static void setSecurityProcessorCacheFactory(
	// SoftReferenceCacheFactory<SecurityProcessor>
	// securityProcessorCacheFactory) {
	// SecurityHelper.securityProcessorCacheFactory =
	// securityProcessorCacheFactory;
	// }

	public static byte[] encryptWithProcessor(final byte[] value) {
		byte[] result = new byte[0];
		//
		// result = (byte[]) securityProcessorCacheFactory
		// .execute(new CacheCallback<SecurityProcessor>() {
		// public Object doInAction(SecurityProcessor obj)
		// throws CacheException {
		// try {
		// return obj.encrypt(value);
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });

		SecurityProcessor obj = new SecurityProcessorImpl();
		obj.setSecurity(ConfigHelper.isSecurity());
		obj.setSecurityKey(ConfigHelper.getSecurityKey());
		obj.setSecurityType(ConfigHelper.getSecurityType());
		result = obj.encrypt(value);
		//
		return result;
	}

	public static byte[] decryptWithProcessor(final byte[] value) {
		byte[] result = new byte[0];
		//
		// result = (byte[]) securityProcessorCacheFactory
		// .execute(new CacheCallback<SecurityProcessor>() {
		// public Object doInAction(SecurityProcessor obj)
		// throws CacheException {
		// try {
		// return obj.decrypt(value);
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });
		//
		SecurityProcessor obj = new SecurityProcessorImpl();
		obj.setSecurity(ConfigHelper.isSecurity());
		obj.setSecurityKey(ConfigHelper.getSecurityKey());
		obj.setSecurityType(ConfigHelper.getSecurityType());
		result = obj.decrypt(value);
		//
		return result;
	}

	// refactor to Securityable
	// /**
	// * 加密執行
	// *
	// * @param securityTypeValue
	// * 安全性類別
	// * @see SecurityType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static byte[] execute(String securityTypeValue, byte[] values,
	// String assignKey) {
	// SecurityType securityType = EnumHelper.valueOf(SecurityType.class,
	// securityTypeValue);
	// return execute(securityType, values, assignKey);
	// }
	//
	// /**
	// * 加密執行
	// *
	// * @param securityType
	// * 安全性類別
	// * @see SecurityType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static byte[] execute(SecurityType securityType, byte[] values,
	// String assignKey) {
	// byte[] result = new byte[0];
	// //
	// if (securityType == null) {
	// throw new IllegalArgumentException(
	// "The SecurityType must not be null");
	// }
	// //
	// if (assignKey == null) {
	// throw new IllegalArgumentException("The AssignKey must not be null");
	// }
	// //
	// switch (securityType) {
	// // DES
	// case DES:
	// // case DES_CBC_NoPadding:
	// case DES_CBC_PKCS5Padding:
	// // case DES_ECB_NoPadding:
	// case DES_ECB_PKCS5Padding: {
	// if (assignKey.length() < 8) {
	// throw new IllegalArgumentException(
	// "The AssignKey length must be greater than 8");
	// }
	// SecretKey secretKey = createSecretKey(assignKey,
	// SecurityType.DES.getValue());
	// result = encrypt(values, secretKey, securityType.getValue());
	// break;
	// }
	//
	// // DESede
	// case DESede:
	// // case DES_CBC_NoPadding:
	// case DESede_CBC_PKCS5Padding:
	// // case DES_ECB_NoPadding:
	// case DESede_ECB_PKCS5Padding: {
	// if (assignKey.length() < 24) {
	// throw new IllegalArgumentException(
	// "The AssignKey length must be greater than 24");
	// }
	// SecretKey secretKey = createSecretKey(assignKey,
	// SecurityType.DESede.getValue());
	// result = encrypt(values, secretKey, securityType.getValue());
	// break;
	// }
	//
	// // AES
	// case AES:
	// // case AES_CBC_NoPadding:
	// case AES_CBC_PKCS5Padding:
	// // case AES_ECB_NoPadding:
	// case AES_ECB_PKCS5Padding: {
	// if (assignKey.length() < 16) {
	// throw new IllegalArgumentException(
	// "The AssignKey length must be greater than 16");
	// }
	// SecretKey secretKey = createSecretKey(assignKey,
	// SecurityType.AES.getValue());
	// result = encrypt(values, secretKey, securityType.getValue());
	// break;
	// }
	//
	// // MD
	// case MD5:
	// case SHA_1:
	// case SHA_256: {
	// result = md(values, securityType.getValue());
	// break;
	// }
	//
	// // Hmac
	// case HmacMD5:
	// case HmacSHA1:
	// case HmacSHA256: {
	// SecretKey secretKey = createSecretKey(assignKey,
	// securityType.getValue());
	// result = mac(values, secretKey, securityType.getValue());
	// break;
	// }
	// default: {
	// throw new UnsupportedOperationException(
	// "The SecurityType is not unsupported" + securityType);
	// }
	// }
	// //
	// return result;
	// }
	//
	// /**
	// * 解密執行
	// *
	// * @param securityTypeValue
	// * 安全性類別
	// * @see SecurityType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static byte[] deexecute(String securityTypeValue, byte[] values,
	// String assignKey) {
	// SecurityType securityType = EnumHelper.valueOf(SecurityType.class,
	// securityTypeValue);
	// return deexecute(securityType, values, assignKey);
	// }
	//
	// /**
	// * 解密執行
	// *
	// * @param securityType
	// * 安全性類別
	// * @see SecurityType
	// * @param values
	// * @param assignKey
	// * @return
	// */
	// public static byte[] deexecute(SecurityType securityType, byte[] values,
	// String assignKey) {
	// byte[] result = new byte[0];
	// //
	// if (securityType == null) {
	// throw new IllegalArgumentException(
	// "The SecurityType must not be null");
	// }
	// //
	// if (assignKey == null) {
	// throw new IllegalArgumentException("The AssignKey must not be null");
	// }
	// //
	// switch (securityType) {
	// // DES
	// case DES:
	// // case DES_CBC_NoPadding:
	// case DES_CBC_PKCS5Padding:
	// // case DES_ECB_NoPadding:
	// case DES_ECB_PKCS5Padding: {
	// if (assignKey.length() < 8) {
	// throw new IllegalArgumentException(
	// "The AssignKey length must be greater than 8");
	// }
	// SecretKey secretKey = createSecretKey(assignKey,
	// SecurityType.DES.getValue());
	// result = decrypt(values, secretKey, securityType.getValue());
	// break;
	// }
	//
	// // DESede
	// case DESede:
	// // case DES_CBC_NoPadding:
	// case DESede_CBC_PKCS5Padding:
	// // case DES_ECB_NoPadding:
	// case DESede_ECB_PKCS5Padding: {
	// if (assignKey.length() < 24) {
	// throw new IllegalArgumentException(
	// "The AssignKey length must be greater than 24");
	// }
	// SecretKey secretKey = createSecretKey(assignKey,
	// SecurityType.DESede.getValue());
	// result = decrypt(values, secretKey, securityType.getValue());
	// break;
	// }
	//
	// // AES
	// case AES:
	// // case AES_CBC_NoPadding:
	// case AES_CBC_PKCS5Padding:
	// // case AES_ECB_NoPadding:
	// case AES_ECB_PKCS5Padding: {
	// if (assignKey.length() < 16) {
	// throw new IllegalArgumentException(
	// "The AssignKey length must be greater than 16");
	// }
	// SecretKey secretKey = createSecretKey(assignKey,
	// SecurityType.AES.getValue());
	// result = decrypt(values, secretKey, securityType.getValue());
	// break;
	// }
	// default: {
	// throw new UnsupportedOperationException(
	// "The SecurityType is not unsupported" + securityType);
	// }
	// }
	// //
	// return result;
	// }
}
