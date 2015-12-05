package org.openyu.commons.opencv;

//import java.io.File;
//
//import org.apache.commons.io.FileUtils;
//import org.junit.BeforeClass;
//import org.junit.Rule;
//import org.junit.Test;
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfByte;
//import org.opencv.core.MatOfInt;
//import org.opencv.core.Size;
//import org.opencv.highgui.Highgui;
//import org.opencv.imgproc.Imgproc;
//
//import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
//import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BenchmarkOpenCvTest {

//	@Rule
//	public BenchmarkRule benchmarkRule = new BenchmarkRule();
//
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
//	@Test
//	// round: 0.72 [+- 0.36], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 1.25, time.warmup: 0.00,
//	// time.bench: 1.25
//	public void compressJpg() throws Exception {
//		Mat srcImage = Highgui.imread("custom/input/image/Tulips.jpg",
//				Highgui.CV_LOAD_IMAGE_COLOR);
//		MatOfInt params = new MatOfInt(Highgui.CV_IMWRITE_JPEG_QUALITY, 80);
//
//		MatOfByte destImage = new MatOfByte();
//		Highgui.imencode(".jpg", srcImage, destImage, params);
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
//	@Test
//	// round: 0.05 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.11, time.warmup: 0.06,
//	// time.bench: 0.05
//	public void compressJpgToFile() throws Exception {
//		Mat srcImage = Highgui.imread("custom/input/image/Tulips.jpg",
//				Highgui.CV_LOAD_IMAGE_COLOR);
//		MatOfInt params = new MatOfInt(Highgui.CV_IMWRITE_JPEG_QUALITY, 80);
//
//		MatOfByte destImage = new MatOfByte();
//		Highgui.imencode(".jpg", srcImage, destImage, params);
//
//		// save to file
//		FileUtils.writeByteArrayToFile(new File(
//				"custom/input/image/Tulips-compress-80-opencv.jpg"), destImage
//				.toArray());
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
//	@Test
//	// round: 0.54 [+- 0.29], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 1.02, time.warmup: 0.00,
//	// time.bench: 1.02
//	public void resizeJpg() throws Exception {
//		Mat srcImage = Highgui.imread("custom/input/image/Tulips.jpg",
//				Highgui.CV_LOAD_IMAGE_COLOR);
//
//		// cols=width, rows=height
//		// System.out.println(srcImage.cols() + ", " + srcImage.rows());
//
//		// System.out.println(srcImage.total());// 786432
//
//		Mat destImage = new Mat();
//		Size size = new Size(srcImage.cols() * 0.8d, srcImage.rows() * 0.8d);
//		Imgproc.resize(srcImage, destImage, size);
//
//		// System.out.println("channels: " + destImage.channels());// 3
//		// System.out.println("cols: " + destImage.cols());// 1024
//		// System.out.println("rows: " + destImage.rows());// 768
//		// System.out.println("total: " + destImage.total());// 502866
//
//		// int buffSize = destImage.channels() * destImage.cols()
//		// * destImage.rows();// 1508598
//		// // System.out.println(bufferSize);
//		// byte[] buff = new byte[buffSize];
//		// destImage.get(0, 0, buff); // get all the pixels
//		//
//		// int type;
//		// if (destImage.channels() == 1)
//		// type = BufferedImage.TYPE_BYTE_GRAY;
//		// else
//		// type = BufferedImage.TYPE_3BYTE_BGR;
//		//
//		// BufferedImage image = new BufferedImage(destImage.cols(),
//		// destImage.rows(), type);
//		// image.getRaster().setDataElements(0, 0, destImage.cols(),
//		// destImage.rows(), buff);
//		//
//		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		// ImageIO.write(image, "jpg", bos);
//
//		// Highgui.imwrite("custom/input/image/Tulips-resize-80-opencv.jpg",
//		// destImage);
//
//		// FileUtils.writeByteArrayToFile(new File(
//		// "custom/input/image/Tulips-resize-80-opencv.jpg"),
//		// bos.toByteArray());
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
//	@Test
//	// round: 0.05 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.10, time.warmup: 0.05,
//	// time.bench: 0.05
//	public void resizeJpgToFile() throws Exception {
//		Mat srcImage = Highgui.imread("custom/input/image/Tulips.jpg",
//				Highgui.CV_LOAD_IMAGE_COLOR);
//
//		// cols=width, rows=height
//		// System.out.println(srcImage.cols() + ", " + srcImage.rows());
//
//		// System.out.println(srcImage.total());// 786432
//
//		Mat destImage = new Mat();
//		Size size = new Size(srcImage.cols() * 0.8d, srcImage.rows() * 0.8d);
//		Imgproc.resize(srcImage, destImage, size);
//
//		// save to file
//		Highgui.imwrite("custom/input/image/Tulips-resize-80-opencv.jpg",
//				destImage);
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
//	@Test
//	// round: 1.50 [+- 0.88], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 3.58, time.warmup: 0.00,
//	// time.bench: 3.58
//	public void compressPng() throws Exception {
//		Mat srcImage = Highgui.imread("custom/input/image/Tulips.png",
//				Highgui.CV_LOAD_IMAGE_COLOR);
//		MatOfInt params = new MatOfInt(Highgui.CV_IMWRITE_PNG_COMPRESSION, 80);
//
//		MatOfByte destImage = new MatOfByte();
//		Highgui.imencode(".png", srcImage, destImage, params);
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
//	@Test
//	// round: 1.73 [+- 0.91], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 3.16, time.warmup: 0.00,
//	// time.bench: 3.15
//	public void compressPngToFile() throws Exception {
//		Mat srcImage = Highgui.imread("custom/input/image/Tulips.png",
//				Highgui.CV_LOAD_IMAGE_COLOR);
//		MatOfInt params = new MatOfInt(Highgui.CV_IMWRITE_PNG_COMPRESSION, 80);
//
//		MatOfByte destImage = new MatOfByte();
//		Highgui.imencode(".png", srcImage, destImage, params);
//
//		// save to file
//		FileUtils.writeByteArrayToFile(new File(
//				"custom/input/image/Tulips-compress-80-opencv.png"), destImage
//				.toArray());
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
//	@Test
//	// round: 0.46 [+- 0.23], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.79, time.warmup: 0.00,
//	// time.bench: 0.79
//	public void resizePng() throws Exception {
//		Mat srcImage = Highgui.imread("custom/input/image/Tulips.png",
//				Highgui.CV_LOAD_IMAGE_COLOR);
//
//		// cols=width, rows=height
//		// System.out.println(srcImage.cols() + ", " + srcImage.rows());
//
//		// System.out.println(srcImage.total());// 786432
//
//		Mat destImage = new Mat();
//		Size size = new Size(srcImage.cols() * 0.8d, srcImage.rows() * 0.8d);
//		Imgproc.resize(srcImage, destImage, size);
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
//	@Test
//	// round: 0.05 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.11, time.warmup: 0.06,
//	// time.bench: 0.05
//	public void resizePngToFile() throws Exception {
//		Mat srcImage = Highgui.imread("custom/input/image/Tulips.png",
//				Highgui.CV_LOAD_IMAGE_COLOR);
//
//		// cols=width, rows=height
//		// System.out.println(srcImage.cols() + ", " + srcImage.rows());
//
//		// System.out.println(srcImage.total());// 786432
//
//		Mat destImage = new Mat();
//		Size size = new Size(srcImage.cols() * 0.8d, srcImage.rows() * 0.8d);
//		Imgproc.resize(srcImage, destImage, size);
//
//		// save to file
//		Highgui.imwrite("custom/input/image/Tulips-resize-80-opencv.png",
//				destImage);
//	}
}
