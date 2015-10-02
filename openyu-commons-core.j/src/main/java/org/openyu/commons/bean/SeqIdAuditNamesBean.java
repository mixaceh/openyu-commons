package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.bean.aware.AuditBeanAware;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 * SeqIdAuditNamesBean => SeqIdAuditNamesEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface SeqIdAuditNamesBean extends SeqBean, IdBean, AuditBeanAware, NamesBean
{

	String KEY = SeqIdAuditNamesBean.class.getName();

}
