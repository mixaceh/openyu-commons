package org.openyu.commons.web.servlet;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

/**
 * 可取得
 * 
 * application,session,request,reponse,cookie,pageContext
 * 
 * 使用者session的locale,timeZone
 */
public interface BaseWeb
{
	/**
	 * application
	 * 
	 * @return
	 */
	ServletContext getApplication();

	/**
	 * session
	 * 
	 * @return
	 */
	Map<String, Object> getSession();

	/**
	 * httpSession
	 * 
	 * @return
	 */
	HttpSession getHttpSession();

	/**
	 * session id
	 * 
	 * @return
	 */
	String getSessionId();

	/**
	 * request
	 * 
	 * @return
	 */
	HttpServletRequest getRequest();

	/**
	 * reponse
	 * 
	 * @return
	 */
	HttpServletResponse getReponse();

	/**
	 * cookiesMap
	 * 
	 * @return
	 */
	Map<String, String> getCookiesMap();

	/**
	 * cookies
	 * 
	 * @return
	 */
	Cookie[] getCookies();

	/**
	 * pageContext
	 * 
	 * @return
	 */
	PageContext getPageContext();

	/**
	 * 使用者session區域
	 * 
	 * @return
	 */
	Locale getLocale();

	/**
	 * 使用者session時區
	 * 
	 * @return
	 */
	TimeZone getTimeZone();
}
