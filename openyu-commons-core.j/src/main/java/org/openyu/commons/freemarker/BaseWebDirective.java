package org.openyu.commons.freemarker;

import org.openyu.commons.web.servlet.BaseWeb;

import freemarker.template.TemplateDirectiveModel;

/**
 * 基底web標籤
 * 
 * 需搭配struts2
 */
public interface BaseWebDirective extends TemplateDirectiveModel, BaseWeb
{
	String KEY = BaseWebDirective.class.getName();

}
