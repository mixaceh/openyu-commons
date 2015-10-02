package org.openyu.commons.editor;

import org.openyu.commons.model.BaseModel;

/**
 * 編輯器
 * 
 * xml-> excel
 * 
 * excel -> xml
 */
public interface BaseEditor extends BaseModel {
	String KEY = BaseEditor.class.getName();

	/**
	 * object -> excel
	 * 
	 * @return 輸出檔名
	 */
	String writeToExcel();

	/**
	 * excel -> object -> xml
	 * 
	 * @return 輸出檔名
	 */
	String writeToXml();
}
