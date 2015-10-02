package org.openyu.commons.web.struts2;

import org.openyu.commons.web.servlet.BaseWeb;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * 基底web攔截器
 */
public interface BaseWebInterceptor extends BaseWeb
{
	String KEY = BaseWebInterceptor.class.getName();

	/**
	 * 停用
	 * 
	 * @return
	 */
	boolean isDisable();

	void setDisable(boolean disable);

	/**
	 * 記錄
	 * 
	 * @return
	 */
	boolean isLogEnable();

	void setLogEnable(boolean debug);

	/**
	 * 攔截
	 * 
	 * @param actionInvocation
	 * @param actionContext
	 * @param action
	 * @return
	 */
	String intercept(ActionInvocation actionInvocation, ActionContext actionContext, Object action);

}
