package org.openyu.commons.entity.supporter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import org.openyu.commons.entity.NameEntity;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public class NameEntitySupporter extends BaseEntitySupporter implements NameEntity, Supporter
{

	private static final long serialVersionUID = 1689473950244958942L;

	private String name;

	public NameEntitySupporter()
	{}

	@Column(name = "name", length = 50)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("name", name);
		return builder.toString();
	}

	public Object clone()
	{
		NameEntitySupporter copy = null;
		copy = (NameEntitySupporter) super.clone();
		return copy;
	}
}
