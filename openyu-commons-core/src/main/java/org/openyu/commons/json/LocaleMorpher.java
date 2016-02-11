package org.openyu.commons.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//import net.sf.ezmorph.MorphException;
//import net.sf.ezmorph.object.AbstractObjectMorpher;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import org.openyu.commons.util.LocaleHelper;

//public final class LocaleMorpher extends AbstractObjectMorpher
public final class LocaleMorpher 
{
//	private Date defaultValue;
//
//	private String[] formats;
//
//	private boolean lenient;
//
//	private Locale locale;
//
//	public LocaleMorpher(String[] formats)
//	{
//		this(formats, LocaleHelper.getLocale(), false);
//	}
//
//	public LocaleMorpher(String[] formats, boolean lenient)
//	{
//		this(formats, LocaleHelper.getLocale(), lenient);
//	}
//
//	public LocaleMorpher(String[] formats, Date defaultValue)
//	{
//		this(formats, defaultValue, LocaleHelper.getLocale(), false);
//	}
//
//	public LocaleMorpher(String[] formats, Date defaultValue, Locale locale, boolean lenient)
//	{
//		super(true);
//		if (formats == null || formats.length == 0)
//		{
//			throw new MorphException("invalid array of formats");
//		}
//		// should use defensive copying ?
//		this.formats = formats;
//
//		if (locale == null)
//		{
//			this.locale = LocaleHelper.getLocale();
//		}
//		else
//		{
//			this.locale = locale;
//		}
//
//		this.lenient = lenient;
//		setDefaultValue(defaultValue);
//	}
//
//	public LocaleMorpher(String[] formats, Locale locale)
//	{
//		this(formats, locale, false);
//	}
//
//	public LocaleMorpher(String[] formats, Locale locale, boolean lenient)
//	{
//		if (formats == null || formats.length == 0)
//		{
//			throw new MorphException("invalid array of formats");
//		}
//		// should use defensive copying ?
//		this.formats = formats;
//
//		if (locale == null)
//		{
//			this.locale = LocaleHelper.getLocale();
//		}
//		else
//		{
//			this.locale = locale;
//		}
//
//		this.lenient = lenient;
//	}
//
//	public boolean equals(Object obj)
//	{
//		if (this == obj)
//		{
//			return true;
//		}
//		if (obj == null)
//		{
//			return false;
//		}
//
//		if (!(obj instanceof LocaleMorpher))
//		{
//			return false;
//		}
//
//		LocaleMorpher other = (LocaleMorpher) obj;
//		EqualsBuilder builder = new EqualsBuilder();
//		builder.append(formats, other.formats);
//		builder.append(locale, other.locale);
//		builder.append(lenient, other.lenient);
//		if (isUseDefault() && other.isUseDefault())
//		{
//			builder.append(getDefaultValue(), other.getDefaultValue());
//			return builder.isEquals();
//		}
//		else if (!isUseDefault() && !other.isUseDefault())
//		{
//			return builder.isEquals();
//		}
//		else
//		{
//			return false;
//		}
//	}
//
//	public Date getDefaultValue()
//	{
//		return (Date) defaultValue.clone();
//	}
//
//	public int hashCode()
//	{
//		HashCodeBuilder builder = new HashCodeBuilder();
//		builder.append(formats);
//		builder.append(locale);
//		builder.append(lenient);
//		if (isUseDefault())
//		{
//			builder.append(getDefaultValue());
//		}
//		return builder.toHashCode();
//	}
//
//	public Object morph(Object value)
//	{
//		if (value == null)
//		{
//			return null;
//		}
//
//		if (Date.class.isAssignableFrom(value.getClass()))
//		{
//			return (Date) value;
//		}
//
//		if (!supports(value.getClass()))
//		{
//			throw new MorphException(value.getClass() + " is not supported");
//		}
//
//		String strValue = (String) value;
//		SimpleDateFormat dateParser = null;
//
//		for (int i = 0; i < formats.length; i++)
//		{
//			if (dateParser == null)
//			{
//				dateParser = new SimpleDateFormat(formats[i], locale);
//			}
//			else
//			{
//				dateParser.applyPattern(formats[i]);
//			}
//			dateParser.setLenient(lenient);
//			try
//			{
//				return dateParser.parse(strValue.toLowerCase());
//			}
//			catch (ParseException pe)
//			{
//				// ignore exception, try the next format
//			}
//		}
//
//		// unable to parse the date
//		if (isUseDefault())
//		{
//			return defaultValue;
//		}
//		else
//		{
//			throw new MorphException("Unable to parse the date " + value);
//		}
//	}
//
//	public Class morphsTo()
//	{
//		return Date.class;
//	}
//
//	public void setDefaultValue(Date defaultValue)
//	{
//		this.defaultValue = (Date) defaultValue.clone();
//	}
//
//	public boolean supports(Class clazz)
//	{
//		return String.class.isAssignableFrom(clazz);
//	}
}