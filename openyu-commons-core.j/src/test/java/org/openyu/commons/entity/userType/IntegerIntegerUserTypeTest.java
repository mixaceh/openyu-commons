package org.openyu.commons.entity.userType;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class IntegerIntegerUserTypeTest extends BaseTestSupporter {

	private static IntegerIntegerUserType userType = new IntegerIntegerUserType();

	@Test
	// 1000000 times: 1597 mills.
	// 1000000 times: 1619 mills.
	// 1000000 times: 1511 mills.
	// verified
	public void marshal() {
		Map<Integer, Integer> value = new LinkedHashMap<Integer, Integer>();
		value.put(1, 111);
		value.put(2, 222);
		value.put(3, 333);
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
		assertEquals("♥1♠3♦1♣111♦2♣222♦3♣333", result);
	}

	@Test
	// 1000000 times: 10301 mills.
	// 1000000 times: 9702 mills.
	// 1000000 times: 9543 mills.
	// verified
	public void unmarshal() {
		String value = "♥1♠3♦1♣111♦2♣222♦3♣333";
		//
		Map<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
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
