package org.openyu.commons.entity.userType;

import static org.junit.Assert.*;

import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Test;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class StringSetUserTypeTest extends BaseTestSupporter {

	private static StringSetUserType userType = new StringSetUserType();

	@Test
	// 1000000 times: 1062 mills.
	// 1000000 times: 1078 mills.
	// 1000000 times: 1134 mills.
	// verified
	public void marshal() {
		Set<String> value = new LinkedHashSet<String>();
		value.add("aaa");
		value.add("bbb");
		value.add("ccc");
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
		assertEquals("♥1♠3♦aaa♦bbb♦ccc", result);
	}

	@Test
	// 1000000 times: 4149 mills.
	// 1000000 times: 4643 mills.
	// 1000000 times: 4221 mills.
	// verified
	public void unmarshal() {
		String value = "♥1♠3♦aaa♦bbb♦ccc";
		//
		Set<String> result = new LinkedHashSet<String>();
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
