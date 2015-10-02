package org.openyu.commons.entity;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openyu.commons.bean.LocaleNameBean;
import org.openyu.commons.entity.supporter.LocaleNameEntitySupporter;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.util.CollectionHelper;
import org.openyu.commons.util.DateHelper;

public class EntityHelper extends BaseHelperSupporter implements Supporter
{

	protected transient static final Logger log = LogManager.getLogger(EntityHelper.class);

	private static EntityHelper instance;

	public static final String DEFAULT_AUDIT_USER = "sys";

	public EntityHelper()
	{}

	public static synchronized EntityHelper getInstance()
	{
		if (instance == null)
		{
			instance = new EntityHelper();
		}
		return instance;
	}

	/**
	 * 設定稽核欄位
	 * 
	 * 原則:
	 * 
	 * 1.implements AuditEntity
	 * 
	 * 2.有 AuditEntity 的 field
	 * 
	 * @param entity
	 * @param modifiedUser
	 */
	public static <T> boolean audit(T entity, String modifiedUser)
	{
		boolean result = false;
		if (entity instanceof AuditEntity)
		{
			result = audit((AuditEntity) entity, modifiedUser);
		}
		else if (entity != null)
		{
			Field[] fields = ClassHelper.getDeclaredField(entity.getClass(), AuditEntity.class);
			if (fields.length > 0)
			{
				result = true;
				for (Field field : fields)
				{
					AuditEntity auditEntity = (AuditEntity) ClassHelper
							.getFieldValue(entity, field);
					result &= audit(auditEntity, modifiedUser);
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param auditEntity
	 * @param modifiedUser
	 * @return
	 */
	protected static boolean audit(AuditEntity auditEntity, String modifiedUser)
	{
		boolean result = false;
		if (auditEntity != null)
		{
			if (auditEntity.getCreateUser() == null && auditEntity.getCreateDate() == null)
			{
				auditEntity.setCreateUser(modifiedUser == null ? DEFAULT_AUDIT_USER : modifiedUser);
				auditEntity.setCreateDate(DateHelper.today());
				result = true;
			}
			else
			{
				auditEntity.setModifiedUser(modifiedUser == null ? DEFAULT_AUDIT_USER
						: modifiedUser);
				auditEntity.setModifiedDate(DateHelper.today());
				result = true;
			}
		}
		return result;
	}

	/**
	 * 依語系取得名稱
	 * 
	 * @param names
	 * @param locale
	 * @return
	 */
	public static <E extends LocaleNameEntity> String getName(Set<E> names, Locale locale)
	{
		String result = null;
		if (names != null && !names.isEmpty())
		{
			for (LocaleNameEntity entry : names)
			{
				if (locale != null && entry.getLocale() != null && locale.equals(entry.getLocale()))
				{
					result = entry.getName();
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param value
	 * @param locale
	 * @return
	 */
	protected static void processFilterName(NamesEntity value, Locale locale)
	{
		processFilterName(value, locale, null);
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param value
	 * @param locale
	 * @param name
	 */
	protected static void processFilterName(NamesEntity value, Locale locale, String name)
	{
		processFilterName(value.getNames(), locale, name);
	}

	/**
	 * 
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param values
	 * @param locale
	 * @return
	 */
	protected static void processFilterName(Collection<LocaleNameEntity> values, Locale locale)
	{
		processFilterName(values, locale, null);
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param values
	 * @param locale
	 * @param name
	 */
	protected static void processFilterName(Collection<LocaleNameEntity> values, Locale locale,
											String name)
	{
		if (locale != null && CollectionHelper.notEmpty(values))
		{
			LocaleNameEntity foundNameEntity = null;
			for (LocaleNameEntity localeNameEntity : values)
			{
				//有符合區域的LocaleNameEntity
				foundNameEntity = getFilterNameEntity(localeNameEntity, locale, name);
				if (foundNameEntity != null)
				{
					break;
				}
			}
			//
			values.clear();
			if (foundNameEntity != null)
			{
				values.add(foundNameEntity);
			}
		}
	}

	/**
	 * 
	 * 有符合區域的
	 * 
	 * @param value
	 * @param locale
	 * @param name
	 * @return
	 */
	protected static LocaleNameEntity getFilterNameEntity(LocaleNameEntity value, Locale locale,
															String name)
	{
		LocaleNameEntity result = null;
		//filter by locale
		if (locale != null && value.getLocale() != null && locale.equals(value.getLocale()))
		{
			result = value;
			//filter by name
			if (name != null && value.getName() != null && value.getName().indexOf(name) < 0)
			{
				result = null;
			}
		}
		return result;
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param value
	 * @param locale
	 */
	public static void filterName(Object value, Locale locale)
	{
		filterName(value, locale, null);
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param value
	 * @param locale
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public static void filterName(Object value, Locale locale, String name)
	{
		if (value == null)
		{
			return;
		}

		//取所有的field值
		List<Object> fieldValues = ClassHelper.getDeclaredFieldValue(value);
		for (Object fieldValue : fieldValues)
		{
			if (fieldValue instanceof NamesEntity)
			{
				NamesEntity namesEntity = (NamesEntity) fieldValue;
				processFilterName(namesEntity, locale, name);
			}
			//set
			else if (fieldValue instanceof Collection<?>)
			{
				boolean isLocaleNameEntity = false;
				LocaleNameEntity foundNameEntity = null;
				@SuppressWarnings("rawtypes")
				Collection buffs = (Collection) fieldValue;
				for (Object entry : buffs)
				{
					//不是LocaleNameEntity的Collection
					if (!(entry instanceof LocaleNameEntity))
					{
						break;
					}
					//是LocaleNameEntity的Collection
					isLocaleNameEntity = true;
					LocaleNameEntity localeNameEntity = (LocaleNameEntity) entry;
					foundNameEntity = getFilterNameEntity(localeNameEntity, locale, name);
					//有符合區域的LocaleNameEntity
					if (foundNameEntity != null)
					{
						break;
					}
				}
				//是LocaleNameEntity的Collection,且有符合區域的LocaleNameEntity
				if (isLocaleNameEntity)
				{
					buffs.clear();
					if (foundNameEntity != null)
					{
						buffs.add(foundNameEntity);
					}
				}
			}
		}
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param values
	 * @param locale
	 */
	public static void filterName(Collection<?> values, Locale locale)
	{
		filterName(values, locale, null);
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param values
	 * @param locale
	 * @param name
	 */
	public static void filterName(Collection<?> values, Locale locale, String name)
	{
		if (CollectionHelper.notEmpty(values))
		{
			for (Object entry : values)
			{
				filterName(entry, locale, name);
			}
		}
	}

	/**
	 * 名稱 bean -> entity
	 * 
	 * @param names
	 * @return
	 */
	public static Set<LocaleNameEntity> toNames(Set<LocaleNameBean> names)
	{
		Set<LocaleNameEntity> result = new LinkedHashSet<LocaleNameEntity>();
		if (CollectionHelper.notEmpty(names))
		{
			for (LocaleNameBean entry : names)
			{
				LocaleNameEntity buff = new LocaleNameEntitySupporter();
				buff.setName(entry.getName());
				buff.setLocale(entry.getLocale());
				result.add(buff);
			}
		}
		return result;
	}

}
