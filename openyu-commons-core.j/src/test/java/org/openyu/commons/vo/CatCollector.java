package org.openyu.commons.vo;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openyu.commons.model.supporter.BaseModelSupporter;

public class CatCollector {
	private Cat[] cats = new Cat[0];

	private Map catMap = new LinkedHashMap();

	public CatCollector() {

	}

	public Cat[] getCats() {
		return cats;
	}

	public void setCats(Cat[] cats) {
		this.cats = cats;
	}

	public Map getCatMap() {
		return catMap;
	}

	public void setCatMap(Map catMap) {
		this.catMap = catMap;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public static class Cat extends BaseModelSupporter {
		private String code;

		private String name;

		private Date birthday;

		// private Locale locale;

		public Cat() {
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getBirthday() {
			return birthday;
		}

		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}

		// public Locale getLocale()
		// {
		// return locale;
		// }
		//
		// public void setLocale(Locale locale)
		// {
		// this.locale = locale;
		// }
	}
}
