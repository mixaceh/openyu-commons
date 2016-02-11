package org.openyu.commons.entity.useraype;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.openyu.commons.entity.useraype.StringStringUserType;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class StringStringUserTypeTest extends BaseTestSupporter {

	private static StringStringUserType userType = new StringStringUserType();

	@Test
	// 1000000 times: 1597 mills.
	// 1000000 times: 1619 mills.
	// 1000000 times: 1511 mills.
	// verified
	public void marshal() {
		Map<String, String> value = new LinkedHashMap<String, String>();
		value.put("aaa", "AAA");
		value.put("bbb", "BBB");
		value.put("ccc", "CCC");
		//
		String result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = userType.marshal(value, null);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		// 1
		System.out.println(result);
		//
		assertEquals("♥1♠3♦aaa♣AAA♦bbb♣BBB♦ccc♣CCC", result);
	}

	@Test
	// 1000000 times: 8256 mills.
	// 1000000 times: 8232 mills.
	// 1000000 times: 8778 mills.
	// verified
	public void unmarshal() {
		String value = "♥1♠3♦aaa♣AAA♦bbb♣BBB♦ccc♣CCC";
		//
		Map<String, String> result = new LinkedHashMap<String, String>();
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = userType.unmarshal(value, null, null);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(3, result.size());
	}
}
