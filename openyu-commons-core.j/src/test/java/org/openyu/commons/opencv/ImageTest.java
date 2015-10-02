package org.openyu.commons.opencv;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

import javax.imageio.ImageIO;

import org.junit.Test;

public class ImageTest {

	// 1. * h 顏色 用角度表示，範圍：0到360度
	// 2. * s 色度 0.0到1.0 0為白色，越高顏色越“純”
	// 3. * v 亮度 0.0到1.0 0為黑色，越高越亮
	@Test
	public void color() {
		System.out.println("--------------------------------");
		System.out.println("Color.YELLOW: " + Color.YELLOW);// java.awt.Color[r=255,g=255,b=0]
		System.out.println("--------------------------------");
		//
		int r = 255;
		int g = 255;
		int b = 0;
		float[] hsv = new float[3];
		Color.RGBtoHSB(r, g, b, hsv);
		for (float value : hsv) {
			System.out.println(value);
			// 0.16666667
			// 1.0
			// 1.0
		}
		int rgb = Color.HSBtoRGB(0.16666667f, 1f, 1f);
		System.out.println(rgb);// -256
		System.out.println(Integer.toHexString(rgb));// ffffff00

		System.out.println("--------------------------------");
		System.out.println("Color.WHITE: " + Color.WHITE);// java.awt.Color[r=255,g=255,b=255]
		System.out.println("--------------------------------");
		//
		r = g = b = 255;
		Color.RGBtoHSB(r, g, b, hsv);
		for (float value : hsv) {
			System.out.println(value);
			// 0.0
			// 0.0
			// 1.0
		}
		//
		rgb = Color.HSBtoRGB(0, 0, 1);
		System.out.println(rgb);// -1
		System.out.println(Integer.toHexString(rgb));// ffffffff

		//
		Color color = new Color(rgb);
		System.out.println(color.getRed());// 255
		System.out.println(color.getGreen());// 255
		System.out.println(color.getBlue());// 255
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

	@Test
	public void rgb2yCbCr() {
		int[] result = new int[3];
		result = rgb2yCbCr(50, 100, 150);
		//
		for (int value : result) {
			System.out.print(value + " ");// 90 161 98
		}
		System.out.println();

		result = rgb2yCbCr(86, 41, 2);
		//
		for (int value : result) {
			System.out.print(value + " ");// 50 100 153
		}
		System.out.println();
		//
		result = rgb2yCbCr(119, 75, 47);
		//
		for (int value : result) {
			System.out.print(value + " ");// 84 106 152
		}
		System.out.println();
		//
		result = rgb2yCbCr(255, 240, 214);
		//
		for (int value : result) {
			System.out.print(value + " ");// 241 112 137
		}
		System.out.println();
		//
		result = rgb2yCbCr(135, 86, 58);
		//
		for (int value : result) {
			System.out.print(value + " ");// 97 105 154
		}
		System.out.println();
		//
		result = rgb2yCbCr(255, 223, 196);
		//
		for (int value : result) {
			System.out.print(value + " ");// 229 109 146
		}
		System.out.println();

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

	@Test
	public void yCbCr2Rgb() {
		int[] result = new int[3];
		result = yCbCr2Rgb(90, 161, 98);
		//
		for (int value : result) {
			System.out.print(value + " ");// 47 101 148
		}
		System.out.println();
		//
		result = yCbCr2Rgb(50, 100, 153);
		//
		for (int value : result) {
			System.out.print(value + " ");// 85 42 0
		}
		System.out.println();
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

	@Test
	public void toRGB() {
		int[] result = null;
		result = toRGB(-1);
		//
		for (int value : result) {
			System.out.println(value);
			// 255
			// 255
			// 255
		}
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
						if ((86 <= yCbCr[1] && yCbCr[1] <= 117)
								&& (140 <= yCbCr[2] && yCbCr[2] <= 168)) {
							count++;
						}
					}
				}
				//
				int factor = (int) (width * height * 0.3);
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
				int width = image.getWidth();
				int height = image.getHeight();
				//
				int count = 0;
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						int pixel = image.getRGB(i, j);
						int[] rgb = toRGB(pixel);
						int[] yCbCr = rgb2yCbCr(rgb[0], rgb[1], rgb[2]);
						// 歐洲白種人
						// result.Add(new yCbCrRange() { y = 0, CbDownBound =
						// 90, CbUpBound = 108, CrDownBound = 138, CrUpBound =
						// 160, ColorProportion = 0.3 });
						//
						// //亞洲黃種人
						// result.Add(new yCbCrRange() { y = 0, CbDownBound =
						// 103, CbUpBound = 116, CrDownBound = 135, CrUpBound =
						// 153, ColorProportion = 0.3 });
						//
						// //非洲黑種人
						// result.Add(new yCbCrRange() { y = 0, CbDownBound =
						// 105, CbUpBound = 117, CrDownBound = 140, CrUpBound =
						// 160, ColorProportion = 0.3 });
						//

						if (((90 <= yCbCr[1] && yCbCr[1] <= 108) && (138 <= yCbCr[2] && yCbCr[2] <= 160))
								|| ((103 <= yCbCr[1] && yCbCr[1] <= 116) && (135 <= yCbCr[2] && yCbCr[2] <= 153))
								|| ((105 <= yCbCr[1] && yCbCr[1] <= 117) && (140 <= yCbCr[2] && yCbCr[2] <= 160))) {
							count++;
						}
					}
				}
				//
				int factor = (int) (width * height * 0.3);
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
		String value = "d:/testimage/adult/Newegg.jpg";
		boolean result = isAdultImage(value);
		//
		System.out.println(value + ", " + result);
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

