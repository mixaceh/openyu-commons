package org.openyu.commons.lang.event.supporter;

import java.lang.reflect.Field;
import java.util.EventObject;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.lang.event.BaseEvent;
import org.openyu.commons.lang.ex.ExceptionChain;
import org.openyu.commons.mark.Supporter;

/**
 * source: 來源
 *
 * attach: 事件附件,event內傳遞用
 *
 * The Class BaseEventSupporter.
 */
public abstract class BaseEventSupporter extends EventObject implements
		BaseEvent, Supporter {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8091752007116094489L;

	/** The Constant UNKNOWN. */
	public static final int UNKNOWN = -1;

	/** The type. */
	private transient int type;

	/** The name. */
	private transient String name;

	// 2012/04/09 目前沒用到,先保留
	/** The exception chain. */
	private transient ExceptionChain exceptionChain;

	/** The attach. */
	private transient Object attach;

	/**
	 * Instantiates a new base event supporter.
	 *
	 * @param source
	 *            the source
	 * @param type
	 *            the type
	 * @param attach
	 *            the attach
	 */
	public BaseEventSupporter(Object source, int type, Object attach) {
		super(source);
		this.type = type;
		this.attach = attach;
	}

	/**
	 * Instantiates a new base event supporter.
	 *
	 * @param source
	 *            the source
	 * @param type
	 *            the type
	 */
	public BaseEventSupporter(Object source, int type) {
		this(source, type, null);
	}

	/**
	 * Adds the exception.
	 *
	 * @param ex
	 *            the ex
	 */
	public void addException(Exception ex) {
		if (exceptionChain == null) {
			exceptionChain = new ExceptionChain();
		}
		exceptionChain.append(ex);
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openyu.commons.lang.event.BaseEvent#getExceptionChain()
	 */
	public ExceptionChain getExceptionChain() {
		return exceptionChain;
	}

	/**
	 * 利用反射,由int取得名稱
	 *
	 * 取常數項int,當作事件常數項判斷.
	 *
	 * @return the name
	 */
	public String getName() {
		if (name == null) {
			// 不要用cache,因會被繼承使用此class
			// Field[] fields =
			// ClassHelper.getDeclaredFieldsAndCache(EventObjectSupporter.class);

			Field[] fields = getClass().getFields();
			for (Field field : fields) {
				try {
					// #issue 有錯
					// #fix ok

					// 取 public static final int 的 field 作為事件常數項判斷
					if (ClassHelper.isConstantField(field)
							&& field.getType().equals(int.class)) {
						// System.out.println(field.getName());
						Integer value = ClassHelper.getFieldValue(this, field);
						if (value != null && type == value) {
							name = field.getName();
							break;
						}
					}
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
		return name;
	}

	/**
	 * 利用反射,由名稱取得int.
	 *
	 * @param name
	 *            the name
	 * @return the int
	 */
	public int typeOf(String name) {
		int result = UNKNOWN;

		// 不要用cache,因會被繼承使用此class
		// Field[] fields =
		// ClassHelper.getDeclaredFieldsAndCache(EventObjectSupporter.class);

		Field[] fields = getClass().getFields();
		for (Field field : fields) {
			try {
				// #issue 有錯
				// #fix ok
				if (field.getName().equals(name)
						&& ClassHelper.isConstantField(field)
						&& field.getType().equals(int.class)) {
					result = field.getInt(this);
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openyu.commons.lang.event.BaseEvent#getAttach()
	 */
	public Object getAttach() {
		return attach;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.EventObject#toString()
	 */
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		builder.append("type", type);
		builder.append("name", getName());
		builder.append("source", source);
		builder.append("attach", attach);
		return builder.toString();
	}

}
