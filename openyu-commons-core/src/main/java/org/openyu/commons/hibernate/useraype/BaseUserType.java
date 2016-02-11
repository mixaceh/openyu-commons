package org.openyu.commons.hibernate.useraype;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.EnhancedUserType;
import org.openyu.commons.enumz.IntEnum;

/**
 * UserType
 * 
 * +-EnhancedUserType 多了 objectToSQLString,toXMLString,fromXMLString
 * 
 * CompositeUserType,也可用但尚未測試
 * 
 * upgrade to hibernate4 2014/09/23
 */
public interface BaseUserType extends Serializable, EnhancedUserType {
	/**
	 * 版本號
	 * 
	 * 反解時,依照不同版本,組成物件
	 * 
	 * 欄位結構版本控管,♥1~♥30,版號最多30
	 * 
	 */
	public enum VolType implements IntEnum {
		_1(1), _2(2), _3(3), _4(4), _5(5),

		_6(6), _7(7), _8(8), _9(9), _10(10),

		//
		_11(11), _12(12), _13(13), _14(14), _15(15),

		_16(16), _17(17), _18(18), _19(19), _20(20),

		//
		_21(21), _22(22), _23(23), _24(24), _25(25),

		_26(26), _27(27), _28(28), _29(29), _30(30),

		;
		private final int intValue;

		private VolType(int intValue) {
			this.intValue = intValue;
		}

		public int getValue() {
			return intValue;
		}
	}

	/**
	 * 版本號
	 * 
	 * @return
	 */
	VolType getVolType();

	void setVolType(VolType volType);

	/**
	 * 由物件組成欄位
	 * 
	 * 原則:
	 * 
	 * 1.若物件為null,則轉為空字串""
	 */
	<R, T> R marshal(T value, SessionImplementor session);

	/**
	 * 由欄位反組成物件
	 * 
	 * 原則:
	 * 
	 * 1.若字串為null,則建構新物件,只是物件中field為null
	 */
	<R, T, O> R unmarshal(T value, O owner, SessionImplementor session);
}
