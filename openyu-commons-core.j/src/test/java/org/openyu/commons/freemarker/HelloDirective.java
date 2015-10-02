package org.openyu.commons.freemarker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openyu.commons.freemarker.supporter.BaseDirectiveSupporter;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class HelloDirective extends BaseDirectiveSupporter
{
	protected static final String RESULTS = "results";

	public HelloDirective()
	{}

	public void directive(Environment env, Map params, TemplateModel loopVars[],
							TemplateDirectiveBody body) throws TemplateException, IOException
	{
		List<String> countries = new LinkedList<String>();
		countries.add("India");
		countries.add("United States");
		countries.add("Germany");
		countries.add("France");
		//
		env.setVariable(RESULTS, ObjectWrapper.DEFAULT_WRAPPER.wrap(countries));
		if (body != null)
		{
			body.render(env.getOut());
		}
	}
}
