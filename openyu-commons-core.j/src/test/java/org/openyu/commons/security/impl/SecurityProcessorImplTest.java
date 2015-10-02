package org.openyu.commons.security.impl;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.security.SecurityProcessor;
import org.openyu.commons.security.SecurityType;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class SecurityProcessorImplTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.69 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.69, time.warmup: 0.00,
	// time.bench: 0.69
	public void writeToXml() {
		SecurityProcessor processor = new SecurityProcessorImpl();
		//
		processor.setSecurity(true);
		processor.setSecurityType(SecurityType.DES_ECB_PKCS5Padding);
		processor.setSecurityKey("NotFarAway");
		//
		String result = CollectorHelper.writeToXml(SecurityProcessorImpl.class,
				processor);
		System.out.println(result);
		assertNotNull(result);
		//
		SecurityProcessor compare = new SecurityProcessorImpl();
		assertTrue(processor.getSecurityTypes() == compare.getSecurityTypes());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.68 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.68, time.warmup: 0.00,
	// time.bench: 0.68
	public void readFromXml() {
		SecurityProcessor processor = CollectorHelper
				.readFromXml(SecurityProcessorImpl.class);
		System.out.println(processor);
		assertNotNull(processor);
		//
		SecurityProcessor compare = new SecurityProcessorImpl();
		assertTrue(processor.getSecurityTypes() == compare.getSecurityTypes());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void encrypt() {
		SecurityProcessor processor = new SecurityProcessorImpl();
		processor.setSecurity(true);
		processor.setSecurityType(SecurityType.DES_CBC_PKCS5Padding);
		processor.setSecurityKey("12345678");
		//
		byte[] value = ByteHelper.toByteArray("中文測試abcdef");
		byte[] result = null;

		// DES
		result = processor.encrypt(value);
		//
		System.out.println(result.length);
		assertEquals(24, result.length);
		//
		processor.setSecurityType(SecurityType.DES_ECB_PKCS5Padding);
		result = processor.encrypt(value);
		System.out.println(result.length);
		assertEquals(24, result.length);
		//
		processor.setSecurityType(SecurityType.DES);
		result = processor.encrypt(value);
		System.out.println(result.length);
		assertEquals(24, result.length);
		//
		result = processor.encrypt("DES/ECB/PKCS5Padding", value);
		System.out.println(result.length);
		assertEquals(24, result.length);

		// DESede
		processor.setSecurityType(SecurityType.DESede_CBC_PKCS5Padding);
		processor.setSecurityKey("123456781234567812345678");
		result = processor.encrypt(value);
		System.out.println(result.length);
		assertEquals(24, result.length);
		//
		processor.setSecurityType(SecurityType.DESede_ECB_PKCS5Padding);
		result = processor.encrypt(value);
		System.out.println(result.length);
		assertEquals(24, result.length);
		//
		processor.setSecurityType(SecurityType.DESede);
		result = processor.encrypt(value);
		System.out.println(result.length);
		assertEquals(24, result.length);

		// AES
		processor.setSecurityType(SecurityType.AES_CBC_PKCS5Padding);
		processor.setSecurityKey("1234567890123456");
		result = processor.encrypt(value);
		System.out.println(result.length);
		assertEquals(32, result.length);
		//
		processor.setSecurityType(SecurityType.AES_ECB_PKCS5Padding);
		result = processor.encrypt(value);
		System.out.println(result.length);
		assertEquals(32, result.length);
		//
		processor.setSecurityType(SecurityType.AES);
		result = processor.encrypt(value);
		System.out.println(result.length);
		assertEquals(32, result.length);

		// MD
		processor.setSecurityType(SecurityType.MD5);
		processor.setSecurityKey("1234567890123456");
		result = processor.encrypt(value);
		System.out.println(result.length);
		assertEquals(16, result.length);

		// MAC
		processor.setSecurityType(SecurityType.HmacSHA1);
		processor.setSecurityKey("1234567890123456");
		result = processor.encrypt(value);
		System.out.println(result.length);
		assertEquals(20, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void encryptWithTypeName() {
		SecurityProcessor processor = new SecurityProcessorImpl();
		processor.setSecurity(true);
		// securityable.setSecurityType(SecurityType.DES_CBC_PKCS5Padding);
		processor.setSecurityKey("12345678");
		//
		byte[] value = ByteHelper.toByteArray("中文測試abcdef");
		byte[] result = null;

		// DES by name
		result = processor.encrypt("DES_ECB_PKCS5Padding", value);
		//
		System.out.println(result.length);
		assertEquals(24, result.length);

		// DES by value
		result = processor.encrypt("DES/ECB/PKCS5Padding", value);
		System.out.println(result.length);
		assertEquals(24, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void decrypt() {
		SecurityProcessor processor = new SecurityProcessorImpl();
		processor.setSecurity(true);
		processor.setSecurityType(SecurityType.DES_ECB_PKCS5Padding);
		processor.setSecurityKey("12345678");
		//
		String value = "中文測試abcdef";
		byte[] encrypt = processor.encrypt(ByteHelper.toByteArray(value));
		byte[] result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// DES
			result = processor.decrypt(encrypt);
		}
		//
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result.length);
		String stringValue = ByteHelper.toString(result);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals(value, stringValue);

		// DESede
		processor.setSecurityType(SecurityType.DESede_ECB_PKCS5Padding);
		processor.setSecurityKey("123456781234567812345678");
		encrypt = processor.encrypt(ByteHelper.toByteArray(value));

		result = processor.decrypt(encrypt);
		System.out.println(result.length);
		stringValue = ByteHelper.toString(result);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals(value, stringValue);

		// AES
		processor.setSecurityType(SecurityType.AES_ECB_PKCS5Padding);
		processor.setSecurityKey("1234567890123456");
		encrypt = processor.encrypt(ByteHelper.toByteArray(value));
		result = processor.decrypt(encrypt);
		System.out.println(result.length);
		stringValue = ByteHelper.toString(result);
		System.out.println(stringValue);// 中文測試abcdef
		assertEquals(value, stringValue);
	}

}
