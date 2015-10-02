package org.openyu.commons.security;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;

public class DigestHelper extends BaseHelperSupporter
{

	private static transient final Logger log = LogManager.getLogger(DigestHelper.class);

	private static DigestHelper instance;

	private DigestHelper()
	{}

	public static synchronized DigestHelper getInstance()
	{
		if (instance == null)
		{
			instance = new DigestHelper();
		}
		return instance;
	}

}
