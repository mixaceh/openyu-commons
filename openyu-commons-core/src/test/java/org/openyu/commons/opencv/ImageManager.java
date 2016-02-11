package org.openyu.commons.opencv;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ImageManager {

	private static ImageManager instance;

	/**
	 * 圖檔base目錄
	 */
	private String baseDir;

	/**
	 * 預設比率id
	 */
	private String defaultRatioId = "DEFAULT";

	/**
	 * 預設範圍id
	 */
	private String defaultRangeId = "DEFAULT";

	/**
	 * 像素佔圖片的比率
	 */
	private Map<String, Ratio> ratios = new LinkedHashMap<String, Ratio>();

	/**
	 * 色階範圍
	 */
	private Map<String, Range> ranges = new LinkedHashMap<String, Range>();

	private ImageManager() {
	}

	public static synchronized ImageManager getInstance() {
		if (instance == null) {
			instance = new ImageManager();
			// 載入xml
			instance.loadXml();
		}
		return instance;
	}

	protected void loadXml() {
		try {
			Properties prob = new Properties();
			FileInputStream fis = new FileInputStream(
					ImageManager.class.getSimpleName() + ".xml");
			prob.loadFromXML(fis);
			//
			Set<Map.Entry<Object, Object>> propSet = prob.entrySet();
			for (Map.Entry<Object, Object> entry : propSet) {
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				// System.out.printf("%s = %s%n", key, value);
				//
				if (key.equals("baseDir")) {
					this.baseDir = value;
				} else if (key.equals("defaultRatioId")) {
					this.defaultRatioId = value;
				} else if (key.equals("defaultRangeId")) {
					this.defaultRangeId = value;
					// 像素佔圖片的比率
				} else if (key.startsWith("ratios.")) {
					Ratio ratio = new Ratio();
					ratio.setId(key);
					ratio.setValue(Double.valueOf(value));
					ratios.put(key, ratio);
					// 色階範圍
				} else if (key.startsWith("ranges.")) {
					int pos = key.lastIndexOf(".");
					String rangeKey = key.substring(0, pos);
					Range range = ranges.get(rangeKey);
					if (range == null) {
						range = new Range();
						range.setId(rangeKey);
						ranges.put(rangeKey, range);
					}
					//
					String fieldName = key.substring(pos + 1);
					if (fieldName.equals("cbDownBound")) {
						range.setCbDownBound(Integer.valueOf(value));
					} else if (fieldName.equals("cbUpBound")) {
						range.setCbUpBound(Integer.valueOf(value));
					} else if (fieldName.equals("crDownBound")) {
						range.setCrDownBound(Integer.valueOf(value));
					} else if (fieldName.equals("crUpBound")) {
						range.setCrUpBound(Integer.valueOf(value));
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 取得圖檔base目錄
	 */
	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	/**
	 * 取得預設比率id
	 * 
	 * @return
	 */
	public String getDefaultRatioId() {
		return defaultRatioId;
	}

	public void setDefaultRatioId(String defaultRatioId) {
		this.defaultRatioId = defaultRatioId;
	}

	/**
	 * 取得預設範圍id
	 * 
	 * @return
	 */
	public String getDefaultRangeId() {
		return defaultRangeId;
	}

	public void setDefaultRangeId(String defaultRangeId) {
		this.defaultRangeId = defaultRangeId;
	}

	/**
	 * 取得所有比率
	 * 
	 * @return
	 */
	public Map<String, Ratio> getRatios() {
		if (ratios == null) {
			ratios = new LinkedHashMap<String, Ratio>();
		}
		return ratios;
	}

	public void setRatios(Map<String, Ratio> ratios) {
		this.ratios = ratios;
	}

	/**
	 * 取得預設比率
	 * 
	 * @return
	 */
	public Ratio getDefaultRatio() {
		if (ratios != null) {
			return ratios.get(defaultRatioId);
		}
		return null;
	}

	/**
	 * 取得所有範圍
	 * 
	 * @return
	 */
	public Map<String, Range> getRanges() {
		if (ranges == null) {
			ranges = new LinkedHashMap<String, Range>();
		}
		return ranges;
	}

	public void setRanges(Map<String, Range> ranges) {
		this.ranges = ranges;
	}

	/**
	 * 取得預設範圍
	 * 
	 * @return
	 */
	public Range getDefaultRange() {
		if (ranges != null) {
			return ranges.get(defaultRangeId);
		}
		return null;
	}

	/**
	 * 像素佔圖片的比率
	 * 
	 */
	public static class Ratio {

		/**
		 * id
		 */
		private String id;

		/**
		 * 比率,0.3
		 */
		private double value;

		public Ratio() {
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

	}

	/**
	 * 色階範圍
	 * 
	 */
	public static class Range {
		/**
		 * id
		 */
		private String id;

		/**
		 * cb上限
		 */
		private int cbUpBound;

		/**
		 * cb下限
		 */
		private int cbDownBound;

		/**
		 * cr上限
		 */
		private int crUpBound;

		/**
		 * cr下限
		 */
		private int crDownBound;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getCbUpBound() {
			return cbUpBound;
		}

		public void setCbUpBound(int cbUpBound) {
			this.cbUpBound = cbUpBound;
		}

		public int getCbDownBound() {
			return cbDownBound;
		}

		public void setCbDownBound(int cbDownBound) {
			this.cbDownBound = cbDownBound;
		}

		public int getCrUpBound() {
			return crUpBound;
		}

		public void setCrUpBound(int crUpBound) {
			this.crUpBound = crUpBound;
		}

		public int getCrDownBound() {
			return crDownBound;
		}

		public void setCrDownBound(int crDownBound) {
			this.crDownBound = crDownBound;
		}

	}
}
