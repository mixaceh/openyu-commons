package org.openyu.commons.entity.userType;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class IntegerStringUserTypeTest extends BaseTestSupporter {

	private static IntegerStringUserType userType = new IntegerStringUserType();

	@Test
	// 1000000 times: 1597 mills.
	// 1000000 times: 1619 mills.
	// 1000000 times: 1511 mills.
	// verified
	public void marshal() {
		Map<Integer, String> value = new LinkedHashMap<Integer, String>();
		value.put(1, "aaa");
		value.put(2, "bbb");
		value.put(3, "ccc");
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
		assertEquals("♥1♠3♦1♣aaa♦2♣bbb♦3♣ccc", result);
	}

	@Test
	// 1000000 times: 10301 mills.
	// 1000000 times: 9702 mills.
	// 1000000 times: 9543 mills.
	// verified
	public void unmarshal() {
		String value = "♥1♠3♦1♣aaa♦2♣bbb♦3♣ccc";
		//
		Map<Integer, String> result = new LinkedHashMap<Integer, String>();
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
