package org.openyu.commons.dom4j;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import org.openyu.commons.io.FileHelper;
import org.openyu.commons.lang.EncodingHelper;
import org.openyu.commons.mark.Supporter;

public class Dom4jHelper implements Supporter
{

	private static Dom4jHelper instance;

	public Dom4jHelper()
	{}

	public static synchronized Dom4jHelper getInstance()
	{
		if (instance == null)
		{
			instance = new Dom4jHelper();
		}
		return instance;
	}

	public static synchronized Document read(String fileName)
	{
		return read(new File(fileName));
	}

	public static synchronized Document read(File file)
	{
		Document result = null;
		URL url = FileHelper.toUrl(file);

		if (url != null)
		{
			result = read(url);
		}
		return result;
	}

	/**
	 * 讀取xml
	 * 
	 * @param url URL
	 * @return Document
	 */
	public static synchronized Document read(URL url)
	{
		Document document = null;
		try
		{
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(url);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return document;
	}

	public static synchronized Document read(InputStream in)
	{
		Document document = null;
		try
		{
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(in);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return document;
	}

	public static synchronized boolean write(Document document, String fileName)
	{
		return write(document, new File(fileName), EncodingHelper.UTF_8);
	}

	public static synchronized boolean write(Document document, String fileName, String encoding)
	{
		return write(document, new File(fileName), encoding);
	}

	public static synchronized boolean write(Document document, File file)
	{
		return write(document, file, EncodingHelper.UTF_8);
	}

	public static synchronized boolean write(Document document, File file, String encoding)
	{
		boolean succeeded = true;
		XMLWriter out = null;
		try
		{
			out = new XMLWriter(new FileOutputStream(file), createOutputFormat(encoding));
			out.write(document);
		}
		catch (Exception ex)
		{
			succeeded = false;
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (IOException iex)
			{

			}
		}
		return succeeded;
	}

	/**
	 * 字串轉xml
	 * 
	 * @param text String
	 * @return Document
	 */
	public static Document parse(String xmlText)
	{
		Document document = null;
		try
		{
			document = DocumentHelper.parseText(xmlText);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return document;
	}

	/**
	 * xml 轉字串
	 * 
	 * @param document Document
	 * @return String
	 */
	public static String toString(Document document)
	{
		return document.asXML();
	}

	public static OutputFormat createOutputFormat()
	{
		return createOutputFormat(null);
	}

	/**
	 * 建構格式化
	 * 
	 * @param encoding String
	 * @return OutputFormat
	 */
	public static OutputFormat createOutputFormat(String encoding)
	{
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(encoding);
		// format.setIndent(StringUtil.TAB);
		format.setIndent("  ");
		format.setLineSeparator(System.getProperty("line.separator"));
		format.setTrimText(true);
		return format;
	}

	public static XMLWriter createXMLWriter()
	{
		return createXMLWriter(null);
	}

	public static XMLWriter createXMLWriter(String encoding)
	{
		XMLWriter out = null;
		try
		{
			out = new XMLWriter(createOutputFormat(encoding));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return out;
	}

	// ----------------------------------------------------
	public static String evalXpath(String xpath)
	{
		if (xpath != null)
		{
			// "@lang"
			return (xpath.charAt(0) != 64) ? "@" + xpath : xpath;
		}
		return null;
	}

	// ----------------------------------------------------
	public static String valueOf(Node node, String xpath)
	{
		return valueOf(node, xpath, true);
	}

	// <html lang="zh-TW">
	// node.valueof("@lang") -> zh_tw
	public static String valueOf(Node node, String xpath, boolean emptyString)
	{
		if (node != null)
		{
			return node.valueOf(evalXpath(xpath));
		}
		return null;
	}

	// --------------------------------------------------------
	public static Node selectNode(Document document, String xpath)
	{
		if (document != null && xpath != null)
		{
			Node node = document.selectSingleNode(xpath);
			return node;
		}
		return null;
	}

	public static List selectNodes(Document document, String xpath)
	{
		if (document != null && xpath != null)
		{
			List list = document.selectNodes(xpath);
			return list;
		}
		return null;
	}

	// --------------------------------------------------------
	//	public static void trace(String fileName)
	//	{
	//		trace(null, fileName);
	//	}
	//
	//	public static void trace(Object source, String fileName)
	//	{
	//		trace(source, fileName, null);
	//	}

	//	public static void trace(Object source, String fileName, String encoding)
	//	{
	//		trace(source, new File(fileName), encoding);
	//	}
	//
	//	//
	//	public static void trace(File file)
	//	{
	//		trace(null, file);
	//	}

	//	public static void trace(Object source, File file)
	//	{
	//		trace(source, file, null);
	//	}
	//
	//	public static void trace(Object source, File file, String encoding)
	//	{
	//		trace(source, FileHelper.toURI(file), encoding);
	//	}

	//

	public static void trace(URL url)
	{
		trace(null, url);
	}

	public static void trace(Object source, URL url)
	{
		trace(source, url, null);
	}

	public static void trace(Object source, URL url, String encoding)
	{
		Document document = read(url);
		trace(source, document, encoding);
	}

	//
	public static void trace(Document document)
	{
		trace(null, document, null);
	}

	public static void trace(Object source, Document document)
	{
		trace(source, document, null);
	}

	public static void trace(Object source, Document document, String encoding)
	{
		XMLWriter out = null;
		try
		{
			if (source != null)
			{
				System.out.println(source);
			}
			out = createXMLWriter(encoding);
			out.write(document);
			out.flush();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
