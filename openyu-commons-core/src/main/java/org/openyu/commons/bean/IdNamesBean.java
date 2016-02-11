package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * IdNamesBean => IdNamesEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface IdNamesBean extends IdBean, NamesBean
{

	String KEY = IdNamesBean.class.getName();

}
