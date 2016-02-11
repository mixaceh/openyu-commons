package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 機率
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface ProbabilityBean extends BaseBean

{
	String KEY = ProbabilityBean.class.getName();

	/**
	 * 
	 * 機率
	 * 
	 * @return
	 */
	double getProbability();

	void setProbability(double probability);
}
