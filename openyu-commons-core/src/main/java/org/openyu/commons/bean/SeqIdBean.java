package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * SeqIdBean => SeqIdEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface SeqIdBean extends SeqBean, IdBean
{

	String KEY = SeqIdBean.class.getName();

}
