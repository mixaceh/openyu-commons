package org.openyu.commons.entity.userType;

import java.sql.Types;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.openyu.commons.entity.AuditEntity;
import org.openyu.commons.entity.supporter.AuditEntitySupporter;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.hibernate.userType.supporter.BaseUserTypeSupporter;
import org.openyu.commons.lang.ArrayHelper;

public class AuditEntityUserType extends BaseUserTypeSupporter {

	private static final long serialVersionUID = -2066924784420555409L;

	public AuditEntityUserType() {
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
		return AuditEntitySupporter.class;
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
		if (!(value instanceof AuditEntity)) {
			return result;
		}
		//
		StringBuilder dest = new StringBuilder();
		AuditEntity src = (AuditEntity) value;
		// vol
		dest.append(assembleVol(getVolType()));
		// v1
		dest.append(assembleBy_1(src));
		//
		result = (R) dest.toString();
		//
		return result;
	}

	/**
	 * v1 由物件組成欄位
	 * 
	 * @param src
	 * @return
	 */
	public String assembleBy_1(AuditEntity src) {
		StringBuilder result = new StringBuilder();
		//
		result.append(toString(src.getCreateUser()));// 0
		result.append(SPLITTER);
		result.append(toString(src.getCreateDate()));// 1
		result.append(SPLITTER);
		result.append(toString(src.getModifiedUser()));// 2
		result.append(SPLITTER);
		result.append(toString(src.getModifiedDate()));// 3
		//
		return result.toString();
	}

	// --------------------------------------------------

	/**
	 * 由欄位反組成物件
	 */
	@SuppressWarnings("unchecked")
	public <R, T, O> R unmarshal(T value, O owner, SessionImplementor session) {
		// 預設傳回空物件,非null
		AuditEntity result = new AuditEntitySupporter();
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

	public AuditEntity disassembleBy_1(StringBuilder src) {
		AuditEntity result = new AuditEntitySupporter();
		//
		if (src == null) {
			return result;
		}
		//
		// 不用這個, //改用StringUtils.splitPreserveAllTokens
		// String[] values = src.toString().split(SPLITTER);
		String[] values = StringUtils.splitPreserveAllTokens(src.toString(),
				SPLITTER);
		if (ArrayHelper.isEmpty(values)) {
			return result;
		}
		//
		int idx = 0;
		result.setCreateUser(toObject(values, idx++, String.class));// 0
		result.setCreateDate(toObject(values, idx++, Date.class));// 1
		result.setModifiedUser(toObject(values, idx++, String.class));// 2
		result.setModifiedDate(toObject(values, idx++, Date.class));// 3
		return result;
	}
}
