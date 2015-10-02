package org.openyu.commons.freemarker;

import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.BooleanHelper;
import org.openyu.commons.lang.CharHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.util.DateHelper;
import org.openyu.commons.util.LocaleHelper;
import org.openyu.commons.util.TimestampHelper;

import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FreeMarkerHelper extends BaseHelperSupporter
{

	private static final transient Logger log = LogManager.getLogger(FreeMarkerHelper.class);

	private static FreeMarkerHelper instance;

	public FreeMarkerHelper()
	{}

	public static synchronized FreeMarkerHelper getInstance()
	{
		if (instance == null)
			instance = new FreeMarkerHelper();
		return instance;
	}

	public static boolean toBoolean(String name, Map<String, TemplateModel> params)
	{
		boolean result = false;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = BooleanHelper.toBoolean(buff.getAsString());
			}
			else if (model instanceof TemplateBooleanModel)
			{
				TemplateBooleanModel buff = (TemplateBooleanModel) model;
				result = buff.getAsBoolean();
			}
			else if (model instanceof TemplateNumberModel)
			{
				TemplateNumberModel buff = (TemplateNumberModel) model;
				result = BooleanHelper.toBoolean(String.valueOf(buff.getAsNumber()));
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static char toChar(String name, Map<String, TemplateModel> params)
	{
		char result = 0;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = CharHelper.toChar(buff.getAsString());
			}
			else if (model instanceof TemplateNumberModel)
			{
				TemplateNumberModel buff = (TemplateNumberModel) model;
				result = (char) buff.getAsNumber().intValue();
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static String toString(String name, Map<String, TemplateModel> params)
	{
		String result = null;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = buff.getAsString();
			}
			else if (model instanceof TemplateNumberModel)
			{
				TemplateNumberModel buff = (TemplateNumberModel) model;
				result = buff.getAsNumber().toString();
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static byte toByte(String name, Map<String, TemplateModel> params)
	{
		byte result = 0;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = NumberHelper.toByte(buff.getAsString());
			}
			else if (model instanceof TemplateNumberModel)
			{
				TemplateNumberModel buff = (TemplateNumberModel) model;
				result = buff.getAsNumber().byteValue();
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static short toShort(String name, Map<String, TemplateModel> params)
	{
		short result = 0;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = NumberHelper.toShort(buff.getAsString());
			}
			else if (model instanceof TemplateNumberModel)
			{
				TemplateNumberModel buff = (TemplateNumberModel) model;
				result = buff.getAsNumber().shortValue();
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static int toInt(String name, Map<String, TemplateModel> params)
	{
		int result = 0;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = NumberHelper.toInt(buff.getAsString());
			}
			else if (model instanceof TemplateNumberModel)
			{
				TemplateNumberModel buff = (TemplateNumberModel) model;
				result = buff.getAsNumber().intValue();
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static long toLong(String name, Map<String, TemplateModel> params)
	{
		long result = 0;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = NumberHelper.toLong(buff.getAsString());
			}
			else if (model instanceof TemplateNumberModel)
			{
				TemplateNumberModel buff = (TemplateNumberModel) model;
				result = buff.getAsNumber().longValue();
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static float toFloat(String name, Map<String, TemplateModel> params)
	{
		float result = 0f;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = NumberHelper.toFloat(buff.getAsString());
			}
			else if (model instanceof TemplateNumberModel)
			{
				TemplateNumberModel buff = (TemplateNumberModel) model;
				result = buff.getAsNumber().floatValue();
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static double toDouble(String name, Map<String, TemplateModel> params)
	{
		double result = 0d;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = NumberHelper.toDouble(buff.getAsString());
			}
			else if (model instanceof TemplateNumberModel)
			{
				TemplateNumberModel buff = (TemplateNumberModel) model;
				result = buff.getAsNumber().doubleValue();
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static Date toDate(String name, Map<String, TemplateModel> params)
	{
		Date result = null;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = DateHelper.toDate(buff.getAsString());
			}
			else if (model instanceof TemplateDateModel)
			{
				TemplateDateModel buff = (TemplateDateModel) model;
				result = buff.getAsDate();
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static Timestamp toTimestamp(String name, Map<String, TemplateModel> params)
	{
		Timestamp result = null;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = TimestampHelper.toTimestamp(buff.getAsString());
			}
			else if (model instanceof TemplateDateModel)
			{
				TemplateDateModel buff = (TemplateDateModel) model;
				result = new Timestamp(buff.getAsDate().getTime());
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}

	public static Locale toLocale(String name, Map<String, TemplateModel> params)
	{
		Locale result = null;
		try
		{
			TemplateModel model = (TemplateModel) params.get(name);
			if (model instanceof TemplateScalarModel)
			{
				TemplateScalarModel buff = (TemplateScalarModel) model;
				result = LocaleHelper.toLocale(buff.getAsString());
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return result;
	}
}

//	public static void processTemplate(String templatePath, String templateName,
//										String templateEncoding, Map<?, ?> root, Writer out)
//	{
//		try
//		{
//			Configuration config = new Configuration();
//			File file = new File(templatePath);
//			//設置要解析的模板所在的目錄，並加載模板文件   
//			config.setDirectoryForTemplateLoading(file);
//			//設置包裝器，並將對像包裝為數據模型   
//			config.setObjectWrapper(new DefaultObjectWrapper());
//
//			//獲取模板,並設置編碼方式，這個編碼必須要與頁面中的編碼格式一致   
//			Template template = config.getTemplate(templateName, templateEncoding);
//			//合併數據模型與模板   
//
//			template.process(root, out);
//			out.flush();
//			out.close();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		catch (TemplateException e)
//		{
//			e.printStackTrace();
//		}
//	}