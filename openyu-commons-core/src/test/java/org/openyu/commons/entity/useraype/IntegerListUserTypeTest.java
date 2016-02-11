package org.openyu.commons.entity.useraype;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.openyu.commons.entity.useraype.IntegerListUserType;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class IntegerListUserTypeTest extends BaseTestSupporter {

	private static IntegerListUserType userType = new IntegerListUserType();

	@Test
	// 1000000 times: 1062 mills.
	// 1000000 times: 1078 mills.
	// 1000000 times: 1134 mills.
	// verified
	public void marshal() {
		List<Integer> value = new LinkedList<Integer>();
		value.add(111);
		value.add(222);
		value.add(333);
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
		assertEquals("♥1♠3♦111♦222♦333", result);
	}

	@Test
	// 1000000 times: 4149 mills.
	// 1000000 times: 4643 mills.
	// 1000000 times: 4221 mills.
	// verified
	public void unmarshal() {
		String value = "♥1♠3♦111♦222♦333";
		//
		List<Integer> result = new LinkedList<Integer>();
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
