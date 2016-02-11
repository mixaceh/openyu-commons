package org.openyu.commons.hibernate.useraype.supporter;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.openyu.commons.enumz.BooleanEnum;
import org.openyu.commons.enumz.ByteEnum;
import org.openyu.commons.enumz.CharEnum;
import org.openyu.commons.enumz.DoubleEnum;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.enumz.FloatEnum;
import org.openyu.commons.enumz.IntEnum;
import org.openyu.commons.enumz.LongEnum;
import org.openyu.commons.enumz.ShortEnum;
import org.openyu.commons.enumz.StringEnum;
import org.openyu.commons.hibernate.useraype.BaseUserType;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.lang.BooleanHelper;
import org.openyu.commons.lang.CharHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.ObjectHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.util.LocaleHelper;
import org.openyu.commons.util.concurrent.MapCache;
import org.openyu.commons.util.concurrent.impl.MapCacheImpl;

public abstract class BaseUserTypeSupporter implements BaseUserType, Supporter {

	private static final long serialVersionUID = 1468584587910633195L;

	/**
	 * 版本號分割 ♥
	 */
	protected static final String VOL_SPLITTER = StringHelper.HEART;

	/**
	 * 欄位分割 ♠
	 */
	protected static final String SPLITTER = StringHelper.SPADE;

	/**
	 * 物件分割 ♦
	 */
	protected static final String OBJECT_SPLITTER = StringHelper.DIAMOND;

	/**
	 * entry分割 ♣
	 */
	protected static final String ENTRY_SPLITTER = StringHelper.CLUB;

	/**
	 * 上層物件分割
	 */
	protected static final String PARENT_OBJECT_SPLITTER = StringHelper.CIRCLE_D;

	/**
	 * dd 上層entry分割
	 */
	protected static final String PARENT_ENTRY_SPLITTER = StringHelper.CIRCLE_C;

	/**
	 * 版本號
	 */
	private VolType volType;

	/**
	 * <1,Enum>
	 */
	private MapCache<String, Enum<?>> valueOfIntCache = new MapCacheImpl<String, Enum<?>>();

	public BaseUserTypeSupporter() {
	}

	public VolType getVolType() {
		return volType;
	}

	public void setVolType(VolType volType) {
		this.volType = volType;
	}

	// 該自訂物件類型所對應的SQL資料的類型。
	public int[] sqlTypes() {
		return new int[] { Types.CLOB };
	}

	public Class<?> returnedClass() {
		return null;
	}

	// 用於設定我們的類是不是可變的。
	public boolean isMutable() {
		return false;
	}

	// 該方法用於提供自訂類型的完全的複製的方法。它主要用於構造返回對象。
	// 當nullSafeGet獲取物件後，將會調用這個方法。進行複製一個完全相同的拷貝。
	// 然後把這個拷貝返回給使用者。
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	// 這是自訂的資料的對比方法。如果返回的是false則hibernate認為資料發生了變化
	// 將會把變化更新到庫的表之中。
	public boolean equals(Object x, Object y) throws HibernateException {
		// if (x != null && y != null)
		// {
		// if (assemble(x).equals(assemble(y)))
		// return true;
		// }
		// return false;
		return ObjectHelper.equals(x, y);
	}

