package org.openyu.commons.helper.supporter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.mark.Supporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 輔助器, 只提供static method/field
 * 
 * 1.無法直接用 spring 配置建構, 要實作FactoryBean
 * 
 * 2.無法用 new 單例建構
 * 
 * 3.無法用 createInstance() 建構
 * 
 * 4.無法用 getInstance() 單例建構
 */
public abstract class BaseHelperSupporter implements Supporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseHelperSupporter.class);

	/**
	 * for extends
	 */
	protected BaseHelperSupporter() {
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
