package org.openyu.commons.freemarker;

import freemarker.template.TemplateDirectiveModel;

/**
 * 標籤
 */
public interface BaseDirective extends TemplateDirectiveModel {
	String KEY = BaseDirective.class.getName();

	/**
	 * 輸出集合結果
	 */
	String TAG_RESULTS = "tag_results";

	/**
	 * 輸出結果
	 */
	String TAG_RESULT = "tag_result";

	/**
	 * 傳入參數: 序號
	 */
	String PARAM_SEQ = "seq";

	/**
	 * 傳入參數: id
	 */
	String PARAM_ID = "id";

	/**
	 * 傳入參數: 唯一
	 */
	String PARAM_ONLY = "only";

}
