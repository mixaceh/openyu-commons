package org.openyu.commons.entity.supporter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.entity.IdEntity;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public class IdEntitySupporter extends BaseEntitySupporter implements IdEntity, Supporter
{

	private static final long serialVersionUID = 2289173166789262806L;

	private String id;

	public IdEntitySupporter()
	{}

	@Column(name = "id", length = 255, unique = true)
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("id", id);
		return builder.toString();
	}

	public Object clone()
	{
		IdEntitySupporter copy = null;
		copy = (IdEntitySupporter) super.clone();
		return copy;
	}
}
