package org.openyu.commons.velocity;

import java.io.File;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

public class VelocityHelper extends BaseHelperSupporter {

	private static transient final Logger log = LogManager.getLogger(VelocityHelper.class);

	private static VelocityHelper instance;

	// velocity 設定檔
	public static final String DEFAULT_CONFIG_FILE_NAME = "velocity.properties";

	public VelocityHelper() {
	}

	public static synchronized VelocityHelper getInstance() {
		if (instance == null) {
			instance = new VelocityHelper();
		}
		return instance;
	}

	public static boolean configure() {
		return configure((File) null);
	}

	public static boolean configure(URL url) {
		if (url != null) {
			return configure(url.getFile());
		}
		return configure((File) null);
	}

	public static boolean configure(String fileName) {
		if (fileName != null) {
			return configure(new File(fileName));
		} else {
			return configure((File) null);
		}
	}

	public static boolean configure(File file) {
		boolean result = false;
		try {
			if (file != null) {
				Velocity.init(file.getAbsolutePath());
			} else {
				// default
				// org/apache/velocity/runtime/defaults/velocity.properties
				Velocity.init();
			}
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static boolean configure(Properties properties) {
		boolean result = false;
		try {
			if (properties != null) {
				Velocity.init(properties);
			} else {
				// default
				// org/apache/velocity/runtime/defaults/velocity.properties
				Velocity.init();
			}
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * from the Velocity resource management system
	 * 
	 * @param name
	 * @return
	 */
	public static Template createTemplate(String name) {
		Template result = null;
		try {
			result = Velocity.getTemplate(name);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static VelocityContext createContext() {
		return new VelocityContext();
	}

	public static boolean merge(Template template, Context context, Writer writer) {
		boolean result = false;
		//
		try {
			if (template != null && context != null && writer != null) {
				template.merge(context, writer);
				result = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

}
