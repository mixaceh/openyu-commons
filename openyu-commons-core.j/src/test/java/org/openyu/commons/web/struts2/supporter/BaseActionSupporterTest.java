package org.openyu.commons.web.struts2.supporter;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;
import org.junit.Test;
//import org.apache.struts2.StrutsTestCase;
//import com.opensymphony.xwork2.ActionProxy;

public class BaseActionSupporterTest //extends StrutsTestCase
{
	private static BaseActionSupporter baseActionSupporter = new BaseActionSupporter();

	//	@Test //struts+junit
	//	public void baseActionSupporter() throws Exception
	//	{
	//		request.setParameter("accountBean.userName", "Bruc");
	//		request.setParameter("accountBean.password", "test");
	//
	//		ActionProxy proxy = getActionProxy("/index");
	//
	//		BaseActionSupporter action = (BaseActionSupporter) proxy.getAction();
	//		System.out.println(action);
	//		//
	//		proxy.execute();
	//	}
	@Test
	public void baseActionSupporter()
	{
		System.out.println(baseActionSupporter);
	}

	@Test
	public void getTrueFalseName()
	{
		String result = null;
		//id=TRUE,names={zh_TW=是, zh_CN=是, en_US=True}
		//id=FALSE,names={zh_TW=否, zh_CN=否, en_US=False}
		System.out.println(baseActionSupporter.getTrueFalseOptions());
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			result = baseActionSupporter.getTrueFalseName(true, Locale.TRADITIONAL_CHINESE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);//是
		assertNotNull(result);
		//
		result = baseActionSupporter.getTrueFalseName(true, Locale.US);
		System.out.println(result);//True
		assertNotNull(result);
	}

	@Test
	public void getWhetherName()
	{
		String result = null;
		//id=ALL,names={zh_TW=全部, zh_CN=全部, en_US=All}
		//id=TRUE,names={zh_TW=是, zh_CN=是, en_US=True}
		//id=FALSE,names={zh_TW=否, zh_CN=否, en_US=False}
		System.out.println(baseActionSupporter.getWhetherOptions());
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			result = baseActionSupporter.getWhetherName("all", Locale.TRADITIONAL_CHINESE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(result);//全部
		assertNotNull(result);
		//
		result = baseActionSupporter.getWhetherName("all", Locale.US);
		System.out.println(result);//All
		assertNotNull(result);
	}
}