	// =================================================
	@Test
	public void mockIsAdultImage() {
		// 1-20次上限的bytes
		int[] bound = null;
		//
		int count = 19;
		bound = new int[count + 1];// 20
		for (int i = 0; i <= count; i++) {
			bound[i] = f(i + 1) * 1024 * 1024;// bytes
			// System.out.println("bound[" + i + "] " + bound[i]);
		}
		// 指定目錄
		File dir = new File("d:/testimage/adult");
		File[] dirFiles = dir.listFiles();

		// 1,4,9,...400mb
		int fileCount = 0;
		for (int i = 0; i < bound.length; i++) {
			// 累計已處理的bytes
			long byteSum = 0L;
			long beg = System.currentTimeMillis();
			while (byteSum <= bound[i]) {
				// 隨機檔案名稱
				String fileName = dirFiles[random(0, dirFiles.length - 1)]
						.getPath();
				// 模擬時,記得每次重新讀取檔案
				File file = new File(fileName);
				if (!file.exists() || file.isDirectory()) {
					continue;
				}
				byteSum += file.length();
				// 測試是否成人圖片
				// boolean result = isAdultImage(file);
				boolean result = isAdultImageWithMultiColor(file);
				fileCount++;
			}
			//
			long end = System.currentTimeMillis();
			System.out.println(bound[i] + " bytes, count: " + fileCount + ", "
					+ (end - beg) + " at mills.");
		}
	}

	// =================================================
	@Test
	public void mockIsAdultImageByMultiThread() throws Exception {
		//
		int count = 19;
		// 1-20次上限的bytes
		final int[] bound = new int[count + 1];// 20
		for (int i = 0; i <= count; i++) {
			bound[i] = f(i + 1) * 1024 * 1024;// bytes
			// System.out.println("bound[" + i + "] " + bound[i]);
		}
		// 指定目錄
		File dir = new File("d:/testimage/adult");
		File[] dirFiles = dir.listFiles();

		long beg = System.currentTimeMillis();
		AtomicLong end = new AtomicLong(beg);

		// 1,4,9,...400mb
		for (int i = 0; i < bound.length; i++) {
			Thread thread = new Thread(new IsAdultImageRunner(end, bound[i],
					dirFiles));
			thread.setName("sub" + i);
			thread.start();
		}
		//
		System.out.println("[" + Thread.currentThread().getId() + "] " + bound
				+ " bytes, " + (end.get() - beg) + " at mills.");
		Thread.sleep(24 * 60 * 60 * 1000);
	}

