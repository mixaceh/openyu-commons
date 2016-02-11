package org.openyu.commons.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

/**
 * IdBean => IdEntity
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface IdBean extends BaseBean
{

	String KEY = IdBean.class.getName();

	/**
	 * id,唯一碼,不可重複,商業邏輯用
	 * 
	 * @return
	 */
	String getId();

	void setId(String id);

	/**
	 * 資料id,xml用
	 * 
	 * @return
	 */
	String getDataId();

	void setDataId(String dataId);

	/**
	 * 是否唯一,搜尋用, true用=,false用like
	 * 
	 * @return
	 */
	boolean isOnly();

	void setOnly(boolean only);
}
