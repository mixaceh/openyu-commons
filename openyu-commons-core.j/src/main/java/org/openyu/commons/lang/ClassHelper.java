package org.openyu.commons.lang;

import java.beans.Introspector;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.openyu.commons.bean.BaseBean;
import org.openyu.commons.entity.BaseEntity;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.util.ConfigHelper;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.CollectionHelper;
import org.openyu.commons.util.concurrent.MapCache;
import org.openyu.commons.util.concurrent.impl.MapCacheImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1.使用cache主要是為了減少反射時大量的消耗資源, 執行效率與原本無cache其實是差不多的
 */
public class ClassHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ClassHelper.class);

	private static ClassHelper instance;

	public static final Class<?>[] EMPTY_CLASSES = new Class<?>[0];

	public static final Field[] EMPTY_FIELDS = new Field[0];

	public static final Method[] EMPTY_METHODS = new Method[0];

	//
	public static final String ARRAY_SUFFIX = "[]";

	private static Class<?>[] PRIMITIVE_CLASSES = { boolean.class, char.class, byte.class, short.class, int.class,
			long.class, float.class, double.class };

	private static Class<?>[] LANG_CLASSES = { String.class, Object.class };

	private static final char PACKAGE_SEPARATOR_CHAR = '.';

	private static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);

	private static final char INNER_CLASS_SEPARATOR_CHAR = '$';

	private static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);

	private static final String CGLIB_CLASS_SEPARATOR_CHAR = "$$";

	//
	private static final Map<Class<?>, Class<?>> primitiveWrapperCache = new LinkedHashMap<Class<?>, Class<?>>();

	private static final Map<Class<?>, Object> defaultValueCache = new LinkedHashMap<Class<?>, Object>();

	private static MapCache<String, Class<?>> forNameAndCache = new MapCacheImpl<String, Class<?>>();

	/**
	 * Field
	 */
	private static MapCache<Class<?>, Field[]> getFieldsAndCache = new MapCacheImpl<Class<?>, Field[]>();

	private static MapCache<Class<?>, Field[]> getDeclaredFieldsAndCache = new MapCacheImpl<Class<?>, Field[]>();

	/**
	 * Method
	 */
	private static MapCache<Class<?>, Method[]> getMethodsAndCache = new MapCacheImpl<Class<?>, Method[]>();

	private static MapCache<Class<?>, Method[]> getDeclaredMethodsAndCache = new MapCacheImpl<Class<?>, Method[]>();

	private static MapCache<Class<?>, MapCacheImpl<Method, Class<?>[]>> getMethodParameterTypesAndCache = new MapCacheImpl<Class<?>, MapCacheImpl<Method, Class<?>[]>>();

	private static MapCache<Class<?>, Class<?>[]> getInterfacesAndCache = new MapCacheImpl<Class<?>, Class<?>[]>();

	//
	public final static String PO2VOS = "classHelper.po2vos";

	private static Map<String, String> po2vos = new LinkedHashMap<String, String>();

	private static MapCache<Class<?>, Class<?>> getConventionClassAndCache = new MapCacheImpl<Class<?>, Class<?>>();

	private static MapCache<Class<?>, Object[]> getEnumConstantsAndCache = new MapCacheImpl<Class<?>, Object[]>();

	/**
	 * Constructor
	 */
	private static MapCache<Class<?>, Constructor<?>[]> getConstructorsAndCache = new MapCacheImpl<Class<?>, Constructor<?>[]>();

	private static MapCache<Class<?>, Constructor<?>[]> getDeclaredConstructorsAndCache = new MapCacheImpl<Class<?>, Constructor<?>[]>();

	private static MapCache<Class<?>, MapCacheImpl<Constructor<?>, Class<?>[]>> getConstructorParameterTypesAndCache = new MapCacheImpl<Class<?>, MapCacheImpl<Constructor<?>, Class<?>[]>>();

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			// 3類8種型別
			primitiveWrapperCache.put(Boolean.class, boolean.class);
			primitiveWrapperCache.put(Character.class, char.class);
			primitiveWrapperCache.put(Byte.class, byte.class);
			primitiveWrapperCache.put(Short.class, short.class);
			primitiveWrapperCache.put(Integer.class, int.class);
			primitiveWrapperCache.put(Long.class, long.class);
			primitiveWrapperCache.put(Float.class, float.class);
			primitiveWrapperCache.put(Double.class, double.class);

			// 預設值
			defaultValueCache.put(Boolean.class, BooleanHelper.DEFAULT_VALUE);
			defaultValueCache.put(Character.class, CharHelper.DEFAULT_VALUE);
			defaultValueCache.put(Byte.class, NumberHelper.DEFAULT_BYTE);
			defaultValueCache.put(Short.class, NumberHelper.DEFAULT_SHORT);
			defaultValueCache.put(Integer.class, NumberHelper.DEFAULT_INT);
			defaultValueCache.put(Long.class, NumberHelper.DEFAULT_LONG);
			defaultValueCache.put(Float.class, NumberHelper.DEFAULT_FLOAT);
			defaultValueCache.put(Double.class, NumberHelper.DEFAULT_DOUBLE);
			//
			defaultValueCache.put(boolean.class, BooleanHelper.DEFAULT_VALUE);
			defaultValueCache.put(char.class, CharHelper.DEFAULT_VALUE);
			defaultValueCache.put(byte.class, NumberHelper.DEFAULT_BYTE);
			defaultValueCache.put(short.class, NumberHelper.DEFAULT_SHORT);
			defaultValueCache.put(int.class, NumberHelper.DEFAULT_INT);
			defaultValueCache.put(long.class, NumberHelper.DEFAULT_LONG);
			defaultValueCache.put(float.class, NumberHelper.DEFAULT_FLOAT);
			defaultValueCache.put(double.class, NumberHelper.DEFAULT_DOUBLE);
			//
			po2vos = ConfigHelper.getMap(PO2VOS, new LinkedHashMap<String, String>());
		}
	}

	public ClassHelper() {
	}

	public static synchronized ClassHelper getInstance() {
		if (instance == null) {
			instance = new ClassHelper();
		}
		return instance;
	}

	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			// No thread context class loader -> use class loader of this class.
			classLoader = ClassHelper.class.getClassLoader();
		}
		return classLoader;
	}

	/**
	 * 由類別名稱取得class
	 *
	 * @param name
	 * @return
	 */
	public static Class<?> forName(String name) {
		return forNameAndCache(name, getClassLoader());
	}

	/**
	 * 由類別名稱取得class
	 *
	 * @param name
	 * @param classLoader
	 * @return
	 */
	public static Class<?> forName(String name, ClassLoader classLoader) {
		return forNameAndCache(name, classLoader);
	}

	/**
	 * 由類別名稱取得class
	 *
	 * 原為 loadClass(String classNames)
	 *
	 * 1.2011/12/05 加cache, forNameAndCache
	 *
	 * 2.2011/12/27 加null cache判斷
	 *
	 * @param name
	 * @param classLoader
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Class<?> forNameAndCache(String name, ClassLoader classLoader) {
		Class<?> clazz = null;
		try {
			forNameAndCache.lockInterruptibly();
			try {
				if (forNameAndCache.isNotNullValue(name)) {
					clazz = forNameAndCache.get(name);
					if (clazz == null) {
						try {
							clazz = resolvePrimitiveClassName(name);
							if (clazz == null) {
								clazz = resolveLangClassName(name);
								if (clazz == null) {
									if (name.endsWith(ARRAY_SUFFIX)) {
										// com.aaa.bbb.ccc[]
										// special handling for array class
										// names
										String elementClassName = name.substring(0,
												name.length() - ARRAY_SUFFIX.length());
										Class<?> elementClass = forName(elementClassName, classLoader);
										clazz = Array.newInstance(elementClass, 0).getClass();
									} else {
										try {
											clazz = Thread.currentThread().getClass().forName(name);
										} catch (Exception ex) {
											try {
												clazz = Class.forName(name, true, classLoader);
											} catch (Exception ex2) {
												// ex2.printStackTrace();
											}
										}
									}
								}
							}
						} catch (Exception ex) {
							// ex.printStackTrace();
						}
						forNameAndCache.put(name, clazz);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				forNameAndCache.unlock();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return clazz;
	}

	public static Class<?> resolvePrimitiveClassName(String name) {
		// Most class names will be quite long, considering that they
		// SHOULD sit in a package, so a length check is worthwhile.
		if (name.length() <= 8) {
			// could be a primitive - likely
			for (int i = 0; i < PRIMITIVE_CLASSES.length; i++) {
				Class<?> clazz = PRIMITIVE_CLASSES[i];
				if (clazz.getName().equals(name)) {
					return clazz;
				}
			}
		}
		return null;
	}

	// java.lang.String
	public static Class<?> resolveLangClassName(String className) {
		Class<?> clazz = null;
		for (int i = 0; i < LANG_CLASSES.length; i++) {
			// java.lang
			if (LANG_CLASSES[i].getName().substring(10).equals(className)) {
				break;
			}
		}
		return clazz;
	}

	/**
	 * org.openyu.commons.lang.ClassHelperTest => ClassHelperTest
	 * 
	 * org.openyu.commons.lang.ClassHelperTest$PlayerPoSupporter =>
	 * ClassHelperTest.PlayerPoSupporter
	 * 
	 * @param className
	 * @return
	 */
	public static String getSimpleName(String className) {
		// Assert.hasLength(className, "Class name must not be empty");
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
		int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR_CHAR);
		if (nameEndIndex == -1) {
			nameEndIndex = className.length();
		}
		//
		StringBuilder result = new StringBuilder();
		result.append(new String(className.substring(lastDotIndex + 1, nameEndIndex)));
		return StringHelper.replace(result.toString(), INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
	}

	public static String getSimpleName(Class<?> clazz) {
		return getSimpleName(getQualifiedName(clazz));
	}

	/**
	 * org.openyu.commons.lang.ClassHelperTest => classHelperTest
	 * 
	 * org.openyu.commons.lang.ClassHelperTest$PlayerPoSupporter =>
	 * classHelperTest.PlayerPoSupporter
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getSimpleNameAsProperty(Class<?> clazz) {
		return Introspector.decapitalize(getSimpleName(clazz));
	}

	public static String getQualifiedName(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		if (clazz.isArray()) {
			return clazz.getComponentType().getName() + ARRAY_SUFFIX;
		} else {
			return clazz.getName();
		}
	}

	// TODO 未測試
	public static String getQualifiedMethodName(Method method) {
		AssertHelper.notNull(method, "The Method must not be null");
		return method.getDeclaringClass().getName() + "." + method.getName();
	}

	public static boolean isMethod(Class<?> clazz, String methodName) {
		return isMethod(clazz, methodName, null);
	}

	public static boolean isMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
		Method method = getMethod(clazz, methodName, parameterTypes);
		return (method != null ? true : false);
	}

	public static boolean isDeclaredMethod(Class<?> clazz, String methodName) {
		return isDeclaredMethod(clazz, methodName, null);
	}

	public static boolean isDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
		Method method = getDeclaredMethod(clazz, methodName, parameterTypes);
		return (method != null ? true : false);
	}

	// TODO 未測試
	public static int getMethodCount(Class<?> clazz, String methodName) {
		int count = 0;
		do {
			for (int i = 0; i < clazz.getDeclaredMethods().length; i++) {
				Method method = clazz.getDeclaredMethods()[i];
				if (methodName.equals(method.getName())) {
					count++;
				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		return count;
	}

	// TODO 未測試
	public static boolean hasAtLeastOneMethod(Class<?> clazz, String methodName) {
		// Assert.notNull(clazz, "Class must not be null");
		// Assert.notNull(methodName, "Method name must not be null");
		do {
			for (int i = 0; i < clazz.getDeclaredMethods().length; i++) {
				Method method = clazz.getDeclaredMethods()[i];
				if (method.getName().equals(methodName)) {
					return true;
				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		return false;
	}

	public static Method getMethod(Class<?> clazz, String methodName) {
		return getMethod(clazz, methodName, (Class[]) null);
	}

	public static Method getMethod(Class<?> clazz, String methodName, Class<?> parameterType) {
		return getMethod(clazz, methodName, new Class[] { parameterType });
	}

	// getMethods 自身的public方法和super class的public方法
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		AssertHelper.notNull(methodName, "The MethodName must not be null");
		//
		Method method = null;
		try {
			// String methodKey = getMethodCacheKey(clazz, methodName,
			// parameterTypes);
			// method = getMethodCache.get(methodKey);

			// #issue 找不到時會花費很多時間
			// method = clazz.getMethod(methodName, parameterTypes);

			// #fix
			Method[] methods = getMethodsAndCache(clazz);
			for (Method entryMethod : methods) {
				Class<?>[] entryParameterTypes = getMethodParameterTypesAndCache(clazz, entryMethod);
				if (entryMethod.getName().equals(methodName)) {
					if (entryParameterTypes.length == 0 && parameterTypes == null) {
						method = entryMethod;
						break;
					} else {
						if (ObjectHelper.equals(entryParameterTypes, parameterTypes)) {
							method = entryMethod;
							break;
						}
					}
				}
			}
			//
			// if (method != null)
			// {
			// getMethodCache.put(methodKey.toString(), method);
			// }

		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return method;
	}

	public static Method getDeclaredMethod(Class<?> clazz, String methodName) {
		return getDeclaredMethod(clazz, methodName, (Class[]) null);
	}

	public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?> parameterType) {
		return getDeclaredMethod(clazz, methodName, new Class[] { parameterType });
	}

	// getDeclaredMethods 自身的public、protected、private方法
	// #fix:當本身找不到,先判斷自身是否為interface,若是往super interface class尋找
	// #fix:若自身不為interface,則往super class 尋找
	public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		AssertHelper.notNull(methodName, "The MethodName must not be null");
		//
		Method method = null;
		try {

			// try
			// {
			// 找不到時會花費很多時間
			// method = clazz.getDeclaredMethod(methodName, parameterTypes);

			// #fix
			Method[] methods = getDeclaredMethodsAndCache(clazz);
			for (Method entryMethod : methods) {
				// System.out.println(clazz.getName() + " " + entry.getName() +
				// " "
				// + entry.getParameterTypes());
				// System.out.println(entry.getParameterTypes().equals(parameterTypes));
				// 會太慢,因把getParameterTypes作key的一部分
				// Class<?>[] temp = entry.getParameterTypes();
				// System.out.println(entry.getName()+" -----"+temp);
				// String parameterTypeKey = getMethodCacheKey(clazz,
				// entry.getName(), temp);
				// 還是太慢
				// String parameterTypeKey = entry.toString();
				// System.out.println("entry: "+entry);
				// System.out.println("----------" + methodParams);

				Class<?>[] entryParameterTypes = getMethodParameterTypesAndCache(clazz, entryMethod);
				if (entryMethod.getName().equals(methodName)) {
					// System.out.println(parameterTypeKey + " " + methodName);
					// 沒有params的method
					if (entryParameterTypes.length == 0 && parameterTypes == null) {
						method = entryMethod;
						break;
					} else {
						if (ObjectHelper.equals(entryParameterTypes, parameterTypes)) {
							method = entryMethod;
							break;
						}
					}
				}
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return method;
	}

	// getMethod
	public static Object invokeMethod(Object object, String methodName) {
		return invokeMethod(object, methodName, (Class[]) null, (Object[]) null);
	}

	public static Object invokeMethod(Object object, String methodName, Class<?> parameterType, Object arg) {
		return invokeMethod(object, methodName, new Class[] { parameterType }, new Object[] { arg });
	}

	public static Object invokeMethod(Object value, String methodName, Class<?>[] parameterTypes, Object[] args) {
		Method method = getMethod(value.getClass(), methodName, parameterTypes);
		return invokeMethod(value, method, args);
	}

	// invokeMethod,invokeDeclaredMethod共用
	public static Object invokeMethod(Object value, Method method, Object[] args) {
		Object object = null;
		try {
			if (method != null) {
				object = method.invoke(value, args);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return object;
	}

	//
	public static Object invokeDeclaredMethod(Object object, String methodName) {
		return invokeDeclaredMethod(object, methodName, (Class[]) null, (Object[]) null);
	}

	public static Object invokeDeclaredMethod(Object object, String methodName, Class<?> parameterType, Object arg) {
		return invokeDeclaredMethod(object, methodName, new Class[] { parameterType }, new Object[] { arg });
	}

	public static Object invokeDeclaredMethod(Object value, String methodName, Class<?>[] parameterTypes,
			Object[] args) {
		Method method = getDeclaredMethod(value.getClass(), methodName, parameterTypes);
		return invokeMethod(value, method, args);
	}

	/**
	 * 取自身及父類的 public field
	 *
	 * @param clazz
	 * @param fieldName
	 *            屬性名稱
	 * @return
	 */
	public static Field getField(Class<?> clazz, String fieldName) {
		Field result = null;
		try {
			// String fieldKey = getFieldCacheKey(clazz, fieldName);
			// field = getFieldCache.get(fieldKey);
			//

			// #issue 找不到時會花費很多時間
			// field = clazz.getField(fieldName);

			// #fix
			Field[] fields = getFieldsAndCache(clazz);
			for (Field entryField : fields) {
				if (entryField.getName().equals(fieldName)) {
					result = entryField;
					break;
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static Field[] getField(Class<?> clazz, Class<?> fieldClass) {
		List<Field> list = new LinkedList<Field>();
		try {
			Field[] fields = getFieldsAndCache(clazz);
			for (Field entryField : fields) {
				if (entryField.getType() == fieldClass) {
					list.add(entryField);
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return list.toArray(new Field[] {});
	}

	/**
	 * 取自身的public,proected,private field
	 *
	 * 當本身找不到,往super class 尋找
	 *
	 * @param clazz
	 * @param fieldName
	 *            屬性名稱
	 * @return
	 */
	public static Field getDeclaredField(Class<?> clazz, String fieldName) {
		Field result = null;
		try {
			// String fieldKey = getFieldCacheKey(clazz, fieldName);
			// field = getFieldCache.get(fieldKey);
			//

			// #issue 找不到時會花費很多時間
			// field = clazz.getDeclaredField(fieldName);

			// #fix
			Field[] fields = getDeclaredFieldsAndCache(clazz);
			for (Field field : fields) {
				if (field.getName().equals(fieldName)) {
					result = field;
					break;
				}
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static Field[] getDeclaredField(Class<?> clazz, Class<?> fieldClass) {
		List<Field> list = new LinkedList<Field>();
		try {
			Field[] fields = getDeclaredFieldsAndCache(clazz);
			for (Field entryField : fields) {
				if (entryField.getType() == fieldClass) {
					list.add(entryField);
				}
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return list.toArray(new Field[] {});
	}

	// 取自身及父類的 public field
	public static <T> T getFieldValue(Object value, String fieldName) {
		T result = null;
		if (value != null && fieldName != null) {
			Field field = getField(value.getClass(), fieldName);
			result = getFieldValue(value, field);
		}
		return result;
	}

	// 取自身的public,proected,private field
	// #fix:當本身找不到,往super class 尋找
	public static <T> T getDeclaredFieldValue(Object value, String fieldName) {
		T result = null;
		if (value != null && fieldName != null) {
			Field field = getDeclaredField(value.getClass(), fieldName);
			result = getFieldValue(value, field);
		}
		return result;
	}

	public static List<Object> getDeclaredFieldValue(Object value) {
		List<Object> result = new LinkedList<Object>();
		if (value != null) {
			Field[] fields = getDeclaredFieldsAndCache(value.getClass());
			for (Field field : fields) {
				Object fieldValue = getFieldValue(value, field);
				result.add(fieldValue);
			}
		}
		return result;
	}

	// getFieldValue,getDeclaredFieldValue共用
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object value, Field field) {
		T result = null;
		if (value != null && field != null) {
			try {
				boolean oldAccessible = field.isAccessible();
				boolean changeAccessible = false;
				if (!oldAccessible) {
					field.setAccessible(true);
					changeAccessible = true;
				}
				//
				result = (T) field.get(value);
				//
				if (changeAccessible) {
					field.setAccessible(oldAccessible);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static boolean setFieldValue(Object value, String fieldName, Object setValue) {
		Field field = getField(value.getClass(), fieldName);
		return setFieldValue(value, field, setValue);
	}

	public static boolean setDeclaredFieldValue(Object value, String fieldName, Object setValue) {
		Field field = getDeclaredField(value.getClass(), fieldName);
		return setFieldValue(value, field, setValue);
	}

	// setFieldValue,setDeclaredFieldValue共用
	public static boolean setFieldValue(Object value, Field field, Object setValue) {
		boolean result = false;
		if (value != null && field != null) {
			try {
				boolean oldAccessible = field.isAccessible();
				boolean changeAccessible = false;
				if (!oldAccessible) {
					field.setAccessible(true);
					changeAccessible = true;
				}
				//
				field.set(value, setValue);
				//
				if (changeAccessible) {
					field.setAccessible(oldAccessible);
				}
				//
				result = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 是否為常數field
	 *
	 * public static final
	 *
	 * @param field
	 * @return
	 */
	public static boolean isConstantField(Field field) {
		boolean result = false;
		if (field != null) {
			try {
				int modifiers = field.getModifiers();
				if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
					result = true;
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
		return result;
	}

	public static boolean isPrimitiveWrapper(Object value) {
		return (value != null ? isPrimitiveWrapper(value.getClass()) : false);
	}

	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		return primitiveWrapperCache.containsKey(clazz);
	}

	public static boolean isPrimitiveOrWrapper(Object value) {
		return (value != null ? isPrimitiveOrWrapper(value.getClass()) : false);
	}

	public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	public static Object getDefaultValue(Class<?> clazz) {
		return defaultValueCache.get(clazz);
	}

	public static boolean isInstanceOf(Class<?> type, Object obj) {
		return type.isInstance(obj);
	}

	// String[] -> className=[Ljava.lang.String;
	// class [Ljava.lang.String;
	// baseClass ->java.lang.String;
	//
	// String[][] -> className=[[Ljava.lang.String;
	// class [[Ljava.lang.String;
	// baseClass -> java.lang.String;
	public static Class<?> getBaseClass(Object object) {
		Class<?> clazz = null;
		if (object == null) {
			clazz = Void.TYPE;
		} else {
			// String className = object.getClass().getName();
			StringBuilder className = new StringBuilder(object.getClass().getName());
			// System.out.println("className: " + className);

			// 判斷是否一維/多維陣列
			int dimension = 0;
			while (className.charAt(dimension) == '[') {
				dimension += 1;
			}

			// 不是一維/多維陣列
			if (dimension == 0) {
				clazz = object.getClass();
			}
			// 一維/多維陣列
			else {
				switch (className.charAt(dimension)) {
				// 基本型別
				// [Z
				case 'Z':
					clazz = Boolean.TYPE;
					break;
				case 'C':
					clazz = Character.TYPE;
					break;
				case 'B':
					clazz = Byte.TYPE;
					break;
				case 'S':
					clazz = Short.TYPE;
					break;
				// [I
				case 'I':
					clazz = Integer.TYPE;
					break;
				// [J
				case 'J':
					clazz = Long.TYPE;
					break;
				case 'F':
					clazz = Float.TYPE;
					break;
				case 'D':
					clazz = Double.TYPE;
					break;
				// 物件型別
				// [Ljava.lang.Integer;
				// [Ljava.lang.String;
				case 'L':
					try {
						clazz = ClassHelper.forName(className.substring(dimension + 1, className.length() - 1));
					} catch (Exception ex) {
						// ex.printStackTrace();
					}
					break;
				}
			}
		}
		return clazz;
	}

	public static String resourcePath2PackagePath(Class<?> clazz, String resourceName) {
		if (!resourceName.startsWith("/")) {
			return classPackageAsResourcePath(clazz) + "/" + resourceName;
		}
		return classPackageAsResourcePath(clazz) + resourceName;
	}

	public static String classPackageAsResourcePath(Class<?> clazz) {
		if (clazz == null || clazz.getPackage() == null) {
			return "";
		}
		return clazz.getPackage().getName().replace('.', '/');
	}

	// -------------------------------------------------------
	public static <T> T newInstanceFromSetting(String key, String defaultValue) {
		T result = null;
		String className = null;
		// 1.從定義檔
		className = ConfigHelper.getString(key, defaultValue);
		result = newInstance(className);

		// 2.從system.properties 尋找
		if (result == null) {
			className = SystemHelper.getProperty(key, defaultValue);
			result = newInstance(className);
		}
		return result;
	}

	/**
	 * 由object建構物件
	 *
	 * @param value
	 * @return
	 */
	public static <T> T newInstance(Object value) {
		T result = null;
		if (value != null) {
			result = newInstance(value.getClass());
		}
		return result;
	}

	/**
	 * 由className建構物件
	 *
	 * @param className
	 * @return
	 */
	public static <T> T newInstance(String className) {
		return newInstance(forName(className));
	}

	/**
	 * 由class建構物件
	 *
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz) {
		T result = null;

		// #fix 目前以此方式最快
		try {
			if (clazz != null && !clazz.isInterface()) {
				result = (T) clazz.newInstance();
			}
		}
		// catch (InstantiationException ex)
		// {
		// }
		catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	// from Constructor
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Constructor<?> constructor, Object[] args) {
		// Assert.notNull(ctor, "Constructor must not be null");
		T result = null;
		try {
			if (!Modifier.isPublic(constructor.getModifiers())
					|| !Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) {
				constructor.setAccessible(true);
			}
			result = (T) constructor.newInstance(args);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T newProxyInstance(Class<?>[] clazzes, InvocationHandler handler) {
		// 因多代繼承時,delegate.getClass().getInterfaces() 只能找到本身的實作介面
		// 無法找到super class實作介面,故改用

		// Class[] clazzes = delegate.getClass().getInterfaces();
		// Class[] clazzes = ClassHelper.getInterfaces(handler.getClass());
		// Spy.trace(clazzes);
		return (T) Proxy.newProxyInstance(handler.getClass().getClassLoader(), clazzes, handler);
	}

	public static Method[] getMethodsAndCache(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		Method[] methods = new Method[0];
		if (clazz != null) {
			try {
				getMethodsAndCache.lockInterruptibly();
				try {
					if (getMethodsAndCache.isNotNullValue(clazz)) {
						methods = getMethodsAndCache.get(clazz);
						// methods = getDeclaredMethods(clazz);
						if (methods == null) {
							try {
								methods = clazz.getMethods();
								// 2015/10/07 for not public
								for (Method method : methods) {
									method.setAccessible(true);
								}
							} catch (Exception ex) {
								// ex.printStackTrace();
							}
							getMethodsAndCache.put(clazz, methods);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					getMethodsAndCache.unlock();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		return methods;
	}

	/**
	 * 使用遞迴, 會往super class 尋找
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method[] getDeclaredMethodsAndCache(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		Method[] methods = new Method[0];
		if (clazz != null) {
			try {
				getDeclaredMethodsAndCache.lockInterruptibly();
				try {
					if (getDeclaredMethodsAndCache.isNotNullValue(clazz)) {
						methods = getDeclaredMethodsAndCache.get(clazz);
						// methods = getDeclaredMethods(clazz);
						if (methods == null) {
							try {
								methods = clazz.getDeclaredMethods();
								// 2015/10/07 for not public
								for (Method method : methods) {
									method.setAccessible(true);
								}
							} catch (Exception ex) {
								// ex.printStackTrace();
							}
							getDeclaredMethodsAndCache.put(clazz, methods);
						}

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					getDeclaredMethodsAndCache.unlock();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			// 是否為interface
			if (clazz.isInterface()) {
				Class<?>[] interfaceClazzes = getInterfacesAndCache(clazz);
				for (Class<?> interfaceClazz : interfaceClazzes) {
					Method[] interfaceMethods = getDeclaredMethodsAndCache(interfaceClazz);
					if (interfaceMethods != null && interfaceMethods.length > 0) {
						methods = ArrayHelper.addUnique(methods, interfaceMethods, Method[].class);
					}
				}
			} else {
				//
				Class<?> superClazz = clazz.getSuperclass();
				if (superClazz != null && !superClazz.equals(Object.class)) {
					Method[] superMethods = getDeclaredMethodsAndCache(superClazz);
					if (superMethods != null && superMethods.length > 0) {
						methods = ArrayHelper.addUnique(methods, superMethods, Method[].class);
					}
				}
			}
		}
		return methods;
	}

	public static Class<?>[] getMethodParameterTypesAndCache(Class<?> clazz, Method method) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		AssertHelper.notNull(method, "The Method must not be null");
		//
		Class<?>[] parameterTypes = null;
		try {
			getMethodParameterTypesAndCache.lockInterruptibly();
			try {
				MapCacheImpl<Method, Class<?>[]> methodParameterTypes = getMethodParameterTypesAndCache.get(clazz);
				if (methodParameterTypes == null) {
					methodParameterTypes = new MapCacheImpl<Method, Class<?>[]>();
					getMethodParameterTypesAndCache.put(clazz, methodParameterTypes);
				}
				// System.out.println(method);
				//
				if (method != null) {
					if (methodParameterTypes.isNotNullValue(method)) {
						parameterTypes = methodParameterTypes.get(method);
						if (parameterTypes == null) {
							try {
								parameterTypes = method.getParameterTypes();
							} catch (Exception ex) {
								// ex.printStackTrace();
							}
							methodParameterTypes.put(method, parameterTypes);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				getMethodParameterTypesAndCache.unlock();
			}

		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return parameterTypes;
	}

	// get form cache, if not exist then add to cache
	// include super class
	public static Field[] getFieldsAndCache(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		Field[] fields = new Field[0];
		if (clazz != null) {
			try {
				getFieldsAndCache.lockInterruptibly();
				try {
					if (getFieldsAndCache.isNotNullValue(clazz)) {
						fields = getFieldsAndCache.get(clazz);
						// fields = getFields(clazz);
						if (fields == null) {
							try {
								fields = clazz.getFields();
								// 2015/10/07 for not public
								for (Field field : fields) {
									field.setAccessible(true);
								}
							} catch (Exception ex) {
								// ex.printStackTrace();
							}
							getFieldsAndCache.put(clazz, fields);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					getFieldsAndCache.unlock();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		return fields;
	}

	//
	/**
	 * 使用遞迴, 會往super class 尋找
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getDeclaredFieldsAndCache(Class<?> clazz) {
		//AssertHelper.notNull(clazz, "The Class must not be null");
		//
		Field[] fields = new Field[0];
		if (clazz != null) {
			try {
				getDeclaredFieldsAndCache.lockInterruptibly();
				try {
					if (getDeclaredFieldsAndCache.isNotNullValue(clazz)) {
						fields = getDeclaredFieldsAndCache.get(clazz);
						// fields = getDeclaredFields(clazz);
						if (fields == null) {
							try {
								fields = clazz.getDeclaredFields();
								// 2015/10/07 for not public
								for (Field field : fields) {
									field.setAccessible(true);
								}
							} catch (Exception ex) {
								// ex.printStackTrace();
							}
							getDeclaredFieldsAndCache.put(clazz, fields);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					getDeclaredFieldsAndCache.unlock();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			//
			Class<?> superClazz = clazz.getSuperclass();
			if (superClazz != null && !superClazz.equals(Object.class)) {
				Field[] superFields = getDeclaredFieldsAndCache(superClazz);
				if (superFields.length > 0) {
					// #issue 太慢
					// Set<?> set = ArrayHelper.___uniqueJoin(fields,
					// superFields);
					// fields = (Field[]) set.toArray(new Field[set.size()]);

					// #fix
					// fields = (Field[]) allFields;//error
					// fields=Arrays.asList(allFields).toArray(new
					// Field[allFields.length]);
					fields = ArrayHelper.addUnique(fields, superFields, Field[].class);
				}
			}
			// System.out.println("declaredFieldCache: "+declaredFieldCache);
		}
		return fields;
	}

	// ---------------------------------------------------
	/**
	 * 目前正在使用的,為deep copy,類似deep clone,
	 *
	 * 但可依慣例規則,針對不同類別自動轉換
	 *
	 * @param orig
	 * @param destClass
	 * @return
	 */
	public static <T, O> T copyProperties(O orig, Class<?> destClass) {
		T dest = null;
		if (destClass != null) {
			dest = newInstance(destClass);
		}
		return copyProperties(orig, dest);
	}

	public static <O, T> T copyProperties(O orig) {
		return copyProperties(orig, (T) null);
	}

	public static <O, T> T copyProperties(O orig, T dest) {
		return deepCopyProperties(orig, dest);
	}

	// ---------------------------------------------------
	// #1.只能copy 同型別的field,不同type會有 ex
	// org.springframework.beans.FatalBeanException: Could not copy properties
	// from source to target; nested exception is
	// java.lang.IllegalArgumentException: argument type mismatch
	// ---------------------------------------------------
	// public static <T> T deepCopyPropertiesBySpring(Object orig, Class<?>
	// destClass)
	// {
	// Object dest = null;
	// //若沒有,無參數建構子,則無法建構會是null
	// dest = newInstance(destClass);
	// //System.out.println("destClass: "+destClass+" "+dest);
	// if (dest != null)
	// {
	// deepCopyPropertiesBySpring(orig, dest);
	// }
	// return (T) dest;
	// }

	// ---------------------------------------------------
	// 遞迴,處理集合物件/陣列
	// ---------------------------------------------------
	@SuppressWarnings("unchecked")
	protected static <O, T> T deepCopyProperties(O orig, T dest) {
		if (ArrayHelper.isArray(orig)) {
			// TODO 陣列尚未處理
		} else if (orig instanceof Collection) {
			Collection<Object> copy = (Collection<Object>) (dest != null ? dest : newInstance(orig.getClass()));
			for (Object entryOrig : (Collection<?>) orig) {
				// #issue: copy class mapping, getCopyClass
				// #fix: ok
				// System.out.println("copy: " +copy);
				Object entryDest = null;
				entryDest = deepCopyProperties(entryOrig, entryDest);
				copy.add(entryDest);
				// System.out.println(copy+" entryDest: " +entryDest);
			}
			dest = (T) copy;
		} else if (orig instanceof Map) {
			Map<Object, Object> copy = (Map<Object, Object>) (dest != null ? dest : newInstance(orig.getClass()));
			for (Map.Entry<?, ?> entryOrig : ((Map<?, ?>) orig).entrySet()) {
				// #issue: copy class mapping, getCopyClass
				// #fix: ok
				Object entryDestKey = null;
				entryDestKey = deepCopyProperties(entryOrig.getKey(), entryDestKey);
				//
				Object entryDestValue = null;
				entryDestValue = deepCopyProperties(entryOrig.getValue(), entryDestValue);
				copy.put(entryDestKey, entryDestValue);
			}
			dest = (T) copy;
		}
		// 到這裡已經都是物件了,沒有集合跟陣列
		else {
			// #issue: 還有field沒處理,需處理遞迴
			Class<?> destClass = null;
			// 當dest==null,表示可以從getConventionClass,尋找轉換class
			if (dest == null) {
				// 當找不到destClass時,表示沒有要轉換class
				destClass = getConventionClass(orig);
				dest = (destClass == null ? (T) orig : (T) newInstance(destClass));
			}
			// System.out.println("generic..." + orig);
			// dest = genericCopyPropertiesBySpring(orig, dest);

			// System.out.println("destClass: "+destClass);

			// #fix:處理遞迴 ok
			dest = deepCopyFields(orig, dest);
		}
		return (T) dest;
	}

	// PlayerPo<->Player
	// 遞迴,處理field
	@SuppressWarnings("unchecked")
	protected static <T> T deepCopyFields(Object orig, Object dest) {
		T result = null;
		// if (orig.getClass().getSimpleName().startsWith("FourSymbolPenImpl")){
		// System.out.println(orig.getClass().getSimpleName());
		// }
		// System.out.println("dest: "+dest.getClass().getSimpleName());
		// 當來源為null
		if (orig == null) {
			result = (T) dest;
		}
		// 當來源與目的ref相同時,用deep clone
		else if (orig != null && dest != null && orig.getClass().equals(dest.getClass())) {
			dest = CloneHelper.clone(orig);
			result = (T) dest;
		}
		// 開始copy field
		else {
			// #issue: when dest=null, convention class???
			// #fix:dest=orig ok
			Class<?> destClass = null;
			if (dest == null) {
				destClass = getConventionClass(orig);
				dest = (destClass != null ? newInstance(destClass) : orig);
			} else {
				destClass = dest.getClass();
			}
			// 來源
			Class<?> origClass = orig.getClass();

			// 由目的開始作
			Field[] fields = getDeclaredFieldsAndCache(destClass);
			// System.out.println("destClass: " + destClass);
			for (Field destField : fields) {
				// final,transient,不需copy
				if (Modifier.isFinal(destField.getModifiers()) || Modifier.isTransient(destField.getModifiers())) {
					continue;
				}
				// System.out.println("destField: " + destField.getName() + ", "
				// + destField.getType().getSimpleName());

				// 目的欄位
				Class<?> destFieldType = destField.getType();
				Object destFieldValue = getFieldValue(dest, destField);

				// 來源欄位
				Field origField = getDeclaredField(origClass, destField.getName());
				Class<?> origFieldType = null;
				if (origField != null) {
					// orig field class
					origFieldType = origField.getType();
					Object origFieldValue = getFieldValue(orig, origField);

					// 非null且值不相同
					if (origFieldValue != null && !origFieldValue.equals(destFieldValue)) {
						// 2012/10/16
						// 1.基本或包裝型別
						if (isPrimitiveOrWrapper(origFieldType)) {
							setFieldValue(dest, destField, origFieldValue);
						}
						// 2.型別相等,且非集合物件不作轉換,直接clone後塞值
						else if (origFieldType.equals(destFieldType) && !ArrayHelper.isArray(origFieldValue)
								&& !(origFieldValue instanceof Collection) && !(origFieldValue instanceof Map)) {
							// System.out.println("1:origField " +
							// origField.getName() + ", "
							// + origFieldValue);

							// 原
							// setFieldValue(dest, destField, origValue);

							destFieldValue = CloneHelper.clone(origFieldValue);

							// System.out.println("1:destField " +
							// destField.getName() + ", "
							// + destFieldValue);
							setFieldValue(dest, destField, destFieldValue);
						}
						// 3.陣列
						else if (ArrayHelper.isArray(origFieldValue)) {
							// System.out.println("2: "+origFieldClass);
							// TODO 陣列還沒處理
						}
						// 4.Collection
						else if (origFieldValue instanceof Collection) {
							// System.out.println("3: "+origFieldClass);
							Collection<Object> copy = (Collection<Object>) (destFieldValue != null ? destFieldValue
									: newInstance(origFieldValue.getClass()));
							for (Object entryOrig : (Collection<?>) origFieldValue) {
								// Class<?> copyClass =
								// getConventionClass(entryOrig);
								// Object entryDest = (copyClass == null ?
								// origValue : newInstance(copyClass));
								Object entryDest = null;
								entryDest = deepCopyFields(entryOrig, entryDest);
								copy.add(entryDest);
								// System.out.println("entryDest: " +
								// entryDest);
							}
							destFieldValue = copy;
						}
						// 5.Map
						else if (origFieldValue instanceof Map) {
							// System.out.println("4: "+origFieldClass);
							Map<Object, Object> copy = (Map<Object, Object>) (destFieldValue != null ? destFieldValue
									: newInstance(origFieldValue.getClass()));
							for (Map.Entry<?, ?> entryOrig : ((Map<?, ?>) origFieldValue).entrySet()) {
								// System.out.println("entryOrig.getKey(): " +
								// entryOrig.getKey());
								Object entryDestKey = null;
								entryDestKey = deepCopyFields(entryOrig.getKey(), entryDestKey);
								//
								Object entryDestValue = null;
								entryDestValue = deepCopyFields(entryOrig.getValue(), entryDestValue);

								// System.out.println("entryDestKey: " +
								// entryDestKey);
								// System.out.println("entryDestValue: " +
								// entryDestValue);
								copy.put(entryDestKey, entryDestValue);
							}
							destFieldValue = copy;
						}
						// 6.到這裡已經都是field了,沒有集合跟陣列
						// 但有個狀況例外,當field裡面有屬性是集合的時候,再用一次遞迴處理
						else {
							// System.out.println("5: " + origFieldClass);
							// 當找不到copy class時,表示沒有要轉換class
							Class<?> copyClass = getConventionClass(origFieldValue);
							// Class<?> copyClass = null;
							destFieldValue = (copyClass != null ? newInstance(copyClass) : origFieldValue);

							// 這裡可換其他的generic copyProperties,目前使用spring的
							// destValue =
							// genericCopyPropertiesBySpring(origValue,
							// destValue);
							// #issue 但若field為集合時,會整個copy過去,形成別的class

							// #fix 改為遞迴方式,直接setFieldValue即可
							destFieldValue = deepCopyFields(origFieldValue, destFieldValue);
							setFieldValue(dest, destField, destFieldValue);

							result = (T) dest;
						}
					}
					// 2012/10/12 當來源=null
					else if (origFieldValue == null) {
						// 1.若為基本型別的wrapper
						// 2.非hibernate的version欄位
						// 有預設值
						boolean isPrimitiveOrWrapper = isPrimitiveOrWrapper(origFieldType);
						if (isPrimitiveOrWrapper) {
							// hibernate用,當version=null,不塞預設值=0
							if (!"version".equals(origField.getName())) {
								setFieldValue(dest, destField, getDefaultValue(origFieldType));
							}
						} else {
							setFieldValue(dest, destField, null);
						}
					}

					// 2012/05/22 最後處理owner
					setOwner(dest, destFieldValue);
				} else {

				}
			}
			// end for
			result = (T) dest;
		}
		return result;
	}

	/**
	 * 處理owner
	 *
	 * @param owner
	 * @param destFieldValue
	 */
	protected static void setOwner(Object owner, Object destFieldValue) {
		if (owner != null && destFieldValue != null) {
			Field[] fields = getDeclaredFieldsAndCache(destFieldValue.getClass());
			for (Field field : fields) {
				// System.out.println(field.getName()+" "+field.getType());
				if (isInterfaceOf(field.getType(), owner.getClass())) {
					setFieldValue(destFieldValue, field, owner);
				}
			}
		}
	}

	// 無遞迴, 泛化 copy
	@SuppressWarnings("unchecked")
	protected static <T> T genericCopyPropertiesBySpring(Object orig, Object dest) {
		// System.out.println("spring: " + orig);
		// System.out.println("spring: " + dest);
		if (orig != dest && dest != null) {
			org.springframework.beans.BeanUtils.copyProperties(orig, dest);
			// System.out.println("copyProperties: " + dest);
		}
		return (T) dest;
	}

	// ---------------------------------------------------
	// #2.copy by field,沒再用了
	// ---------------------------------------------------
	// 1.傳入PlayerPo<->Player
	// 2.傳入List<PlayerPo> <-> List<Player>
	// dest會重新建構
	// 遞迴
	// #issus 會有無窮回圈,當雙向關聯時
	@SuppressWarnings("unchecked")
	protected static <T> T deepCopyPropertiesByField(Object orig, Class<?> destClass) {
		Object dest = null;
		if (orig != null) {
			if (ArrayHelper.isArray(orig)) {
				// TODO 陣列尚未處理
			} else if (orig instanceof Collection) {
				Collection<?> origs = (Collection<?>) orig;
				Collection<T> copy = newInstance(orig.getClass());
				for (Object entryOrig : origs) {
					copy.add((T) deepCopyPropertiesByField(entryOrig, entryOrig.getClass()));
				}
				dest = copy;
			} else if (orig instanceof Map) {
				Map<?, ?> origs = (Map<?, ?>) orig;
				Map<T, T> copy = ClassHelper.newInstance(orig.getClass());
				for (Map.Entry<?, ?> entryOrig : origs.entrySet()) {
					copy.put((T) deepCopyPropertiesByField(entryOrig.getKey(), entryOrig.getKey().getClass()),
							(T) deepCopyPropertiesByField(entryOrig.getValue(), entryOrig.getValue().getClass()));
				}
				dest = copy;
			}
			// generic copy
			else {
				// 若沒有,無參數建構子,則無法建構會是null
				Object copy = newInstance(destClass);
				if (copy != null) {
					genericCopyPropertiesByField(orig, copy);
				}
				dest = copy;
			}
		}
		return (T) dest;
	}

	// 原取自身的public,proected,private field
	//
	// #fix 2011/12/10, ok
	// 當本身找不到,往super class 尋找
	// 用本身的public,proected,private field
	// 及super 的public,proected,private field,來copy
	//
	// #fix 2011/12/16, ok
	// 當field為自訂class
	// 1.使用慣例isPoClass/isVoClass
	// 2.使用appConfig-init.xml <classHelper.poMapping>
	protected static void genericCopyPropertiesByField(Object orig, Object dest) {
		if (dest == null || orig == null) {
			return;
		}
		// 目的
		Class<?> destClass = dest.getClass();
		// 來源
		Class<?> origClass = orig.getClass();
		Field[] fields = getDeclaredFieldsAndCache(destClass);
		// 由目的開始作
		for (Field destField : fields) {
			if (Modifier.isFinal(destField.getModifiers())) {
				continue;
			}
			// dest class
			Object destValue = getFieldValue(dest, destField);
			Class<?> destFieldClass = destField.getType();
			// orig class
			Field origField = getDeclaredField(origClass, destField.getName());
			Class<?> origFieldClass = null;
			if (origField != null) {
				// orig field class
				Object origValue = getFieldValue(orig, origField);
				origFieldClass = origField.getType();
				// System.out.println(origFieldClass);
				// System.out.println("isPoClass: " +
				// ClassHelper.isPoClass(origFieldClass));

				if (origValue != null && !origValue.equals(destValue)) {
					// 型別相等及非集合物件不作轉換
					if (origFieldClass.equals(destFieldClass) && !ArrayHelper.isArray(origValue)
							&& !(origValue instanceof Collection) && !(origValue instanceof Map)) {
						setFieldValue(dest, destField, origValue);
					} else {
						// 轉換
						// System.out.println("copyByConvention:
						// "+origFieldClass+" "+destFieldClass);
						// Object conventionValue = copyByConvention(origValue);
						// setFieldValue(dest, destField, conventionValue);
					}
				} else if (origValue == null) {
					setFieldValue(dest, destField, null);
				}
			}
		}
	}

	// ---------------------------------------------------
	// copy po<->vo
	// 當有應對的conventionClass,會copyProperties
	// 若無,則直接dest = orig;
	// ---------------------------------------------------
	// public static <T> T copyByConvention(Object orig)
	// {
	// T dest = null;
	// Class<?> conventionClass = getConventionClass(orig);;
	// //System.out.println("destClass:" + destClass);
	// if (conventionClass != null)
	// {
	// dest = copyProperties(orig, conventionClass);
	// }
	// else
	// {
	// dest = (T) orig;
	// }
	//
	// return dest;
	// }

	// 以下type不尋找,以增加效率
	// ------------------------------------------------------------
	// Object
	// String
	// StringBuffer
	// StringBuilder
	// Throwable
	//
	// primitive,wrrapper
	// Number,BigDecimal,BigInteger
	// Boolean
	// Charcter
	//
	// Date
	// Loccale
	// ------------------------------------------------------------

	public static boolean isExcludeConvention(Object value) {
		boolean ret = false;
		if (value != null) {
			if (value.getClass().equals(Object.class)) {
				ret = true;
			} else if (value.getClass().equals(String.class)) {
				ret = true;
			} else if (value.getClass().equals(StringBuffer.class)) {
				ret = true;
			} else if (value.getClass().equals(StringBuilder.class)) {
				ret = true;
			} else if (value instanceof Throwable) {
				ret = true;
			} else if (isPrimitiveOrWrapper(value)) {
				ret = true;
			}
			// BigDecimal, BigInteger, Byte, Double, Float, Integer, Long, Short
			else if (value instanceof Number) {
				ret = true;
			}
			// ------------------------------------------------------------
			else if (value.getClass().equals(Date.class)) {
				ret = true;
			} else if (value.getClass().equals(Locale.class)) {
				ret = true;
			}
		} else {
			ret = true;
		}
		return ret;
	}

	// 取得轉換後的class
	// 1.需實作 BaseEntity/BaseBean,因使用慣例isPoClass/isVoClass
	// 是依照實作interface來判斷的
	// 2.使用appConfig-init.xml <classHelper.poMapping>
	// 3.若找不到傳回null
	// #issue 應該還能再調整效率,cache
	//
	// #fix cache, ok
	public static Class<?> getConventionClass(Object orig) {
		Class<?> conventionClass = null;
		Class<?> keyClass = null;
		Object keyValue = null;
		try {
			getConventionClassAndCache.lockInterruptibly();
			try {
				if (orig != null) {
					if (isExcludeConvention(orig)) {
						conventionClass = null;
					}
					// 1.array
					else if (ArrayHelper.isArray(orig)) {
						Object[] firstOrig = ArrayHelper.getFirstEntry((Object[]) orig);
						keyClass = (Class<?>) firstOrig[0];
						keyValue = firstOrig[1];
						if (keyClass != null) {
							if (getConventionClassAndCache.isNotNullValue(keyClass)) {
								conventionClass = getConventionClassAndCache.get(keyClass);
								if (conventionClass == null) {
									if (isExcludeConvention(keyValue)) {
										conventionClass = keyClass;
									} else {
										conventionClass = getConventionClass(keyValue);
									}
									getConventionClassAndCache.put(keyClass, conventionClass);
								}
							}
						}
					}
					// 2.Collection
					else if (orig instanceof Collection) {
						// [0]=class
						// [1]=value
						Object[] firstOrig = CollectionHelper.getFirstValue((Collection<?>) orig);
						keyClass = (Class<?>) firstOrig[0];
						keyValue = firstOrig[1];
						//
						if (keyClass != null) {
							if (getConventionClassAndCache.isNotNullValue(keyClass)) {
								conventionClass = getConventionClassAndCache.get(keyClass);
								if (conventionClass == null) {
									if (isExcludeConvention(keyValue)) {
										conventionClass = keyClass;
									} else {
										conventionClass = getConventionClass(keyValue);
									}
									getConventionClassAndCache.put(keyClass, conventionClass);
								}
							}
						}
					}
					// 3.Map, 不取key作判斷,取value作判斷
					else if (orig instanceof Map) {
						// [0]=class
						// [1]=value
						Object[] firstOrig = CollectionHelper.getFirstValue((Map<?, ?>) orig);
						keyClass = (Class<?>) firstOrig[0];
						keyValue = firstOrig[1];
						//
						if (keyClass != null) {
							if (getConventionClassAndCache.isNotNullValue(keyClass)) {
								conventionClass = getConventionClassAndCache.get(keyClass);
								if (conventionClass == null) {
									if (isExcludeConvention(keyValue)) {
										conventionClass = keyClass;
									} else {
										conventionClass = getConventionClass(keyValue);
									}
									// conventionClass=keyClass;
									getConventionClassAndCache.put(keyClass, conventionClass);
								}
							}
						}
					}
					// 4.使用isPoClass
					else if (isPoClass(orig)) {
						keyClass = orig.getClass();
						if (getConventionClassAndCache.isNotNullValue(keyClass)) {
							conventionClass = getConventionClassAndCache.get(keyClass);
							if (conventionClass == null) {
								conventionClass = po2VoClass(orig);
								getConventionClassAndCache.put(keyClass, conventionClass);
							}
						}
						// conventionClass = po2VoClass(orig);
					}
					// 5.使用isVoClass
					else if (isVoClass(orig)) {
						// System.out.println("isVoClass: "+isVoClass(orig));
						keyClass = orig.getClass();
						if (getConventionClassAndCache.isNotNullValue(keyClass)) {
							conventionClass = getConventionClassAndCache.get(keyClass);
							if (conventionClass == null) {
								conventionClass = vo2PoClass(orig);
								getConventionClassAndCache.put(keyClass, conventionClass);
							}
						}
						// conventionClass = vo2PoClass(orig);
					}
					// 6.appConfig-ini.xml
					else if (!po2vos.isEmpty()) {
						String mappingClassName = po2vos.get(orig.getClass().getName());
						List<String> poKeys = CollectionHelper.getKeysByValue(po2vos, orig.getClass().getName());
						// 從設定檔尋找對應的class, key->value
						if (mappingClassName != null) {
							keyClass = orig.getClass();
							if (getConventionClassAndCache.isNotNullValue(keyClass)) {
								conventionClass = getConventionClassAndCache.get(keyClass);
								if (conventionClass == null) {
									conventionClass = forName(mappingClassName);
									getConventionClassAndCache.put(keyClass, conventionClass);
								}
							}
							// conventionClass = forName(mappingClassName);
						}
						// value -> key
						else if (!poKeys.isEmpty()) {
							keyClass = orig.getClass();
							if (getConventionClassAndCache.isNotNullValue(keyClass)) {
								conventionClass = getConventionClassAndCache.get(keyClass);
								if (conventionClass == null) {
									conventionClass = forName(poKeys.get(0));
									getConventionClassAndCache.put(keyClass, conventionClass);
								}
							}
							// conventionClass = forName(poKeys.get(0));
						} else {
							conventionClass = null;
						}
					}
					// 到這裡已經沒有集合,陣列,自訂mapping class了
					else {
						conventionClass = null;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				getConventionClassAndCache.unlock();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		return conventionClass;
	}

	// ---------------------------------------------------
	// #3.沒再用,只是測試
	// ---------------------------------------------------
	@SuppressWarnings("unchecked")
	protected static <T> T copyPropertiesByMethod(Object orig, Class<?> destClass) {
		Object dest = null;
		if (orig instanceof Collection) {
			@SuppressWarnings("rawtypes")
			Collection origs = (Collection) orig;
			//
			dest = newInstance(orig.getClass());
			for (Object entryOrig : origs) {
				Object entryDest = newInstance(destClass);
				genericCopyPropertiesByMethod(entryOrig, entryDest);
				((Collection) dest).add(entryDest);
			}
		} else {
			dest = newInstance(destClass);
			genericCopyPropertiesByMethod(orig, dest);
		}
		return (T) dest;
	}

	// 自身的public方法和父類Object的public方法
	// 用本身public method及super class public method,來copy
	protected static void genericCopyPropertiesByMethod(Object orig, Object dest) {
		if (dest == null || orig == null) {
			return;
		}
		//
		Class<?> destClass = dest.getClass();
		Class<?> origClass = orig.getClass();
		Method[] methods = getMethodsAndCache(destClass);
		for (Method destMethod : methods) {
			// setter
			String setterName = destMethod.getName();
			String getterName = null;
			if (setterName.startsWith("set")) {
				// setEnable(boolean enable)
				Class<?>[] destParameterTypes = getMethodParameterTypesAndCache(destClass, destMethod);
				if (destParameterTypes == null || destParameterTypes.length != 1) {
					continue;
				}
				//
				Class<?> setterType = destParameterTypes[0];
				getterName = "get" + setterName.substring(3);
				// getter
				Method origMethod = null;
				if (setterType.isInstance(boolean.class) || setterType.isInstance(Boolean.class)) {
					origMethod = getMethod(origClass, getterName);
					// boolean 若無 getXxx,則換成isXxx,再找一次
					if (origMethod == null) {
						getterName = "is" + setterName.substring(3);
					}
				}
				//
				origMethod = getMethod(origClass, getterName);
				if (origMethod != null && setterType.equals(origMethod.getReturnType())) {
					// 取得getter value
					Object origValue = invokeMethod(orig, origMethod, null);
					if (origValue != null) {
						// 設定 setter value
						invokeMethod(dest, destMethod, new Object[] { origValue });
					}
				}
			}
		}
	}

	// 2011/12/12
	// 只有extend superClass會成立,若implement superClass 則不成立
	// clazz是否為superClass的子類
	public static boolean isSubClassOf(Class<?> superClass, Class<?> clazz) {
		boolean ret = false;
		if (clazz != null) {
			clazz = clazz.getSuperclass();
			// clazz = ___getSuperClassAndAdd(clazz);
			if (clazz != null) {
				if (clazz.equals(superClass)) {
					ret = true;
				} else {
					ret = isSubClassOf(superClass, clazz);
				}
			}
		}
		return ret;
	}

	public static Class<?>[] getInterfacesAndCache(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		Class<?>[] result = null;
		try {
			getInterfacesAndCache.lockInterruptibly();
			try {
				if (getInterfacesAndCache.isNotNullValue(clazz)) {
					result = getInterfacesAndCache.get(clazz);
					if (result == null) {
						try {
							result = clazz.getInterfaces();
						} catch (Exception ex) {
							// ex.printStackTrace();
						}
						getInterfacesAndCache.put(clazz, result);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				getInterfacesAndCache.unlock();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static Class<?>[] getInterfaces(Object value) {
		return (value != null ? getInterfaces(value.getClass()) : new Class[0]);
	}

	// 1.實作類別本身class,及super class 的interface
	// 2.interface本身class,及super class 的interface
	public static Class<?>[] getInterfaces(Class<?> clazz) {
		Set<Class<?>> ret = new LinkedHashSet<Class<?>>();
		if (clazz != null) {
			if (clazz.isInterface()) {
				Class<?>[] interfaces = getInterfacesByExtend(clazz);
				for (Class<?> interfaceClass : interfaces) {
					ret.add(interfaceClass);
				}
			} else {
				while (clazz != null) {
					// get from cache
					Class<?>[] interfaces = getInterfacesAndCache(clazz);
					for (Class<?> interfaceClass : interfaces) {
						ret.add(interfaceClass);
						// interfaceClasses =
						// ArrayHelper.uniqueJoin(Class[].class,
						// interfaceClasses,
						// new Class[] { interfaceClass });
					}
					clazz = clazz.getSuperclass();
				}
			}
		}
		return (Class<?>[]) ret.toArray(new Class[ret.size()]);
	}

	// 當interface有extends其他多個interface時
	public static Class<?>[] getInterfacesByExtend(Class<?> interfaceClass) {
		Set<Class<?>> ret = new LinkedHashSet<Class<?>>();
		if (interfaceClass != null && interfaceClass.isInterface()) {
			Class<?>[] interfaces = getInterfacesAndCache(interfaceClass);
			for (Class<?> entryInterface : interfaces) {
				ret.add(entryInterface);
				Class<?>[] extendInterfaces = getInterfacesByExtend(entryInterface);
				for (Class<?> extendInterface : extendInterfaces) {
					ret.add(extendInterface);
				}
			}
		}
		return (Class<?>[]) ret.toArray(new Class[ret.size()]);
	}

	// clazz是否有implement interfaceClass
	public static boolean isInterfaceOf(Class<?> interfaceClass, Class<?> clazz) {
		boolean ret = false;
		Class<?>[] interfaces = getInterfaces(clazz);
		ret = ArrayHelper.contains(interfaces, interfaceClass);
		return ret;
	}

	// ------------------------------------------------------------
	public static boolean isPoClass(Object value) {
		return (value != null ? isPoClass(value.getClass()) : false);
	}

	// BaseEntity 表po
	public static boolean isPoClass(Class<?> clazz) {
		boolean ret = false;
		if (clazz != null) {
			ret = ClassHelper.isInterfaceOf(BaseEntity.class, clazz);
		}
		return ret;
	}

	public static boolean isVoClass(Object value) {
		return (value != null ? isVoClass(value.getClass()) : false);
	}

	// BaseBean 表vo
	public static boolean isVoClass(Class<?> clazz) {
		boolean ret = false;
		if (clazz != null) {
			ret = ClassHelper.isInterfaceOf(BaseBean.class, clazz);
		}
		return ret;
	}

	// ------------------------------------------------------------
	protected static boolean isFcEntityClass(Object value) {
		return (value != null ? isFcEntityClass(value.getClass()) : false);
	}

	// org.openyu.commons.entity.AuditEntity
	// org.openyu.commons.entity.supporter.AuditEntitySupporter
	protected static boolean isFcEntityClass(Class<?> clazz) {
		boolean ret = false;
		if (clazz != null) {
			StringBuilder sb = new StringBuilder(clazz.getName());
			ret = sb.indexOf("org.openyu.commons.entity") > -1;
		}
		return ret;
	}

	protected static boolean isFcBeanClass(Object value) {
		return (value != null ? isFcBeanClass(value.getClass()) : false);
	}

	// org.openyu.commons.bean.AuditBean
	// org.openyu.commons.bean.supporter.AuditBeanSupporter
	protected static boolean isFcBeanClass(Class<?> clazz) {
		boolean ret = false;
		if (clazz != null) {
			StringBuilder sb = new StringBuilder(clazz.getName());
			ret = sb.indexOf("org.openyu.commons.bean") > -1;
		}
		return ret;
	}

	// ------------------------------------------------------------
	public static Class<?> po2VoClass(Object poEntity) {
		return (poEntity != null ? po2VoClass(poEntity.getClass()) : null);
	}

	// po->vo, 使用慣例轉換
	// org.openyu.adm.authz.user.po.impl.UserPoImpl
	// org.openyu.adm.authz.user.vo.impl.UserImpl
	public static Class<?> po2VoClass(Class<?> poClass) {
		Class<?> clazz = null;
		if (poClass != null) {
			// #issus: 有點慢,可以再改進
			//
			// #fix: 改isSfcEntityClass先判斷
			if (isFcEntityClass(poClass)) {
				// bean->entity
				clazz = toClass(poClass, ".entity.supporter", ".bean.supporter", "Entity", "Bean");
			} else {
				// 規則1
				clazz = toClass(poClass, ".po.impl", ".vo.impl", "PoImpl", "Impl");
				// #issue 多加其他條件判斷
				if (clazz == null) {
					// #fix
					// 規則2
					clazz = toClass(poClass, ".po", ".vo", "Po", "");
				}
			}
		}
		return clazz;
	}

	public static Class<?> vo2PoClass(Object voEntity) {
		return (voEntity != null ? vo2PoClass(voEntity.getClass()) : null);
	}

	// vo->po, 使用慣例轉換
	// org.openyu.adm.authz.user.vo.impl.UserImpl
	// org.openyu.adm.authz.user.po.impl.UserPoImpl
	//
	// bean->audit 內部再用的
	public static Class<?> vo2PoClass(Class<?> voClass) {
		Class<?> clazz = null;
		if (voClass != null) {
			// #issus: 有點慢,可以再改進
			// #fix: 改isSfcBeanClass先判斷
			if (isFcBeanClass(voClass)) {
				// bean->entity
				clazz = toClass(voClass, ".bean.supporter", ".entity.supporter", "Bean", "Entity");
			} else {
				// 規則1
				clazz = toClass(voClass, ".vo.impl", ".po.impl", "Impl", "PoImpl");
				// #issue 多加其他條件判斷
				if (clazz == null) {
					// #fix
					// 規則2
					clazz = toClass(voClass, ".vo", ".po", "", "Po");
				}
			}
		}
		return clazz;
	}

	protected static Class<?> toClass(Class<?> fromClass, String packExp, String replacePackExp, String nameExp,
			String replaceNameExp) {
		Class<?> clazz = null;
		// String packExp = ".vo.impl";
		// String nameExp = "Impl";
		StringBuilder toClassName = new StringBuilder(fromClass.getName());
		// vo->po
		int packPos = toClassName.indexOf(packExp);
		// System.out.println("packPos: " + packPos);
		if (packPos > -1) {
			toClassName.replace(packPos, packPos + packExp.length(), replacePackExp);
			int namePos = toClassName.indexOf(nameExp);
			// System.out.println("namePos: " + namePos);

			// org.openyu.commons.vo.Dog -> org.openyu.commons.Po.DogPo
			if (namePos == 0) {
				toClassName.append(replaceNameExp);
				// System.out.println(toClassName);
				clazz = ClassHelper.forName(toClassName.toString());
			}
			// org.openyu.commons.vo.impl.CatImpl ->
			// org.openyu.commons.po.impl.CatPoImpl
			else if (namePos > -1) {

				toClassName.replace(namePos, namePos + nameExp.length(), replaceNameExp);
				// System.out.println(toClassName);
				clazz = ClassHelper.forName(toClassName.toString());
			}
		}
		return clazz;
	}

	public static String setterName(String fieldName) {
		StringBuilder result = new StringBuilder();
		if (fieldName != null) {
			result.append("set");
			result.append(StringHelper.capitalize(fieldName));
		}

		return result.length() != 0 ? result.toString() : null;
	}

	public static String getterName(String fieldName) {
		StringBuilder result = new StringBuilder();
		if (fieldName != null) {
			result.append("get");
			result.append(StringHelper.capitalize(fieldName));
		}

		return result.length() != 0 ? result.toString() : null;
	}

	/**
	 * 取得列舉常數
	 * 
	 * 減少資源消耗, 2014/10/13
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] getEnumConstantsAndCache(Class<T> clazz) {
		T[] result = null;
		try {
			getEnumConstantsAndCache.lockInterruptibly();
			try {
				if (getEnumConstantsAndCache.isNotNullValue(clazz)) {
					result = (T[]) getEnumConstantsAndCache.get(clazz);
					if (result == null) {
						try {
							result = clazz.getEnumConstants();
						} catch (Exception ex) {
							// ex.printStackTrace();
						}
						getEnumConstantsAndCache.put(clazz, result);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				getEnumConstantsAndCache.unlock();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static Constructor<?> getConstructor(Class<?> clazz) {
		return getConstructor(clazz, (Class[]) null);
	}

	// getConstructors 自身的public方法和super class的public方法
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>[] parameterTypes) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		Constructor<?> constructor = null;
		try {
			Constructor<?>[] constructors = getConstructorsAndCache(clazz);
			for (Constructor<?> entryConstructor : constructors) {
				Class<?>[] entryParameterTypes = getConstructorParameterTypesAndCache(clazz, entryConstructor);
				if (entryParameterTypes.length == 0 && parameterTypes == null) {
					constructor = entryConstructor;
					break;
				} else {
					if (ObjectHelper.equals(entryParameterTypes, parameterTypes)) {
						constructor = entryConstructor;
						break;
					}
				}
			}
			//
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return constructor;
	}

	public static Constructor<?> getDeclaredConstructor(Class<?> clazz) {
		return getDeclaredConstructor(clazz, (Class[]) null);
	}

	// getDeclaredConstructors 自身的public、protected、private方法
	// #fix:當本身找不到,先判斷自身是否為interface,若是往super interface class尋找
	// #fix:若自身不為interface,則往super class 尋找
	public static Constructor<?> getDeclaredConstructor(Class<?> clazz, Class<?>[] parameterTypes) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		Constructor<?> constructor = null;
		try {
			// 找不到時會花費很多時間
			// method = clazz.getDeclaredConstructor( parameterTypes);

			// #fix
			Constructor<?>[] constructors = getDeclaredConstructorsAndCache(clazz);
			for (Constructor<?> entryConstructor : constructors) {
				Class<?>[] entryParameterTypes = getConstructorParameterTypesAndCache(clazz, entryConstructor);
				// System.out.println(parameterTypeKey + " " + methodName);
				// 沒有params的method
				if (entryParameterTypes.length == 0 && parameterTypes == null) {
					constructor = entryConstructor;
					break;
				} else {
					if (ObjectHelper.equals(entryParameterTypes, parameterTypes)) {
						constructor = entryConstructor;
						break;
					}
				}
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return constructor;
	}

	public static Constructor<?>[] getConstructorsAndCache(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		Constructor<?>[] constructors = new Constructor<?>[0];
		if (clazz != null) {
			try {
				getConstructorsAndCache.lockInterruptibly();
				try {
					if (getConstructorsAndCache.isNotNullValue(clazz)) {
						constructors = getConstructorsAndCache.get(clazz);
						if (constructors == null) {
							try {
								constructors = clazz.getConstructors();
								// 2015/10/07 for not public
								for (Constructor<?> constructor : constructors) {
									constructor.setAccessible(true);
								}
							} catch (Exception ex) {
								// ex.printStackTrace();
							}
							getConstructorsAndCache.put(clazz, constructors);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					getConstructorsAndCache.unlock();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		return constructors;
	}

	public static Constructor<?>[] getDeclaredConstructorsAndCache(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		Constructor<?>[] constructors = new Constructor<?>[0];
		if (clazz != null) {
			try {
				getDeclaredConstructorsAndCache.lockInterruptibly();
				try {
					if (getDeclaredConstructorsAndCache.isNotNullValue(clazz)) {
						constructors = getDeclaredConstructorsAndCache.get(clazz);
						if (constructors == null) {
							try {
								constructors = clazz.getDeclaredConstructors();
								// 2015/10/07 for not public
								for (Constructor<?> constructor : constructors) {
									constructor.setAccessible(true);
								}
							} catch (Exception ex) {
								// ex.printStackTrace();
							}
							getDeclaredConstructorsAndCache.put(clazz, constructors);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					getDeclaredConstructorsAndCache.unlock();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		return constructors;
	}

	public static Class<?>[] getConstructorParameterTypesAndCache(Class<?> clazz, Constructor<?> constructor) {
		AssertHelper.notNull(clazz, "The Class must not be null");
		AssertHelper.notNull(constructor, "The Constructor must not be null");
		//
		Class<?>[] parameterTypes = null;
		try {
			getConstructorParameterTypesAndCache.lockInterruptibly();
			try {
				MapCacheImpl<Constructor<?>, Class<?>[]> constructorParameterTypes = getConstructorParameterTypesAndCache
						.get(clazz);
				if (constructorParameterTypes == null) {
					constructorParameterTypes = new MapCacheImpl<Constructor<?>, Class<?>[]>();
					getConstructorParameterTypesAndCache.put(clazz, constructorParameterTypes);
				}
				// System.out.println(constructor);
				//
				if (constructor != null) {
					if (constructorParameterTypes.isNotNullValue(constructor)) {
						parameterTypes = constructorParameterTypes.get(constructor);
						if (parameterTypes == null) {
							try {
								parameterTypes = constructor.getParameterTypes();
							} catch (Exception ex) {
								// ex.printStackTrace();
							}
							constructorParameterTypes.put(constructor, parameterTypes);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				getConstructorParameterTypesAndCache.unlock();
			}

		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return parameterTypes;
	}

	// public static Constructor<?> getConstructor(Class<?> clazz, Class<?>...
	// parameterTypes) {
	// AssertHelper.notNull(clazz, "The Class must not be null");
	// //
	// Constructor<?> result = null;
	// try {
	// if (clazz != null) {
	// result = clazz.getConstructor(parameterTypes);
	// result.setAccessible(true);
	// }
	//
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// return result;
	// }

}
