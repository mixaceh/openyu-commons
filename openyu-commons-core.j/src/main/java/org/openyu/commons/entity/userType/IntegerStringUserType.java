package org.openyu.commons.entity.userType;

import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.hibernate.userType.supporter.BaseUserTypeSupporter;
import org.openyu.commons.lang.ArrayHelper;

/**
 * Map<Integer,String>
 */
public class IntegerStringUserType extends BaseUserTypeSupporter {

	private static final long serialVersionUID = -1446482401777717340L;

	public IntegerStringUserType() {
		// --------------------------------------------------
		// 最新版本,目前用1,若將來有新版本
		// 可用其他版號,如:VolType._2
		// --------------------------------------------------
		setVolType(VolType._1);
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { Types.VARCHAR };
	}

	@Override
	public Class<?> returnedClass() {
		return Map.class;
	}

	// --------------------------------------------------
	// assemble
	// --------------------------------------------------
	/**
	 * 由物件組成欄位
	 */
	@SuppressWarnings("unchecked")
	public <R, T> R marshal(T value, SessionImplementor session) {
		R result = null;
		if (!(value instanceof Map)) {
			return result;
		}
		//
		Map<Integer, String> src = (Map<Integer, String>) value;
		StringBuilder dest = new StringBuilder();
		// vol
		dest.append(assembleVol(getVolType()));
		// v1
		dest.append(assembleBy_1(src));
		//
		result = (R) dest.toString();
		return result;
	}

	/**
	 * v1 由物件組成欄位
	 * 
	 * @param src
	 * @return
	 */
	public String assembleBy_1(Map<Integer, String> src) {
		StringBuilder result = new StringBuilder();
		//
		result.append(src.size());
		for (Map.Entry<Integer, String> entry : src.entrySet()) {
			result.append(OBJECT_SPLITTER);
			// key
			result.append(toString(entry.getKey()));// e0
			result.append(ENTRY_SPLITTER);
			// value
			result.append(toString(entry.getValue()));// e1
		}
		//
		return result.toString();
	}

	// --------------------------------------------------
	// disassemble
	// --------------------------------------------------
	/**
	 * 反欄位組成物件
	 */
	@SuppressWarnings("unchecked")
	public <R, T, O> R unmarshal(T value, O owner, SessionImplementor session) {
		Map<Integer, String> result = new LinkedHashMap<Integer, String>();
		//
		if (!(value instanceof String)) {
			return (R) result;
		}
		//
		StringBuilder src = new StringBuilder((String) value);
		int vol = disassembleVol(src);
		VolType volType = EnumHelper.valueOf(VolType.class, vol);
		//
		if (volType == null) {
			return (R) result;
		}
		// v1
		if (volType.getValue() >= 1) {
			result = disassembleBy_1(src);
		}

		// v2,有新增的欄位,則繼續塞
		if (volType.getValue() >= 2) {

		}
		return (R) result;
	}

	public Map<Integer, String> disassembleBy_1(StringBuilder src) {
		Map<Integer, String> result = new LinkedHashMap<Integer, String>();
		if (src == null) {
			return result;
		}
		//
		String[] values = StringUtils.splitPreserveAllTokens(src.toString(),
				OBJECT_SPLITTER);
		if (ArrayHelper.isEmpty(values)) {
			return result;
		}
		//
		int idx = 0;
		int size = toObject(values, idx++, int.class);// 0
		//
		for (int i = 0; i < size; i++)// 1
		{
			String eValue = ArrayHelper.get(values, idx++);
			String[] entryValues = StringUtils.splitPreserveAllTokens(eValue,
					ENTRY_SPLITTER);
			if (ArrayHelper.isEmpty(entryValues)) {
				continue;
			}
			int edx = 0;
			Integer key = toObject(entryValues, edx++, Integer.class);
			String value = toObject(entryValues, edx++, String.class);
			result.put(key, value);
		}
		return result;
	}
}
