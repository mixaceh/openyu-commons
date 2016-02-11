package org.openyu.commons.hibernate.usertype.supporter;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.junit.Test;
import org.openyu.commons.hibernate.usertype.supporter.BaseUserTypeSupporter;

public class BaseUserTypeSupporterTest {

	private static BaseUserTypeImpl userType = new BaseUserTypeImpl();

	// --------------------------------------------------
	// just for test, coz BaseUserTypeSupporter is an abstract class
	public static class BaseUserTypeImpl extends BaseUserTypeSupporter {

		private static final long serialVersionUID = 6012175079003136458L;

		public Class<?> returnedClass() {
			return null;
		}

		public <R, T> R marshal(T value, SessionImplementor session) {
			return null;
		}

		public <R, T, O> R unmarshal(T value, O owner,
				SessionImplementor session) {
			return null;
		}
	}

	// --------------------------------------------------

	@Test
	// 1000000 times: 858 mills.
	// 1000000 times: 857 mills.
	// 1000000 times: 871 mills.
	// verified
	public void disassembleVol() {
		String value = "♥1♠sys♠2649600000♠sys♠2649600000";
		StringBuilder buff = null;
		int result = 0;
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			buff = new StringBuilder(value);
			result = userType.disassembleVol(buff);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result + ", " + buff);
		assertEquals(1, result);
	}

	@Test
	// 1000000 times: 76 mills.
	// 1000000 times: 75 mills.
	// 1000000 times: 75 mills.
	// verified
	public void toStringz() {
		Date date = new Date(70, 1, 1, 0, 0, 0);
		String result = null;
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = userType.toString(date);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals("2649600000", result);
	}

	@Test
	// 1000000 times: 51 mills.
	// 1000000 times: 52 mills.
	// 1000000 times: 51 mills.
	// verified
	public void toObject() {
		String[] values = StringUtils
				.splitPreserveAllTokens("sys♠2649600000♠sys♠2649600000",
						BaseUserTypeSupporter.SPLITTER);
		Object result = null;
		List results = new LinkedList();
		int idx = 0;
		boolean first = true;
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = userType.toObject(values, idx++, String.class);
			if (first) {
				results.add(result);
			}
			//
			result = userType.toObject(values, idx++, Date.class);
			if (first) {
				results.add(result);
			}
			//
			result = userType.toObject(values, idx++, String.class);
			if (first) {
				results.add(result);
			}
			//
			result = userType.toObject(values, idx++, Date.class);
			if (first) {
				results.add(result);
			}
			//
			first = false;
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(results);
		assertEquals(4, results.size());
		//
		values = StringUtils.splitPreserveAllTokens("aaa♠1",
				BaseUserTypeSupporter.SPLITTER);
		idx = 0;
		result = userType.toObject(values, idx++, String.class);
		System.out.println(result);
		//
		result = userType.toObject(values, idx++, int.class);
		System.out.println(result);
	}

}
