package org.openyu.commons.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTest extends BaseTestSupporter
{
	private static Configuration freeMarkerConfiguration;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		applicationContext = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext-init.xml",// 
			});

		freeMarkerConfiguration = (Configuration) applicationContext.getBean("freeMarkerConfiguration");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{}

	@Before
	public void setUp() throws Exception
	{}

	@After
	public void tearDown() throws Exception
	{}

	@Test
	public void process() throws Exception
	{
		//原設定是file system, user/template/hello.ftl
		//Template template = configuration.getTemplate("hello.ftl");

		//為了測試改為class system
		freeMarkerConfiguration.setClassForTemplateLoading(getClass(), "/");
		Template template = freeMarkerConfiguration.getTemplate("org/openyu/commons/freeMarker/hello.ftl");
		System.out.println("template: " + template);
		System.out.println("--------------------------------------");
		//
		// Build the data-model
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("message", "Hello World!");

		//List parsing 
		List<String> countries = new LinkedList<String>();
		countries.add("India");
		countries.add("United States");
		countries.add("Germany");
		countries.add("France");

		rootMap.put("countries", countries);
		//
		// Console output
		Writer out = new OutputStreamWriter(System.out);
		//
		template.process(rootMap, out);
		//
		out.flush();

		// File output
		//		Writer file = new FileWriter(new File("hello.txt"));
		//
		//		template.process(data, file);
		//
		//		file.flush();
		//		file.close();
	}

	@Test
	public void directive() throws Exception
	{
		//原設定是file system, user/template/hello.ftl
		//Template template = configuration.getTemplate("helloDirective.ftl");

		//為了測試改為class system
		freeMarkerConfiguration.setClassForTemplateLoading(getClass(), "/");
		Template template = freeMarkerConfiguration.getTemplate("org/openyu/commons/freeMarker/helloDirective.ftl");

		System.out.println("template: " + template);
		System.out.println("--------------------------------------");
		//
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("hello", new HelloDirective());
		//
		// Console output
		Writer out = new OutputStreamWriter(System.out);
		//
		template.process(rootMap, out);
		//
		out.flush();
	}

}
