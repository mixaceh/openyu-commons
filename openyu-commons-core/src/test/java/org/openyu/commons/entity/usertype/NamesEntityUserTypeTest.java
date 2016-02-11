package org.openyu.commons.entity.usertype;

import static org.junit.Assert.*;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;
import org.openyu.commons.entity.LocaleNameEntity;
import org.openyu.commons.entity.supporter.LocaleNameEntitySupporter;
import org.openyu.commons.entity.usertype.NamesEntityUserType;

public class NamesEntityUserTypeTest {

	private static NamesEntityUserType namesEntityUserType = new NamesEntityUserType();

	@Test
	// 1000000 times: 405 mills.
	// 1000000 times: 405 mills.
	// 1000000 times: 405 mills.
	// verified
	public void marshal() {
		Set<LocaleNameEntity> value = new LinkedHashSet<LocaleNameEntity>();

		LocaleNameEntity name = new LocaleNameEntitySupporter();
		name.setLocale(Locale.TRADITIONAL_CHINESE);
		name.setName("拉拉");
		value.add(name);
		//
		name = new LocaleNameEntitySupporter();
		name.setLocale(Locale.US);
		name.setName("LaLa");
		value.add(name);
		//
		String result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = namesEntityUserType.marshal(value, null);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		// ♥1♠2♠zh_TW♦拉拉♠en_US♦LaLa
		System.out.println(result.length() + ", " + result);
		//
		assertEquals("♥1♠2♠zh_TW♦拉拉♠en_US♦LaLa", result);
	}

	@Test
	// 1000000 times: 2431 mills.
	// 1000000 times: 2484 mills.
	// 1000000 times: 2434 mills.
	// verified
	public void unmarshal() {
		String value = "♥1♠2♠zh_TW♦拉拉♠en_US♦LaLa";
		//
		Set result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = namesEntityUserType.unmarshal(value, null, null);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(2, result.size());
	}
}
