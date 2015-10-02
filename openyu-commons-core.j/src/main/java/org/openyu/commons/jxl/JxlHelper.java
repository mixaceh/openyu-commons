package org.openyu.commons.jxl;

import jxl.write.WritableWorkbook;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class JxlHelper {

	private static transient final Logger log = LogManager
			.getLogger(JxlHelper.class);

	private static JxlHelper instance;

	public JxlHelper() {
	}

	public static synchronized JxlHelper getInstance() {
		if (instance == null) {
			instance = new JxlHelper();
		}
		return instance;
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
