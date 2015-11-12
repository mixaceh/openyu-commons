package org.openyu.commons.web.struts2.supporter;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.struts2.ServletActionContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StopWatch;

import org.openyu.commons.mark.Supporter;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.anno.DefaultThreadService;
import org.openyu.commons.web.struts2.BaseAction;
import org.openyu.commons.web.struts2.BaseWebInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 基底web攔截器
 * 
 * round advice,可在invoke前後,處理其他邏輯
 */
public abstract class BaseWebInterceptorSupporter extends AbstractInterceptor implements
		BaseWebInterceptor, Supporter
{

	private static final long serialVersionUID = 1155259612617422182L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(BaseWebInterceptorSupporter.class);

	/**
	 * 停用
	 */
	private boolean disable;

	/**
	 * 記錄
	 */
	private boolean logEnable;

	/**
	 * 多緒服務
	 */
//	@Autowired
//	@Qualifier("threadService")
	@DefaultThreadService
	protected transient ThreadService threadService;

	public BaseWebInterceptorSupporter()
	{}

	public boolean isDisable()
	{
		return disable;
	}

	public void setDisable(boolean disable)
	{
		this.disable = disable;
	}

	public boolean isLogEnable()
	{
		return logEnable;
	}

	public void setLogEnable(boolean logEnable)
	{
		this.logEnable = logEnable;
	}

	public String intercept(ActionInvocation actionInvocation) throws Exception
	{
		String result = null;
		try
		{
			if (!disable)
			{
				//被攔截的context
				ActionContext actionContext = actionInvocation.getInvocationContext();
				//action
				Object action = actionInvocation.getAction();
				//
				ActionProxy proxy = actionInvocation.getProxy();
				//
				result = intercept(actionInvocation, actionContext, action);
				log(LOGGER, proxy);
			}
			else
			{
				result = actionInvocation.invoke();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return result;
	}

	protected void log(Logger log, ActionProxy actionProxy)
	{
		log(log, actionProxy, null);
	}

	protected void log(Logger log, ActionProxy actionProxy, StopWatch stopWatch)
	{
		if (logEnable)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("[thread-");
			sb.append(Thread.currentThread().getId());
			sb.append("] ");
			sb.append(actionProxy.getNamespace());
			sb.append("/");
			sb.append(actionProxy.getActionName());
			sb.append(" ");
			sb.append(actionProxy.getMethod());
			//
			if (stopWatch != null)
			{
				sb.append(" in ");
				sb.append(stopWatch.getTotalTimeMillis());
				sb.append(" mills.");
			}
			//
			log.info(sb.toString());
		}
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

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