	protected class IsAdultImageRunner implements Runnable {

		private AtomicLong end;

		private int bound;

		private File[] dirFiles;

		public IsAdultImageRunner(AtomicLong end, int bound, File[] dirFiles) {
			this.end = end;
			this.bound = bound;
			this.dirFiles = dirFiles;
		}

		public void run() {
			// 累計已處理的bytes
			long byteSum = 0L;
			long beg = System.currentTimeMillis();
			while (byteSum <= bound) {
				// 隨機檔案名稱
				String fileName = dirFiles[random(0, dirFiles.length - 1)]
						.getPath();
				// 模擬時,記得每次重新讀取檔案
				File file = new File(fileName);
				if (!file.exists() || file.isDirectory()) {
					continue;
				}
				byteSum += file.length();
				// 測試是否成人圖片
				// boolean result = isAdultImage(file);
				boolean result = isAdultImageWithMultiColor(file);
			}
			//
			long end = System.currentTimeMillis();
			this.end.set(end);
			System.out.println("[" + Thread.currentThread().getId() + "] "
					+ bound + " bytes, " + (end - beg) + " at mills.");
		}
	}

}

// ----------------------------------
// single thread
// ----------------------------------

// ----------------------------------
// #1
// ----------------------------------
// 1048576 bytes, 399 at mills.
// 4194304 bytes, 1145 at mills.
// 9437184 bytes, 2267 at mills.
// 16777216 bytes, 3973 at mills.
// 26214400 bytes, 6037 at mills.
// 37748736 bytes, 8806 at mills.
// 51380224 bytes, 12394 at mills.
// 67108864 bytes, 15745 at mills.
// 84934656 bytes, 20389 at mills.
// 104857600 bytes, 23956 at mills.
// 126877696 bytes, 28072 at mills.
// 150994944 bytes, 33952 at mills.
// 177209344 bytes, 39700 at mills.
// 205520896 bytes, 46819 at mills.
// 235929600 bytes, 53262 at mills.
// 268435456 bytes, 60398 at mills.
// 303038464 bytes, 68012 at mills.
// 339738624 bytes, 75427 at mills.
// 378535936 bytes, 84319 at mills.
// 419430400 bytes, 93928 at mills.

// ----------------------------------
// #2
// ----------------------------------
// 1048576 bytes, 418 at mills.
// 4194304 bytes, 1057 at mills.
// 9437184 bytes, 2213 at mills.
// 16777216 bytes, 3789 at mills.
// 26214400 bytes, 6204 at mills.
// 37748736 bytes, 8223 at mills.
// 51380224 bytes, 11636 at mills.
// 67108864 bytes, 15699 at mills.
// 84934656 bytes, 19575 at mills.
// 104857600 bytes, 23354 at mills.
// 126877696 bytes, 27146 at mills.
// 150994944 bytes, 31597 at mills.
// 177209344 bytes, 38538 at mills.
// 205520896 bytes, 44060 at mills.
// 235929600 bytes, 51321 at mills.
// 268435456 bytes, 57542 at mills.
// 303038464 bytes, 65952 at mills.
// 339738624 bytes, 72288 at mills.
// 378535936 bytes, 80167 at mills.
// 419430400 bytes, 91233 at mills.

// ----------------------------------
// #3
// ----------------------------------
// 1048576 bytes, 404 at mills.
// 4194304 bytes, 1010 at mills.
// 9437184 bytes, 2286 at mills.
// 16777216 bytes, 4089 at mills.
// 26214400 bytes, 5961 at mills.
// 37748736 bytes, 8954 at mills.
// 51380224 bytes, 12132 at mills.
// 67108864 bytes, 15955 at mills.
// 84934656 bytes, 19823 at mills.
// 104857600 bytes, 23292 at mills.
// 126877696 bytes, 27967 at mills.
// 150994944 bytes, 33550 at mills.
// 177209344 bytes, 39114 at mills.
// 205520896 bytes, 44833 at mills.
// 235929600 bytes, 52752 at mills.
// 268435456 bytes, 59403 at mills.
// 303038464 bytes, 67384 at mills.
// 339738624 bytes, 75723 at mills.
// 378535936 bytes, 85241 at mills.
// 419430400 bytes, 92863 at mills.

