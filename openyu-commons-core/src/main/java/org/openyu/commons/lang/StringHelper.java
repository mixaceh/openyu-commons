package org.openyu.commons.lang;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字串輔助類
 */
public final class StringHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(StringHelper.class);

	public static final String DEFAULT_VALUE = "";

	/** 鍵值符號 #$% */
	public static final String KEY_DELIMITER = "#$%";

	/** 標題字尾 _t */
	public static final String HEADER_SUFFIX = "_t";

	//
	public static final String BACKSPACE = "\b";

	public static final String FORM_FEED = "\f";

	// public static final String RETURN = "\r"; // Enter 鍵
	//
	// public static final String ENTER = RETURN;

	// public static final String TAB = "\t";/

	public static final String CR = "\r";// carriage return

	public static final String LF = "\n";// line feed

	//
	public static final String EMPTY = "";

	// d41d8cd98f00b204e9800998ecf8427e
	public static final String EMPTY_HASH = "d41d8cd98f00b204e9800998ecf8427e";

	public static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	// Ascii table
	public static final char SPACE_CHAR = ' ';

	public static final String SPACE = String.valueOf(SPACE_CHAR);

	public static final String SPACE_HTML = "&nbsp;";

	//
	public static final char EXCLAMATION_POINT_CHAR = '!';

	public static final String EXCLAMATION_POINT = String.valueOf(EXCLAMATION_POINT_CHAR);

	//
	public static final char DOUBLE_QUOTES_CHAR = '"';

	public static final String DOUBLE_QUOTES = String.valueOf(DOUBLE_QUOTES_CHAR);

	public static final String DOUBLE_QUOTES_HTML = "&quot;";

	//
	public static final char NUMBER_SIGN_CHAR = '#';

	public static final String NUMBER_SIGN = String.valueOf(NUMBER_SIGN_CHAR);

	//
	public static final char DOLLAR_SIGN_CHAR = '$';

	public static final String DOLLAR_SIGN = String.valueOf(DOLLAR_SIGN_CHAR);

	//
	public static final char PERCENT_SIGN_CHAR = '%';

	public static final String PERCENT_SIGN = String.valueOf(PERCENT_SIGN_CHAR);

	//
	public static final char AMPERSAND_CHAR = '&';

	public static final String AMPERSAND = String.valueOf(AMPERSAND_CHAR);

	public static final String AMPERSAND_HTML = "&amp;";

	//
	public static final char SINGLE_QUOTE_CHAR = '\'';

	public static final String SINGLE_QUOTE = String.valueOf(SINGLE_QUOTE_CHAR);

	//
	public static final char OPENING_PARENTHESIS_CHAR = '(';

	public static final String OPENING_PARENTHESIS = String.valueOf(OPENING_PARENTHESIS_CHAR);

	//
	public static final char CLOSING_PARENTHESIS_CHAR = ')';

	public static final String CLOSING_PARENTHESIS = String.valueOf(CLOSING_PARENTHESIS_CHAR);

	//
	public static final char ASTERISK_CHAR = '*';

	public static final String ASTERISK = String.valueOf(ASTERISK_CHAR);

	//
	public static final char PLUS_SIGN_CHAR = '+';

	public static final String PLUS_SIGN = String.valueOf(PLUS_SIGN_CHAR);

	//
	public static final char COMMA_CHAR = ',';

	public static final String COMMA = String.valueOf(COMMA_CHAR);

	public static final String COMMA_SPACE = COMMA_CHAR + SPACE;

	//
	public static final char MINUS_SIGN_CHAR = '-';

	public static final String MINUS_SIGN = String.valueOf(MINUS_SIGN_CHAR);

	//
	public static final char PERIOD_CHAR = '.';

	public static final String PERIOD = String.valueOf(PERIOD_CHAR);

	public static final char DOT_CHAR = PERIOD_CHAR;

	public static final String DOT = String.valueOf(DOT_CHAR);

	//
	public static final char SLASH_CHAR = '/';

	public static final String SLASH = String.valueOf(SLASH_CHAR);

	//
	public static final char COLON_CHAR = ':';

	public static final String COLON = String.valueOf(COLON_CHAR);

	//
	public static final char SEMICOLON_CHAR = ';';

	public static final String SEMICOLON = String.valueOf(SEMICOLON_CHAR);

	//
	public static final char LESS_THAN_SIGN_CHAR = '<';

	public static final String LESS_THAN_SIGN = String.valueOf(LESS_THAN_SIGN_CHAR);

	public static final String LESS_THAN_SIGN_HTML = "&lt;";

	//
	public static final char EQUAL_SIGN_CHAR = '=';

	public static final String EQUAL_SIGN = String.valueOf(EQUAL_SIGN_CHAR);

	//
	public static final char GREATER_THAN_SIGN_CHAR = '>';

	public static final String GREATER_THAN_SIGN = String.valueOf(GREATER_THAN_SIGN_CHAR);

	public static final String GREATER_THAN_SIGN_HTML = "&gt;";

	//
	public static final char QUESTION_MARK_CHAR = '?';

	public static final String QUESTION_MARK = String.valueOf(QUESTION_MARK_CHAR);

	//
	public static final char AT_SYMBOL_CHAR = '@';

	public static final String AT_SYMBOL = String.valueOf(AT_SYMBOL_CHAR);

	//
	public static final char OPENING_BRACKET_CHAR = '[';

	public static final String OPENING_BRACKET = String.valueOf(OPENING_BRACKET_CHAR);

	//
	public static final char BACKSLASH_CHAR = '\\';

	public static final String BACKSLASH = String.valueOf(BACKSLASH_CHAR);

	//
	public static final char CLOSING_BRACKET_CHAR = ']';

	public static final String CLOSING_BRACKET = String.valueOf(CLOSING_BRACKET_CHAR);

	//
	public static final char CARET_CHAR = '^';

	public static final String CARET = String.valueOf(CARET_CHAR);

	//
	public static final char UNDERSCORE_CHAR = '_';

	public static final String UNDERSCORE = String.valueOf(UNDERSCORE_CHAR);

	//
	public static final char OPENING_BRACE_CHAR = '{';

	public static final String OPENING_BRACE = String.valueOf(OPENING_BRACE_CHAR);

	//
	public static final char VERTICAL_BAR_CHAR = '|';

	public static final String VERTICAL_BAR = String.valueOf(VERTICAL_BAR_CHAR);

	//
	public static final char CLOSING_BRACE_CHAR = '}';

	public static final String CLOSING_BRACE = String.valueOf(CLOSING_BRACE_CHAR);

	//
	public static final char EQUIVALENCY_SIGN_CHAR = '~';

	public static final String EQUIVALENCY_SIGN = String.valueOf(EQUIVALENCY_SIGN_CHAR);

	//
	public static final char DASH_CHAR = '-';

	public static final String DASH = String.valueOf(DASH_CHAR);

	// 圈圈中的英文

	// ⓐ ⓑ ⓒ ⓓ ⓔ ⓕ ⓖ ⓗ ⓘ ⓙ ⓚ ⓛ ⓜ
	// ⓝ ⓞ ⓟ ⓠ ⓡ ⓢ ⓣ ⓤ ⓥ ⓦ ⓧ ⓨ ⓩ

	//
	public static final char CIRCLE_A_CHAR = 'ⓐ';

	public static final String CIRCLE_A = String.valueOf(CIRCLE_A_CHAR);

	//
	public static final char CIRCLE_C_CHAR = 'ⓒ';

	public static final String CIRCLE_C = String.valueOf(CIRCLE_C_CHAR);

	//
	public static final char CIRCLE_D_CHAR = 'ⓓ';

	public static final String CIRCLE_D = String.valueOf(CIRCLE_D_CHAR);

	//
	public static final char CIRCLE_E_CHAR = 'ⓔ';

	public static final String CIRCLE_E = String.valueOf(CIRCLE_E_CHAR);

	//
	public static final char CIRCLE_H_CHAR = 'ⓗ';

	public static final String CIRCLE_H = String.valueOf(CIRCLE_H_CHAR);

	//
	public static final char CIRCLE_V_CHAR = 'ⓥ';

	public static final String CIRCLE_V = String.valueOf(CIRCLE_V_CHAR);

	//
	public static final char CIRCLE_S_CHAR = 'ⓢ';

	public static final String CIRCLE_S = String.valueOf(CIRCLE_S_CHAR);

	// 特殊符號

	// ♠ ♥ ♦ ♣
	//
	public static final char SPADE_CHAR = '♠';

	public static final String SPADE = String.valueOf(SPADE_CHAR);// \u2660

	//
	public static final char HEART_CHAR = '♥';

	public static final String HEART = String.valueOf(HEART_CHAR);// \u2665

	//
	public static final char DIAMOND_CHAR = '♦';

	public static final String DIAMOND = String.valueOf(DIAMOND_CHAR);// \u2666

	//
	public static final char CLUB_CHAR = '♣';

	public static final String CLUB = String.valueOf(CLUB_CHAR);// \u2663

	private StringHelper() {
		throw new HelperException(
				new StringBuilder().append(StringHelper.class.getName()).append(" can not construct").toString());
	}

	public static boolean equals(String x, String y) {
		return ObjectHelper.equals(x, y);
	}

	public static boolean equalsIgnoreCase(String x, String y) {
		if ((x == null) || (y == null)) {
			return false;
		}
		return x.equalsIgnoreCase(y);
	}

	/**
	 * 空白函數
	 *
	 * @param length int 長度
	 * @return
	 */
	public static String space(int length) {
		return pad(SPACE, length);
	}

	/**
	 * 左取字串函數
	 *
	 * @param source String 來源字串
	 * @param length int 長度
	 * @return
	 */
	public static String left(String source, int length) {
		if (source == null) {
			return null;
		}
		return ((length > source.length()) ? source : source.substring(0, length));
	}

	/**
	 * 右取字串函數
	 *
	 * @param source String 來源字串
	 * @param length int 長度
	 * @return
	 */
	public static String right(String source, int length) {
		if (source == null) {
			return null;
		}
		return ((length > source.length()) ? source : source.substring(source.length() - length));

	}

	/**
	 * 填充函數
	 *
	 * @param source String
	 * @param length int 長度
	 * @return
	 */
	public static String pad(String source, int length) {
		if (source == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			sb.append(source);
		}
		return sb.substring(0, length).toString();
	}

	/**
	 * 移除左邊空白函數 消左邊空白函數
	 *
	 * @param value String
	 * @return
	 */
	public static String leftTrim(String value) {
		if (value == null) {
			return null;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!value.substring(i, i + 1).equals(SPACE)) {
				return value.substring(i);
			}
		}
		return value;
	}

	/**
	 * 移除右邊空白函數
	 *
	 * @param value String
	 * @return
	 */
	public static String rightTrim(String value) {
		if (value == null) {
			return null;
		}
		String ret = value;
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(value.length() - i - 1, value.length() - i);
			if (!temp.equals(SPACE)) {
				break;
			}
			ret = value.substring(0, value.length() - i - 1);
		}
		return ret;
	}

	/**
	 * 反轉字串函數
	 *
	 * @param source String
	 * @return
	 */
	public static String reverse(String source) {
		if (source == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < source.length(); i++) {
			sb.append(source.substring(source.length() - i - 1, source.length() - i));
		}
		return sb.toString();
	}

	/**
	 * 字首為大寫函數
	 *
	 * @param value String
	 * @return String
	 */
	public static String capitalize(String value) {
		return wordCase(value, true);
	}

	/**
	 * 字首為小寫函數
	 *
	 * @param value String
	 * @return String
	 */
	public static String uncapitalize(String value) {
		return wordCase(value, false);
	}

	protected static String wordCase(String value, boolean upper) {
		if (value == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(value, " ");
		StringBuilder result = new StringBuilder();
		while (st.hasMoreTokens()) {
			StringBuilder temp = new StringBuilder();
			temp.append(st.nextToken());
			char cur = temp.charAt(0);
			if (upper) {
				if (Character.isLowerCase(cur)) {
					result.append(Character.toUpperCase(cur) + temp.substring(1));
				} else {
					result.append(temp);
				}
			} else {
				if (Character.isUpperCase(cur)) {
					result.append(Character.toLowerCase(cur) + temp.substring(1));
				} else {
					result.append(temp);
				}
			}
			//
			if (st.hasMoreTokens()) {
				result.append(" ");
			}
		}
		return result.toString();
	}

	/**
	 * 重覆字串函數,與fill(String source, int length)不同 times為次數,不像fill為長度
	 *
	 * @param source String 來源字串
	 * @param times  int 次數
	 * @return
	 */
	public static String repeat(String source, int times) {
		if (source == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(source.length() * times);
		for (int i = 0; i < times; i++) {
			sb.append(source);
		}
		return sb.toString();
	}

	public static String rightPad(String source, int length) {
		return rightPad(source, length, SPACE);
	}

	/**
	 * 右邊補充總長度為length
	 *
	 * @param source String
	 * @param length int
	 * @param regex  String
	 * @return String
	 */
	public static String rightPad(String source, int length, String regex) {
		if (source == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		int len = length - source.length();
		if (len > 0) {
			result.append(source);
			result.append(pad(regex, len));
		} else {
			result.append(right(source, length));
		}
		return result.toString();

	}

	public static String leftPad(String source, int length) {
		return leftPad(source, length, SPACE);
	}

	/**
	 * 左邊補充總長度為length
	 *
	 * @param source String
	 * @param length int
	 * @param regex  String
	 * @return String
	 */
	public static String leftPad(String source, int length, String regex) {
		if (source == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		int len = length - source.length();
		if (len > 0) {
			result.append(pad(regex, len));
			result.append(source);
		} else {
			result.append(left(source, length));
		}
		return result.toString();

	}

	/**
	 * 傳回姓名,組合依國家有所不同
	 *
	 * @param firstName String
	 * @param mi        String
	 * @param lastName  String
	 * @return
	 */
	public static String formatUserName(String firstName, String mi, String lastName, Locale locale) {
		StringBuilder sb = new StringBuilder();
		firstName = ((firstName == null) ? "" : firstName);
		mi = ((mi == null || mi.equals("")) ? " " : " " + mi + ". ");
		lastName = ((lastName == null) ? "" : lastName);
		if (locale.equals(Locale.TRADITIONAL_CHINESE) || locale.equals(Locale.SIMPLIFIED_CHINESE)) {
			sb.append(lastName);
			sb.append(firstName);
		} else {
			sb.append(firstName);
			sb.append(mi);
			sb.append(lastName);
		}
		return sb.toString();
	}

	public static String strip(String command) {

		char c;
		if (command == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder(command);
			for (int i = 0; i < sb.length(); ++i) {
				c = sb.charAt(i);
				if (c == '\r' || c == '\n') {
					sb.setCharAt(i, ' ');
					continue;
				}
				if (c == '\\') {
					if (sb.length() <= (i + 1)) {
						// sb.removeCharsAt(i, 1);
						sb.delete(i, i + 1);
						continue;
					}
					if (sb.charAt(i + 1) == 'r' || sb.charAt(i + 1) == 'n') { // NORES
																				// sb.removeCharsAt(i,
																				// 2);
						sb.delete(i, i + 2);
						sb.insert(i, " ");
					}
				}
			}
			return sb.toString();
		}
	}

	// apache
	// StringUtils.splitPreserveAllTokens("ab:cd:ef::", ":") = ["ab", "cd",
	// "ef", "", ""]

	// /**
	// * 同String a.split(","),並增加是否包含整個字串 String a="aa,bb,cc";
	// * StringHelper.split(a,",") -> {aa, bb, cc}
	// *
	// * StringHelper.split(a,",",true) -> {aa, ,, bb, ,, cc}
	// *
	// * @param value String
	// * @param regex String
	// * @return String[]
	// */
	// 1、如果用“.”作为分隔的话，必须是如下写法：String.split("\\."),这样才能正确的分隔开，不能用String.split(".");
	// 2、如果用“|”作为分隔的话，必须是如下写法：String.split("\\|"),这样才能正确的分隔开，不能用String.split("|");
	// “.”和“|”都是转义字符，必须得加"\\";
	// 3、如果在一个字符串中有多个分隔符，可以用“|”作为连字符，比如：“acount=? and uu =? or
	// n=?”,把三个都分隔出来，可以用String.split("and|or");
	//
	// 不要用這個split,改用 StringUtils.splitPreserveAllTokens("ab:cd:ef::", ":") =
	// ["ab", "cd", "ef", "", ""]

	// public static String[] split(String value, String regex)
	// {
	// StringTokenizer st = new StringTokenizer(value, regex, true);
	//
	// List<String> fields = new LinkedList<String>();
	// while (st.hasMoreTokens())
	// {
	// String fieldValue = st.nextToken();
	// if (regex.equals(fieldValue))
	// {
	// fieldValue = "";
	// }
	// else if (st.hasMoreTokens())
	// {
	// st.nextToken();
	// }
	// fields.add(fieldValue);
	// }
	// return (String[]) fields.toArray(new String[] {});
	// }

	/**
	 * 結合陣列為字串 combine({"a","b"},", ") -> a, b
	 *
	 * @param values String[]
	 * @param regex  String
	 * @return String
	 */
	public static String combine(String[] values, String regex) {
		if (values == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) {
				continue;
			}
			sb.append(values[i]);
			if (i < values.length - 1) {
				sb.append(regex);
			}
		}
		return sb.toString();
	}

	/**
	 * 要置換的字串 把patten換成 replacement
	 * 
	 * @param value
	 * @param patten
	 * @param replacement
	 * @return
	 */
	public static String replace(String value, String patten, String replacement) {
		int pos = value.indexOf(patten);
		return pos < 0 ? value : replace(value, patten, replacement, pos);
	}

	/**
	 * 1.從頭開始找出符合的字串，並且標示第幾個字放進pos
	 * 
	 * 2.先把在pos前面的字放進結果區(newContent)
	 * 
	 * 3.再放進replacement
	 * 
	 * 4.再去找下一個字的位置放進pos
	 * 
	 * 5.接續步驟三直到全部找完為止
	 * 
	 * @param value       資料
	 * @param patten      要換掉的文字
	 * @param replacement 要改成的文字
	 * @param pos         那段文字在哪
	 * @return
	 */
	public static String replace(String value, String patten, String replacement, int pos) {
		if (value == null || patten == null || replacement == null) {
			return null;
		}
		//
		int len = value.length();
		int plen = patten.length();
		StringBuilder result = new StringBuilder(len);
		//
		int lastPos = 0;
		do {
			result.append(value, lastPos, pos);
			result.append(replacement);
			lastPos = pos + plen;
			pos = value.indexOf(patten, lastPos);
		} while (pos > 0);
		result.append(value, lastPos, len);
		return result.toString();
	}

	public static boolean contains(String value, String str) {
		return value != null && value.indexOf(str) != -1;
	}

	public static boolean containsMnemonic(String value) {
		return contains(value, AMPERSAND);
	}

	public static boolean contains(String[] array, String value) {
		return ArrayHelper.contains(array, value);
	}

	public static boolean containsIgnoreCase(String[] array, String value) {
		for (int i = 0; array != null && i < array.length; i++) {
			if (equalsIgnoreCase(array[i], value)) {
				return true;
			}
		}
		return false;
	}

	// &Ok => Ok
	public static String excludeMnemonic(String text) {
		if (text == null) {
			return null;
		}
		int pos = 0;
		while ((pos = text.indexOf(AMPERSAND_CHAR, pos)) != -1 && pos > 0 && text.charAt(pos - 1) == '\\') {
			pos = text.indexOf(AMPERSAND_CHAR, pos + 1);
			if (pos == -1) {
				break;
			}
		}
		if (pos != -1 && pos < text.length() - 1) {
			if (pos == 0) {
				text = text.substring(1);
			} else {
				text = text.substring(0, pos) + text.substring(pos + 1);
			}
		}
		return text;
	}

	// &Cancel => C,若無& => 傳回char=0;
	public static char extractMnemonic(String text) {
		if (text == null) {
			return 0;
		}
		int pos = 0;
		char mnemonicChar = 0;
		while ((pos = text.indexOf(AMPERSAND_CHAR, pos)) != -1 && pos > 0 && text.charAt(pos - 1) == '\\') {
			pos = text.indexOf(AMPERSAND_CHAR, pos + 1);
			if (pos == -1) {
				break;
			}
		}
		if (pos != -1 && pos < text.length() - 1) {
			mnemonicChar = text.charAt(pos + 1);
		}
		return mnemonicChar;
	}

	/**
	 * 是否為空白
	 *
	 * <pre>
	 * isBlank(null)      = true
	 * isBlank("")        = true
	 * isBlank(" ")       = true
	 * isBlank("bob")     = false
	 * isBlank("  bob  ") = false
	 * 判斷 null, length() == 0, 含有空白字串,如"  "
	 * </pre>
	 *
	 * @param value String
	 * @return boolean
	 */
	public static boolean isBlank(String value) {
		int len;
		if (value == null || (len = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < len; i++) {
			if ((!Character.isWhitespace(value.charAt(i)))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否不為空白
	 * 
	 * isNotBlank = hasText
	 *
	 * <pre>
	 * isNotBlank(null)      = false
	 * isNotBlank("")        = false
	 * isNotBlank(" ")       = false
	 * isNotBlank("bob")     = true
	 * isNotBlank("  bob  ") = true
	 * 判斷 !=null, length() > 0, 不含有空白字串,如"  "
	 * </pre>
	 *
	 * @param value String
	 * @return boolean
	 */
	public static boolean notBlank(String value) {
		int len;
		if (value == null || (len = value.length()) == 0) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if ((!Character.isWhitespace(value.charAt(i)))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否為空值
	 *
	 * <pre>
	 * isEmpty(null)      = true
	 * isEmpty("")        = true
	 * isEmpty(" ")       = false
	 * isEmpty("bob")     = false
	 * isEmpty("  bob  ") = false
	 * 只判斷 null, length() == 0
	 * </pre>
	 *
	 * @param value String
	 * @return boolean
	 */
	public static boolean isEmpty(String value) {
		return (value == null || value.length() == 0);
	}

	/**
	 * 是否不為空值
	 * 
	 * isNotEmpty = hasLength
	 *
	 * <pre>
	 * isEmpty(null)      = false
	 * isEmpty("")        = false
	 * isBlank(" ")       = true
	 * isBlank("bob")     = true
	 * isBlank("  bob  ") = true
	 * 只判斷 !=null, length() > 0
	 * </pre>
	 *
	 * @param value String
	 * @return boolean
	 */
	public static boolean notEmpty(String value) {
		return (value != null && value.length() > 0);
	}

	//
	public static String splashToDot(String value) {
		return value.replace(SLASH_CHAR, DOT_CHAR);
	}

	public static String dotToSlash(String value) {
		return value.replace(DOT_CHAR, SLASH_CHAR);
	}

	public static String dotToFileSeparator(String value) {
		return replace(value, DOT, SystemHelper.FILE_SEPARATOR);
	}

	public static String fileSeparatorToDot(String value) {
		return replace(value, SystemHelper.FILE_SEPARATOR, DOT);
	}

	/**
	 * 去除第一個字串
	 *
	 * @param value String
	 * @param regex String
	 * @return String
	 */
	public static String excludeFirst(String value, String regex) {
		StringBuilder result = new StringBuilder();
		//
		if (value == null || regex == null) {
			return null;
		}
		//
		int pos = value.indexOf(regex);
		if (pos != -1) {
			result.append(new String(value.substring(pos + regex.length())));
		} else {
			result.append(value);
		}
		return result.toString();
	}

	public static String excludeFirst(String value, String regex, int times) {
		if (times <= 1) {
			return excludeFirst(value, regex);
		}
		return excludeFirst(excludeFirst(value, regex), regex, times - 1);
	}

	/**
	 * 取出第一個字串
	 *
	 * @param value String
	 * @param regex String
	 * @return String
	 */
	public static String extractFirst(String value, String regex) {
		StringBuilder result = new StringBuilder();
		//
		if (value == null || regex == null) {
			return null;
		}
		//
		int pos = value.indexOf(regex);
		if (pos != -1) {
			result.append(value.substring(0, pos + regex.length() - 1));
		} else {
			result.append(value);
		}
		return result.toString();
	}

	public static String extractFirst(String value, String regex, int times) {
		if (times <= 1) {
			return extractFirst(value, regex);
		}
		return extractFirst(extractFirst(value, regex), regex, times - 1);
	}

	/**
	 * 去除最後一個字串
	 *
	 * @param value String
	 * @param regex String
	 * @return String
	 */
	public static String excludeLast(String value, String regex) {
		if (value == null || regex == null) {
			return null;
		}
		//
		StringBuilder sb = new StringBuilder();
		int index = value.lastIndexOf(regex);
		if (index > -1) {
			sb.append(value.substring(0, index));
		} else {
			sb.append(value);
		}
		return sb.toString();
	}

	public static String excludeLast(String value, String regex, int times) {
		if (times <= 1) {
			return excludeLast(value, regex);
		}
		return excludeLast(excludeLast(value, regex), regex, times - 1);
	}

	/**
	 * 取出最後一個字串
	 *
	 * @param value String
	 * @param regex String
	 * @return String
	 */
	public static String extractLast(String value, String regex) {
		if (value == null || regex == null) {
			return null;
		}
		//
		StringBuilder sb = new StringBuilder();
		int index = value.lastIndexOf(regex);
		if (index > -1) {
			sb.append(value.substring(index + 1));
		} else {
			sb.append(value);
		}
		return sb.toString();
	}

	public static String extractLast(String value, String regex, int times) {
		if (times <= 1) {
			return extractLast(value, regex);
		}
		return extractLast(extractLast(value, regex), regex, times - 1);
	}

	/**
	 * 取出中間字串
	 *
	 * @param value    String
	 * @param begRegex String
	 * @param endRegex String
	 * @return String
	 */
	public static String extractBetween(String value, String begRegex, String endRegex) {
		return excludeLast(excludeFirst(value, begRegex), endRegex);
	}

	public static String swapCase(String value) {
		if (value == null || (value.length()) == 0) {
			return value;
		}
		int length = value.length();
		StringBuilder sb = new StringBuilder(length);
		char ch = 0;
		for (int i = 0; i < length; i++) {
			ch = value.charAt(i);
			if (Character.isUpperCase(ch)) {
				ch = Character.toLowerCase(ch);
			} else if (Character.isTitleCase(ch)) {
				ch = Character.toLowerCase(ch);
			} else if (Character.isLowerCase(ch)) {
				ch = Character.toUpperCase(ch);
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	public static String quote(String value) {
		if (StringHelper.isEmpty(value)) {
			return value;
		}
		//
		StringBuilder result = null;
		String filtered = null;
		for (int i = 0; i < value.length(); i++) {
			filtered = null;
			switch (value.charAt(i)) {
			case '<':
				filtered = "&lt;";
				break;
			case '>':
				filtered = "&gt;";
				break;
			case '&':
				filtered = "&amp;";
				break;
			case '"':
				filtered = "&quot;";
				break;
			case '\'':
				filtered = "&#39;";
				break;
			}
			//
			if (result == null) {
				if (filtered != null) {
					result = new StringBuilder(value.length() + 50);
					if (i > 0) {
						result.append(value.substring(0, i));
					}
					result.append(filtered);
				}
			} else {
				if (filtered == null) {
					result.append(value.charAt(i));
				} else {
					result.append(filtered);
				}
			}
		}
		return (result == null) ? value : result.toString();
	}

	// 20110709
	/**
	 *
	 * 字串轉ISO-8859-1碼
	 *
	 * @param value
	 * @return
	 */
	public static String encodeIso8859_1(String value) {
		return encodeString(value, EncodingHelper.ISO_8859_1);
	}

	/**
	 *
	 * 字串轉UTF-8碼
	 *
	 * @param value
	 * @return
	 */
	public static String encodeUtf8(String value) {
		return encodeString(value, EncodingHelper.UTF_8);
	}

	/**
	 *
	 * 字串轉BIG5碼
	 *
	 * @param value
	 * @return
	 */
	public static String encodeBig5(String value) {
		return encodeString(value, EncodingHelper.BIG5);
	}

	/**
	 *
	 * 字串轉GBK碼
	 *
	 * @param value
	 * @return
	 */
	public static String encodeGbk(String value) {
		return encodeString(value, EncodingHelper.GBK);
	}

	/**
	 * 字串轉碼
	 *
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static String encodeString(String value, String charsetName) {
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(charsetName, "The CharsetName must not be null");
		//
		String result = null;
		try {
			result = new String(value.getBytes(charsetName), charsetName);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during encodeString()").toString(), e);
		}
		return result;
	}

	// 2011/07/21
	public static boolean isNumeric(String value) {
		AssertHelper.notNull(value, "The Value must not be null");
		//
		boolean result = false;
		try {
			Double.parseDouble(value);
			result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;

	}

	public static String extractNumeric(String value) {
		StringBuilder result = new StringBuilder();
		//
		if (value == null) {
			return null;
		}
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (Character.isDigit(ch)) {
				result.append(ch);
			}
		}
		return result.toString();
	}

	// 20111005 for full text search
	public static String keyword(String value) {
		return StringHelper.isBlank(value) ? "*" : "*" + value + "*";
	}

	public static String keyword(Boolean value) {
		return value == null ? "false" : value.toString();
	}

	/**
	 *
	 * 隨機產生unique id,長度16 i=0-6,為數字 i=7-15,為隨機字母
	 *
	 * @return
	 */
	public static String randomUnique() {
		StringBuilder result = new StringBuilder();
		//
		long now = System.currentTimeMillis();// len=14
		result.append(now & 0xddeeff); // len=7
		for (int i = 0; i < 9; i++)// len=7+9=16
		{
			result.append(randomAlphabet());
		}
		return result.toString();
	}

	/**
	 *
	 * 隨機產生一個字母或數字
	 *
	 * @return
	 */
	public static String randomAlphabet() {
		String result = null;
		int random = NumberHelper.randomInt(0, 62);
		//
		int ascii = 0;
		//
		if (random >= 0 && random <= 9) {
			// 0-9, ascii: 48-57
			ascii = random + 48;
		}
		//
		else if (random >= 10 && random <= 35) {
			// A-Z, ascii: 65-90
			ascii = random - 10 + 65;
		}
		// 36-62
		else if (random >= 36 && random <= 61) {
			// a-z, ascii: 97-122
			ascii = random - 36 + 97;
		} else {
			// just for pretty
		}
		//
		result = String.valueOf((char) ascii);
		return result;
	}

	/**
	 * 隨機產生10個字的字串
	 *
	 * @return
	 */
	public static String randomString() {
		// #issue randomInt(10) 有可能會長度0

		// #fix
		int length = NumberHelper.randomInt(1, 11);
		return randomString(length);
	}

	/**
	 * 隨機產生 n 個字串
	 *
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < Math.abs(length); i++) {
			result.append(randomAlphabet());
		}
		return result.toString();
	}

	/**
	 * 隨機ip
	 *
	 * @param prefixIp 前綴 192.168.1.1-254
	 * @return
	 */
	public static String randomIp(String prefixIp) {
		StringBuilder result = new StringBuilder();
		if (notBlank(prefixIp)) {
			result.append(prefixIp);
			result.append(".");
			result.append(NumberHelper.randomInt(1, 255));
		}
		return result.toString();
	}

	/**
	 * 產生UUID
	 * 
	 * @param length     長度
	 * @param spacing    空白
	 * @param spacerChar 空白字元
	 * @return
	 * 
	 * randomUUID(16,-1,'\0')
	 */
	public static String randomUUID(int length, int spacing, char spacerChar) {
		StringBuilder result = new StringBuilder();
		int spacer = 0;
		while (length > 0) {
			if (spacer == spacing) {
				result.append(spacerChar);
				spacer = 0;
			}
			length--;
			spacer++;
			result.append(NumberHelper.randomChar());
		}
		return result.toString();
	}

	public static String safeGet(String value) {
		return (value != null ? value : DEFAULT_VALUE);
	}

	public static List<String> extractList(String value, String begValue, String endValue) {
		List<String> result = new LinkedList<String>();
		//
		extractCollection(result, value, begValue, endValue);
		return result;
	}

	public static Set<String> extractSet(String value, String begValue, String endValue) {
		Set<String> result = new LinkedHashSet<String>();
		//
		extractCollection(result, value, begValue, endValue);
		return result;
	}

	protected static void extractCollection(Collection<String> result, String value, String begValue, String endValue) {
		if (result == null || value == null || begValue == null || endValue == null) {
			return;
		}
		//
		int pos = 0;
		while ((pos = value.indexOf(begValue, pos)) != -1) {
			int begIndex = pos;
			pos = value.indexOf(endValue, pos + begValue.length());
			if (pos != -1) {
				result.add(new String(value.substring(begIndex + begValue.length(), pos)));
			} else {
				break;
			}
		}
	}
}
