package org.openyu.commons.entity.usertype;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;
import org.openyu.commons.entity.usertype.StringIntegerUserType;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class StringIntegerUserTypeTest extends BaseTestSupporter {

	private static StringIntegerUserType userType = new StringIntegerUserType();

	@Test
	// 1000000 times: 1597 mills.
	// 1000000 times: 1619 mills.
	// 1000000 times: 1511 mills.
	// verified
	public void marshal() {
		Map<String, Integer> value = new LinkedHashMap<String, Integer>();
		value.put("aaa", 111);
		value.put("bbb", 222);
		value.put("ccc", 333);
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
		assertEquals("♥1♠3♦aaa♣111♦bbb♣222♦ccc♣333", result);
	}

	@Test
	// 1000000 times: 8256 mills.
	// 1000000 times: 8232 mills.
	// 1000000 times: 8778 mills.
	// verified
	public void unmarshal() {
		String value = "♥1♠3♦aaa♣111♦bbb♣222♦ccc♣333";
		//
		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
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
