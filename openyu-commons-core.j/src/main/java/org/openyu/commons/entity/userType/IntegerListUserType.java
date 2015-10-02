package org.openyu.commons.entity.userType;

import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.hibernate.userType.supporter.BaseUserTypeSupporter;
import org.openyu.commons.lang.ArrayHelper;

/**
 * List<Integer>
 */
public class IntegerListUserType extends BaseUserTypeSupporter {

	private static final long serialVersionUID = -2066924784420555409L;

	public IntegerListUserType() {
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
		return List.class;
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
		if (!(value instanceof List)) {
			return result;
		}
		//
		List<Integer> src = (List<Integer>) value;
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
	public String assembleBy_1(List<Integer> src) {
		StringBuilder result = new StringBuilder();
		//
		result.append(src.size());
		for (Integer value : src) {
			result.append(OBJECT_SPLITTER);
			// value
			result.append(toString(value));// e0
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
		List<Integer> result = new LinkedList<Integer>();
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

	public List<Integer> disassembleBy_1(StringBuilder src) {
		List<Integer> result = new LinkedList<Integer>();
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
			Integer value = toObject(values, idx++, Integer.class);
			result.add(value);
		}
		return result;
	}
}
