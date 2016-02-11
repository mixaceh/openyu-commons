package org.openyu.commons.model.supporter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.openyu.commons.enumz.ByteEnum;
import org.openyu.commons.enumz.DoubleEnum;
import org.openyu.commons.enumz.EnumHelper;
import org.openyu.commons.enumz.FloatEnum;
import org.openyu.commons.enumz.IntEnum;
import org.openyu.commons.enumz.LongEnum;
import org.openyu.commons.enumz.ShortEnum;
import org.openyu.commons.lang.BooleanHelper;
import org.openyu.commons.lang.CharHelper;
import org.openyu.commons.lang.CloneHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.model.BaseModel;

/**
 * The Class BaseModelSupporter.
 */
public class BaseModelSupporter implements BaseModel, Supporter {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6557014172084192250L;

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		BaseModelSupporter copy = null;
		try {
			copy = (BaseModelSupporter) super.clone();
		} catch (CloneNotSupportedException ex) {
			// throw new InternalError();
			ex.printStackTrace();
		}
		return copy;
	}

	// only use implement BaseModel
	/*
	 * (non-Javadoc)
	 *
	 * @see org.openyu.commons.model.BaseModel#clone(org.openyu.commons.model.BaseModel)
	 */
	@SuppressWarnings("unchecked")
	public <T> T clone(BaseModel value) {
		T result = null;
		if (value != null) {
			result = (T) value.clone();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openyu.commons.model.BaseModel#clone(java.lang.Object)
	 */
	public <T> T clone(Object value) {
		T result = null;
		if (value != null) {
			result = CloneHelper.clone(value);
		}
		return result;
	}

	// ----------------------------------------------------------------
	// safeGet 只是為了簡化寫法
	// ----------------------------------------------------------------
	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	protected boolean safeGet(Boolean value) {
		return BooleanHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the char
	 */
	protected char safeGet(Character value) {
		return CharHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	protected String safeGet(String value) {
		return StringHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the byte
	 */
	protected byte safeGet(Byte value) {
		return NumberHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the short
	 */
	protected short safeGet(Short value) {
		return NumberHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the int
	 */
	protected int safeGet(Integer value) {
		return NumberHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the long
	 */
	protected long safeGet(Long value) {
		return NumberHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the float
	 */
	protected float safeGet(Float value) {
		return NumberHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the double
	 */
	protected double safeGet(Double value) {
		return NumberHelper.safeGet(value);
	}

	/**
	 * Ratio.
	 *
	 * @param rate
	 *            the rate
	 * @return the double
	 */
	protected double ratio(int rate) {
		return NumberHelper.ratio(rate);
	}

	// ----------------------------------------------------------------
	// 列舉safeGet 只是為了簡化寫法
	// ----------------------------------------------------------------
	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the byte
	 */
	protected byte safeGet(ByteEnum value) {
		return EnumHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the short
	 */
	protected short safeGet(ShortEnum value) {
		return EnumHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the int
	 */
	protected int safeGet(IntEnum value) {
		return EnumHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the long
	 */
	protected long safeGet(LongEnum value) {
		return EnumHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the float
	 */
	protected float safeGet(FloatEnum value) {
		return EnumHelper.safeGet(value);
	}

	/**
	 * Safe get.
	 *
	 * @param value
	 *            the value
	 * @return the double
	 */
	protected double safeGet(DoubleEnum value) {
		return EnumHelper.safeGet(value);
	}

}
