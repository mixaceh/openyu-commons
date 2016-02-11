package org.openyu.commons.web.struts2;

import org.springframework.context.ApplicationContextAware;

import org.openyu.commons.web.servlet.BaseWeb;

/**
 * 控制器
 */
public interface BaseAction extends ApplicationContextAware, BaseWeb
{
	String KEY = BaseAction.class.getName();

}
