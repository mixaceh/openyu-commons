package org.openyu.commons.entity.useraype;

import java.sql.Types;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.openyu.commons.entity.LocaleNameEntity;
import org.openyu.commons.entity.supporter.LocaleNameEntitySupporter;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.hibernate.useraype.supporter.BaseUserTypeSupporter;
import org.openyu.commons.lang.ArrayHelper;

public class NamesEntityUserType extends BaseUserTypeSupporter {

	private static final long serialVersionUID = -2066924784420555409L;

	public NamesEntityUserType() {
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
		return Set.class;
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
		if (!(value instanceof Set)) {
			return result;
		}
		//
		StringBuilder dest = new StringBuilder();
		@SuppressWarnings("rawtypes")
		Set<LocaleNameEntity> src = (Set) value;
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
	 * 2♦zh_TW♣測試角色♦en_US♣Test Role
	 * 
	 * @param src
	 * @return
	 */
	public String assembleBy_1(Set<LocaleNameEntity> src) {
		StringBuilder result = new StringBuilder();
		result.append((src != null ? src.size() : 0));// 0
		for (LocaleNameEntity entry : src) {
			result.append(OBJECT_SPLITTER);
			result.append(toString(entry.getLocale()));// e0
			result.append(ENTRY_SPLITTER);
			result.append(toString(entry.getName()));// e1
		}
		return result.toString();
	}

	// --------------------------------------------------
	// disassemble
	// --------------------------------------------------
	/**
	 * 由欄位反組成物件
	 */
	@SuppressWarnings("unchecked")
	public <R, T, O> R unmarshal(T value, O owner, SessionImplementor session) {
		// 預設傳回空物件,非null
		Set<LocaleNameEntity> result = new LinkedHashSet<LocaleNameEntity>();
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

	/**
	 * v1 由欄位反組成物件
	 */
	public Set<LocaleNameEntity> disassembleBy_1(StringBuilder src) {
		Set<LocaleNameEntity> result = new LinkedHashSet<LocaleNameEntity>();
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
		int size = toObject(values, idx++, int.class);
		//
		for (int i = 0; i < size; i++) {
			String eValue = ArrayHelper.get(values, idx++);
			String[] entryValues = StringUtils.splitPreserveAllTokens(eValue,
					ENTRY_SPLITTER);
			if (ArrayHelper.isEmpty(entryValues)) {
				continue;
			}
			int edx = 0;
			LocaleNameEntity entry = new LocaleNameEntitySupporter();
			//
			entry.setLocale(toObject(entryValues, edx++, Locale.class));// e0
			entry.setName(toObject(entryValues, edx++, String.class));// e1
			result.add(entry);
		}
		return result;
	}
}
