package org.openyu.commons.imgscalr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BenchmarkImgscalrTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 2.20 [+- 0.21], round.block: 0.35 [+- 0.05], round.gc: 0.00 [+-
	// 0.00], GC.calls: 9, GC.time: 0.15, time.total: 2.28, time.warmup: 0.00,
	// time.bench: 2.28
	public void resizeJpg() throws Exception {
		BufferedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.jpg"));
		int resizeWidth = (int) (srcImage.getWidth() * 0.8);
		int resizeHeight = (int) (srcImage.getHeight() * 0.8);

		BufferedImage destImage = Scalr.resize(srcImage,
				Scalr.Method.AUTOMATIC, resizeWidth, resizeHeight);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(destImage, "jpg", bos);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.07 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.36, time.warmup: 0.29,
	// time.bench: 0.07
	public void resizeJpgToFile() throws Exception {
		BufferedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.jpg"));
		int resizeWidth = (int) (srcImage.getWidth() * 0.8);
		int resizeHeight = (int) (srcImage.getHeight() * 0.8);

		BufferedImage destImage = Scalr.resize(srcImage,
				Scalr.Method.AUTOMATIC, resizeWidth, resizeHeight);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(destImage, "jpg", bos);

		// save to file
		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-resize-80-imgscalr.jpg"), bos
				.toByteArray());
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 3.64 [+- 1.11], round.block: 0.00 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 9, GC.time: 0.10, time.total: 5.37, time.warmup: 0.00,
	// time.bench: 5.37
	public void resizePng() throws Exception {
		BufferedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.png"));
		int resizeWidth = (int) (srcImage.getWidth() * 0.8);
		int resizeHeight = (int) (srcImage.getHeight() * 0.8);

		BufferedImage destImage = Scalr.resize(srcImage,
				Scalr.Method.AUTOMATIC, resizeWidth, resizeHeight);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(destImage, "png", bos);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.20 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.61, time.warmup: 0.41,
	// time.bench: 0.20
	public void resizePngToFile() throws Exception {
		BufferedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.png"));
		int resizeWidth = (int) (srcImage.getWidth() * 0.8);
		int resizeHeight = (int) (srcImage.getHeight() * 0.8);

		BufferedImage destImage = Scalr.resize(srcImage,
				Scalr.Method.AUTOMATIC, resizeWidth, resizeHeight);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(destImage, "png", bos);

		// save to file
		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-resize-80-imgscalr.png"), bos
				.toByteArray());
	}
}
