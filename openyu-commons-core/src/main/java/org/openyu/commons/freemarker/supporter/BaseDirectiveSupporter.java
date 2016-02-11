package org.openyu.commons.freemarker.supporter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.openyu.commons.freemarker.BaseDirective;
import org.openyu.commons.freemarker.FreeMarkerHelper;
import org.openyu.commons.mark.Supporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 標籤
 */
public abstract class BaseDirectiveSupporter implements BaseDirective, Supporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseDirectiveSupporter.class);

	public BaseDirectiveSupporter() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		try {
			// --------------------------------------------------
			doExecute(env, params, loopVars, body);
			// --------------------------------------------------
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during execute()").toString(), e);
			throw e;
		}
	}

	/**
	 * 內部執行
	 */
	protected abstract void doExecute(Environment env, Map<String, TemplateModel> params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException;

	/**
	 * 
	 * @param env
	 * @param body
	 * @param variables
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void render(Environment env, TemplateDirectiveBody body, Map<String, TemplateModel> variables)
			throws TemplateException, IOException {
		Map<String, TemplateModel> origVariables = addVariables(env, variables);
		body.render(env.getOut());
		removeVariables(env, variables, origVariables);
	}

	/**
	 * 將要輸出的TemplateModel放到env的variable中
	 * 
	 * @param env
	 * @param variables
	 * @return
	 * @throws TemplateException
	 */
	public Map<String, TemplateModel> addVariables(Environment env, Map<String, TemplateModel> variables)
			throws TemplateException {
		Map<String, TemplateModel> origVariables = new HashMap<String, TemplateModel>();
		if (variables.size() <= 0) {
			return origVariables;
		}
		//
		for (Map.Entry<String, TemplateModel> entry : variables.entrySet()) {
			String key = entry.getKey();
			TemplateModel value = env.getVariable(key);
			if (value != null) {
				origVariables.put(key, value);
			}
			env.setVariable(key, entry.getValue());
		}
		return origVariables;
	}

	/**
	 * 
	 * 移出先前放入env的variable中,TemplateModel
	 * 
	 * @param env
	 * @param variables
	 * @param origVariables
	 */
	public void removeVariables(Environment env, Map<String, TemplateModel> variables,
			Map<String, TemplateModel> origVariables) throws TemplateException {
		if (variables.size() <= 0) {
			return;
		}
		//
		for (String key : variables.keySet()) {
			env.setVariable(key, origVariables.get(key));
		}
	}

	// ----------------------------------------------------------------
	// 只是為了簡化寫法
	// ----------------------------------------------------------------

	protected boolean toBoolean(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toBoolean(name, params);
	}

	protected char toChar(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toChar(name, params);
	}

	protected String toString(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toString(name, params);
	}

	protected byte toByte(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toByte(name, params);
	}

	protected short toShort(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toShort(name, params);
	}

	protected int toInt(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toInt(name, params);
	}

	protected long toLong(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toLong(name, params);
	}

	protected float toFloat(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toFloat(name, params);
	}

	protected double toDouble(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toDouble(name, params);
	}

	protected Date toDate(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toDate(name, params);
	}

	protected Timestamp toTimestamp(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toTimestamp(name, params);
	}

	protected Locale toLocale(String name, Map<String, TemplateModel> params) {
		return FreeMarkerHelper.toLocale(name, params);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
