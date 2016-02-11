package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 權重
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface WeightBean extends ProbabilityBean
{
	String KEY = WeightBean.class.getName();

	/**
	 * 
	 * 權重
	 * 
	 * @return
	 */
	int getWeight();

	void setWeight(int weight);

}
