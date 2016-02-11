package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.bean.aware.AuditBeanAware;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 * SeqIdAuditBean => SeqIdAuditEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface SeqIdAuditBean extends SeqBean, IdBean, AuditBeanAware
{

	String KEY = SeqIdAuditBean.class.getName();

}
