package org.openyu.commons.lang;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.bean.supporter.AuditBeanSupporter;
import org.openyu.commons.bean.supporter.LocaleNameBeanSupporter;
import org.openyu.commons.bean.supporter.NamesBeanSupporter;
import org.openyu.commons.bean.supporter.SeqAuditBeanSupporter;
import org.openyu.commons.bean.supporter.SeqBeanSupporter;
import org.openyu.commons.bean.supporter.SeqIdAuditNamesBeanSupporter;
import org.openyu.commons.cat.po.impl.CatPoImpl;
import org.openyu.commons.cat.vo.impl.CatImpl;
import org.openyu.commons.dog.po.impl.DogPoImpl;
import org.openyu.commons.dog.vo.impl.DogImpl;
import org.openyu.commons.entity.SeqIdAuditEntity;
import org.openyu.commons.entity.supporter.AuditEntitySupporter;
import org.openyu.commons.entity.supporter.SeqAuditEntitySupporter;
import org.openyu.commons.entity.supporter.SeqEntitySupporter;
import org.openyu.commons.entity.supporter.SeqIdAuditEntitySupporter;
import org.openyu.commons.enumz.IntEnum;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class ClassHelperTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	// 1000000 times: 104 mills.
	// 1000000 times: 101 mills.
	// 1000000 times: 101 mills.
	//
	// #issue 找不到時,會花費很多時間
	// 1000000 times: 超久 mills.
	// 1000000 times: 超久 mills.
	// 1000000 times: 超久 mills.
	//
	// #fix 找不到時,會花費很多時間, ok
	// 1000000 times: 102 mills.
	// 1000000 times: 103 mills.
	// 1000000 times: 103 mills.
	public void forName() {
		Class<?> result = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// clazz =
			// ClassHelper.forName("org.openyu.commons.lang.ClassHelperTest$PlayerPo");
			result = ClassHelper.forName("org.openyu.commons.lang.ClassHelperTest$PlayerPo");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(PlayerPo.class, result);
		//
		Class<?> results = null;
		results = ClassHelper.forName("org.openyu.commons.lang.ClassHelperTest$PlayerPo[]");
		System.out.println(results);
		assertEquals(PlayerPo[].class, results);
	}

	@Test
	// "java.lang.StringBuilder" no cache
	// 1000000 times: 774 mills.
	// 1000000 times: 840 mills.
	// 1000000 times: 780 mills.
	//
	// "java.lang.StringBuilder" cache map
	// 1000000 times: 219 mills.
	// 1000000 times: 216 mills.
	// 1000000 times: 219 mills.
	//
	// "java.lang.StringBuilder" cache sync map
	// 1000000 times: 228 mills.
	// 1000000 times: 227 mills.
	// 1000000 times: 229 mills.
	//
	// "java.lang.StringBuilder" cache ConcurrentHashMap
	// 1000000 times: 225 mills.
	// 1000000 times: 228 mills.
	// 1000000 times: 227 mills.
	// 1000000 times: 227 mills.
	//
	// "java.lang.StringBuilder" cache ConcurrentHashMap #fix
	// good!!! save (774-94)=(680/94)=7.23倍 time
	// 1000000 times: 94 mills.
	// 1000000 times: 94 mills.
	// 1000000 times: 95 mills.
	//
	// StringBuilder.class
	// 1000000 times: 199 mills.
	// 1000000 times: 197 mills.
	// 1000000 times: 200 mills.
	//
	// StringBuilder.class #fix
	// good!!! save (199-51)=(148/51)=2.9倍 time
	// 1000000 times: 51 mills.
	// 1000000 times: 51 mills.
	// 1000000 times: 52 mills.
	public void newInstance() {
		Object result = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// 使用class name 會稍慢些,還可以接受
			result = ClassHelper.newInstance("org.openyu.commons.lang.ClassHelperTest$PlayerPo");
			// object =
			// ClassHelper.newInstance("xxxorg.openyu.commons.lang.ClassHelperTest$PlayerPo");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertNotNull(result);
		System.out.println("===============================");
		//
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// 目前以此方式最快,且穩定,故先選此方法
			result = ClassHelper.newInstance(PlayerPo.class);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertNotNull(result);

	}

	@Test
	// 1000000 times: 20 mills.
	// 1000000 times: 20 mills.
	// 1000000 times: 20 mills.
	// 目前以此方式最快,且穩定,故先選此方法
	public void newInstanceByClass() {
		Object result = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			try {
				result = StringBuilder.class.newInstance();
			} catch (InstantiationException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		assertNotNull(result);
	}

	@Test
	// no cache
	// 1000000 times: 295 mills.
	// 1000000 times: 514 mills.
	// 1000000 times: 302 mills.
	//
	// cache
	// 1000000 times: 556 mills.
	// 1000000 times: 539 mills.
	// 1000000 times: 554 mills.
	//
	// newCache
	// 1000000 times: 323 mills.
	// 1000000 times: 331 mills.
	// 1000000 times: 319 mills.
	//
	// #issue 找不到時,會花費很多時間
	// 1000000 times: 3833 mills.
	// 1000000 times: 3169 mills.
	// 1000000 times: 3955 mills.
	//
	// #fix 找不到時,會花費很多時間,ok
	// 1000000 times: 1012 mills.
	// 1000000 times: 1002 mills.
	// 1000000 times: 1009 mills.
	public void getMethod() {
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		Method method = null;
		for (int i = 0; i < count; i++) {
			// method = ClassHelper.getMethod(PlayerPo.class, "getSuperField");
			method = ClassHelper.getMethod(PlayerPo.class, "getSuperField", String.class);
			// Method method2 = ClassHelper.getMethod(PlayerPo.class,
			// "getSuperField",String.class);
			// System.out.println("method2: " + method2);
			// System.out.println(method.equals(method2));
		}
		System.out.println("method: " + method);
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// 1000000 times: 120 mills.
	// 1000000 times: 118 mills.
	// 1000000 times: 118 mills.
	public void getMethodsAndCache() {
		Method[] methods = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			methods = ClassHelper.getMethodsAndCache(PlayerPo.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		for (Method method : methods) {
			System.out.println(method.getName());
		}
	}

	@Test
	// newCache
	// 1000000 times: 312 mills.
	// 1000000 times: 315 mills.
	// 1000000 times: 319 mills.
	//
	// 當本身找不到,往super class 尋找
	// 1000000 times: 430 mills.
	// 1000000 times: 430 mills.
	// 1000000 times: 430 mills.
	//
	// #issue 找不到時,會花費很多時間
	// 1000000 times: 8578 mills.
	// 1000000 times: 8643 mills.
	// 1000000 times: 8616 mills.
	//
	// #fix 找不到時,會花費很多時間,ok
	// 1000000 times: 1599 mills.
	// 1000000 times: 1599 mills.
	// 1000000 times: 1552 mills.
	public void getDeclaredMethod() {
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		Method method = null;
		for (int i = 0; i < count; i++) {
			// method = ClassHelper.getDeclaredMethod(PlayerPo.class,
			// "protectedHello");
			// method = ClassHelper.getDeclaredMethod(PlayerPo.class,
			// "getSuperField");
			method = ClassHelper.getDeclaredMethod(PlayerPo.class, "getSuperField", String.class);
			// Method method2 = ClassHelper.getDeclaredMethod(PlayerPo.class,
			// "getSuperField",
			// String.class);
			// System.out.println("method2: " + method2);
			// System.out.println(method.equals(method2));
		}
		System.out.println(method);
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		method = ClassHelper.getDeclaredMethod(PlayerPo.class, "onConnect", String.class);
		System.out.println(method);
	}

	@Test
	public void getDeclaredMethodByInterface() {
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		Method method = null;
		for (int i = 0; i < count; i++) {
			method = ClassHelper.getDeclaredMethod(Human.class, "sleep");
		}
		System.out.println(method);
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		method = ClassHelper.getDeclaredMethod(Human.class, "eat");
		System.out.println(method);
	}

	@Test
	// 1000000 times: 342 mills.
	// 1000000 times: 345 mills.
	// 1000000 times: 343 mills.
	public void getDeclaredMethodsAndCache() {

		Method[] methods = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			methods = ClassHelper.getDeclaredMethodsAndCache(PlayerPo.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		for (Method method : methods) {
			System.out.println(method.getName());
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 100)
	public void getConstructor() {
		Constructor<?> constructor = null;
		constructor = ClassHelper.getConstructor(PlayerPo.class);
		System.out.println(constructor);
		assertNotNull(constructor);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 100)
	public void getConstructorsAndCache() {
		Constructor<?>[] constructors = null;
		constructors = ClassHelper.getConstructorsAndCache(PlayerPo.class);
		//
		for (Constructor<?> constructor : constructors) {
			System.out.println(constructor.getName());
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 100)
	public void getDeclaredConstructor() {
		Constructor<?> constructor = null;
		constructor = ClassHelper.getDeclaredConstructor(PlayerPo.class);
		System.out.println(constructor);
		//
		constructor = ClassHelper.getDeclaredConstructor(PlayerPo.class);
		System.out.println(constructor);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 100)
	public void getDeclaredConstructorsAndCache() {
		Constructor<?>[] constructors = null;
		constructors = ClassHelper.getDeclaredConstructorsAndCache(PlayerPo.class);
		//
		for (Constructor<?> constructor : constructors) {
			System.out.println(constructor.getName());
		}
	}

	@Test
	// no cache
	// 1000000 times: 306 mills.
	// 1000000 times: 316 mills.
	// 1000000 times: 304 mills.
	//
	// newCache
	// 1000000 times: 330 mills.
	// 1000000 times: 325 mills.
	// 1000000 times: 328 mills.
	public void isMethod() {
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		boolean isMethod = false;
		for (int i = 0; i < count; i++) {
			isMethod = ClassHelper.isMethod(StringBuilder.class, "toString", null);

		}
		System.out.println("isMethod: " + isMethod);
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// 1000000 times: 158 mills.
	// 1000000 times: 145 mills.
	// 1000000 times: 143 mills.
	public void getParameterTypesAndCache() {
		Class<?>[] parameterTypes = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();

		Method method = ClassHelper.getMethod(PlayerPo.class, "setId", Integer.class);
		// Method method = ClassHelper.getMethod(PlayerPo.class, "setId",
		// String.class);
		// System.out.println(method);
		for (int i = 0; i < count; i++) {
			parameterTypes = ClassHelper.getMethodParameterTypesAndCache(PlayerPo.class, method);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(parameterTypes);
	}

	@Test
	// no cache
	// 1000000 times: 240 mills.
	// 1000000 times: 243 mills.
	// 1000000 times: 224 mills.
	//
	// cache
	// 1000000 times: 303 mills.
	// 1000000 times: 302 mills.
	// 1000000 times: 303 mills.
	//
	// #issue 找不到時,會花費很多時間
	// 1000000 times: 2545 mills.
	// 1000000 times: 2509 mills.
	// 1000000 times: 2606 mills.
	//
	// #fix 找不到時,會花費很多時間,ok
	// 1000000 times: 311 mills.
	// 1000000 times: 315 mills.
	// 1000000 times: 311 mills.
	public void getField() {
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		Field field = null;
		for (int i = 0; i < count; i++) {
			// field = ClassHelper.getField(PlayerPo.class, "id");
			field = ClassHelper.getField(PlayerPo.class, "xxxid");
		}
		System.out.println("field: " + field);
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// 1000000 times: 423 mills.
	// 1000000 times: 423 mills.
	// 1000000 times: 422 mills.
	public void getFieldByClass() {
		Field[] result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.getField(PlayerPo.class, int.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.print("len: " + result.length + ", ");
		SystemHelper.println(result);
	}

	@Test
	// cache
	// 1000000 times: 306 mills.
	// 1000000 times: 307 mills.
	// 1000000 times: 303 mills.
	//
	// 當本身找不到,往super class 尋找
	// 1000000 times: 319 mills.
	// 1000000 times: 315 mills.
	// 1000000 times: 319 mills.
	//
	// #issue 找不到時,會花費很多時間
	// 1000000 times: 5129 mills.
	// 1000000 times: 5224 mills.
	// 1000000 times: 5160 mills.
	//
	// #fix 找不到時,會花費很多時間,ok
	// 1000000 times: 441 mills.
	// 1000000 times: 446 mills.
	// 1000000 times: 447 mills.
	public void getDeclaredField() {
		int count = 1000000;// 100w
		// 取自身的public,proected,private field 用getDeclaredField
		// 但無法取到super class field
		// #fix,已經修正可以往super class 尋找
		long beg = System.currentTimeMillis();
		Field field = null;
		for (int i = 0; i < count; i++) {
			// field = ClassHelper.getDeclaredField(PlayerPo.class,
			// "superField");
			field = ClassHelper.getDeclaredField(PlayerPo.class, "xxxsuperField");
		}
		System.out.println("field: " + field);
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// 1000000 times: 635 mills.
	// 1000000 times: 647 mills.
	// 1000000 times: 641 mills.
	public void getDeclaredFieldByClass() {
		Field[] result = null;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.getDeclaredField(PlayerPo.class, String.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.print("len: " + result.length + ", ");
		SystemHelper.println(result);
	}

	@Test
	// cache
	// 1000000 times: 436 mills.
	// 1000000 times: 437 mills.
	// 1000000 times: 448 mills.
	public void invokeMethod() {
		PlayerPo playerPo = new PlayerPo();

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		Object value = null;
		String methodName = "getSuperField";
		for (int i = 0; i < count; i++) {
			value = ClassHelper.invokeMethod(playerPo, methodName);
			// ClassHelper.invokeMethod(playerPo, "setId", Integer.class, new
			// Integer(100));
		}
		System.out.println(methodName + ": " + value);
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// cache
	// 1000000 times: 441 mills.
	// 1000000 times: 440 mills.
	// 1000000 times: 443 mills.
	public void invokeDeclaredMethod() {
		PlayerPo playerPo = new PlayerPo();

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		Object value = null;
		String methodName = "getSuperField";
		for (int i = 0; i < count; i++) {
			value = ClassHelper.invokeDeclaredMethod(playerPo, methodName);
			// ClassHelper.invokeMethod(playerPo, "setId", Integer.class, new
			// Integer(100));
		}
		System.out.println(methodName + ": " + value);
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// cache
	// 1000000 times: 322 mills.
	// 1000000 times: 320 mills.
	// 1000000 times: 324 mills.
	public void getFieldValue() {
		PlayerPo playerPo = new PlayerPo();

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		Object value = null;
		String fieldName = "superField";
		for (int i = 0; i < count; i++) {
			value = ClassHelper.getFieldValue(playerPo, fieldName);
			// ClassHelper.setFieldValue(playerPo, "id", 100);
		}
		System.out.println(fieldName + ": " + value);
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// cache
	// 1000000 times: 3198 mills.
	// 1000000 times: 3198 mills.
	// 1000000 times: 2974 mills.
	public void getDeclaredFieldValue() {
		PlayerPo value = new PlayerPo();
		Object result = null;
		String fieldName = "superField";
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			result = ClassHelper.getDeclaredFieldValue(value, fieldName);
			// ClassHelper.setFieldValue(playerPo, "id", 100);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(fieldName + ": " + result);
	}

	@Test
	// cache
	// 1000000 times: 322 mills.
	// 1000000 times: 320 mills.
	// 1000000 times: 324 mills.
	public void getDeclaredFieldValueByAll() {
		DogImpl value = new DogImpl();
		value.setName(Locale.TRADITIONAL_CHINESE, "拉拉");
		value.setName(Locale.US, "LaLa");
		//
		value.setDescription(Locale.TRADITIONAL_CHINESE, "拉拉看起來像拉拉");
		value.setDescription(Locale.US, "LaLa looks like LaLa");
		//
		value.setSeq(1L);
		value.setId("ID_1234567890");
		value.setValid(true);
		value.setVersion(10);
		//
		List<Object> result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.getDeclaredFieldValue(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result.size());
		System.out.println(result);
	}

	@Test
	// cache
	// 1000000 times: 318 mills.
	// 1000000 times: 312 mills.
	// 1000000 times: 317 mills.
	public void setFieldValue() {
		PlayerPo playerPo = new PlayerPo();
		boolean result = false;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.setFieldValue(playerPo, "superField", "newValue");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertFalse(result);
		System.out.println(playerPo.getSuperField());
	}

	@Test
	// cache
	// 1000000 times: 341 mills.
	// 1000000 times: 338 mills.
	// 1000000 times: 343 mills.
	public void setDeclaredFieldValue() {
		PlayerPo playerPo = new PlayerPo();
		boolean result = false;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.setDeclaredFieldValue(playerPo, "superField", "newValue...........");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertTrue(result);
		System.out.println(playerPo.getSuperField());
	}

	// --------------------------------------------------------
	// 結論:
	// 1.getField比getMethod快一點
	// 2.setFieldValue比invokeMethod快一點
	// 3.#issure 須克服private field的get/set, ->用getDeclaredField取
	// #fix ok
	// 4.
	// --------------------------------------------------------
	@Test
	// no cache
	// 1000000 times: 440 mills.
	// 1000000 times: 389 mills.
	// 1000000 times: 394 mills.
	//
	// cache
	// 1000000 times: 337 mills.
	// 1000000 times: 330 mills.
	// 1000000 times: 326 mills.
	public void getFieldsAndCache() {
		Field[] fields = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			fields = ClassHelper.getFieldsAndCache(PlayerPo.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		for (Field field : fields) {
			System.out.println(field.getName());
		}
	}

	@Test
	// no cache
	// 1000000 times: 474 mills.
	// 1000000 times: 474 mills.
	// 1000000 times: 472 mills.
	//
	// cache
	// ArrayHelper.uniqueJoin return set
	// 1000000 times: 407 mills.
	// 1000000 times: 403 mills.
	// 1000000 times: 407 mills.
	//
	// ArrayHelper.mergeUnique return Object[]
	// 1000000 times: 231 mills.
	// 1000000 times: 233 mills.
	// 1000000 times: 229 mills.
	public void getDeclaredFieldsAndCache() {

		Field[] fields = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			fields = ClassHelper.getDeclaredFieldsAndCache(PlayerPo.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		for (Field field : fields) {
			System.out.println(field.getName());
		}
	}

	@Test
	// 1000000 times: 5 mills.
	// 1000000 times: 3 mills.
	// 1000000 times: 3 mills.
	public void isConstantField() {
		Player player = new Player();
		Field field = ClassHelper.getDeclaredField(Player.class, "UNKNOWN");
		System.out.println(field);

		boolean result = false;

		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.isConstantField(field);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
	}

	@Test
	// 1000000 times: 242 mills.
	// 1000000 times: 263 mills.
	// 1000000 times: 270 mills.
	public void isExcludeConvention() {
		boolean ret = false;
		Object value = "111";
		value = 1;
		// value = new PlayerPo();
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ret = ClassHelper.isExcludeConvention(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(ret);
	}

	@Test
	// no cache
	// 1000000 times: 1541 mills.
	// 1000000 times: 1355 mills.
	// 1000000 times: 1386 mills.
	//
	// cache
	// 1000000 times: 833 mills.
	// 1000000 times: 814 mills.
	// 1000000 times: 869 mills.
	//
	// 1000000 times: 1270 mills.
	// 1000000 times: 1265 mills.
	// 1000000 times: 1267 mills.
	public void getConventionClass() {
		Class<?> result = null;

		PlayerPo playerPo = new PlayerPo();
		PlayerPo playerPo2 = new PlayerPo();
		//
		List list = new LinkedList();
		list.add(null);
		// list.add("aaa");
		// list.add(playerPo);

		List nestlist = new LinkedList();
		nestlist.add("bbb");
		list.add(nestlist);
		//
		Map map = new LinkedHashMap();
		map.put(0, null);
		map.put(1, "aaa");
		map.put(2, playerPo);
		//
		//
		Map stringMap = new LinkedHashMap();
		stringMap.put(1, "aaa");
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// result = ClassHelper.getConventionClass(new Date());
			// result =
			// ClassHelper.getConventionClass(Locale.TRADITIONAL_CHINESE);
			// result = ClassHelper.getConventionClass(playerPo);
			// result = ClassHelper.getConventionClass(list);
			// result = ClassHelper.getConventionClass(map);
			// result = ClassHelper.getConventionClass(playerPo);
			// result = ClassHelper.getConventionClass(stringMap);
			// result = ClassHelper.getConventionClass(new String[] { "aaa",
			// "bbb" });
			result = ClassHelper.getConventionClass(new PlayerPo[] { playerPo, playerPo2 });
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		//
		result = ClassHelper.getConventionClass(new SeqAuditBeanSupporter());
		System.out.println(result);
		//
		result = ClassHelper.getConventionClass(new SeqIdAuditNamesBeanSupporter());
		System.out.println(result);
		//
		result = ClassHelper.getConventionClass(new LocaleNameBeanSupporter());
		System.out.println(result);
		//
		result = ClassHelper.getConventionClass(new NamesBeanSupporter());
		System.out.println(result);
	}

	// @Test
	// 1000000 times: 4599 mills.
	// 1000000 times: 4664 mills.
	// 1000000 times: 4530 mills.
	//
	// #issus 無窮迴圈了
	// public void copyByConvention()
	// {
	// PlayerPo playerPo = new PlayerPo();
	// playerPo.setCode("PO_VODE");
	//
	// PlayerPoName playerPoName = new PlayerPoName();
	// //playerPoName.setPlayer(playerPo);
	// playerPo.getNames().add(playerPoName);
	//
	// //Player player = new Player();
	// Player player = null;
	// //
	// int count = 1000000;//100w
	// long beg = System.currentTimeMillis();
	// for (int i = 0; i < count; i++)
	// {
	// player = ClassHelper.copyByConvention(playerPo);
	// }
	// long end = System.currentTimeMillis();
	// System.out.println(count + " times: " + (end - beg) + " mills. ");
	// //
	// System.out.println("player: " + player);
	// }

	@Test
	// 1000000 times: 1266 mills.
	// 1000000 times: 1277 mills.
	// 1000000 times: 1289 mills.
	//
	// getConventionClass
	// 1000000 times: 7407 mills.
	// 1000000 times: 7517 mills.
	// 1000000 times: 7463 mills.
	//
	// #fix getConventionClass
	// 1000000 times: 4195 mills.
	// 1000000 times: 4450 mills.
	// 1000000 times: 4382 mills.
	//
	// #issus 無窮迴圈了
	public void deepCopyPropertiesByField() {
		PlayerPo playerPo = new PlayerPo();
		playerPo.setId(1);
		playerPo.setCode("PO_VODE");

		PlayerPoName playerPoName = new PlayerPoName();
		// playerPoName.setPlayer(playerPo);
		playerPo.getNames().add(playerPoName);

		Player player = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// ClassHelper.deepCopyPropertiesByField( playerPo,player);
			player = ClassHelper.deepCopyPropertiesByField(playerPo, Player.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("playerPo.getObject(): " + playerPo.getObject());

		System.out.println("player: " + player);

	}

	@Test
	// 1000000 times: 1266 mills.
	// 1000000 times: 1277 mills.
	// 1000000 times: 1289 mills.
	//
	// getConventionClass
	// 1000000 times: 6818 mills.
	// 1000000 times: 6806 mills.
	// 1000000 times: 6758 mills.
	//
	// #fix getConventionClass
	// 1000000 times: 3260 mills.
	// 1000000 times: 3271 mills.
	// 1000000 times: 3253 mills.
	public void deepCopyPropertiesByFieldReverse() {
		Player player = new Player();
		player.setId(1);
		player.setCode("VO_CODE");

		PlayerName playerName = new PlayerName();
		// playerName.setPlayer(player);
		player.getNames().add(playerName);

		PlayerPo playerPo = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// ClassHelper.copyPropertiesByField( player,playerPo);
			playerPo = ClassHelper.deepCopyPropertiesByField(player, PlayerPo.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("playerPo: " + playerPo);

	}

	@Test
	// copy from list, List<PlayerPo> -> List<Player>
	// 1000000 times: 1266 mills.
	// 1000000 times: 1277 mills.
	// 1000000 times: 1289 mills.
	public void deepCopyPropertiesByFieldList() {
		List<PlayerPo> orig = new LinkedList<PlayerPo>();
		PlayerPo playerPo = new PlayerPo();
		playerPo.setCode("xxxxxxxxxxxxxxxxxxxxx");
		orig.add(playerPo);
		//
		PlayerPo playerPo2 = new PlayerPo();
		playerPo2.setCode("yyy222");
		orig.add(playerPo2);

		List<Player> dest = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = ClassHelper.deepCopyPropertiesByField(orig, Player.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("orig: " + orig);
		System.out.println("dest: " + dest);
		// System.out.println(orig.equals(dest));
	}

	@Test
	// copy from list, List<Player> -> List<PlayerPo>
	// 1000000 times: 4879 mills.
	// 1000000 times: 4728 mills.
	// 1000000 times: 4761 mills.
	public void deepCopyPropertiesByFieldListReverse() {
		List<Player> orig = new LinkedList<Player>();
		Player player = new Player();
		player.setCode("aaaaaaaaaaaaaaa");
		orig.add(player);
		//
		Player player2 = new Player();
		player2.setCode("bbb222");
		orig.add(player2);

		List<PlayerPo> dest = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = ClassHelper.deepCopyPropertiesByField(orig, PlayerPo.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("orig: " + orig);
		System.out.println("dest: " + dest);
		// System.out.println(orig.equals(dest));
	}

	@Test
	// getDeclaredMethodsAndCache
	// 1000000 times: 3778 mills.
	// 1000000 times: 3763 mills.
	// 1000000 times: 3784 mills.
	//
	// getMethodsAndCache
	// 1000000 times: 2731 mills.
	// 1000000 times: 2748 mills.
	// 1000000 times: 2701 mills.
	public void copyPropertiesByMethod() {
		PlayerPo playerPo = new PlayerPo();
		playerPo.setCode("xxxxxxxxxxxxxxxxxxxxx");
		// Player player = new Player();
		Player player = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// ClassHelper.copyPropertiesByMethod(playerPo, player);
			player = ClassHelper.copyPropertiesByMethod(playerPo, Player.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("player: " + player);
	}

	@Test
	// 1000000 times: 9448 mills.
	// 1000000 times: 9565 mills.
	// 1000000 times: 9406 mills.
	// verified
	public void copyProperties() {
		List orig = new LinkedList();
		orig.add(new Date());// 0
		orig.add(Locale.TRADITIONAL_CHINESE);// 1
		orig.add(1);// 2
		orig.add("2bb");// 3
		//
		List nestList = new LinkedList();
		nestList.add("nestList");
		orig.add(nestList);// 4
		//
		Set nestSet = new LinkedHashSet();
		nestSet.add("nestSet");
		nestSet.add(nestList);
		orig.add(nestSet);// 5
		//
		PlayerPo playerPo = new PlayerPo();
		playerPo.setId(111);
		PlayerPoName playerPoName = new PlayerPoName();
		// playerPoName.setPlayer(playerPo);
		playerPo.getNames().add(playerPoName);
		// #issus:field型別不同會有ex
		// #fix:ok
		orig.add(playerPo);// 6

		// System.out.println("orig: " + orig);
		List dest = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = new LinkedList();
			dest = ClassHelper.copyProperties(orig, dest);// 就是這個了,不要再搞了
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("dest: " + dest.get(0));
		Date date = (Date) dest.get(0);
		date.setYear(0);// 1900
		System.out.println("dest: " + dest.get(0));
		System.out.println("orig: " + orig.get(0));
		//
		assertNotSame(orig.get(0), dest.get(0));
		//
		System.out.println(dest.get(6));// Player
	}

	@Test
	// 原無deep clone
	// 1000000 times: 1697 mills.
	// 1000000 times: 1588 mills.
	// 1000000 times: 1637 mills.
	//
	// 改為deep clone
	// 1000000 times: 640 mills.
	// 1000000 times: 687 mills.
	// 1000000 times: 640 mills.
	// verified
	public void copyProperties2() {
		Date orig = new Date();
		Date dest = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = null;
			dest = ClassHelper.copyProperties(orig, Date.class);// 就是這個了,不要再搞了
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("dest: " + dest);
		//
		dest.setYear(0);// 1900
		//
		System.out.println("dest: " + dest);
		System.out.println("orig: " + orig);
		//
		assertNotSame(orig, dest);
	}

	@Test
	// 1000000 times: 5652 mills.
	// 1000000 times: 5667 mills.
	// 1000000 times: 5746 mills.
	// verified
	public void copyProperties3() {
		CatImpl orig = new CatImpl();
		orig.getAudit().setCreateDate(new Date());
		//
		CatImpl dest = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = null;
			dest = ClassHelper.copyProperties(orig, CatImpl.class);// 就是這個了,不要再搞了
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("dest: " + dest.getAudit().getCreateDate());
		//
		dest.getAudit().setCreateDate(null);
		//
		System.out.println("dest: " + dest.getAudit().getCreateDate());
		System.out.println("orig: " + orig.getAudit().getCreateDate());
		//
		assertNotSame(orig, dest);
		// assertNotSame(orig.getAudit().getCreateDate(),
		// dest.getAudit().getCreateDate());
	}

	@Test
	// PlayerPo -> Player
	public void copyPropertiesWrapper2Wrapper() {
		PlayerPo orig = new PlayerPo();
		//
		Player dest = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = null;
			dest = ClassHelper.copyProperties(orig);// 就是這個了,不要再搞了
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("dest: " + dest);
		//
		assertNotSame(orig, dest);
	}

	@Test
	// BankerPo -> Banker
	public void copyPropertiesWrapper2Primitive() {
		BankerPo orig = new BankerPo();
		//
		Banker dest = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = null;
			dest = ClassHelper.copyProperties(orig);// 就是這個了,不要再搞了
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("dest: " + dest);
		//
		assertNotSame(orig, dest);
	}

	@Test
	// Banker -> BankerPo
	public void copyPropertiesPrimitive2Wrapper() {
		Banker orig = new Banker();
		//
		BankerPo dest = null;
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = null;
			dest = ClassHelper.copyProperties(orig);// 就是這個了,不要再搞了
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("dest: " + dest);
		//
		assertNotSame(orig, dest);
	}

	@Test
	// by spring
	// 1000000 times: 761 mills.
	// 1000000 times: 794 mills.
	// 1000000 times: 788 mills.
	//
	// 1000000 times: 307 mills.
	// 1000000 times: 306 mills.
	// 1000000 times: 310 mills.
	//
	// deep copy
	// 1000000 times: 3658 mills.
	// 1000000 times: 3753 mills.
	// 1000000 times: 3699 mills.
	//
	// deepCopyField
	// 1000000 times: 9448 mills.
	// 1000000 times: 9565 mills.
	// 1000000 times: 9406 mills.
	public void deepCopyPropertiesByList() {
		List orig = new LinkedList();
		orig.add(new Date());
		orig.add(Locale.TRADITIONAL_CHINESE);
		orig.add(1);
		orig.add("2bb");
		//
		List nestList = new LinkedList();
		nestList.add("nestList");
		orig.add(nestList);
		//
		Set nestSet = new LinkedHashSet();
		nestSet.add("nestSet");
		orig.add(nestSet);
		nestSet.add(nestList);
		//
		PlayerPo playerPo = new PlayerPo();
		playerPo.setId(111);
		PlayerPoName playerPoName = new PlayerPoName();
		// playerPoName.setPlayer(playerPo);
		playerPo.getNames().add(playerPoName);
		// #issus:field型別不同會有ex
		// #fix:ok
		orig.add(playerPo);

		// System.out.println("orig: " + orig);
		List dest = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = new LinkedList();
			dest = ClassHelper.deepCopyProperties(orig, dest);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		Date date = (Date) dest.get(0);
		date.setYear(50);// 1950
		System.out.println("dest: " + dest);
		// System.out.println("after orig: " + orig);
	}

	@Test
	// 遞迴
	// 1000000 times: 609 mills.
	// 1000000 times: 624 mills.
	// 1000000 times: 655 mills.
	//
	// deepCopyField
	// 1000000 times: 8864 mills.
	// 1000000 times: 8879 mills.
	// 1000000 times: 8949 mills.
	public void deepCopyPropertiesByMap() {
		Map orig = new LinkedHashMap();
		orig.put(1, new Date());
		orig.put("2bb", Locale.TRADITIONAL_CHINESE);

		PlayerPo playerPo = new PlayerPo();
		playerPo.setId(111);
		PlayerPoName playerPoName = new PlayerPoName();
		// playerPoName.setPlayer(playerPo);
		playerPo.getNames().add(playerPoName);
		orig.put(3, playerPo);

		// System.out.println("orig: " + orig);
		//
		Map dest = null;
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = new LinkedHashMap();
			dest = ClassHelper.deepCopyProperties(orig, dest);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		Date date = (Date) dest.get(1);
		date.setYear(50);// 1950
		System.out.println("dest: " + dest);
		// System.out.println("after orig: " + value);
	}

	@Test
	// 1000000 times: 4703 mills.
	// 1000000 times: 4886 mills.
	// 1000000 times: 4858 mills.
	public void deepCopyFields() {
		PlayerPo orig = new PlayerPo();
		orig.setId(111);
		orig.setCode("PO_VODE");

		PlayerPoName playerPoName = new PlayerPoName();
		// playerPoName.setPlayer(orig);
		// orig.setName(playerPoName);
		orig.getNames().add(playerPoName);

		Player dest = new Player();

		// System.out.println("orig: " + orig);
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			dest = new Player();
			dest = ClassHelper.deepCopyFields(orig, dest);
			// dest = ClassHelper.deepCopyPropertiesBySpring(orig, dest);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		// dest.setAttributes(null);
		System.out.println("dest: " + dest);
		// System.out.println("after orig: " + orig);
	}

	@Test
	// 1000000 times: 857 mills.
	// 1000000 times: 862 mills.
	// 1000000 times: 833 mills.
	public void genericCopyPropertiesBySpring() {
		PlayerPo orig = new PlayerPo();
		orig.setId(111);
		PlayerPo dest = new PlayerPo();
		//
		List origList = new LinkedList();
		origList.add(orig);
		List destList = new LinkedList();
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ClassHelper.genericCopyPropertiesBySpring(orig, dest);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		System.out.println("orig: " + orig);
		System.out.println("dest: " + dest);
		//
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// 1.集合copy很沒效率,不要直接呼叫genericCopyPropertiesBySpring,僅供內部用
			// 1000000 times: 27739 mills.
			// 2.用deepCopyProperties
			ClassHelper.genericCopyPropertiesBySpring(origList, destList);
		}
		end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println("origList: " + origList);
		System.out.println("destList: " + destList);
	}

	@Test
	public void classEquals() {
		Class<?> clazz = PlayerPoSupporter.class;
		System.out.println(clazz.equals(Object.class));
		System.out.println(clazz.getSuperclass() == Object.class);// true
		System.out.println(clazz.getSuperclass().equals(Object.class));// true
	}

	@Test
	// no cache
	// 1000000 times: 81 mills.
	// 1000000 times: 80 mills.
	// 1000000 times: 83 mills.
	public void isSubClassOf() {
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		boolean isSubClassOf = false;
		for (int i = 0; i < count; i++) {
			isSubClassOf = ClassHelper.isSubClassOf(AbstractCollection.class, LinkedList.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		System.out.println("isSubClassOf: " + isSubClassOf);

		// System.out.println(ClassHelper.isSubClassOf(PlayerPoSupporter.class,
		// PlayerPo.class));
		System.out.println(ClassHelper.isSubClassOf(PlayerPo.class, C.class));
		// System.out.println(ClassHelper.isSubClassOf(B.class, C.class));
		// System.out.println(ClassHelper.isSubClassOf(A.class, C.class));
		// System.out.println(ClassHelper.isSubClassOf(I.class, C.class));
	}

	@Test
	// cache
	// 1000000 times: 289 mills.
	// 1000000 times: 296 mills.
	// 1000000 times: 291 mills.
	public void getInterfacesAndCache() {
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		Class<?>[] result = null;
		for (int i = 0; i < count; i++) {
			result = ClassHelper.getInterfacesAndCache(LinkedList.class);
			// interfaces = ClassHelper.getInterfacesAndCache(PlayerPo.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(result);
		assertNotNull(result);
	}

	@Test
	// 1000000 times: 2400 mills.
	// 1000000 times: 2405 mills.
	// 1000000 times: 2400 mills.
	//
	// cache
	// 1000000 times: 462 mills.
	// 1000000 times: 460 mills.
	// 1000000 times: 465 mills.
	public void getInterfaces() {
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		Class<?>[] interfaces = null;
		for (int i = 0; i < count; i++) {
			interfaces = ClassHelper.getInterfaces(LinkedList.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		SystemHelper.println(interfaces);
		System.out.println("--------------------------");
		SystemHelper.println(SeqIdAuditEntitySupporter.class.getInterfaces());

		System.out.println("--------------------------");
		SystemHelper.println(ClassHelper.getInterfaces(SeqIdAuditEntitySupporter.class));

		System.out.println("getInterfaces: --------------------------");
		SystemHelper.println(ClassHelper.getInterfaces(SeqIdAuditEntity.class));
		System.out.println("Collections.singleton: --------------------------");
		System.out.println(Collections.singleton(SeqIdAuditEntity.class));
		System.out.println("getInterfaces: --------------------------");
		SystemHelper.println(SeqIdAuditEntity.class.getInterfaces());
		System.out.println("getInterfacesFromInterface: --------------------------");
		SystemHelper.println(ClassHelper.getInterfacesByExtend(SeqIdAuditEntity.class));
		System.out.println("getInterfacesFromInterface: --------------------------");
		SystemHelper.println(ClassHelper.getInterfacesByExtend(Cloneable.class));

	}

	@Test
	// cache
	// 1000000 times: 947 mills.
	// 1000000 times: 941 mills.
	// 1000000 times: 951 mills.
	//
	// verified ok
	public void isInterfaceOf() {
		boolean result = false;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			result = ClassHelper.isInterfaceOf(Collection.class, LinkedList.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertTrue(result);
	}

	@Test
	// cache
	// 1000000 times: 613 mills.
	// 1000000 times: 610 mills.
	// 1000000 times: 614 mills.
	//
	// verified ok
	public void isPoClass() {
		boolean result = false;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.isPoClass(DogPoImpl.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertTrue(!result);
		//
		result = ClassHelper.isPoClass(CatPoImpl.class);
		System.out.println(result);
		assertTrue(result);
	}

	@Test
	// cache
	// 1000000 times: 564 mills.
	// 1000000 times: 574 mills.
	// 1000000 times: 562 mills.
	//
	// verified ok
	public void isVoClass() {
		boolean result = false;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.isVoClass(DogImpl.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);// false
		assertTrue(!result);
		//
		result = ClassHelper.isVoClass(CatImpl.class);
		System.out.println(result);// true
		assertTrue(result);
	}

	@Test
	// cache
	// 1000000 times: 374 mills.
	// 1000000 times: 388 mills.
	// 1000000 times: 376 mills.
	//
	// verified ok
	public void isFcEntityClass() {
		boolean result = false;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.isFcEntityClass(new AuditEntitySupporter());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertTrue(result);
	}

	@Test
	// cache
	// 1000000 times: 394 mills.
	// 1000000 times: 387 mills.
	// 1000000 times: 396 mills.
	//
	// verified ok
	public void isFcBeanClass() {
		boolean result = false;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.isFcBeanClass(new AuditBeanSupporter());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertTrue(result);
	}

	@Test
	// 1000000 times: 260 mills.
	// 1000000 times: 251 mills.
	// 1000000 times: 245 mills.
	//
	// verified: ok
	public void toClass() {
		Class<?> result = null;
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.toClass(CatImpl.class, ".vo.impl", ".po.impl", "Impl", "PoImpl");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(CatPoImpl.class, result);// CatPoImpl
		//
		result = ClassHelper.toClass(DogImpl.class, ".vo", ".po", "", "Po");
		System.out.println(result);// DogPo
		assertEquals(DogPoImpl.class, result);

		result = ClassHelper.toClass(DogPoImpl.class, ".po", ".vo", "Po", "");
		System.out.println(result);// Dog
		assertEquals(DogImpl.class, result);
	}

	@Test
	// 1000000 times: 793 mills.
	// 1000000 times: 790 mills.
	// 1000000 times: 793 mills.
	//
	// verified: ok
	public void vo2PoClass() {
		Class<?> result = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.vo2PoClass(new SeqAuditBeanSupporter());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		System.out.println(result);// SeqAuditEntitySupporter
		assertEquals(SeqAuditEntitySupporter.class, result);
		//
		result = ClassHelper.vo2PoClass(new CatImpl());
		System.out.println(result);// CatPoImpl
		assertEquals(CatPoImpl.class, result);
		//
		result = ClassHelper.vo2PoClass(new DogImpl());
		System.out.println(result);// DogPo
		assertEquals(DogPoImpl.class, result);
	}

	@Test
	// 1000000 times: 765 mills.
	// 1000000 times: 839 mills.
	// 1000000 times: 772 mills.
	//
	// verified: ok
	public void po2VoClass() {
		Class<?> result = null;
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = ClassHelper.po2VoClass(SeqAuditEntitySupporter.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);// SeqAuditBeanSupporter
		assertEquals(SeqAuditBeanSupporter.class, result);
		//
		result = ClassHelper.po2VoClass(new CatPoImpl());
		System.out.println(result);// CatImpl
		assertEquals(CatImpl.class, result);
		//
		result = ClassHelper.po2VoClass(new DogPoImpl());
		System.out.println(result);// Dog
		assertEquals(DogImpl.class, result);
	}

	@Test
	// cache
	// 1000000 times: 598 mills.
	// 1000000 times: 429 mills.
	// 1000000 times: 408 mills.
	public void getBaseClass() {
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		Class<?> clazz = null;
		for (int i = 0; i < count; i++) {
			// clazz = ClassHelper.getBaseClass(new String[0]);
			clazz = ClassHelper.getBaseClass(new String[0][0]);
			// clazz = ClassHelper.getBaseClass(new int[0]);
			// clazz = ClassHelper.getBaseClass(new Integer[0]);
			// clazz = ClassHelper.getBaseClass(new boolean[0]);
			// clazz = ClassHelper.getBaseClass(new long[0]);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		System.out.println("clazz: " + clazz);
		System.out.println(new String[0][0].getClass());
	}

	@Test
	// 1000000 times: 1578 mills.
	// 1000000 times: 1498 mills.
	// 1000000 times: 1466 mills.
	public void setterName() {
		String value = "id";
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		String result = null;
		for (int i = 0; i < count; i++) {
			result = ClassHelper.setterName(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertEquals("setId", result);
	}

	@Test
	// 1000000 times: 1578 mills.
	// 1000000 times: 1498 mills.
	// 1000000 times: 1466 mills.
	public void getterName() {
		String value = "id";
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		String result = null;
		for (int i = 0; i < count; i++) {
			result = ClassHelper.getterName(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);
		assertEquals("getId", result);
	}

	@Test
	// cache
	// 1000000 times: 469 mills.
	// 1000000 times: 472 mills.
	// 1000000 times: 460 mills.
	public void getEnumConstantsAndCache() {
		//
		int count = 1000000;// 100w
		long beg = System.currentTimeMillis();
		IntType[] result = null;
		for (int i = 0; i < count; i++) {
			result = ClassHelper.getEnumConstantsAndCache(IntType.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		SystemHelper.println(result);
		assertNotNull(result);
	}

	@Test
	public void getQualifiedName() {
		String result = null;
		result = ClassHelper.getQualifiedName(getClass());
		// org.openyu.commons.lang.ClassHelperTest
		System.out.println(result);
		assertEquals("org.openyu.commons.lang.ClassHelperTest", result);
		//
		result = ClassHelper.getQualifiedName(PlayerPoSupporter.class);
		// org.openyu.commons.lang.ClassHelperTest$PlayerPoSupporter
		System.out.println(result);
		assertEquals("org.openyu.commons.lang.ClassHelperTest$PlayerPoSupporter", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.44 [+- 0.04], round.block: 0.02 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.45, time.warmup: 0.00,
	// time.bench: 0.45
	public void getSimpleName() {
		String result = null;
		result = ClassHelper.getSimpleName(getClass());
		// ClassHelperTest
		System.out.println(result);
		assertEquals("ClassHelperTest", result);
		//
		result = ClassHelper.getSimpleName(PlayerPoSupporter.class);
		// ClassHelperTest.PlayerPoSupporter
		System.out.println(result);
		assertEquals("ClassHelperTest.PlayerPoSupporter", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	// round: 0.42 [+- 0.04], round.block: 0.02 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.44, time.warmup: 0.00,
	// time.bench: 0.44
	public void getSimpleNameAsProperty() {
		String result = null;
		result = ClassHelper.getSimpleNameAsProperty(getClass());
		// classHelperTest
		System.out.println(result);
		assertEquals("classHelperTest", result);
		//
		result = ClassHelper.getSimpleNameAsProperty(PlayerPoSupporter.class);
		// classHelperTest.PlayerPoSupporter
		System.out.println(result);
		assertEquals("classHelperTest.PlayerPoSupporter", result);
	}

	// --------------------------------------------------------
	public static class PlayerPoSupporter // extends BaseEntitySupporter
	{

		private static final long serialVersionUID = 9032469918367858242L;

		private String superField = "PO_superField12345.......";

		public int publicSuperField = 123;

		public String getSuperField() {
			return superField;
		}

		public String getSuperField(String abc) {
			return abc;
		}

		public void setSuperField(String superField) {
			this.superField = superField;
		}

	}

	public interface PlayerPoConnect {
		boolean onConnect(String code);
	}

	// po
	public static class PlayerPo extends PlayerPoSupporter implements PlayerPoConnect {

		private static final long serialVersionUID = 3002192355513773388L;

		public Integer id = 999;

		private String code;

		// 雙向鏈結
		private Set<PlayerPoName> names = new LinkedHashSet<PlayerPoName>();

		private Map<String, Date> attributes;

		private Object object = new Object();

		private PlayerPoName name = new PlayerPoName();

		private Long exp;

		private Integer level;

		// private Integer version;
		private Integer version;

		public PlayerPo() {
			attributes = new LinkedHashMap<String, Date>();
			attributes.put("testDate", new Date(2000, 0, 1));
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Set<PlayerPoName> getNames() {
			return names;
		}

		public void setNames(Set<PlayerPoName> names) {
			this.names = names;
		}

		public Map<String, Date> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, Date> attributes) {
			this.attributes = attributes;
		}

		// public static method
		public static String publicHi() {
			return "hi";
		}

		// protected static method
		protected static String protectedHello() {
			return "hello";
		}

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public PlayerPoName getName() {
			return name;
		}

		public void setName(PlayerPoName name) {
			this.name = name;
		}

		public Long getExp() {
			return exp;
		}

		public void setExp(Long exp) {
			this.exp = exp;
		}

		public Integer getLevel() {
			return level;
		}

		public void setLevel(Integer level) {
			this.level = level;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}

		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}

		public boolean onConnect(String code) {
			return false;
		}
	}

	public static class PlayerPoName {
		// TODO 雙向鏈結,會造成無窮迴圈
		// private PlayerPo player;

		private String name = "PO_name";

		public PlayerPoName() {

		}

		// public PlayerPo getPlayer()
		// {
		// return player;
		// }
		//
		// public void setPlayer(PlayerPo playerPo)
		// {
		// this.player = playerPo;
		// }

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String toString() {
			ToStringBuilder builder = new ToStringBuilder(this);
			builder.appendSuper(super.toString());
			// builder.append("player", player != null ? player.getId() : null);
			builder.append("name", name);
			return builder.toString();
		}

	}

	// vo
	public static class PlayerSupporter // extends BaseBeanSupporter
	{
		private static final long serialVersionUID = 5538610096891950206L;

		private String superField = "VO_superField12345.......";

		public int publicSuperField = 123;

		public static final int UNKNOWN = -1;

		public String getSuperField() {
			return superField;
		}

		public String getSuperField(String abc) {
			return abc;
		}

		public void setSuperField(String superField) {
			this.superField = superField;
		}

	}

	public static class Player extends PlayerSupporter {
		private Integer id;

		private String code;

		// 雙向鏈結
		private Set<PlayerName> names = new LinkedHashSet<PlayerName>();

		private Map<String, Date> attributes;

		private String search;

		private Object object = new Object();

		private PlayerName name = new PlayerName();

		private Long exp;

		private Integer level;

		private Integer version;

		public Player() {
			attributes = new LinkedHashMap<String, Date>();
			attributes.put("testDate", new Date(2000, 0, 1));
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Set<PlayerName> getNames() {
			return names;
		}

		public void setNames(Set<PlayerName> names) {
			this.names = names;
		}

		public Map<String, Date> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, Date> attributes) {
			this.attributes = attributes;
		}

		public String getSearch() {
			return search;
		}

		public void setSearch(String search) {
			this.search = search;
		}

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public PlayerName getName() {
			return name;
		}

		public void setName(PlayerName name) {
			this.name = name;
		}

		public Long getExp() {
			return exp;
		}

		public void setExp(Long exp) {
			this.exp = exp;
		}

		public Integer getLevel() {
			return level;
		}

		public void setLevel(Integer level) {
			this.level = level;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}

		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}
	}

	public static class PlayerName {
		// TODO 雙向鏈結,會造成無窮迴圈
		// private Player player;

		private String name = "VO_name";

		public PlayerName() {

		}

		// public Player getPlayer()
		// {
		// return player;
		// }
		//
		// public void setPlayer(Player player)
		// {
		// this.player = player;
		// }

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String toString() {
			ToStringBuilder builder = new ToStringBuilder(this);
			builder.appendSuper(super.toString());
			// builder.append("player", player != null ? player.getId() : null);
			builder.append("name", name);
			return builder.toString();
		}
	}

	// --------------------------------------------------
	public static class BankerPo {
		private String id = "TEST_BANKER";

		private Long exp;

		private Integer level = 10;

		// private Integer version=9;
		private Integer version;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Long getExp() {
			return exp;
		}

		public void setExp(Long exp) {
			this.exp = exp;
		}

		public Integer getLevel() {
			return level;
		}

		public void setLevel(Integer level) {
			this.level = level;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}

		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}
	}

	public static class Banker {
		private String id = "TEST_BANKER";

		private long exp;

		private int level = 10;

		// private int version=9;
		private int version;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public long getExp() {
			return exp;
		}

		public void setExp(long exp) {
			this.exp = exp;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}
	}

	// --------------------------------------------------
	public static class A {

	}

	public static class B extends A {

	}

	public static class C extends B implements I {

	}

	public static interface I {

	}

	// --------------------------------------------------
	public static interface Behavior {
		void sleep();
	}

	public static interface Human extends Behavior {
		void eat();
	}

	// --------------------------------------------------

	public enum IntType implements IntEnum {

		TYPE_A(1),

		TYPE_B(2),

		TYPE_C(3),

		;

		private final int value;

		/**
		 * Instantiates a new int type.
		 *
		 * @param value
		 *            the value
		 */
		private IntType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

}
