package org.openyu.commons.json;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import net.sf.json.JSON;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.openyu.commons.io.IoHelper;
import org.openyu.commons.lang.SystemHelper;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.util.DateHelper;

public class JsonHelperTest
{

//	@Test
//	//1000000 times: 4550 mills.
//	//1000000 times: 4550 mills.
//	//1000000 times: 4618 mills.
//	//verification: ok
//	public void fromObject()
//	{
//		//object -> json
//		Cat cat = new Cat();
//		cat.setCode("CAT_01");
//		cat.setName("Mimi Cat");
//		cat.setBirthday(new Date());
//		//cat.setLocale(Locale.TRADITIONAL_CHINESE);
//		//
//		JSON json = null;
//
//		int count = 1000000;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			//{'birthday':'2012/02/15 17:38:18:957','code':'CAT_01','locale':'zh_TW','name':'Mimi Cat'}
//			json = JsonHelper.fromObject(cat);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(json);
//	}
//
//	@Test
//	//1000000 times: 3481 mills. 
//	//1000000 times: 3499 mills. 
//	//1000000 times: 3551 mills. 
//	//verification: ok
//	public void fromObjectString()
//	{
//		//string -> json
//		String jsonString = "{'birthday':'2012/02/15 17:38:18:957','code':'CAT_01','locale':'zh_TW','name':'Mimi Cat'}";
//		//
//		JSON json = null;
//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			json = JsonHelper.fromObject(jsonString);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(json);
//		//
//		jsonString = "['get',1,true,null]";
//		json = JsonHelper.fromObject(jsonString);
//		System.out.println(json);
//	}
//
//	@Test
//	//1000000 times: 3481 mills. 
//	public void fromObjectCollector()
//	{
//		//object -> json
//		CatCollector collector = CatCollector.getInstance();
//		collector.setLevelLimit(20);
//		//
//		Cat cat = new Cat();
//		cat.setCode("CAT_01");
//		cat.setName("Mimi Cat");
//		cat.setBirthday(new Date());
//		Cat[] cats = new Cat[] { cat };
//		collector.setCats(cats);
//		//
//		Map<String, Integer> golds = new LinkedHashMap<String, Integer>();
//		golds.put("a", 100);
//		golds.put("b", 200);
//		collector.setGolds(golds);
//		//
//		JSON json = null;
//
//		int count = 1000000;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			//{"cats":[{"birthday":"2013/11/12 22:53:11.722","code":"CAT_01","name":"Mimi Cat"}],"golds":{"a":100,"b":200},"levelLimit":20}
//			json = JsonHelper.fromObject(collector);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(json);
//	}
//
//	@Test
//	//1000000 times: 6522 mills. 
//	//1000000 times: 6824 mills. 
//	//1000000 times: 6495 mills. 
//	//verification: ok
//	public void toBean()
//	{
//		//String jsonString = "{'birthday':'2012/02/15 17:38:18:957','code':'CAT_01','locale':'zh_TW','name':'Mimi Cat'}";
//		String jsonString = "{'birthday':'2012/02/15 17:38:18:957','code':'CAT_01','name':'Mimi Cat'}";
//
//		//json object
//		JSON json = JsonHelper.fromObject(jsonString);
//		//System.out.println("json: " + json);
//
//		Cat cat = null;
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			cat = JsonHelper.toBean(json, Cat.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(cat);
//		System.out.println("getBirthday: " + DateHelper.toString(cat.getBirthday()));
//	}
//
//	@Test
//	//1000000 times: 28023 mills. 
//	public void toBeanCollector()
//	{
//		String jsonString = "{'cats':[{'birthday':'2013/11/12 22:53:11.722','code':'CAT_01','name':'Mimi Cat'}],'golds':{'a':100,'b':200},'levelLimit':20}";
//
//		//json object
//		JSON json = JsonHelper.fromObject(jsonString);
//		//System.out.println("json: " + json);
//
//		CatCollector catCollector = null;
//		//
//		int count = 1000000;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			catCollector = JsonHelper.toBean(json, CatCollector.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(catCollector);
//	}
//
//	@Test
//	//1000000 times: 31936 mills. 
//	//1000000 times: 30786 mills. 
//	//1000000 times: 31936 mills. 
//	//verification: ok
//	public void toBeanByList()
//	{
//		String jsonString = "[{'birthday':'2012/02/09 11:36:35:569','code':'CAT_01','name':'Mimi Cat'},{'birthday':'2012/02/10 11:36:35:569','code':'CAT_02','name':'Big Cat'}]";
//		//json object
//		JSON json = JsonHelper.fromObject(jsonString);
//		//System.out.println("json: " + json.getClass());
//		//System.out.println("json.isArray(): " + json.isArray());
//
//		List list = null;
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			list = JsonHelper.toBean(json, Cat.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(list);
//	}
//
//	@Test
//	//1000000 times: 58886 mills. 
//	//verification: ok
//	public void toBeanByMap()
//	{
//		String jsonString = "{'CAT_01':{'birthday':'2012/02/20 13:35:56:710','code':'CAT_01','name':'Mimi Cat'},'CAT_02':{'birthday':'2012/02/20 13:35:56:710','code':'CAT_02','name':'Big Cat'}}";
//		//json object
//		JSON json = JsonHelper.fromObject(jsonString);
//		//System.out.println("json: " + json.getClass());
//		//System.out.println("json.isArray(): " + json.isArray());
//
//		Map map = null;
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			map = JsonHelper.toBean(json, Cat.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(map);
//	}
//
//	@Test
//	//1000000 times: 10485 mills. 
//	//1000000 times: 10372 mills. 
//	//1000000 times: 10436 mills. 
//	public void toObject()
//	{
//		//String jsonString = "{'birthday':'2012/02/15 17:38:18:957','code':'CAT_01','locale':'zh_TW','name':'Mimi Cat'}";
//		String jsonString = "{'birthday':'2012/02/15 17:38:18:957','code':'CAT_01','name':'Mimi Cat'}";
//
//		Cat cat = null;
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			cat = JsonHelper.toObject(jsonString, Cat.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(cat);
//		System.out.println("getBirthday: " + DateHelper.toString(cat.getBirthday()));
//	}
//
//	@Test
//	//1000000 times: 10485 mills. 
//	//1000000 times: 10372 mills. 
//	//1000000 times: 10436 mills. 
//	public void toObjectByList()
//	{
//		//		String jsonString = "[{'birthday':'2012/02/17 15:46:45:108','code':'CAT_01','locale':'','name':'Mimi Cat'},{'birthday':'2012/02/17 15:46:45:108','code':'CAT_02','locale':'','name':'Big Cat'}]";
//		String jsonString = "[{'birthday':'2012/02/17 15:46:45:108','code':'CAT_01','name':'Mimi Cat'},{'birthday':'2012/02/17 15:46:45:108','code':'CAT_02','name':'Big Cat'}]";
//
//		List list = null;
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			//	list = JsonHelper.toList(jsonString, Cat.class);
//			list = JsonHelper.toObject(jsonString, Cat.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(list);
//	}
//
//	@Test
//	//1000000 times: 1792 mills. 
//	//1000000 times: 1814 mills. 
//	//1000000 times: 1911 mills.
//	//verification: ok
//	public void toObjects()
//	{
//		String jsonString = "['set',3,false,null]";
//
//		Object[] objects = null;
//		//
//		int count = 1000000;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			objects = JsonHelper.toObjects(jsonString);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		SystemHelper.println(objects);
//		//
//	}
//
//	@Test
//	//1000000 times: 22700 mills. 
//	//1000000 times: 22371 mills. 
//	//1000000 times: 22874 mills. 
//	//verification: ok
//	public void toObjectsByClass()
//	{
//		String jsonString = "[{'birthday':'2012/02/09 11:36:35:569','code':'CAT_01','name':'Mimi Cat'},{'birthday':'2012/02/10 11:36:35:569','code':'CAT_02','name':'Big Cat'}]";
//
//		Object[] objects = null;
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			objects = JsonHelper.toObjects(jsonString, Cat.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		SystemHelper.println(objects);
//	}
//
//	@Test
//	//1000000 times: 48657 mills. 
//	//verification: ok
//	public void toObjectsByClassMap()
//	{
//		String jsonString = "[{'cats':[{'birthday':'2012/02/09 11:36:35:569','code':'CAT_01','name':'Mimi Cat'}]},{'cats':[{'birthday':'2012/02/10 11:36:35:569','code':'CAT_02','name':'Big Cat'}]}]";
//
//		Object[] objects = null;
//		Map<String, Class<?>> classMap = new LinkedHashMap<String, Class<?>>();
//		classMap.put("cats", Cat.class);
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			objects = JsonHelper.toObjects(jsonString, CatCollector.class, classMap);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		SystemHelper.println(objects);
//	}
//
//	@Test
//	//1000000 times: 1636 mills. 
//	//1000000 times: 1659 mills. 
//	//1000000 times: 1674 mills.
//	//verification: ok
//	public void toList()
//	{
//		String jsonString = "['get',1,true,null]";
//
//		List list = null;
//		//
//		int count = 1000000;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			list = JsonHelper.toList(jsonString);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(list);
//		//
//	}
//
//	@Test
//	//1000000 times: 22933 mills. 
//	//1000000 times: 22769 mills. 
//	//1000000 times: 22911 mills.
//	//verification: ok
//	public void toListByClass()
//	{
//		String jsonString = "[{'birthday':'2012/02/09 11:36:35:569','code':'CAT_01','name':'Mimi Cat'},{'birthday':'2012/02/10 11:36:35:569','code':'CAT_02','name':'Big Cat'}]";
//
//		List list = null;
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			list = JsonHelper.toList(jsonString, Cat.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(list);
//		//
//	}
//
//	@Test
//	//1000000 times: 48982 mills. 
//	//verification: ok
//	public void toListByClassMap()
//	{
//		String jsonString = "[{'cats':[{'birthday':'2012/02/09 11:36:35:569','code':'CAT_01','name':'Mimi Cat'}]},{'cats':[{'birthday':'2012/02/10 11:36:35:569','code':'CAT_02','name':'Big Cat'}]}]";
//
//		List list = null;
//		Map<String, Class<?>> classMap = new LinkedHashMap<String, Class<?>>();
//		classMap.put("cats", Cat.class);
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			list = JsonHelper.toList(jsonString, CatCollector.class, classMap);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(list);
//		//
//	}
//
//	@Test
//	//1000000 times: 4309 mills. 
//	//1000000 times: 4275 mills. 
//	//1000000 times: 4329 mills.
//	//verification: ok
//	public void toMap()
//	{
//		String jsonString = "{'name':'get','int':1,'double':1.1,'null':null}";
//
//		Map map = null;
//		//
//		int count = 1000000;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			map = JsonHelper.toMap(jsonString);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(map);
//		//
//	}
//
//	@Test
//	//1000000 times: 26777 mills. 
//	//1000000 times: 26236 mills. 
//	//verification: ok
//	public void toMapByClass()
//	{
//		String jsonString = "{'CAT_01':{'birthday':'2012/02/09 11:36:35:569','code':'CAT_01','name':'Mimi Cat'},'CAT_02':{'birthday':'2012/02/10 11:36:35:569','code':'CAT_02','name':'Big Cat'}}";
//
//		Map map = null;
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			map = JsonHelper.toMap(jsonString, Cat.class);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(map);
//		//
//	}
//
//	@Test
//	public void toMapByClassMap()
//	{
//		//不知怎放jsonString ???
//		String jsonString = "{'catMap':{'cat':[{'birthday':'2012/02/09 11:36:35:569'}]}}";
//
//		Map map = null;
//		Map<String, Class<?>> classMap = new LinkedHashMap<String, Class<?>>();
//		classMap.put("cat", Cat.class);
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			map = JsonHelper.toMap(jsonString, CatCollector.class, classMap);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(map);
//		//
//	}
//
//	@Test
//	//1000000 times: 10944 mills.  
//	//1000000 times: 10858 mills.  
//	//1000000 times: 10819 mills.  
//	//verification: ok
//	public void toStringByObject()
//	{
//		//object -> jsonString
//		Cat cat = new Cat();
//		cat.setCode("CAT_01");
//		cat.setName("Mimi Cat");
//		cat.setBirthday(new Date());
//		//cat.setLocale(Locale.TRADITIONAL_CHINESE);
//		//
//		String result = null;
//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			//{"birthday":"2012/02/15 14:22:44:651","code":"CAT_01","locale":"zh_TW","name":"Mimi Cat"}
//			result = JsonHelper.toString(cat);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(result);
//	}
//
//	@Test
//	//1000000 times: 10944 mills.  
//	//1000000 times: 10858 mills.  
//	//1000000 times: 10819 mills.  
//	//verification: ok
//	public void toStringByList()
//	{
//		List list = new LinkedList();
//		//object -> jsonString
//		Cat cat = new Cat();
//		cat.setCode("CAT_01");
//		cat.setName("Mimi Cat");
//		cat.setBirthday(new Date());
//		//cat.setLocale(Locale.TRADITIONAL_CHINESE);
//		list.add(cat);
//		//
//		cat = new Cat();
//		cat.setCode("CAT_02");
//		cat.setName("Big Cat");
//		cat.setBirthday(new Date());
//		//cat.setLocale(Locale.TRADITIONAL_CHINESE);	
//		list.add(cat);
//		//
//		String result = null;
//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			//[{"birthday":"2012/02/17 15:42:06:465","code":"CAT_01","locale":"","name":"Mimi Cat"},{"birthday":"2012/02/17 15:42:06:465","code":"CAT_02","locale":"","name":"Big Cat"}]
//			result = JsonHelper.toString(list);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(result);
//	}
//
//	@Test
//	//1000000 times: 21531 mills.  
//	//1000000 times: 21323 mills.  
//	//1000000 times: 21407 mills.   
//	//verification: ok
//	public void toXml()
//	{
//		//object -> xml
//		Cat cat = new Cat();
//		cat.setCode("CAT_01");
//		cat.setName("Mimi Cat");
//		cat.setBirthday(new Date());
//		//cat.setLocale(Locale.TRADITIONAL_CHINESE);
//		//
//		String result = null;
//
//		int count = 1000000;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			//<?xml version="1.0" encoding="UTF-8"?>
//			//<o><birthday type="string">2012/02/16 15:20:54:669</birthday><code type="string">CAT_01</code><locale type="string">zh_TW</locale><name type="string">Mimi Cat</name></o>
//			result = JsonHelper.toXml(cat);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(result);
//	}
//
//	@Test
//	//verification: ok
//	public void toStringFromXml()
//	{
//		//xml -> json string
//		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
//		xmlString += "<o><birthday type=\"string\">2012/02/16 15:20:54:669</birthday><code type=\"string\">CAT_01</code><locale type=\"string\">zh_TW</locale><name type=\"string\">Mimi Cat</name></o>";
//		String result = null;
//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			//<?xml version="1.0" encoding="UTF-8"?>
//			//<o><birthday type="string">2012/02/16 15:20:54:669</birthday><code type="string">CAT_01</code><locale type="string">zh_TW</locale><name type="string">Mimi Cat</name></o>
//			result = JsonHelper.toStringFromXml(xmlString);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(result);
//	}
//
//	@Test
//	//1000 times: 1511 mills. 
//	//verification: ok
//	public void write() throws Exception
//	{
//		Cat cat = new Cat();
//		cat.setCode("CAT_01");
//		cat.setName("Mimi Cat");
//		cat.setBirthday(new Date());
//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		boolean result = false;
//		for (int i = 0; i < count; i++)
//		{
//			//1.data/json/xxxCat.json
//			//			FileOutputStream fos = new FileOutputStream(new File("data/json/xxxCat.json"));
//			//			BufferedOutputStream bos = new BufferedOutputStream(fos);
//
//			OutputStream out = IoHelper.createOutputStream("data/json/xxxCat.json");
//			result = JsonHelper.write(out, cat);
//		}
//		//
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(result);
//
//		//2.System.out
//		result = JsonHelper.write(System.out, cat);
//		System.out.println(result);
//	}
//
//	@Test
//	//1000 times: 1666 mills. 
//	//1000 times: 1587 mills. 
//	//1000 times: 1604 mills. 
//	//verification: ok
//	public void writeByList() throws Exception
//	{
//		List list = new LinkedList();
//		Cat cat = new Cat();
//		cat.setCode("CAT_01");
//		cat.setName("Mimi Cat");
//		cat.setBirthday(new Date());
//		list.add(cat);
//		//
//		cat = new Cat();
//		cat.setCode("CAT_02");
//		cat.setName("Big Cat");
//		cat.setBirthday(new Date());
//		list.add(cat);
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		boolean result = false;
//		for (int i = 0; i < count; i++)
//		{
//			OutputStream out = IoHelper.createOutputStream("data/json/xxxCatList.json");
//			result = JsonHelper.write(out, list);
//		}
//		//
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(result);
//
//		//2.System.out
//		result = JsonHelper.write(System.out, list);
//		System.out.println(result);
//	}
//
//	@Test
//	//1000 times: 1771 mills. 
//	//1000 times: 1756 mills. 
//	//1000 times: 1720 mills. 
//	//verification: ok
//	public void writeByMap() throws Exception
//	{
//		Map map = new LinkedHashMap();
//		Cat cat = new Cat();
//		cat.setCode("CAT_01");
//		cat.setName("Mimi Cat");
//		cat.setBirthday(new Date());
//		map.put(cat.getCode(), cat);
//		//
//		cat = new Cat();
//		cat.setCode("CAT_02");
//		cat.setName("Big Cat");
//		cat.setBirthday(new Date());
//		map.put(cat.getCode(), cat);
//		//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		boolean result = false;
//		for (int i = 0; i < count; i++)
//		{
//			OutputStream out = IoHelper.createOutputStream("data/json/xxxCatMap.json");
//			result = JsonHelper.write(out, map);
//		}
//		//
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(result);
//
//		//2.System.out
//		result = JsonHelper.write(System.out, map);
//		System.out.println(result);
//	}
//
//	@Test
//	//1000 times: 972 mills. 
//	//1000 times: 954 mills. 
//	//1000 times: 984 mills. 
//	//verification: ok
//	public void read() throws Exception
//	{
//		Cat cat = null;
//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			//1.data/json/xxxCat.json
//			//			FileInputStream fis = new FileInputStream(new File("data/json/xxxCat.json"));
//			//			BufferedInputStream bis = new BufferedInputStream(fis);
//
//			InputStream in = IoHelper.createInputStream("data/json/xxxCat.json");
//			cat = JsonHelper.read(in, Cat.class);
//		}
//		//
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(cat);
//	}
//
//	@Test
//	//1000 times: 1141 mills. 
//	//1000 times: 1142 mills. 
//	//1000 times: 1155 mills. 
//	//verification: ok
//	public void readByList() throws Exception
//	{
//		List list = null;
//
//		int count = 1;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			//1.data/json/xxxCat.json
//			//			FileInputStream fis = new FileInputStream(new File("data/json/xxxCat.json"));
//			//			BufferedInputStream bis = new BufferedInputStream(fis);
//
//			InputStream in = IoHelper.createInputStream("data/json/xxxCatList.json");
//			list = JsonHelper.read(in, Cat.class);
//		}
//		//
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(list);
//	}
//
//	@Test
//	//1000 times: 1380 mills. 
//	//1000 times: 1357 mills. 
//	//1000 times: 1369 mills. 
//	//verification: ok
//	public void readByMap() throws Exception
//	{
//		Map map = null;
//
//		int count = 1000;//100w
//		long beg = System.currentTimeMillis();
//		for (int i = 0; i < count; i++)
//		{
//			//1.data/json/xxxCat.json
//			//			FileInputStream fis = new FileInputStream(new File("data/json/xxxCat.json"));
//			//			BufferedInputStream bis = new BufferedInputStream(fis);
//
//			InputStream in = IoHelper.createInputStream("data/json/xxxCatMap.json");
//			map = JsonHelper.read(in, Cat.class);
//		}
//		//
//		long end = System.currentTimeMillis();
//		System.out.println(count + " times: " + (end - beg) + " mills. ");
//		//
//		System.out.println(map);
//	}
//
//	@Test
//	public void booleanOperation()
//	{
//		System.out.println(true & true);
//		System.out.println(true & false);
//		System.out.println(false & true);
//		System.out.println(false & false);
//		//
//		System.out.println(true | true);
//		System.out.println(true | false);
//		System.out.println(false | true);
//		System.out.println(false | false);
//	}
//
//	public static class Cat extends BaseModelSupporter
//	{
//		private String code;
//
//		private String name;
//
//		private Date birthday;
//
//		//private Locale locale;
//
//		public Cat()
//		{}
//
//		public String getCode()
//		{
//			return code;
//		}
//
//		public void setCode(String code)
//		{
//			this.code = code;
//		}
//
//		public String getName()
//		{
//			return name;
//		}
//
//		public void setName(String name)
//		{
//			this.name = name;
//		}
//
//		public Date getBirthday()
//		{
//			return birthday;
//		}
//
//		public void setBirthday(Date birthday)
//		{
//			this.birthday = birthday;
//		}
//
//		//		public Locale getLocale()
//		//		{
//		//			return locale;
//		//		}
//		//
//		//		public void setLocale(Locale locale)
//		//		{
//		//			this.locale = locale;
//		//		}
//	}
//
//	public static class CatCollector extends BaseModelSupporter
//	{
//
//		private static final long serialVersionUID = 1430223554828900113L;
//
//		private static CatCollector catCollector;
//
//		private int levelLimit = 20;
//
//		private Cat[] cats = new Cat[0];
//
//		private Map<String, Integer> golds = new LinkedHashMap<String, Integer>();
//
//		public CatCollector()
//		{}
//
//		public synchronized static CatCollector getInstance()
//		{
//			if (catCollector == null)
//			{
//				catCollector = new CatCollector();
//			}
//			return catCollector;
//		}
//
//		public int getLevelLimit()
//		{
//			return levelLimit;
//		}
//
//		public void setLevelLimit(int levelLimit)
//		{
//			this.levelLimit = levelLimit;
//		}
//
//		public Cat[] getCats()
//		{
//			return cats;
//		}
//
//		public void setCats(Cat[] cats)
//		{
//			this.cats = cats;
//		}
//
//		public Map<String, Integer> getGolds()
//		{
//			return golds;
//		}
//
//		public void setGolds(Map<String, Integer> golds)
//		{
//			this.golds = golds;
//		}
//
//	}
}
