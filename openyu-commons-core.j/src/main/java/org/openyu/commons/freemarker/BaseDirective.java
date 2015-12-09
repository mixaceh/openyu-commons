package org.openyu.commons.freemarker;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 標籤
 */
public interface BaseDirective extends TemplateDirectiveModel
{
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

	/**
	 * 停用
	 * 
	 * @return
	 */
	boolean isDisable();

	void setDisable(boolean disable);

	/**
	 * 記錄
	 * 
	 * @return
	 */
	boolean isLogEnable();

	void setLogEnable(boolean debug);

	void directive(Environment env, Map<String, TemplateModel> params, TemplateModel[] loopVars,
					TemplateDirectiveBody body) throws TemplateException, IOException;
}
