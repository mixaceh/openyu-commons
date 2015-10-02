package org.openyu.commons.freemarker.supporter;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import org.openyu.commons.freemarker.BaseWebDirective;
import org.openyu.commons.web.struts2.BaseAction;

/**
 * 基底web標籤
 * 
 * 需搭配struts2
 */
public abstract class BaseWebDirectiveSupporter extends BaseDirectiveSupporter implements
		BaseWebDirective
{
	private static transient final Logger log = LogManager
			.getLogger(BaseWebDirectiveSupporter.class);

	public BaseWebDirectiveSupporter()
	{

	}

	public ServletContext getApplication()
	{
		return ServletActionContext.getServletContext();
	}

	public Map<String, Object> getSession()
	{
		return ServletActionContext.getContext().getSession();
	}

	public HttpSession getHttpSession()
	{
		return getRequest().getSession();
	}

	public String getSessionId()
	{
		return getHttpSession().getId();
	}

	public HttpServletRequest getRequest()
	{
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getReponse()
	{
		return ServletActionContext.getResponse();
	}

	public Map<String, String> getCookiesMap()
	{
		Map<String, String> result = null;
		BaseAction baseAction = getBaseAction();
		if (baseAction != null)
		{
			result = baseAction.getCookiesMap();
		}
		return result;
	}

	public Cookie[] getCookies()
	{
		return getRequest().getCookies();
	}

	public PageContext getPageContext()
	{
		return ServletActionContext.getPageContext();
	}

	/**
	 * 使用者session區域
	 * 
	 * @return
	 */
	public Locale getLocale()
	{
		Locale result = null;
		BaseAction baseAction = getBaseAction();
		if (baseAction != null)
		{
			result = baseAction.getLocale();
		}
		return result;
	}

	/**
	 * 使用者session時區
	 * 
	 * @return
	 */
	public TimeZone getTimeZone()
	{
		TimeZone result = null;
		BaseAction baseAction = getBaseAction();
		if (baseAction != null)
		{
			result = baseAction.getTimeZone();
		}
		return result;
	}

	/**
	 * 取得baseAciton
	 * 
	 * @return
	 */
	protected BaseAction getBaseAction()
	{
		BaseAction result = null;
		Object action = ServletActionContext.getContext().getActionInvocation().getAction();
		if (action instanceof BaseAction)
		{
			result = (BaseAction) action;
		}
		return result;
	}
}
