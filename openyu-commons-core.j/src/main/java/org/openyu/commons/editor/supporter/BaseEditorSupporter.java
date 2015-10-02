package org.openyu.commons.editor.supporter;

import java.io.File;
import java.util.Date;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.openyu.commons.bean.BeanCollector;
import org.openyu.commons.editor.BaseEditor;
import org.openyu.commons.lang.BooleanHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.util.ConfigHelper;
import org.openyu.commons.util.DateHelper;
import org.openyu.commons.util.LocaleHelper;

public abstract class BaseEditorSupporter extends BaseModelSupporter implements
		BaseEditor, Supporter {

	private static final long serialVersionUID = -5427571101171717261L;

	protected BeanCollector beanCollector = new BeanCollector();

	/**
	 * 宣告格式
	 */
	private WritableCellFormat defFormat = new WritableCellFormat();

	private WritableFont defFont = new WritableFont(WritableFont.ARIAL, 8);

	/**
	 * 標題格式
	 */
	private WritableCellFormat headerFormat = new WritableCellFormat();

	private WritableFont headerFont = new WritableFont(WritableFont.TIMES, 10);

	/**
	 * 數字格式
	 */
	private WritableCellFormat numberFormat = new WritableCellFormat(
			new NumberFormat(NumberHelper.DEFAULT_PATTERN));

	private WritableFont numberFont = new WritableFont(WritableFont.TIMES, 10);

	/**
	 * 字串格式
	 */
	private WritableCellFormat stringFormat = new WritableCellFormat();

	private WritableFont stringFont = new WritableFont(WritableFont.TIMES, 10);

	/**
	 * 布林格式
	 */
	private WritableCellFormat booleanFormat = new WritableCellFormat();

	private WritableFont booleanFont = new WritableFont(WritableFont.TIMES, 10);

	/**
	 * 日期格式
	 */
	private WritableCellFormat dateFormat = new WritableCellFormat(
			new DateFormat(DateHelper.DEFAULT_PATTERN));

	private WritableFont dateFont = new WritableFont(WritableFont.TIMES, 10);

	public BaseEditorSupporter() {
		try {
			// defFormat
			defFormat.setAlignment(Alignment.LEFT);
			defFormat.setVerticalAlignment(VerticalAlignment.TOP);
			defFont.setColour(Colour.GRAY_50);
			defFormat.setFont(defFont);

			// headFormat
			headerFormat.setBackground(Colour.ICE_BLUE);
			headerFormat.setAlignment(Alignment.CENTRE);
			headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			headerFormat.setWrap(true);
			headerFormat.setVerticalAlignment(VerticalAlignment.TOP);
			// headFont.setColour(Colour.WHITE);
			headerFormat.setFont(headerFont);

			// numberFormat
			numberFormat.setAlignment(Alignment.RIGHT);
			numberFormat.setVerticalAlignment(VerticalAlignment.TOP);
			numberFormat.setFont(numberFont);

			// stringFormat
			stringFormat.setAlignment(Alignment.LEFT);
			stringFormat.setWrap(true);
			stringFormat.setVerticalAlignment(VerticalAlignment.TOP);
			stringFormat.setFont(stringFont);

			// booleanFormat
			booleanFormat.setAlignment(Alignment.LEFT);
			booleanFormat.setVerticalAlignment(VerticalAlignment.TOP);
			booleanFormat.setFont(booleanFont);

			// dateFormat
			dateFormat.setAlignment(Alignment.LEFT);
			dateFormat.setVerticalAlignment(VerticalAlignment.TOP);
			dateFormat.setFont(dateFont);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * excel檔名
	 * 
	 * @param clazz
	 * @return
	 */
	protected String excelName(Class<?> clazz) {
		StringBuilder result = new StringBuilder();
		result.append(ConfigHelper.getExcelDir());
		result.append(File.separator);
		result.append(clazz.getSimpleName());
		result.append(".xls");
		return result.toString();
	}

	protected void addNumber(WritableSheet sheet, int column, int row,
			double value) {
		addNumber(sheet, column, row, value, numberFormat);
	}

	protected void addNumber(WritableSheet sheet, int column, int row,
			double value, CellFormat cellFormat) {
		try {
			jxl.write.Number label = null;
			if (cellFormat != null) {
				label = new jxl.write.Number(column, row, value, cellFormat);
			} else {
				label = new jxl.write.Number(column, row, value);
			}
			sheet.addCell(label);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void addBoolean(WritableSheet sheet, int column, int row,
			boolean value) {
		addBoolean(sheet, column, row, value, booleanFormat);
	}

	protected void addBoolean(WritableSheet sheet, int column, int row,
			boolean value, CellFormat cellFormat) {
		try {
			jxl.write.Boolean label = null;
			if (cellFormat != null) {
				label = new jxl.write.Boolean(column, row, value, cellFormat);
			} else {
				label = new jxl.write.Boolean(column, row, value);
			}
			sheet.addCell(label);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void addDate(WritableSheet sheet, int column, int row, Date value) {
		addDate(sheet, column, row, value, dateFormat);
	}

	protected void addDate(WritableSheet sheet, int column, int row,
			Date value, CellFormat cellFormat) {
		try {
			jxl.write.DateTime label = null;
			if (cellFormat != null) {
				label = new jxl.write.DateTime(column, row, value, cellFormat);
			} else {
				label = new jxl.write.DateTime(column, row, value);
			}
			sheet.addCell(label);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected <T> void addDef(WritableSheet sheet, int column, int row,
			T content) {
		addLabel(sheet, column, row, content, defFormat);
	}

	protected <T> void addHeader(WritableSheet sheet, int column, int row,
			T content) {
		addLabel(sheet, column, row, content, headerFormat);
	}

	protected <T> void addString(WritableSheet sheet, int column, int row,
			T content) {
		addLabel(sheet, column, row, content, stringFormat);
	}

	protected <T> void addLabel(WritableSheet sheet, int column, int row,
			T content) {
		addLabel(sheet, column, row, content, null);
	}

	protected <T> void addLabel(WritableSheet sheet, int column, int row,
			T content, CellFormat cellFormat) {
		try {
			Label label = null;
			if (cellFormat != null) {
				label = new Label(column, row,
						String.valueOf(content != null ? content : ""),
						cellFormat);
			} else {
				label = new Label(column, row,
						String.valueOf(content != null ? content : ""));
			}
			sheet.addCell(label);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> T toObject(String value, String className) {
		Object result = null;
		// 非空白才處理
		if (StringHelper.notBlank(value)) {
			if ("java.util.Locale".equals(className)) {
				result = LocaleHelper.toLocale(value);
			} else if ("java.lang.String".equals(className)) {
				result = value;
			} else if ("boolean".equals(className)
					|| "java.lang.Boolean".equals(className)) {
				result = BooleanHelper.toBoolean(value);
			} else if ("int".equals(className)
					|| "java.lang.Integer".equals(className)) {
				result = NumberHelper.toInt(value);
			} else if ("long".equals(className)
					|| "java.lang.Long".equals(className)) {
				result = NumberHelper.toLong(value);
			}
		}
		return (T) result;
	}

	public void close(WritableWorkbook book) {
		try {
			if (book != null) {
				book.close();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	public void close(Workbook book) {
		try {
			if (book != null) {
				book.close();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}
}
