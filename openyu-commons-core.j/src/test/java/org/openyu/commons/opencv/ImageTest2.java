package org.openyu.commons.opencv;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.openyu.commons.lang.SystemHelper;

public class ImageTest2 {

	private static ImageManager manager = ImageManager.getInstance();

	@Test
	public void getInstance() {
		System.out.println(manager);
		System.out.println("getBaseDir: " + manager.getBaseDir());
		//
		System.out.println("getDefaultRatioId: " + manager.getDefaultRatioId());
		System.out.println(manager.getDefaultRatio());
		System.out.println(manager.getDefaultRatio().getId());
		System.out.println(manager.getDefaultRatio().getValue());
		//
		System.out.println("getDefaultRangeId: " + manager.getDefaultRangeId());
		System.out.println(manager.getDefaultRange());
		System.out.println(manager.getDefaultRange().getCbDownBound());
		System.out.println(manager.getDefaultRange().getCbUpBound());
		System.out.println(manager.getDefaultRange().getCrDownBound());
		System.out.println(manager.getDefaultRange().getCrUpBound());
		//
		System.out.println(manager.getRatios());
		System.out.println(manager.getRanges());
	}

	/**
	 * RGB to yCbCr
	 *
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int[] rgb2yCbCr(int r, int g, int b) {
		int[] result = new int[3];
		int y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		int cb = (int) (-0.1687 * r - 0.3313 * g + 0.5 * b + 128);
		int cr = (int) (0.5 * r - 0.4187 * g - 0.0813 * b + 128);
		result[0] = y;
		result[1] = cb;
		result[2] = cr;
		return result;
	}

	/**
	 * yCbCr to RGB
	 *
	 * @param y
	 * @param cb
	 * @param cr
	 * @return
	 */
	public static int[] yCbCr2Rgb(int y, int cb, int cr) {
		int[] result = new int[3];
		int r = y + ((1436 * (cr - 128)) >> 10);
		int g = y - ((354 * (cb - 128) + 732 * (cr - 128)) >> 10);
		int b = y + ((1814 * (cb - 128)) >> 10);
		//
		result[0] = r;
		result[1] = g;
		result[2] = b;
		//
		return result;
	}

	/**
	 * pixel to RGB
	 *
	 * @param image
	 * @param x
	 * @param y
	 * @return
	 */
	public static int[] toRGB(int pixel) {
		int[] result = new int[3];
		//
		result[0] = (pixel & 0xff0000) >> 16;
		result[1] = (pixel & 0xff00) >> 8;
		result[2] = (pixel & 0xff);
		return result;
	}

	/**
	 * f(x)=x^2
	 *
	 * @param x
	 * @return
	 */
	public static int f(int x) {
		return (int) Math.pow(x, 2);
	}

	@Test
	public void f() {
		for (int i = 1; i <= 20; i++) {
			System.out.println(f(i));
			// 1
			// 4
			// 9
		}
	}

	public static int random(int x, int y) {
		return (int) (Math.random() * (y - x + 1)) + x;
	}

