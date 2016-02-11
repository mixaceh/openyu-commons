package org.openyu.commons.bean.adapter;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.bean.ProbabilityBean;
import org.openyu.commons.bean.supporter.ProbabilityBeanSupporter;
import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;

//--------------------------------------------------
//reslove: JAXB can’t handle interfaces
//--------------------------------------------------
//<probability>0.5</probability>
//--------------------------------------------------
public class ProbabilityBeanXmlAdapter extends
		BaseXmlAdapterSupporter<ProbabilityBeanXmlAdapter.ProbabilityBeanEntry, ProbabilityBean>
{

	public ProbabilityBeanXmlAdapter()
	{}

	// --------------------------------------------------
	public static class ProbabilityBeanEntry
	{
		@XmlValue
		public double value;

		public ProbabilityBeanEntry(double value)
		{
			this.value = value;
		}

		public ProbabilityBeanEntry()
		{}
	}

	// --------------------------------------------------
	public ProbabilityBean unmarshal(ProbabilityBeanEntry value) throws Exception
	{
		ProbabilityBean result = null;
		//
		if (value != null)
		{
			result = new ProbabilityBeanSupporter();
			result.setProbability(value.value);
		}
		return result;
	}

	public ProbabilityBeanEntry marshal(ProbabilityBean value) throws Exception
	{
		ProbabilityBeanEntry result = null;
		//
		if (value != null)
		{
			result = new ProbabilityBeanEntry(value.getProbability());
		}
		return result;
	}
}
