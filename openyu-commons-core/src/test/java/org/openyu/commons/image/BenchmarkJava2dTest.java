package org.openyu.commons.image;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BenchmarkJava2dTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.80 [+- 0.20], round.block: 0.12 [+- 0.08], round.gc: 0.00 [+-
	// 0.00], GC.calls: 7, GC.time: 0.11, time.total: 2.05, time.warmup: 0.00,
	// time.bench: 2.05
	public void resizeJpg() throws Exception {
		File file = new File("custom/input/image/Tulips.jpg");
		BufferedImage srcImage = ImageIO.read(file);
		int width = (int) (srcImage.getWidth() * 0.8);
		int height = (int) (srcImage.getHeight() * 0.8);
		//
		BufferedImage destImage = new BufferedImage(width, height,
				srcImage.getType());

		// Graphics g = bufferedThumbnail.getGraphics();
		Graphics2D g = destImage.createGraphics();

		// g.setComposite(AlphaComposite.Src);
		// // 內插
		// g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		// // 品質
		// g.setRenderingHint(RenderingHints.KEY_RENDERING,
		// RenderingHints.VALUE_RENDER_QUALITY);
		// // 平滑化
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(srcImage, 0, 0, width, height, null);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(destImage, "jpg", bos);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.06 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.30, time.warmup: 0.24,
	// time.bench: 0.06
	public void resizeJpgToFile() throws Exception {
		File file = new File("custom/input/image/Tulips.jpg");
		BufferedImage srcImage = ImageIO.read(file);
		int width = (int) (srcImage.getWidth() * 0.8);
		int height = (int) (srcImage.getHeight() * 0.8);
		//
		BufferedImage destImage = new BufferedImage(width, height,
				srcImage.getType());

		// Graphics g = bufferedThumbnail.getGraphics();
		Graphics2D g = destImage.createGraphics();

		// g.setComposite(AlphaComposite.Src);
		// // 內插
		// g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		// // 品質
		// g.setRenderingHint(RenderingHints.KEY_RENDERING,
		// RenderingHints.VALUE_RENDER_QUALITY);
		// // 平滑化
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(srcImage, 0, 0, width, height, null);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(destImage, "jpg", bos);

		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-resize-80-java2d.jpg"), bos
				.toByteArray());
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 3.38 [+- 1.16], round.block: 0.01 [+- 0.03], round.gc: 0.00 [+-
	// 0.00], GC.calls: 9, GC.time: 0.07, time.total: 5.07, time.warmup: 0.00,
	// time.bench: 5.07
	public void resizePng() throws Exception {
		File file = new File("custom/input/image/Tulips.png");
		BufferedImage srcImage = ImageIO.read(file);
		int width = (int) (srcImage.getWidth() * 0.8);
		int height = (int) (srcImage.getHeight() * 0.8);
		//
		BufferedImage destImage = new BufferedImage(width, height,
				srcImage.getType());

		// Graphics g = bufferedThumbnail.getGraphics();
		Graphics2D g = destImage.createGraphics();

		// g.setComposite(AlphaComposite.Src);
		// // 內插
		// g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		// // 品質
		// g.setRenderingHint(RenderingHints.KEY_RENDERING,
		// RenderingHints.VALUE_RENDER_QUALITY);
		// // 平滑化
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(srcImage, 0, 0, width, height, null);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(destImage, "png", bos);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.27 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.73, time.warmup: 0.46,
	// time.bench: 0.27
	public void resizePngToFile() throws Exception {
		File file = new File("custom/input/image/Tulips.png");
		BufferedImage srcImage = ImageIO.read(file);
		int width = (int) (srcImage.getWidth() * 0.8);
		int height = (int) (srcImage.getHeight() * 0.8);
		//
		BufferedImage destImage = new BufferedImage(width, height,
				srcImage.getType());

		// Graphics g = bufferedThumbnail.getGraphics();
		Graphics2D g = destImage.createGraphics();

		// g.setComposite(AlphaComposite.Src);
		// // 內插
		// g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		// // 品質
		// g.setRenderingHint(RenderingHints.KEY_RENDERING,
		// RenderingHints.VALUE_RENDER_QUALITY);
		// // 平滑化
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(srcImage, 0, 0, width, height, null);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(destImage, "png", bos);

		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-resize-80-java2d.png"), bos
				.toByteArray());
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 27.27 [+- 4.84], round.block: 6.90 [+- 3.41], round.gc: 0.00 [+-
	// 0.00], GC.calls: 70, GC.time: 1.76, time.total: 31.32, time.warmup: 0.00,
	// time.bench: 31.32
	public void compressPng() throws Exception {
		File file = new File("custom/input/image/Tulips.png");
		BufferedImage srcImage = ImageIO.read(file);

		if (srcImage == null)
			throw new NullPointerException("空图片");
		BufferedImage cutedImage = null;
		BufferedImage tempImage = null;
		BufferedImage compressedImage = null;
		Graphics2D g2D = null;
		// 图片自动裁剪
		cutedImage = cutImageAuto(srcImage);
		int width = cutedImage.getWidth();
		int height = cutedImage.getHeight();
		// 图片格式为555格式
		tempImage = new BufferedImage(width, height,
				BufferedImage.TYPE_USHORT_555_RGB);
		g2D = (Graphics2D) tempImage.getGraphics();
		g2D.drawImage(srcImage, 0, 0, null);
		compressedImage = getConvertedImage(tempImage);
		// 经过像素转化后的图片
		compressedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		g2D = (Graphics2D) compressedImage.getGraphics();
		g2D.drawImage(tempImage, 0, 0, null);
		int pixel[] = new int[width * height];
		int sourcePixel[] = new int[width * height];
		int currentPixel[] = new int[width * height];
		sourcePixel = cutedImage.getRGB(0, 0, width, height, sourcePixel, 0,
				width);
		currentPixel = tempImage.getRGB(0, 0, width, height, currentPixel, 0,
				width);
		for (int i = 0; i < currentPixel.length; i++) {
			if (i == 0 || i == currentPixel.length - 1) {
				pixel[i] = 0;
				// 内部像素
			} else if (i > width && i < currentPixel.length - width) {
				int bef = currentPixel[i - 1];
				int now = currentPixel[i];
				int aft = currentPixel[i + 1];
				int up = currentPixel[i - width];
				int down = currentPixel[i + width];
				// 背景像素直接置为0
				if (isBackPixel(now)) {
					pixel[i] = 0;
					// 边框像素和原图一样
				} else if ((!isBackPixel(now) && isBackPixel(bef))
						|| (!isBackPixel(now) && isBackPixel(aft))
						|| (!isBackPixel(now) && isBackPixel(up))
						|| (!isBackPixel(now) && isBackPixel(down))) {
					pixel[i] = sourcePixel[i];
					// 其他像素和555压缩后的像素一样
				} else {
					pixel[i] = now;
				}
				// 边界像素
			} else {
				int bef = currentPixel[i - 1];
				int now = currentPixel[i];
				int aft = currentPixel[i + 1];
				if (isBackPixel(now)) {
					pixel[i] = 0;
				} else if ((!isBackPixel(now) && isBackPixel(bef))
						|| (!isBackPixel(now) && isBackPixel(aft))) {
					pixel[i] = sourcePixel[i];
				} else {
					pixel[i] = now;
				}
			}
		}
		compressedImage.setRGB(0, 0, width, height, pixel, 0, width);
		g2D.drawImage(compressedImage, 0, 0, null);
		// ImageIO.write(cutedImage, "png", new File("cut/a_cut.png"));
		// ImageIO.write(tempImage, "png", new File("cut/b_555.png"));
		// ImageIO.write(compressedImage, "png", new File(
		// "custom/input/image/testoutput.png"));

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(compressedImage, "png", bos);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	public void compressPngToFile() throws Exception {
		File file = new File("custom/input/image/Tulips.png");
		BufferedImage srcImage = ImageIO.read(file);

		if (srcImage == null)
			throw new NullPointerException("空图片");
		BufferedImage cutedImage = null;
		BufferedImage tempImage = null;
		BufferedImage compressedImage = null;
		Graphics2D g2D = null;
		// 图片自动裁剪
		cutedImage = cutImageAuto(srcImage);
		int width = cutedImage.getWidth();
		int height = cutedImage.getHeight();
		// 图片格式为555格式
		tempImage = new BufferedImage(width, height,
				BufferedImage.TYPE_USHORT_555_RGB);
		g2D = (Graphics2D) tempImage.getGraphics();
		g2D.drawImage(srcImage, 0, 0, null);
		compressedImage = getConvertedImage(tempImage);
		// 经过像素转化后的图片
		compressedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		g2D = (Graphics2D) compressedImage.getGraphics();
		g2D.drawImage(tempImage, 0, 0, null);
		int pixel[] = new int[width * height];
		int sourcePixel[] = new int[width * height];
		int currentPixel[] = new int[width * height];
		sourcePixel = cutedImage.getRGB(0, 0, width, height, sourcePixel, 0,
				width);
		currentPixel = tempImage.getRGB(0, 0, width, height, currentPixel, 0,
				width);
		for (int i = 0; i < currentPixel.length; i++) {
			if (i == 0 || i == currentPixel.length - 1) {
				pixel[i] = 0;
				// 内部像素
			} else if (i > width && i < currentPixel.length - width) {
				int bef = currentPixel[i - 1];
				int now = currentPixel[i];
				int aft = currentPixel[i + 1];
				int up = currentPixel[i - width];
				int down = currentPixel[i + width];
				// 背景像素直接置为0
				if (isBackPixel(now)) {
					pixel[i] = 0;
					// 边框像素和原图一样
				} else if ((!isBackPixel(now) && isBackPixel(bef))
						|| (!isBackPixel(now) && isBackPixel(aft))
						|| (!isBackPixel(now) && isBackPixel(up))
						|| (!isBackPixel(now) && isBackPixel(down))) {
					pixel[i] = sourcePixel[i];
					// 其他像素和555压缩后的像素一样
				} else {
					pixel[i] = now;
				}
				// 边界像素
			} else {
				int bef = currentPixel[i - 1];
				int now = currentPixel[i];
				int aft = currentPixel[i + 1];
				if (isBackPixel(now)) {
					pixel[i] = 0;
				} else if ((!isBackPixel(now) && isBackPixel(bef))
						|| (!isBackPixel(now) && isBackPixel(aft))) {
					pixel[i] = sourcePixel[i];
				} else {
					pixel[i] = now;
				}
			}
		}
		compressedImage.setRGB(0, 0, width, height, pixel, 0, width);
		g2D.drawImage(compressedImage, 0, 0, null);
		// ImageIO.write(cutedImage, "png", new File("cut/a_cut.png"));
		// ImageIO.write(tempImage, "png", new File("cut/b_555.png"));
		// ImageIO.write(compressedImage, "png", new File(
		// "custom/input/image/testoutput.png"));

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(compressedImage, "png", bos);

		// save to file
		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-compress-80-java2d.png"), bos
				.toByteArray());
	}

	/**
	 * 图片自动裁剪
	 * 
	 * @param image
	 *            要裁剪的图片
	 * @return 裁剪后的图片
	 */
	public static BufferedImage cutImageAuto(BufferedImage image) {
		Rectangle area = getCutAreaAuto(image);
		return image.getSubimage(area.x, area.y, area.width, area.height);
	}

	/**
	 * 获得裁剪图片保留区域
	 * 
	 * @param image
	 *            要裁剪的图片
	 * @return 保留区域
	 */
	private static Rectangle getCutAreaAuto(BufferedImage image) {
		if (image == null)
			throw new NullPointerException("图片为空");
		int width = image.getWidth();
		int height = image.getHeight();
		int startX = width;
		int startY = height;
		int endX = 0;
		int endY = 0;
		int[] pixel = new int[width * height];

		pixel = image.getRGB(0, 0, width, height, pixel, 0, width);
		for (int i = 0; i < pixel.length; i++) {
			if (isCutBackPixel(pixel[i]))
				continue;
			else {
				int w = i % width;
				int h = i / width;
				startX = (w < startX) ? w : startX;
				startY = (h < startY) ? h : startY;
				endX = (w > endX) ? w : endX;
				endY = (h > endY) ? h : endY;
			}
		}
		if (startX > endX || startY > endY) {
			startX = startY = 0;
			endX = width;
			endY = height;
		}
		return new Rectangle(startX, startY, endX - startX, endY - startY);
	}

	/**
	 * 当前像素是否为背景像素
	 * 
	 * @param pixel
	 * @return
	 */
	private static boolean isCutBackPixel(int pixel) {
		int back[] = { 0, 8224125, 16777215, 8947848, 460551, 4141853, 8289918 };
		for (int i = 0; i < back.length; i++) {
			if (back[i] == pixel)
				return true;
		}
		return false;
	}

	private static boolean isBackPixel(int pixel) {
		int back[] = { -16777216 };
		for (int i = 0; i < back.length; i++) {
			if (back[i] == pixel)
				return true;
		}
		return false;
	}

	private static BufferedImage getConvertedImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage convertedImage = null;
		Graphics2D g2D = null;
		// 采用带1 字节alpha的TYPE_4BYTE_ABGR，可以修改像素的布尔透明
		convertedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		g2D = (Graphics2D) convertedImage.getGraphics();
		g2D.drawImage(image, 0, 0, null);
		// 像素替换，直接把背景颜色的像素替换成0
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = convertedImage.getRGB(i, j);
				if (isBackPixel(rgb)) {
					convertedImage.setRGB(i, j, 0);
				}
			}
		}
		g2D.drawImage(convertedImage, 0, 0, null);
		return convertedImage;
	}
}