// ----------------------------------
// multi thread
// ----------------------------------

// ----------------------------------
// #1
// ----------------------------------
// [11] 1048576 bytes, 2195 at mills.
// [12] 4194304 bytes, 5673 at mills.
// [13] 9437184 bytes, 12253 at mills.
// [14] 16777216 bytes, 18850 at mills.
// [15] 26214400 bytes, 28823 at mills.
// [16] 37748736 bytes, 39041 at mills.
// [17] 51380224 bytes, 45845 at mills.
// [18] 67108864 bytes, 64961 at mills.
// [19] 84934656 bytes, 74316 at mills.
// [20] 104857600 bytes, 87814 at mills.
// [21] 126877696 bytes, 102556 at mills.
// [22] 150994944 bytes, 120826 at mills.
// [23] 177209344 bytes, 121205 at mills.
// [24] 205520896 bytes, 133439 at mills.
// [25] 235929600 bytes, 150723 at mills.
// [27] 303038464 bytes, 161535 at mills.
// [26] 268435456 bytes, 164269 at mills.
// [28] 339738624 bytes, 170969 at mills.
// [29] 378535936 bytes, 184631 at mills.
// [30] 419430400 bytes, 197872 at mills.
// [1] [I@4201583a bytes, 197875 at mills.

// ----------------------------------
// #2
// ----------------------------------
// [11] 1048576 bytes, 1294 at mills.
// [12] 4194304 bytes, 6053 at mills.
// [13] 9437184 bytes, 10451 at mills.
// [14] 16777216 bytes, 19759 at mills.
// [15] 26214400 bytes, 27578 at mills.
// [16] 37748736 bytes, 41684 at mills.
// [17] 51380224 bytes, 49410 at mills.
// [18] 67108864 bytes, 64153 at mills.
// [19] 84934656 bytes, 72692 at mills.
// [20] 104857600 bytes, 90134 at mills.
// [21] 126877696 bytes, 105567 at mills.
// [22] 150994944 bytes, 116302 at mills.
// [23] 177209344 bytes, 119157 at mills.
// [24] 205520896 bytes, 137465 at mills.
// [25] 235929600 bytes, 157131 at mills.
// [26] 268435456 bytes, 159079 at mills.
// [27] 303038464 bytes, 161892 at mills.
// [28] 339738624 bytes, 174800 at mills.
// [29] 378535936 bytes, 188923 at mills.
// [30] 419430400 bytes, 193120 at mills.
// [1] [I@4732234a bytes, 193122 at mills.

// ----------------------------------
// #3
// ----------------------------------
// [11] 1048576 bytes, 1675 at mills.
// [12] 4194304 bytes, 4256 at mills.
// [13] 9437184 bytes, 10029 at mills.
// [14] 16777216 bytes, 22531 at mills.
// [15] 26214400 bytes, 25248 at mills.
// [16] 37748736 bytes, 38381 at mills.
// [17] 51380224 bytes, 48605 at mills.
// [18] 67108864 bytes, 63795 at mills.
// [19] 84934656 bytes, 73890 at mills.
// [20] 104857600 bytes, 84013 at mills.
// [21] 126877696 bytes, 100049 at mills.
// [22] 150994944 bytes, 119043 at mills.
// [23] 177209344 bytes, 120407 at mills.
// [24] 205520896 bytes, 136150 at mills.
// [25] 235929600 bytes, 146738 at mills.
// [27] 303038464 bytes, 158807 at mills.
// [26] 268435456 bytes, 164392 at mills.
// [28] 339738624 bytes, 171510 at mills.
// [29] 378535936 bytes, 182272 at mills.
// [30] 419430400 bytes, 196759 at mills.
// [1] [I@5ed12b20 bytes, 196762 at mills.
