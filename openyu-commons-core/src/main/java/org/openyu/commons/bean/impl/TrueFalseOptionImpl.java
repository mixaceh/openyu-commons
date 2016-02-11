package org.openyu.commons.bean.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.TrueFalseOption;
import org.openyu.commons.bean.supporter.NamesBeanSupporter;

/**
 * 是否
 */
//--------------------------------------------------
//jaxb
//--------------------------------------------------
@XmlRootElement(name = "trueFalseOption")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrueFalseOptionImpl extends NamesBeanSupporter implements TrueFalseOption
{

	private static final long serialVersionUID = 4644282406281853593L;

	/**
	 * 是否類別,key
	 */
	private TrueFalseType id;

	public TrueFalseOptionImpl(TrueFalseType id)
	{
		this.id = id;
	}

	public TrueFalseOptionImpl()
	{
		this(null);
	}

	public TrueFalseType getId()
	{
		return id;
	}

	public void setId(TrueFalseType id)
	{
		this.id = id;
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(id).toHashCode();
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.appendSuper(super.toString());
		return builder.toString();
	}

	public Object clone()
	{
		TrueFalseOptionImpl copy = null;
		copy = (TrueFalseOptionImpl) super.clone();
		return copy;
	}

}
