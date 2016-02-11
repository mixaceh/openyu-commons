package org.openyu.commons.enumz;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.BooleanHelper;
import org.openyu.commons.lang.CharHelper;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.StringHelper;

/**
 * Enum輔助類
 */
public final class EnumHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(EnumHelper.class);

	private EnumHelper() {
		throw new HelperException(
				new StringBuilder().append(EnumHelper.class.getName()).append(" can not construct").toString());
	}

	/**
	 * 依boolean取得enum.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @param value
	 *            the value
	 * @return the t
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, boolean value) {
		T result = null;
		//
		T[] constants = values(enumType);
		for (T entry : constants) {
			if (entry instanceof BooleanEnum) {
				BooleanEnum enumz = (BooleanEnum) entry;
				if (enumz.getValue() == value) {
					result = (T) entry;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 依char取得enum.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @param value
	 *            the value
	 * @return the t
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, char value) {
		T result = null;
		//
		T[] constants = values(enumType);
		for (T entry : constants) {
			if (entry instanceof CharEnum) {
				CharEnum enumz = (CharEnum) entry;
				if (enumz.getValue() == value) {
					result = (T) entry;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 依String取得enum.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @param value
	 *            the value
	 * @return the t
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, String value) {
		T result = null;
		//
		T[] constants = values(enumType);
		for (T entry : constants) {
			if (entry instanceof StringEnum) {
				StringEnum enumz = (StringEnum) entry;
				if (enumz.getValue().equals(value)) {
					result = (T) entry;
					break;
				}
			}
			// 或用名稱判斷
			if (value != null && entry.name().equals(value)) {
				result = (T) entry;
				break;
			}

		}
		return result;
	}

	/**
	 * 依byte取得enum.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @param value
	 *            the value
	 * @return the t
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, byte value) {
		T result = null;
		//
		// TODO isInterfaceOf
		// System.out.println(ClassHelper.isInterfaceOf(ByteEnum.class,
		// enumType));
		T[] constants = values(enumType);
		for (T entry : constants) {
			if (entry instanceof ByteEnum) {
				ByteEnum enumz = (ByteEnum) entry;
				if (enumz.getValue() == value) {
					result = (T) entry;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 依short取得enum.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @param value
	 *            the value
	 * @return the t
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, short value) {
		T result = null;
		//
		T[] constants = values(enumType);
		for (T entry : constants) {
			if (entry instanceof ShortEnum) {
				ShortEnum enumz = (ShortEnum) entry;
				if (enumz.getValue() == value) {
					result = (T) entry;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 依int取得enum.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @param value
	 *            the value
	 * @return the t
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, int value) {
		T result = null;
		// #issue 耗資源
		// T[] constants = enumType.getEnumConstants();

		// #fix 改為cache
		T[] constants = values(enumType);
		for (T entry : constants) {
			if (entry instanceof IntEnum) {
				IntEnum enumz = (IntEnum) entry;
				if (enumz.getValue() == value) {
					result = (T) entry;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 依long取得enum.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @param value
	 *            the value
	 * @return the t
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, long value) {
		T result = null;
		//
		T[] constants = values(enumType);
		for (T entry : constants) {
			if (entry instanceof LongEnum) {
				LongEnum enumz = (LongEnum) entry;
				if (enumz.getValue() == value) {
					result = (T) entry;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 依float取得enum.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @param value
	 *            the value
	 * @return the t
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, float value) {
		T result = null;
		//
		T[] constants = values(enumType);
		for (T entry : constants) {
			if (entry instanceof FloatEnum) {
				FloatEnum enumz = (FloatEnum) entry;
				if (enumz.getValue() == value) {
					result = (T) entry;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 依double取得enum.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @param value
	 *            the value
	 * @return the t
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, double value) {
		T result = null;
		//
		T[] constants = values(enumType);
		for (T entry : constants) {
			if (entry instanceof DoubleEnum) {
				DoubleEnum enumz = (DoubleEnum) entry;
				if (enumz.getValue() == value) {
					result = (T) entry;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 依name取得enum.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @param name
	 *            the name
	 * @return the t
	 */
	public static <T extends Enum<T>> T nameOf(Class<T> enumType, String name) {
		return Enum.valueOf(enumType, name);
	}

	/**
	 * 判斷列舉的常數,是否唯一
	 *
	 * 唯一,傳回size=0
	 *
	 * 重複,傳回重複的常數.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @return the list
	 */
	@SuppressWarnings({ "rawtypes" })
	public static <T extends Enum<T>> List<T> checkDuplicate(Class<T> enumType) {
		List<T> result = new LinkedList<T>();
		//
		Set buffs = new LinkedHashSet();
		T[] constants = values(enumType);
		for (T entry : constants) {
			// BooleanEnum
			if (entry instanceof BooleanEnum) {
				BooleanEnum enumz = (BooleanEnum) entry;
				processDuplicate(enumz.getValue(), entry, buffs, result);
			}
			// CharEnum
			else if (entry instanceof CharEnum) {
				CharEnum enumz = (CharEnum) entry;
				processDuplicate(enumz.getValue(), entry, buffs, result);
			}
			// StringEnum
			else if (entry instanceof StringEnum) {
				StringEnum enumz = (StringEnum) entry;
				processDuplicate(enumz.getValue(), entry, buffs, result);
			}
			// ByteEnum
			else if (entry instanceof ByteEnum) {
				ByteEnum enumz = (ByteEnum) entry;
				processDuplicate(enumz.getValue(), entry, buffs, result);
			}
			// ShortEnum
			else if (entry instanceof ShortEnum) {
				ShortEnum enumz = (ShortEnum) entry;
				processDuplicate(enumz.getValue(), entry, buffs, result);
			}
			// IntEnum
			else if (entry instanceof IntEnum) {
				IntEnum enumz = (IntEnum) entry;
				processDuplicate(enumz.getValue(), entry, buffs, result);
			}
			// LongEnum
			else if (entry instanceof LongEnum) {
				LongEnum enumz = (LongEnum) entry;
				processDuplicate(enumz.getValue(), entry, buffs, result);
			}
			// FloatEnum
			else if (entry instanceof FloatEnum) {
				FloatEnum enumz = (FloatEnum) entry;
				processDuplicate(enumz.getValue(), entry, buffs, result);
			}
			// DoubleEnum
			else if (entry instanceof DoubleEnum) {
				DoubleEnum enumz = (DoubleEnum) entry;
				processDuplicate(enumz.getValue(), entry, buffs, result);
			}
		}
		return result;
	}

	/**
	 * 處理唯一, 把重複的放到list上.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @param entry
	 *            the entry
	 * @param buffs
	 *            the buffs
	 * @param duplicates
	 *            the duplicates
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static <T extends Enum<T>> void processDuplicate(Object value, T entry, Set buffs, List<T> duplicates) {
		if (!buffs.contains(value)) {
			buffs.add(value);
		} else {
			duplicates.add(entry);
		}
	}

	/**
	 * 列舉常數累計加總.
	 *
	 * @param <T>
	 *            the generic type
	 * @param values
	 *            the values
	 * @return the double
	 */
	public static <T extends Enum<T>> double sumOf(Collection<T> values) {
		double result = 0d;
		if (values != null) {
			for (T entry : values) {
				result = NumberHelper.add(result, doubleValue(entry));
			}
		}
		return result;
	}

	/**
	 * 列舉常數累計加總.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @return the double
	 */
	public static <T extends Enum<T>> double sumOf(Class<T> enumType) {
		double result = 0d;
		if (enumType != null) {
			T[] constants = values(enumType);
			for (T entry : constants) {
				result = NumberHelper.add(result, doubleValue(entry));
			}
		}
		return result;
	}

	/**
	 * 取得列舉的doubleValue.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @return the double
	 */
	protected static <T extends Enum<T>> double doubleValue(T value) {
		double result = 0d;
		// BooleanEnum
		if (value instanceof BooleanEnum) {
			BooleanEnum enumz = (BooleanEnum) value;
			result = (enumz.getValue() ? 1 : 0);
		}
		// CharEnum
		else if (value instanceof CharEnum) {
			CharEnum enumz = (CharEnum) value;
			result = enumz.getValue();
		}
		// ByteEnum
		if (value instanceof ByteEnum) {
			ByteEnum enumz = (ByteEnum) value;
			result = enumz.getValue();
		}
		// ShortEnum
		else if (value instanceof ShortEnum) {
			ShortEnum enumz = (ShortEnum) value;
			result = enumz.getValue();
		}
		// IntEnum
		else if (value instanceof IntEnum) {
			IntEnum enumz = (IntEnum) value;
			result = enumz.getValue();
		}
		// LongEnum
		else if (value instanceof LongEnum) {
			LongEnum enumz = (LongEnum) value;
			result = enumz.getValue();
		}
		// FloatEnum
		else if (value instanceof FloatEnum) {
			FloatEnum enumz = (FloatEnum) value;
			result = enumz.getValue();
		}
		// DoubleEnum
		else if (value instanceof DoubleEnum) {
			DoubleEnum enumz = (DoubleEnum) value;
			result = enumz.getValue();
		}
		return result;
	}

	/**
	 * 隨機取得列舉元素.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @return the t
	 */
	public static <T extends Enum<T>> T randomType(Class<T> enumType) {
		T result = null;
		//
		if (enumType != null) {
			T[] constants = values(enumType);
			int index = NumberHelper.randomInt(0, constants.length);
			result = constants[index];
		}
		return result;
	}

	/**
	 * 取得所有列舉元素.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @return the t[]
	 */
	public static <T extends Enum<T>> T[] values(Class<T> enumType) {
		T[] result = null;
		//
		if (enumType != null) {
			// result = enumType.getEnumConstants();
			result = ClassHelper.getEnumConstantsAndCache(enumType);
		}
		return result;
	}

	/**
	 * 取得所有列舉元素, list.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @return the list
	 */
	public static <T extends Enum<T>> List<T> valuesList(Class<T> enumType) {
		List<T> result = new LinkedList<T>();
		//
		if (enumType != null) {
			// result = Arrays.asList(enumType.getEnumConstants());
			result = Arrays.asList(values(enumType));
		}
		return result;
	}

	/**
	 * 取得所有列舉元素, set.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumType
	 *            the enum type
	 * @return the sets the
	 */
	public static <T extends Enum<T>> Set<T> valuesSet(Class<T> enumType) {
		return new LinkedHashSet<T>(valuesList(enumType));
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public static boolean safeGet(BooleanEnum value) {
		return (value != null ? value.getValue() : BooleanHelper.DEFAULT_VALUE);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the char
	 */
	public static char safeGet(CharEnum value) {
		return (value != null ? value.getValue() : CharHelper.DEFAULT_VALUE);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String safeGet(StringEnum value) {
		return (value != null ? value.getValue() : StringHelper.DEFAULT_VALUE);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the byte
	 */
	public static byte safeGet(ByteEnum value) {
		return (value != null ? value.getValue() : NumberHelper.DEFAULT_BYTE);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the short
	 */
	public static short safeGet(ShortEnum value) {
		return (value != null ? value.getValue() : NumberHelper.DEFAULT_SHORT);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static int safeGet(IntEnum value) {
		return (value != null ? value.getValue() : NumberHelper.DEFAULT_INT);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the long
	 */
	public static long safeGet(LongEnum value) {
		return (value != null ? value.getValue() : NumberHelper.DEFAULT_LONG);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the float
	 */
	public static float safeGet(FloatEnum value) {
		return (value != null ? value.getValue() : NumberHelper.DEFAULT_FLOAT);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the double
	 */
	public static double safeGet(DoubleEnum value) {
		return (value != null ? value.getValue() : NumberHelper.DEFAULT_DOUBLE);
	}

}
