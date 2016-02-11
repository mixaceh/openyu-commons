package org.openyu.commons.lang.event.supporter;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.supporter.BaseBeanSupporter;
import org.openyu.commons.enumz.ByteEnum;
import org.openyu.commons.enumz.DoubleEnum;
import org.openyu.commons.enumz.FloatEnum;
import org.openyu.commons.enumz.IntEnum;
import org.openyu.commons.enumz.LongEnum;
import org.openyu.commons.enumz.ShortEnum;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.event.EventAttach;
import org.openyu.commons.mark.Supporter;

public class EventAttachSupporter<OLD_VALUE, NEW_VALUE> extends
		BaseBeanSupporter implements EventAttach<OLD_VALUE, NEW_VALUE>,
		Supporter {
			
	private static final long serialVersionUID = -7601323244937092780L;

	private OLD_VALUE oldValue;

	private NEW_VALUE newValue;

	private Double diffValue;

	public EventAttachSupporter(OLD_VALUE oldValue, NEW_VALUE newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
		//
		this.diffValue = calcDiffValue(oldValue, newValue);
	}

	public EventAttachSupporter() {
		this(null, null);
	}

	public OLD_VALUE getOldValue() {
		return oldValue;
	}

	public NEW_VALUE getNewValue() {
		return newValue;
	}

	public Double getDiffValue() {
		return diffValue;
	}

	/**
	 * 計算差異值 newValue - oldValue ,以下類別方能處理,其他則無法做差異值
	 * 
	 * Number
	 * 
	 * IntEnum
	 * 
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	protected Double calcDiffValue(OLD_VALUE oldValue, NEW_VALUE newValue) {
		Double result = null;
		if (oldValue instanceof Number || newValue instanceof Number) {
			result = NumberHelper.subtract(newValue, oldValue);
		}
		// byte
		else if (oldValue instanceof ByteEnum || newValue instanceof ByteEnum) {
			result = (double) ((ByteEnum) newValue).getValue()
					- ((ByteEnum) oldValue).getValue();
		}
		// short
		else if (oldValue instanceof ShortEnum || newValue instanceof ShortEnum) {
			result = (double) ((ShortEnum) newValue).getValue()
					- ((ShortEnum) oldValue).getValue();
		}
		// int
		else if (oldValue instanceof IntEnum || newValue instanceof IntEnum) {
			IntEnum oldEnum = (IntEnum) oldValue;
			IntEnum newEnum = (IntEnum) newValue;
			result = (double) safeGet(newEnum) - safeGet(oldEnum);
		}
		// long
		else if (oldValue instanceof LongEnum || newValue instanceof LongEnum) {
			result = (double) ((LongEnum) newValue).getValue()
					- ((LongEnum) oldValue).getValue();
		}
		// float
		else if (oldValue instanceof FloatEnum || newValue instanceof FloatEnum) {
			result = (double) ((FloatEnum) newValue).getValue()
					- ((FloatEnum) oldValue).getValue();
		}
		// double
		else if (oldValue instanceof DoubleEnum
				|| newValue instanceof DoubleEnum) {
			result = (double) ((DoubleEnum) newValue).getValue()
					- ((DoubleEnum) oldValue).getValue();
		}
		return result;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("oldValue", oldValue);
		builder.append("newValue", newValue);
		builder.append("diffValue", diffValue);
		return builder.toString();
	}
}