	/**
	 * disassemble,db 2 object,若欄位為null,傳回非null物件
	 */
	// 從JDBC的ResultSet中獲取到資料，然後返回為相應的類型。
	// 其中names包含了要映射的欄位的名稱。
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		// String value = (String) Hibernate.STRING.nullSafeGet(rs, names[0]);
		String value = StandardBasicTypes.STRING.nullSafeGet(rs, names[0],
				session);
		Object result = unmarshal(value, owner, session);
		return result;
	}

	/**
	 * assemble,object 2 db
	 */
	// 這個方法將在資料保存時使用。本方法可以使用PreparedStatement將資料寫入對應的資料庫欄位中。
	// 其中的value表示的是要寫入的值。index表示的是在statement的參數中的index.
	public void nullSafeSet(PreparedStatement st, Object target, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		Object value = marshal(target, session);
		StandardBasicTypes.STRING.nullSafeSet(st, value, index, session);
	}

	public Object assemble(Serializable arg0, Object arg1)
			throws HibernateException {
		return null;
	}

	public Serializable disassemble(Object arg0) throws HibernateException {
		return null;
	}

	public int hashCode(Object arg0) throws HibernateException {
		return 0;
	}

	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	/**
	 * 組成vol字串
	 * 
	 * @param volType
	 * @return ♥1♠
	 */
	protected StringBuilder assembleVol(VolType volType) {
		StringBuilder result = new StringBuilder();
		if (volType != null) {
			result.append(VOL_SPLITTER);
			result.append(getVolType().getValue());
			result.append(SPLITTER);
		}
		return result;
	}

	/**
	 * 取得vol,並將已處理過的部分移除,傳回剩下尚未處理的部分
	 * 
	 * 原:♥1♠sys♠1331780303919♠♠
	 * 
	 * 取vol=1,之後會變為新的string
	 * 
	 * 新:sys♠1331780303919♠♠
	 * 
	 * @param value
	 * @return
	 */
	protected int disassembleVol(StringBuilder value) {
		int result = 0;
		//
		int volLength = VOL_SPLITTER.length();
		if (value != null && value.length() > volLength
				&& value.substring(0, volLength).equals(VOL_SPLITTER)) {
			int pos = value.indexOf(SPLITTER);
			if (pos > 0) {
				String vol = value.substring(volLength, pos);
				// value.delete(0, pos + SPLITTER.length());
				value.replace(0, pos + SPLITTER.length(), "");
				result = NumberHelper.toInt(vol);
			}
		}
		return result;
	}

	/**
	 * assemble常用
	 * 
	 * 1.將null轉成"", value==null, -> ""
	 * 
	 * 2.非null轉成string, value!=null, -> 依型別不同,轉成對應的string
	 * 
	 * 3.當disassemble時,記得依原本轉換的原則,反轉回來
	 * 
	 * 原則:
	 * 
	 * 1.Date -> long -> String
	 * 
	 * 2.Calendar -> long -> String
	 * 
	 * 3.Other -> String
	 * 
	 * @param value
	 * @return
	 */
	protected <T> String toString(T value) {
		String result = "";
		if (value != null) {
			if (value instanceof Date) {
				// 用string
				// result = DateHelper.toString((Date) value,
				// DateHelper.DATE_TIME_MILLS_PATTERN);

				// 用long
				result = String.valueOf((((Date) value).getTime()));
			} else if (value instanceof Calendar) {
				// 用string
				// result = CalendarHelper.toString((Calendar)
				// value,DateHelper.DATE_TIME_MILLS_PATTERN);

				// 用long
				result = String.valueOf(((Calendar) value).getTimeInMillis());
			}
			//
			else if (value instanceof BooleanEnum) {
				BooleanEnum buff = (BooleanEnum) value;
				result = BooleanHelper.toString(EnumHelper.safeGet(buff));
			} else if (value instanceof Boolean) {
				Boolean buff = (Boolean) value;
				result = BooleanHelper.toString(buff);
			}
			//
			else if (value instanceof CharEnum) {
				CharEnum buff = (CharEnum) value;
				result = CharHelper.toString(EnumHelper.safeGet(buff));
			} else if (value instanceof Character) {
				Character buff = (Character) value;
				result = CharHelper.toString(buff);
			}
			//
			else if (value instanceof StringEnum) {
				StringEnum buff = (StringEnum) value;
				result = EnumHelper.safeGet(buff);
			} else if (value instanceof String) {
				result = (String) value;
			}
			//
			else if (value instanceof ByteEnum) {
				ByteEnum buff = (ByteEnum) value;
				result = String.valueOf(EnumHelper.safeGet(buff));
			} else if (value instanceof Byte) {
				Byte buff = (Byte) value;
				result = String.valueOf(buff);
			}
			//
			else if (value instanceof ShortEnum) {
				ShortEnum buff = (ShortEnum) value;
				result = String.valueOf(EnumHelper.safeGet(buff));
			} else if (value instanceof Short) {
				Short buff = (Short) value;
				result = String.valueOf(buff);
			}
			//
			else if (value instanceof IntEnum) {
				IntEnum buff = (IntEnum) value;
				result = String.valueOf(EnumHelper.safeGet(buff));
			} else if (value instanceof Integer) {
				Integer buff = (Integer) value;
				result = String.valueOf(buff);
			}
			//
			else if (value instanceof LongEnum) {
				LongEnum buff = (LongEnum) value;
				result = String.valueOf(EnumHelper.safeGet(buff));
			} else if (value instanceof Long) {
				Long buff = (Long) value;
				result = String.valueOf(buff);
			}
			//
			else if (value instanceof FloatEnum) {
				FloatEnum buff = (FloatEnum) value;
				result = String.valueOf(EnumHelper.safeGet(buff));
			} else if (value instanceof Float) {
				Float buff = (Float) value;
				result = String.valueOf(buff);
			}
			//
			else if (value instanceof DoubleEnum) {
				DoubleEnum buff = (DoubleEnum) value;
				result = String.valueOf(EnumHelper.safeGet(buff));
			} else if (value instanceof Double) {
				Double buff = (Double) value;
				result = String.valueOf(buff);
			}
			//
			else {
				result = String.valueOf(value);
			}
		}
		return result;
	}

	protected <T extends Enum<T>> T toEnumByByte(String[] values, int index,
			Class<T> enumType) {
		byte byteValue = toObject(ArrayHelper.get(values, index), byte.class);
		return EnumHelper.valueOf(enumType, byteValue);
	}

	protected <T extends Enum<T>> T toEnumByInt(String[] values, int index,
			Class<T> enumType) {
		int intValue = toObject(ArrayHelper.get(values, index), int.class);
		return EnumHelper.valueOf(enumType, intValue);
	}

	protected <T extends Enum<T>> T toEnumByBouble(String[] values, int index,
			Class<T> enumType) {
		double doubleValue = toObject(ArrayHelper.get(values, index),
				double.class);
		return EnumHelper.valueOf(enumType, doubleValue);
	}

	protected <T extends Enum<T>> T toEnumByString(String[] values, int index,
			Class<T> enumType) {
		String stringValue = toObject(ArrayHelper.get(values, index),
				String.class);
		return EnumHelper.valueOf(enumType, stringValue);
	}

	protected <T extends Enum<T>> T toEnumByName(String[] values, int index,
			Class<T> enumType) {
		String stringValue = toObject(ArrayHelper.get(values, index),
				String.class);
		return EnumHelper.nameOf(enumType, stringValue);
	}

	protected <T> T toObject(String[] values, int index, Class<T> clazz) {
		return toObject(ArrayHelper.get(values, index), clazz);
	}

	/**
	 * disassemble常用
	 * 
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T toObject(String value, Class<T> clazz) {
		Object result = null;
		// 非空白才處理
		if (StringHelper.notBlank(value)) {
			if (String.class == clazz) {
				result = value;
			}
			// date,calendar
			else if (Date.class == clazz || Calendar.class == clazz) {
				// 用long
				long time = NumberHelper.toLong(value);
				result = new Date(time);
			} else if (Locale.class == clazz) {
				result = LocaleHelper.toLocale(value);
			}
			// boolean
			else if (boolean.class == clazz || Boolean.class == clazz) {
				result = BooleanHelper.toBoolean(value);
			}
			// char
			else if (char.class == clazz || Character.class == clazz) {
				result = CharHelper.toChar(value);
			}
			// number
			else if (byte.class == clazz || Byte.class == clazz) {
				result = NumberHelper.toByte(value);
			} else if (short.class == clazz || Short.class == clazz) {
				result = NumberHelper.toShort(value);
			} else if (int.class == clazz || Integer.class == clazz) {
				result = NumberHelper.toInt(value);
			} else if (long.class == clazz || Long.class == clazz) {
				result = NumberHelper.toLong(value);
			} else if (float.class == clazz || Float.class == clazz) {
				result = NumberHelper.toFloat(value);
			} else if (double.class == clazz || Double.class == clazz) {
				result = NumberHelper.toDouble(value);
			}
		}
		return (T) result;
	}

	public Object fromXMLString(String arg0) {
		return null;
	}

	public String objectToSQLString(Object arg0) {
		return null;
	}

	public String toXMLString(Object arg0) {
		return null;
	}

	// String -> int -> Enum
	@SuppressWarnings("unchecked")
	protected <T extends Enum<T>> T intValueOf(Class<T> enumType, String value) {
		T result = null;
		//
		try {
			valueOfIntCache.lockInterruptibly();
			try {
				String key = value;
				if (valueOfIntCache.isNotNullValue(key)) {
					result = (T) valueOfIntCache.get(key);
					if (result == null) {
						result = EnumHelper.valueOf(enumType,
								toObject(value, int.class));
						valueOfIntCache.put(key, result);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				valueOfIntCache.unlock();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return result;
	}

}
