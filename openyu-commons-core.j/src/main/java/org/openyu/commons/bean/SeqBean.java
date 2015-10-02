package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * SeqBean => SeqEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface SeqBean extends BaseBean
{

	String KEY = SeqBean.class.getName();

	/**
	 * DB序號
	 * 
	 * @return
	 */
	long getSeq();

	void setSeq(long seq);

	/**
	 * 版本號,樂觀鎖
	 * 
	 * @return
	 */
	int getVersion();

	void setVersion(int version);

}
