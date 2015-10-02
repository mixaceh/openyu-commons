package org.openyu.commons.bean.supporter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.bean.IdBean;
import org.openyu.commons.bean.SeqIdBean;
import org.openyu.commons.bean.adapter.IdBeanXmlAdapter;

@XmlRootElement(name = "seqIdBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeqIdBeanSupporter extends SeqBeanSupporter implements SeqIdBean
{

	private static final long serialVersionUID = -7961050697548764075L;

	//@XmlElement(type = IdBeanSupporter.class) //<id><id>WPvQ</id></id>
	@XmlJavaTypeAdapter(IdBeanXmlAdapter.class)
	//<id>WPvQ</id>
	private IdBean id = new IdBeanSupporter();

	public SeqIdBeanSupporter()
	{}

	public String getId()
	{
		return id.getId();
	}

	public void setId(String id)
	{
		this.id.setId(id);
	}

	public String getDataId()
	{
		return id.getDataId();
	}

	public void setDataId(String dataId)
	{
		this.id.setDataId(dataId);
	}

	public boolean isOnly()
	{
		return id.isOnly();
	}

	public void setOnly(boolean only)
	{
		this.id.setOnly(only);
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
		SeqIdBeanSupporter copy = null;
		copy = (SeqIdBeanSupporter) super.clone();
		copy.id = clone(id);
		return copy;
	}
}
