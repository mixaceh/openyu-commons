package org.openyu.commons.entity.supporter;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.MappedSuperclass;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openyu.commons.entity.AuditEntity;
import org.openyu.commons.entity.BaseEntity;
import org.openyu.commons.entity.LocaleNameEntity;
import org.openyu.commons.entity.NamesEntity;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.util.DateHelper;

@MappedSuperclass
public class BaseEntitySupporter extends BaseModelSupporter implements BaseEntity, Supporter
{

	private static final long serialVersionUID = 5611550004049774822L;

	public BaseEntitySupporter()
	{}

	public String toString()
	{
		return "";
	}

	protected void append(ToStringBuilder builder, String fieldName,
										NamesEntity namesEntity)
	{
		if (namesEntity != null)
		{
			Map<Locale, String> buff = new LinkedHashMap<Locale, String>();
			for (LocaleNameEntity entry : namesEntity.getNames())
			{
				buff.put(entry.getLocale(), entry.getName());
			}
			builder.append(fieldName, buff);
		}
		else
		{
			builder.append(fieldName, (Object) null);
		}
	}
	
	protected void append(ToStringBuilder builder, String fieldName, Set<LocaleNameEntity> names)
	{
		if (names != null)
		{
			Map<Locale, String> buff = new LinkedHashMap<Locale, String>();
			for (LocaleNameEntity entry : names)
			{
				buff.put(entry.getLocale(), entry.getName());
			}
			builder.append(fieldName, buff);
		}
		else
		{
			builder.append(fieldName, (Object) null);
		}
	}

	protected void append(ToStringBuilder builder, LocaleNameEntity localeNameEntity)
	{
		if (localeNameEntity != null)
		{
			builder.append("locale", localeNameEntity.getLocale());
			builder.append("name", localeNameEntity.getName());
		}
		else
		{
			builder.append("locale", (Object) null);
			builder.append("name", (Object) null);
		}
	}
	protected void append(ToStringBuilder builder, AuditEntity auditEntity)
	{
		if (auditEntity != null)
		{
			builder.append("createDate", DateHelper.toString(auditEntity.getCreateDate()));
			builder.append("createUser", auditEntity.getCreateUser());
			builder.append("modifiedDate", DateHelper.toString(auditEntity.getModifiedDate()));
			builder.append("modifiedUser", auditEntity.getModifiedUser());
		}
		else
		{
			builder.append("createDate", (Object) null);
			builder.append("createUser", (Object) null);
			builder.append("modifiedDate", (Object) null);
			builder.append("modifiedUser", (Object) null);
		}
	}
}
