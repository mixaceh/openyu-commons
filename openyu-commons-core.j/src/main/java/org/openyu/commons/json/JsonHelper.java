package org.openyu.commons.json;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import net.sf.ezmorph.object.DateMorpher;
//import net.sf.json.JSON;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import net.sf.json.JsonConfig;
//import net.sf.json.util.JSONUtils;
//import net.sf.json.xml.XMLSerializer;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.lang.ArrayHelper;
import org.openyu.commons.util.DateHelper;

/**
 * 將save/load -> 改成read/write
 * 
 */
public class JsonHelper extends BaseHelperSupporter
{

	private static transient final Logger log = LogManager.getLogger(JsonHelper.class);

//	private static JsonHelper instance;
//
//	private static JsonConfig jsonConfig = new JsonConfig();
//	static
//	{
//		//fromObject用
//		//Date
//		jsonConfig.registerJsonValueProcessor(Date.class, new DateValueProcessor());
//		//Locale
//		jsonConfig.registerJsonValueProcessor(Locale.class, new LocaleValueProcessor());
//
//		//toBean用
//		//Date, 可轉換的日期格式，即Json串中可以出現以下格式的日期與時間  
//		//#issue: ""會無法轉換
//		DateMorpher dateMorpher = new DateMorpher(new String[] { DateHelper.DEFAULT_PATTERN,
//				DateHelper.DATE_TIME_MILLS_PATTERN, DateHelper.DATE_PATTERN,
//				DateHelper.TIME_PATTERN });
//		JSONUtils.getMorpherRegistry().registerMorpher(dateMorpher);
//	}
//
//	public JsonHelper()
//	{}
//
//	public static synchronized JsonHelper getInstance()
//	{
//		if (instance == null)
//		{
//			instance = new JsonHelper();
//		}
//		return instance;
//	}
//
//	protected static JSON fromObject(Object value)
//	{
//		return fromObject(value, null);
//	}
//
//	/**
//	 * 原JSONObject.fromObject,JSONArray.fromObject
//	 * 
//	 * @param value 字串,或物件,轉成JSONArray或JSONObject
//	 * @param jsonConfig
//	 * @return
//	 */
//	protected static JSON fromObject(Object value, JsonConfig jsonConfig)
//	{
//		JSON result = null;
//		if (value != null)
//		{
//			try
//			{
//				//當null時,有預設的jsonConfig
//				jsonConfig = (jsonConfig != null ? jsonConfig : JsonHelper.jsonConfig);
//
//				//集合,陣列,字串有"["視為json陣列
//				if (value instanceof Collection || ArrayHelper.isArray(value) || isJsonArray(value))
//				{
//					result = JSONArray.fromObject(value, jsonConfig);
//				}
//				else
//				{
//					result = JSONObject.fromObject(value, jsonConfig);
//				}
//			}
//			catch (Exception ex)
//			{
//				//log.warn(ex);
//				ex.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	protected static <T> T toBean(JSON json, Class<?> clazz)
//	{
//		return toBean(json, clazz, null);
//	}
//
//	/**
//	 * 物件,集合
//	 * 
//	 * @param json
//	 * @param clazz
//	 * @param classMap
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	protected static <T> T toBean(JSON json, Class<?> clazz, Map<String, Class<?>> classMap)
//	{
//		T result = null;
//		//JSONArray
//		//System.out.println(json.isArray());
//		//objects,list
//		if (json.isArray())
//		{
//			//JSONArray jsonArray = (JSONArray) json;
//			//只轉成list,沒有轉成objcets[]
//			List<?> list = toList(json.toString(), clazz, classMap);
//			result = (T) list;
//		}
//		//JSONObject
//		else if (json instanceof JSONObject)
//		{
//			JSONObject jsonObject = (JSONObject) json;
//			//System.out.println("jsonObject: "+jsonObject);
//			result = toBean(jsonObject, clazz, classMap);
//			//System.out.println("t: "+t);
//			//當t=null,再試圖轉成map看是否可以轉換
//			if (result == null)
//			{
//				result = (T) toMap(json.toString(), clazz, classMap);
//			}
//		}
//		return result;
//	}
//
//	protected static <T> T toBean(JSONObject jsonObject, Class<?> clazz)
//	{
//		return toBean(jsonObject, clazz, null);
//	}
//
//	/**
//	 * 原 JSONObject.toBean
//	 * 
//	 * @param jsonObject JSON物件,轉成物件
//	 * @param clazz
//	 * @param classMap
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	protected static <T> T toBean(JSONObject jsonObject, Class<?> clazz,
//									Map<String, Class<?>> classMap)
//	{
//		T result = null;
//		if (jsonObject != null)
//		{
//			try
//			{
//				//System.out.println("classMap: "+classMap);
//				if (classMap == null || classMap.isEmpty())
//				{
//					result = (T) JSONObject.toBean(jsonObject, clazz);
//					//System.out.println("t: "+t);
//				}
//				else
//				{
//					//System.out.println("jsonObject: "+jsonObject);
//					result = (T) JSONObject.toBean(jsonObject, clazz, classMap);
//				}
//			}
//			catch (Exception ex)
//			{
//				//log.warn(ex);
//				ex.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	//String.valueOf(value).startsWith("[")
//	public static boolean isJsonArray(Object value)
//	{
//		boolean result = false;
//		try
//		{
//			result = (value != null && value instanceof String && String.valueOf(value).startsWith(
//				"["));
//		}
//		catch (Exception ex)
//		{
//			//log.warn(ex);
//			//ex.printStackTrace();
//		}
//		return result;
//	}
//
//	public static <T> T toObject(String jsonString, Class<?> clazz)
//	{
//		return toObject(jsonString, null, clazz, null);
//	}
//
//	public static <T> T toObject(String jsonString, JsonConfig jsonConfig, Class<?> clazz)
//	{
//		return toObject(jsonString, jsonConfig, clazz, null);
//	}
//
//	/**
//	 * 從json轉換成實體對象，並且實體集合屬性存有另外實體Bean
//	 * 
//	 * @param jsonString e.g. {'data':[{'name':'get'},{'name':'set'}]}
//	 * @param clazz e.g. MyBean.class
//	 * @param classMap e.g. classMap.put("data", Person.class)
//	 * @return Object
//	 */
//	public static <T> T toObject(String jsonString, JsonConfig jsonConfig, Class<?> clazz,
//									Map<String, Class<?>> classMap)
//	{
//		T result = null;
//		if (jsonString != null)
//		{
//			try
//			{
//				JSON json = fromObject(jsonString, jsonConfig);
//				//System.out.println("json: "+json);
//				result = toBean(json, clazz, classMap);
//				//System.out.println("t: "+t);
//			}
//			catch (Exception ex)
//			{
//				//log.warn(ex);
//				//ex.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 把一個json數組串轉換成普通數組
//	 * 
//	 * @param jsonArrStr e.g. ['get',1,true,null]
//	 * @return Object[]
//	 */
//	@SuppressWarnings("unchecked")
//	public static <T> T[] toObjects(String jsonArrayString)
//	{
//		T[] results = null;
//		if (isJsonArray(jsonArrayString))
//		{
//			try
//			{
//				results = (T[]) JSONArray.fromObject(jsonArrayString).toArray();
//			}
//			catch (Exception ex)
//			{
//				//log.warn(ex);
//				//ex.printStackTrace();
//			}
//		}
//		return results;
//	}
//
//	/**
//	 * 把一個json數組串轉換成實體數組
//	 * 
//	 * @param jsonArrStr e.g. [{'name':'get'},{'name':'set'}]
//	 * @param clazz e.g. Person.class
//	 * @return Object[]
//	 */
//	public static <T> T[] toObjects(String jsonArrayString, Class<?> clazz)
//	{
//		return toObjects(jsonArrayString, clazz, null);
//	}
//
//	/**
//	 * 把一個json數組串轉換成實體數組，且數組元素的屬性含有另外實例Bean
//	 * 
//	 * @param jsonArrStr e.g.
//	 *            [{'data':[{'name':'get'}]},{'data':[{'name':'set'}]}]
//	 * @param clazz e.g. MyBean.class
//	 * @param classMap e.g. classMap.put("data", Person.class)
//	 * @return Object[]
//	 */
//	@SuppressWarnings("unchecked")
//	public static <T> T[] toObjects(String jsonArrayString, Class<?> clazz,
//									Map<String, Class<?>> classMap)
//	{
//		T[] results = null;
//		if (isJsonArray(jsonArrayString))
//		{
//			try
//			{
//				JSONArray jsonArray = JSONArray.fromObject(jsonArrayString);
//				results = (T[]) new Object[jsonArray.size()];
//				for (int i = 0; i < jsonArray.size(); i++)
//				{
//					JSONObject jsonObject = jsonArray.getJSONObject(i);
//					results[i] = toBean(jsonObject, clazz, classMap);
//				}
//			}
//			catch (Exception ex)
//			{
//				//log.warn(ex);
//				//ex.printStackTrace();
//			}
//		}
//		return results;
//	}
//
//	/**
//	 * 把一個json數組串轉換成存放普通類型元素的集合
//	 * 
//	 * @param jsonArrStr e.g. ['get',1,true,null]
//	 * @return List
//	 */
//	public static <E> List<E> toList(String jsonArrayString)
//	{
//		List<E> result = new LinkedList<E>();
//		if (jsonArrayString != null)
//		{
//			try
//			{
//				JSONArray jsonArray = JSONArray.fromObject(jsonArrayString);
//				for (int i = 0; i < jsonArray.size(); i++)
//				{
//					@SuppressWarnings("unchecked")
//					E element = (E) jsonArray.get(i);
//					result.add(element);
//				}
//			}
//			catch (Exception ex)
//			{
//				//log.warn(ex);
//				//ex.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 把一個json數組串轉換成集合，且集合裡存放的為實例Bean
//	 * 
//	 * @param jsonArrStr e.g. [{'name':'get'},{'name':'set'}]
//	 * @param clazz
//	 * @return List
//	 */
//	public static <E> List<E> toList(String jsonArrayString, Class<?> clazz)
//	{
//		return toList(jsonArrayString, clazz, null);
//	}
//
//	/**
//	 * 把一個json數組串轉換成集合，且集合裡的對象的屬性含有另外實例Bean
//	 * 
//	 * @param jsonArrStr e.g.
//	 *            [{'data':[{'name':'get'}]},{'data':[{'name':'set'}]}]
//	 * @param clazz e.g. MyBean.class
//	 * @param classMap e.g. classMap.put("data", Person.class)
//	 * @return List
//	 */
//	public static <E> List<E> toList(String jsonArrayString, Class<?> clazz,
//										Map<String, Class<?>> classMap)
//	{
//		List<E> result = new LinkedList<E>();
//		if (jsonArrayString != null)
//		{
//			try
//			{
//				JSONArray jsonArray = JSONArray.fromObject(jsonArrayString);
//				for (int i = 0; i < jsonArray.size(); i++)
//				{
//					JSONObject jsonObject = jsonArray.getJSONObject(i);
//					E element = toBean(jsonObject, clazz, classMap);
//					result.add(element);
//				}
//			}
//			catch (Exception ex)
//			{
//				//log.warn(ex);
//				//ex.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 把json對像串轉換成map對像
//	 * 
//	 * @param jsonObjStr e.g. {'name':'get','int':1,'double':1.1,'null':null}
//	 * @return Map
//	 */
//	@SuppressWarnings("unchecked")
//	public static <K, V> Map<K, V> toMap(String jsonArrayString)
//	{
//		Map<K, V> result = new LinkedHashMap<K, V>();
//		if (jsonArrayString != null)
//		{
//			try
//			{
//				JSONObject jsonObject = JSONObject.fromObject(jsonArrayString);
//				for (Object key : jsonObject.keySet())
//				{
//					result.put((K) key, (V) jsonObject.get(key));
//				}
//			}
//			catch (Exception ex)
//			{
//				//log.warn(ex);
//				//ex.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 把json對像串轉換成map對象，且map對像裡存放的為其他實體Bean
//	 * 
//	 * @param jsonObjStr e.g. {'data1':{'name':'get'},'data2':{'name':'set'}}
//	 * @param clazz e.g. Person.class
//	 * @return Map
//	 */
//	public static <K, V> Map<K, V> toMap(String jsonArrayString, Class<?> clazz)
//	{
//
//		return toMap(jsonArrayString, clazz, null);
//	}
//
//	/**
//	 * 把json對像串轉換成map對象，且map對像裡存放的其他實體Bean還含有另外實體Bean
//	 * 
//	 * @param jsonObjStr e.g. {'mybean':{'data':[{'name':'get'}]}}
//	 * @param clazz e.g. MyBean.class
//	 * @param classMap e.g. classMap.put("data", Person.class)
//	 * @return Map
//	 */
//	@SuppressWarnings("unchecked")
//	public static <K, V> Map<K, V> toMap(String jsonArrayString, Class<?> clazz, Map classMap)
//	{
//		Map<K, V> result = new LinkedHashMap<K, V>();
//		JSONObject jsonObject = JSONObject.fromObject(jsonArrayString);
//		for (Object key : jsonObject.keySet())
//		{
//			result.put((K) key, (V) toBean(jsonObject.getJSONObject((String) key), clazz, classMap));
//		}
//		return result;
//	}
//
//	public static String toString(Object value)
//	{
//		return toString(value, null);
//	}
//
//	/**
//	 * 把實體Bean、Map對像、數組、列表集合轉換成Json串
//	 * 
//	 * @param obj
//	 * @return
//	 * @throws Exception String
//	 */
//	public static String toString(Object value, JsonConfig jsonConfig)
//	{
//		String result = null;
//		JSON json = fromObject(value, jsonConfig);
//		if (json != null)
//		{
//			result = json.toString();
//		}
//		return (result != null ? result : "{}");
//	}
//
//	public static String toXml(Object value)
//	{
//		return toXml(value, null);
//	}
//
//	/**
//	 * 把json串、數組、集合(collection map)、實體Bean轉換成XML XMLSerializer API：
//	 * http://json-lib
//	 * .sourceforge.net/apidocs/net/sf/json/xml/XMLSerializer.html 具體實例請參考：
//	 * http://json-lib.sourceforge.net/xref-test/net/sf/json/xml/
//	 * TestXMLSerializer_writes.html
//	 * http://json-lib.sourceforge.net/xref-test/net
//	 * /sf/json/xml/TestXMLSerializer_writes.html
//	 * 
//	 * @param obj
//	 * @return
//	 * @throws Exception String
//	 */
//	public static String toXml(Object value, JsonConfig jsonConfig)
//	{
//		String result = null;
//		if (value != null)
//		{
//			try
//			{
//				XMLSerializer xmlSerializer = new XMLSerializer();
//				JSON json = fromObject(value, jsonConfig);
//				result = xmlSerializer.write(json);
//			}
//			catch (Exception ex)
//			{
//				//log.warn(ex);
//				//ex.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 從XML轉json串
//	 * 
//	 * @param xml
//	 * @return String
//	 */
//	public static String toStringFromXml(String xmlValue)
//	{
//		String result = null;
//		if (xmlValue != null)
//		{
//			try
//			{
//				XMLSerializer xmlSerial = new XMLSerializer();
//				JSON json = xmlSerial.read(xmlValue);
//				if (json != null)
//				{
//					result = json.toString();
//				}
//
//			}
//			catch (Exception ex)
//			{
//				//log.warn(ex);
//				//ex.printStackTrace();
//			}
//		}
//		return (result != null ? result : "{}");
//	}
//
//	public static boolean write(OutputStream outputStream, Object value)
//	{
//		return write(outputStream, value, null);
//	}
//
//	/**
//	 * 物件 -> aaa.json jsonString -> aaa.json
//	 * 
//	 * @param outputStream 處理完後,會將outputStream關閉
//	 * @param value
//	 * @return
//	 */
//	public static boolean write(OutputStream outputStream, Object value, JsonConfig jsonConfig)
//	{
//		boolean result = false;
//		OutputStream out = null;
//		try
//		{
//			out = new BufferedOutputStream(outputStream);
//			if (out != null)
//			{
//				String jsonString = toString(value, jsonConfig);
//				out.write(jsonString.getBytes());
//				result = true;
//			}
//		}
//		catch (Exception ex)
//		{
//			//log.warn(ex);
//			ex.printStackTrace();
//		}
//		finally
//		{
//			//system.out不需關閉,以供輸出
//			//				if (outputStream != System.out)
//			//				{
//			IoHelper.close(out);
//			//				}
//		}
//		return result;
//	}
//
//	public static <T> T read(InputStream inputStream, Class<?> clazz)
//	{
//		return read(inputStream, null, clazz, null);
//	}
//
//	public static <T> T read(InputStream inputStream, JsonConfig jsonConfig, Class<?> clazz)
//	{
//		return read(inputStream, jsonConfig, clazz, null);
//	}
//
//	/**
//	 * aaa.json -> 物件
//	 * 
//	 * @param inputStream 處理完後,會將inputStream關閉
//	 * @param clazz
//	 * @return
//	 */
//	public static <T> T read(InputStream inputStream, JsonConfig jsonConfig, Class<?> clazz,
//								Map<String, Class<?>> classMap)
//	{
//		T result = null;
//		InputStream in = null;
//		try
//		{
//			in = new BufferedInputStream(inputStream);
//			if (in != null)
//			{
//				byte[] bytes = IoHelper.read(in);
//				StringBuilder sb = new StringBuilder(new String(bytes));
//				//System.out.println(sb);
//				result = toObject(sb.toString(), jsonConfig, clazz, classMap);
//			}
//		}
//		catch (Exception ex)
//		{
//			//log.warn(ex);
//			ex.printStackTrace();
//		}
//		finally
//		{
//			//system.in不需關閉,以供輸入
//			//				if (inputStream != System.in)
//			//				{
//			//System.out.println(t);
//			IoHelper.close(in);
//			//				}
//		}
//		return result;
//	}
}
