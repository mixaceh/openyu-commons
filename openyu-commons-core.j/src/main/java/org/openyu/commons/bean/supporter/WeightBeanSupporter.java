package org.openyu.commons.bean.supporter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.WeightBean;

@XmlRootElement(name = "weightBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeightBeanSupporter extends ProbabilityBeanSupporter implements WeightBean
{

	private static final long serialVersionUID = 6273277090254322472L;

	/**
	 * 權重
	 */
	private int weight;

	public WeightBeanSupporter()
	{}

	public int getWeight()
	{
		return weight;
	}

	public void setWeight(int weight)
	{
		this.weight = weight;
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("weight", weight);
		return builder.toString();
	}

	public Object clone()
	{
		WeightBeanSupporter copy = null;
		copy = (WeightBeanSupporter) super.clone();
		return copy;
	}
}
