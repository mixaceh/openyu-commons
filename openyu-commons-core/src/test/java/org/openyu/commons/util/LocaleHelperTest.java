package org.openyu.commons.util;

import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.lang.SystemHelper;

public class LocaleHelperTest {

	@Test
	public void getLocale() {
		Locale result = LocaleHelper.getLocale();
		System.out.println(result);// zh_TW
		System.out.println(result.getLanguage());// zh
		System.out.println(result.getCountry());// TW
		System.out.println(result.getVariant());//
	}

	@Test
	public void getAvailableLocales() {
		SystemHelper.println(Locale.getAvailableLocales());
	}

}
