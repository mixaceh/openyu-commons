package org.openyu.commons.bean.supporter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.ProbabilityBean;

@XmlRootElement(name = "probabilityBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProbabilityBeanSupporter extends BaseBeanSupporter implements ProbabilityBean
{

	private static final long serialVersionUID = -7135920877023111808L;

	/**
	 * 機率
	 */
	private double probability;

	public ProbabilityBeanSupporter()
	{}

	public double getProbability()
	{
		return probability;
	}

	public void setProbability(double probability)
	{
		this.probability = probability;
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("probability", probability);
		return builder.toString();
	}

	public Object clone()
	{
		ProbabilityBeanSupporter copy = null;
		copy = (ProbabilityBeanSupporter) super.clone();
		return copy;
	}
}
