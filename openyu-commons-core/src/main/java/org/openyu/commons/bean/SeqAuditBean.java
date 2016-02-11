package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.bean.aware.AuditBeanAware;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 * SeqAuditBean => SeqAuditEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface SeqAuditBean extends SeqBean, AuditBeanAware
{

	String KEY = SeqAuditBean.class.getName();

}
