package org.openyu.commons.lang;

//import static org.testng.AssertJUnit.assertEquals;
//import static org.testng.AssertJUnit.assertFalse;
//import static org.testng.AssertJUnit.assertNotNull;
//import static org.testng.AssertJUnit.assertNull;
//import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;

import org.openyu.commons.testng.supporter.BaseTestNGSupporter;

public class BooleanHelperTestNG extends BaseTestNGSupporter {

//	@Test(expectedExceptions = InvocationTargetException.class)
//	public void BooleanHelper() throws Exception {
//		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.BooleanHelper");
//		//
//		Object result = null;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			// InvocationTargetException
//			result = constructor.newInstance();
//		}
//		System.out.println(result);
//		assertNull(result);
//	}
//
//	@Test
//	public void InstanceHolder() throws Exception {
//		Constructor<?> constructor = getDeclaredConstructor("org.openyu.commons.lang.BooleanHelper$InstanceHolder");
//		//
//		Object result = null;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			// InvocationTargetException
//			result = constructor.newInstance();
//		}
//		//
//		System.out.println(result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void getInstance() {
//		BooleanHelper result = null;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			result = BooleanHelper.getInstance();
//		}
//		//
//		System.out.println(result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void initialize() {
//		BooleanHelper instance = BooleanHelper.getInstance();
//		//
//		boolean result = false;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			instance.initialize();
//			result = instance.isInitialized();
//		}
//		//
//		System.out.println(result);
//		assertTrue(result);
//	}
//
//	@Test
//	public void clear() {
//		BooleanHelper instance = BooleanHelper.getInstance();
//		//
//		boolean result = false;
//		//
//		instance.setInitialized(true);
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			instance.clear();
//			result = instance.isInitialized();
//		}
//		//
//		System.out.println(result);
//		assertFalse(result);
//	}
//
//	@Test
//	public void createBoolean() {
//		Boolean result = null;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			result = BooleanHelper.createBoolean(true);
//		}
//		//
//		System.out.println(result);
//		assertTrue(result);
//		//
//		result = BooleanHelper.createBoolean(false);
//		assertFalse(result);
//		//
//		result = BooleanHelper.createBoolean(null);
//		assertFalse(result);
//		//
//		result = BooleanHelper.createBoolean("1");
//		assertTrue(result);
//		//
//		result = BooleanHelper.createBoolean("0");
//		assertFalse(result);
//	}
//
//	@Test
//	public void toBoolean() {
//		boolean result = false;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			result = BooleanHelper.toBoolean("true");
//		}
//		//
//		System.out.println(result);
//		assertTrue(result);
//		//
//		result = BooleanHelper.toBoolean("false");
//		assertFalse(result);
//		//
//		result = BooleanHelper.toBoolean(null);
//		assertFalse(result);
//		//
//		result = BooleanHelper.toBoolean("");
//		assertFalse(result);
//		//
//		result = BooleanHelper.toBoolean("on");
//		assertTrue(result);
//		//
//		result = BooleanHelper.toBoolean("off");
//		assertFalse(result);
//		//
//		result = BooleanHelper.toBoolean("yes");
//		assertTrue(result);
//		//
//		result = BooleanHelper.toBoolean("no");
//		assertFalse(result);
//		//
//		result = BooleanHelper.toBoolean("1");
//		assertTrue(result);
//		//
//		result = BooleanHelper.toBoolean("0");
//		assertFalse(result);
//		//
//		result = BooleanHelper.toBoolean("?");
//		assertFalse(result);
//	}
//
//	@Test
//	public void randomBooleanz() {
//		boolean result = false;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			result = BooleanHelper.randomBoolean();
//		}
//		//
//		System.out.println(result);
//	}
//
//	@Test
//	public void safeGet() {
//		boolean result = false;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			result = BooleanHelper.safeGet(Boolean.TRUE);
//		}
//		//
//		System.out.println(result);
//		assertTrue(result);
//		//
//		result = BooleanHelper.safeGet(null);
//		assertFalse(result);
//	}
//
//	@Test
//	public void toStringz() {
//		String result = null;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			result = BooleanHelper.toString(true);
//		}
//		//
//		System.out.println(result);
//		assertEquals("1", result);
//		//
//		result = BooleanHelper.toString(false);
//		assertEquals("0", result);
//		//
//		result = BooleanHelper.toString(null);
//		assertEquals("0", result);
//		//
//		result = BooleanHelper.toString(Boolean.TRUE);
//		assertEquals("1", result);
//	}
//
//	@Test
//	public void toInt() {
//		int result = -1;
//		//
//		final int COUNT = 10000;
//		for (int i = 0; i < COUNT; i++) {
//			result = BooleanHelper.toInt(true);
//		}
//		//
//		System.out.println(result);
//		assertEquals(1, result);
//		//
//		result = BooleanHelper.toInt(false);
//		assertEquals(0, result);
//		//
//		result = BooleanHelper.toInt(null);
//		assertEquals(0, result);
//		//
//		result = BooleanHelper.toInt(Boolean.TRUE);
//		assertEquals(1, result);
//	}
//
//	// @Test
//	// public void readResolve() throws Exception {
//	// BooleanHelper booleanHelper = BooleanHelper.getInstance();
//	// Method method = getDeclaredMethod(booleanHelper.getClass(),
//	// "readResolve");
//	// //
//	// Object result = null;
//	// //
//	// final int COUNT = 10000;
//	// for (int i = 0; i < COUNT; i++) {
//	// result = method.invoke(booleanHelper);
//	// }
//	// //
//	// System.out.println(result);
//	// assertNotNull(result);
//	// }
//
//	// --------------------------------------------------
//	// DataProvider
//	// --------------------------------------------------
//
//	@DataProvider(name = "toBooleanData")
//	// input, expected
//	public Object[][] toBooleanData() {
//		return new Object[][] { { "true", true }, { "on", true },
//				{ "false", false }, { "off", false } };
//	}
//
//	@Test(dataProvider = "toBooleanData")
//	public void toBooleanParam(final String value, final boolean expected) {
//		boolean result = BooleanHelper.toBoolean(value);
//		assertEquals(expected, result);
//	}

}
