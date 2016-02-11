package org.openyu.commons.jxl;

import org.openyu.commons.lang.BooleanHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.write.WritableWorkbook;

public class JxlHelper {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(JxlHelper.class);

	public JxlHelper() {
	}

	public static void writeClose(WritableWorkbook writableWorkbook) {
		if (writableWorkbook != null) {
			try {
				writableWorkbook.write();
				writableWorkbook.close();
			} catch (Exception ex) {
			}
		}
	}

	public static void write(WritableWorkbook writableWorkbook) {
		if (writableWorkbook != null) {
			try {
				writableWorkbook.write();
			} catch (Exception ex) {
			}
		}
	}

}
