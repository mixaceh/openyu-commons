package org.openyu.commons.helper.supporter;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.mark.Supporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 輔助器,只提供static method/field
 * 
 * 1.無法直接用 spring 配置建構, 要實作FactoryBean
 * 
 * 2.無法用 getInstance() 單例建構
 * 
 * 3.無法用 createInstance() 建構
 */
public abstract class BaseHelperSupporter implements Supporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseHelperSupporter.class);

	protected BaseHelperSupporter() {
	}

	/**
	 * To string.
	 *
	 * @return String
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
