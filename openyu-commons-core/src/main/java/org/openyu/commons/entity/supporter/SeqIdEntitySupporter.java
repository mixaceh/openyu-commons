package org.openyu.commons.entity.supporter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import org.openyu.commons.entity.IdEntity;
import org.openyu.commons.entity.SeqIdEntity;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public abstract class SeqIdEntitySupporter extends SeqEntitySupporter implements SeqIdEntity,
		Supporter
{

	private static final long serialVersionUID = -8317992088888475132L;

	private IdEntity id = new IdEntitySupporter();

	public SeqIdEntitySupporter()
	{}

	@Column(name = "id", length = 255, unique = true)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	public String getId()
	{
		return id.getId();
	}

	public void setId(String id)
	{
		this.id.setId(id);
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("id", getId());
		return builder.toString();
	}

	public Object clone()
	{
		SeqIdEntitySupporter copy = null;
		copy = (SeqIdEntitySupporter) super.clone();
		copy.id = clone(id);
		return copy;
	}
}
