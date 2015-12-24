package org.openyu.commons.dog.vo;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.dog.vo.impl.DogImpl;

@XmlRootElement(name = "dogCollector")
@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
public class DogCollector
{
	private List<DogImpl> dogList = new LinkedList<DogImpl>();

	private Map<String, DogImpl> dogMap = new LinkedHashMap<String, DogImpl>();

	public DogCollector()
	{}

	public List<DogImpl> getDogList()
	{
		return dogList;
	}

	public void setDogList(List<DogImpl> dogList)
	{
		this.dogList = dogList;
	}

	//@XmlJavaTypeAdapter(DogMapAdapter.class)
	public Map<String, DogImpl> getDogMap()
	{
		return dogMap;
	}

	public void setDogMap(Map<String, DogImpl> dogMap)
	{
		this.dogMap = dogMap;
	}

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
