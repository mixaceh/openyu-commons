package org.openyu.commons.awt.icon;

import static org.junit.Assert.*;

import javax.swing.Icon;

import org.junit.Test;
import org.openyu.commons.awt.icon.IconHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class IconHelperTest extends BaseTestSupporter {

	@Test
	// 1000 times: 416 mills.
	// 1000 times: 420 mills.
	// 1000 times: 419 mills.
	public void createIcon() {
		Icon result = null;
		//
		int count = 1;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = IconHelper.createIcon("src/test/config/icon/java-site.png");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertNotNull(result);
	}

}