	/**
	 * 是否為成人圖片
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean isAdultImage(String fileName) {
		if (fileName == null) {
			return false;
		}
		return isAdultImage(new File(fileName));
	}

	/**
	 * 是否為成人圖片
	 *
	 * @param file
	 * @return
	 */
	public static boolean isAdultImage(File file) {
		boolean result = false;
		try {
			BufferedImage image = ImageIO.read(file);
			if (image != null) {
				// 取預設範圍
				ImageManager.Range range = manager.getDefaultRange();
				if (range == null) {
					System.out.println("範圍規則為空");
					return result;
				}

				// 取預設比率
				ImageManager.Ratio ratio = manager.getDefaultRatio();
				// System.out.println("比率規則: " + ratio.getId());
				if (ratio == null) {
					System.out.println("比率規則為空");
					return result;
				}

				int width = image.getWidth();
				int height = image.getHeight();
				//
				int count = 0;
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						int pixel = image.getRGB(i, j);
						int[] rgb = toRGB(pixel);
						int[] yCbCr = rgb2yCbCr(rgb[0], rgb[1], rgb[2]);
						//
						if ((range.getCbDownBound() <= yCbCr[1] && yCbCr[1] <= range
								.getCbUpBound())
								&& (range.getCrDownBound() <= yCbCr[2] && yCbCr[2] <= range
										.getCrUpBound())) {
							count++;
						}
					}
				}
				//
				int factor = (int) (width * height * ratio.getValue());
				if (count > factor) {
					result = true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 是否為成人圖片, 多重色階比對
	 *
	 * @param file
	 * @return
	 */
	public static boolean isAdultImageWithMultiColor(File file) {
		boolean result = false;
		try {
			BufferedImage image = ImageIO.read(file);
			if (image != null) {
				// 取預設範圍
				ImageManager.Range range = manager.getDefaultRange();
				if (range == null) {
					System.out.println("範圍規則為空");
					return result;
				}

				// 取預設比率
				ImageManager.Ratio ratio = manager.getDefaultRatio();
				// System.out.println("比率規則: " + ratio.getId());
				if (ratio == null) {
					System.out.println("比率規則為空");
					return result;
				}

				int width = image.getWidth();
				int height = image.getHeight();
				//
				int count = 0;
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						int pixel = image.getRGB(i, j);
						int[] rgb = toRGB(pixel);
						int[] yCbCr = rgb2yCbCr(rgb[0], rgb[1], rgb[2]);
						//
						if (((116 <= yCbCr[1] && yCbCr[1] <= 129) && (128 <= yCbCr[2] && yCbCr[2] <= 133))
								|| ((114 <= yCbCr[1] && yCbCr[1] <= 116) && (132 <= yCbCr[2] && yCbCr[2] <= 133))
								|| ((99 <= yCbCr[1] && yCbCr[1] <= 106) && (152 <= yCbCr[2] && yCbCr[2] <= 157))) {
							count++;
						}
					}
				}
				//
				int factor = (int) (width * height * ratio.getValue());
				if (count > factor) {
					result = true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Test
	public void isAdultImage() {
		String value = "d:/testimage/4b1149546034c.jpg";
		boolean result = isAdultImage(value);
		//
		System.out.println(value + ", " + result);
	}

	@Test
	public void mockIsAdultImage() {
		// 指定目錄
		File dir = new File("d:/testimage/adult");
		// File dir = new File("d:/testimage");
		File[] dirFiles = dir.listFiles();

		// 累計已處理的bytes
		long byteSum = 0L;
		int fileCount = 0;
		int adultCount = 0;
		long beg = System.currentTimeMillis();
		// 模擬張數
		int[] mockCounts = new int[] { 100, 400, 900, 1600, 2500 };
		for (int i = 0; i < mockCounts.length; i++) {
			int mockCount = mockCounts[i];
			while (fileCount < mockCount) {
				// 隨機檔案名稱
				String fileName = dirFiles[random(0, dirFiles.length - 1)]
						.getPath();
				File file = new File(fileName);
				if (!file.exists() || file.isDirectory()) {
					continue;
				}
				fileCount++;
				byteSum += file.length();
				// 測試是否成人圖片
				boolean result = isAdultImage(file);
				if (result) {
					adultCount++;
				}
				// 顯示圖檔名稱
				// System.out.println(fileName + ", " + result);
			}
			//
			long end = System.currentTimeMillis();
			//
			int normalCount = fileCount - adultCount;
			double accuracy = Math
					.round((adultCount / (double) fileCount) * 100d);

			System.out.println("all: " + fileCount + " = audlt: " + adultCount
					+ " + normal: " + normalCount + ", accuracy: " + accuracy
					+ "%, " + Math.round((byteSum / 1024d / 1024d)) + " mb. "
					+ (end - beg) + " at mills.");
		}
	}

	@Test
	public void rangesToRgb() {
		int[] r1Down = yCbCr2Rgb(150, 86, 140);
		SystemHelper.println(r1Down);
		//
		int[] r1Up = yCbCr2Rgb(150, 117, 168);
		SystemHelper.println(r1Up);
	}

	@Test
	public void skinList() throws Exception {
		File file = new File("d:/testimage/skin/list/36.jpg");
		BufferedImage image = ImageIO.read(file);
		int width = image.getWidth();
		int height = image.getHeight();
		int count = 0;
		for (int j = 0; j < width; j++) {
			for (int k = 0; k < height; k++) {
				int pixel = image.getRGB(j, k);
				int[] rgb = toRGB(pixel);
				int[] yCbCr = rgb2yCbCr(rgb[0], rgb[1], rgb[2]);
				System.out.print("rgb: ");
				SystemHelper.println(rgb);
				System.out.print("yCbCr: ");
				SystemHelper.println(yCbCr);
				count++;
				if (count > 2) {
					return;
				}
			}
		}
	}
}
