package org.openyu.commons.bean.adapter;

import javax.xml.bind.annotation.XmlValue;

import org.openyu.commons.bean.WeightBean;
import org.openyu.commons.bean.supporter.WeightBeanSupporter;
import org.openyu.commons.jaxb.adapter.supporter.BaseXmlAdapterSupporter;

//--------------------------------------------------
//reslove: JAXB can’t handle interfaces
//--------------------------------------------------
//<weight>1</weight>
//--------------------------------------------------
public class WeightBeanXmlAdapter extends
		BaseXmlAdapterSupporter<WeightBeanXmlAdapter.WeightBeanEntry, WeightBean>
{

	public WeightBeanXmlAdapter()
	{}

	// --------------------------------------------------
	public static class WeightBeanEntry
	{
		@XmlValue
		public int value;

		public WeightBeanEntry(int value)
		{
			this.value = value;
		}

		public WeightBeanEntry()
		{}
	}

	// --------------------------------------------------
	public WeightBean unmarshal(WeightBeanEntry value) throws Exception
	{
		WeightBean result = null;
		//
		if (value != null)
		{
			result = new WeightBeanSupporter();
			result.setWeight(value.value);
		}
		return result;
	}

	public WeightBeanEntry marshal(WeightBean value) throws Exception
	{
		WeightBeanEntry result = null;
		//
		if (value != null)
		{
			result = new WeightBeanEntry(value.getWeight());
		}
		return result;
	}
}
