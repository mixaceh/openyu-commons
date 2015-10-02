package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 * NameBean => NameEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface NameBean extends BaseBean
{

	String KEY = NameBean.class.getName();

	String getName();

	void setName(String name);

}
