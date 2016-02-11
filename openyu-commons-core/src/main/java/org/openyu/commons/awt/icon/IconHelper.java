package org.openyu.commons.awt.icon;

import java.io.File;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.io.FileHelper;
import org.openyu.commons.lang.ArrayHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IconHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(IconHelper.class);

	private IconHelper() {
		throw new HelperException(
				new StringBuilder().append(IconHelper.class.getName()).append(" can not construct").toString());

	}

	public static Icon createIcon(String fileName) {
		return createIcon(new File(fileName));
	}

	public static Icon createIcon(File file) {
		return createIcon(FileHelper.toUrl(file));
	}

	public static Icon createIcon(URL url) {
		Icon result = null;
		//
		if (url == null) {
			throw new IllegalArgumentException("The Url must not be null");
		}
		//
		result = new ImageIcon(url);
		//
		return result;
	}

	// public static Icon hasIcon(String fileName)
	// {
	// return hasIcon(new File(fileName));
	// }
	//
	// public static Icon hasIcon(File file)
	// {
	// return hasIcon(FileHelper.hasURL(file));
	// }
	//
	// public static Icon hasIcon(URL url)
	// {
	// Icon icon = null;
	// if (url != null)
	// {
	// icon = new ImageIcon(url);
	// }
	// return icon;
	// }
	//
	// // 1.gif
	// public static URL hasIcon(String prefix, String name)
	// {
	// if (prefix != null && name != null)
	// {
	// return FileHelper.getResourceAsUrl(prefix + "/" + name);
	// }
	// else if (name != null)
	// {
	// return FileHelper.getResourceAsUrl(name);
	//
	// }
	// else
	// {
	// return null;
	// }
	// }
}
