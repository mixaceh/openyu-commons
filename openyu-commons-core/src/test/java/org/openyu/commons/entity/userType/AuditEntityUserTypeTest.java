package org.openyu.commons.entity.userType;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.openyu.commons.entity.AuditEntity;
import org.openyu.commons.entity.supporter.AuditEntitySupporter;

public class AuditEntityUserTypeTest {

	private static AuditEntityUserType auditEntityUserType = new AuditEntityUserType();

	@Test
	// 1000000 times: 405 mills.
	// 1000000 times: 405 mills.
	// 1000000 times: 405 mills.
	// verified
	public void marshal() {
		AuditEntity value = new AuditEntitySupporter();
		Date date = new Date(70, 1, 1, 0, 0, 0);

		value.setCreateUser("sys");
		value.setCreateDate(date);
		value.setModifiedUser("sys");
		value.setModifiedDate(date);
		//
		String result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = auditEntityUserType.marshal(value, null);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		// ♥1♠sys♠2649600000♠sys♠2649600000
		System.out.println(result.length() + ", " + result);
		//
		assertEquals("♥1♠sys♠2649600000♠sys♠2649600000", result);
	}

	@Test
	// 1000000 times: 2039 mills.
	// 1000000 times: 2072 mills.
	// 1000000 times: 2035 mills.
	// verified
	public void unmarshal() {
		String value = "♥1♠sys♠2649600000♠sys♠2649600000";
		//
		AuditEntity result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = auditEntityUserType.unmarshal(value, null, null);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals("sys", result.getCreateUser());
		assertEquals(2649600000L, result.getCreateDate().getTime());
		assertEquals("sys", result.getModifiedUser());
		assertEquals(2649600000L, result.getModifiedDate().getTime());
	}
}
