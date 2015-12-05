package org.openyu.commons.image;

//import java.awt.image.BufferedImage;
//import java.awt.image.renderable.ParameterBlock;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Iterator;
//
//import javax.imageio.IIOImage;
//import javax.imageio.ImageIO;
//import javax.imageio.ImageWriteParam;
//import javax.imageio.ImageWriter;
//import javax.imageio.stream.ImageOutputStream;
//import javax.media.jai.InterpolationNearest;
//import javax.media.jai.JAI;
//import javax.media.jai.PlanarImage;
//
//import org.apache.commons.io.FileUtils;
//import org.junit.Rule;
//import org.junit.Test;
//
//import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
//import com.carrotsearch.junitbenchmarks.BenchmarkRule;

// https://joinup.ec.europa.eu/svn/giseiel/trunk/EIEL-GestionImagenesFTP/src/es/udc/lbd/eiel/gestionImagenesFTP/model/ImageManager.java
public class BenchmarkJaiTest {

//	@Rule
//	public BenchmarkRule benchmarkRule = new BenchmarkRule();
//	
//	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
//	@Test
//	// round: 1.51 [+- 0.46], round.block: 0.05 [+- 0.01], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 22, GC.time: 0.19, time.total: 1.95, time.warmup: 0.00,
//	// time.bench: 1.94
//	public void compressJpg() throws Exception {
//		File input = new File("custom/input/image/Tulips.jpg");
//
//		InputStream is = new FileInputStream(input);
//		BufferedImage image = ImageIO.read(is);
//
//		Iterator<ImageWriter> writers = ImageIO
//				.getImageWritersByFormatName("jpg"); // here "png" does not work
//		ImageWriter writer = (ImageWriter) writers.next();
//
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
//		writer.setOutput(ios);
//
//		ImageWriteParam param = writer.getDefaultWriteParam();
//		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//		param.setCompressionQuality(0.8f);
//
//		writer.write(null, new IIOImage(image, null, null), param);
//	}
//	
//	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
//	@Test
//	// round: 2.84 [+- 0.59], round.block: 0.77 [+- 0.19], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 22, GC.time: 0.41, time.total: 3.25, time.warmup: 0.00,
//	// time.bench: 3.24
//	public void compressJpgToFile() throws Exception {
//		File input = new File("custom/input/image/Tulips.jpg");
//
//		InputStream is = new FileInputStream(input);
//		BufferedImage srcImage = ImageIO.read(is);
//
//		Iterator<ImageWriter> writers = ImageIO
//				.getImageWritersByFormatName("jpg"); // here "png" does not work
//		ImageWriter writer = (ImageWriter) writers.next();
//
//		OutputStream bos = new FileOutputStream(new File(
//				"custom/input/image/Tulips-compress-80-jai.jpg"));
//		ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
//		writer.setOutput(ios);
//
//		ImageWriteParam param = writer.getDefaultWriteParam();
//		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//		param.setCompressionQuality(0.5f);
//
//		writer.write(null, new IIOImage(srcImage, null, null), param);
//		ios.close();
//		writer.dispose();
//	}
//	
//	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
//	@Test
//	// round: 2.67 [+- 0.94], round.block: 0.05 [+- 0.02], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 27, GC.time: 0.31, time.total: 3.99, time.warmup: 0.00,
//	// time.bench: 3.99
//	public void compressPng() throws Exception {
//		File input = new File("custom/input/image/Tulips.png");
//
//		InputStream is = new FileInputStream(input);
//		BufferedImage image = ImageIO.read(is);
//
//		Iterator<ImageWriter> writers = ImageIO
//				.getImageWritersByFormatName("png");
//		ImageWriter writer = null;
//		while (writers.hasNext()) {
//			ImageWriter candidate = writers.next();
//			if (candidate.getClass().getSimpleName()
//					.equals("CLibPNGImageWriter")) {
//				writer = candidate;
//				break;
//			} else if (writer == null) {
//				writer = candidate;
//			}
//		}
//
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
//		writer.setOutput(ios);
//
//		ImageWriteParam param = writer.getDefaultWriteParam();
//
//	    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//	    param.setCompressionQuality(0.8f);
//
//		writer.write(null, new IIOImage(image, null, null), param);
//	}
//	
//	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
//	@Test
//	// round: 8.86 [+- 3.78], round.block: 0.08 [+- 0.06], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 27, GC.time: 0.43, time.total: 12.51, time.warmup: 0.02,
//	// time.bench: 12.50
//	public void compressPngToFile() throws Exception {
//		File input = new File("custom/input/image/Tulips.png");
//
//		InputStream is = new FileInputStream(input);
//		BufferedImage srcImage = ImageIO.read(is);
//
//		Iterator<ImageWriter> writers = ImageIO
//				.getImageWritersByFormatName("png");
//		ImageWriter writer = null;
//		while (writers.hasNext()) {
//			ImageWriter candidate = writers.next();
//			if (candidate.getClass().getSimpleName()
//					.equals("CLibPNGImageWriter")) {
//				writer = candidate;
//				break;
//			} else if (writer == null) {
//				writer = candidate;
//			}
//		}
//
//		OutputStream bos = new FileOutputStream(new File(
//				"custom/input/image/Tulips-compress-80-jai.png"));
//		ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
//		writer.setOutput(ios);
//
//		ImageWriteParam param = writer.getDefaultWriteParam();
//
//		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//		param.setCompressionType("HUFFMAN_ONLY");
//		param.setCompressionQuality(0.8f);
//
//		writer.write(null, new IIOImage(srcImage, null, null), param);
//		ios.close();
//		writer.dispose();
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
//	@Test
//	// round: 1.73 [+- 0.71], round.block: 1.35 [+- 0.73], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 8, GC.time: 0.08, time.total: 2.96, time.warmup: 0.01,
//	// time.bench: 2.95
//	public void resizeJpg() throws Exception {
//		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
//		//
//		PlanarImage srcImage = JAI.create("fileload",
//				"custom/input/image/Tulips.jpg");
//		// System.out.println(image);
//		//
//		float scale = 0.8f;
//		ParameterBlock pb = new ParameterBlock();
//		pb.addSource(srcImage);
//
//		pb.add(scale);
//		pb.add(scale);
//		pb.add(1.0F);
//		pb.add(1.0F);
//
//		pb.add(new InterpolationNearest());// ;InterpolationBilinear());
//		srcImage = JAI.create("scale", pb);
//		BufferedImage destImage = srcImage.getAsBufferedImage();
//
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ImageIO.write(destImage, "jpg", bos);
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
//	@Test
//	// round: 0.07 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.64, time.warmup: 0.57,
//	// time.bench: 0.07
//	public void resizeJpgToFile() throws Exception {
//		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
//		//
//		PlanarImage srcImage = JAI.create("fileload",
//				"custom/input/image/Tulips.jpg");
//		// System.out.println(image);
//		//
//		float scale = 0.8f;
//		ParameterBlock pb = new ParameterBlock();
//		pb.addSource(srcImage);
//
//		pb.add(scale);
//		pb.add(scale);
//		pb.add(1.0F);
//		pb.add(1.0F);
//
//		pb.add(new InterpolationNearest());// ;InterpolationBilinear());
//		srcImage = JAI.create("scale", pb);
//		BufferedImage destImage = srcImage.getAsBufferedImage();
//
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ImageIO.write(destImage, "jpg", bos);
//
//		// save to file
//		FileUtils.writeByteArrayToFile(new File(
//				"custom/input/image/Tulips-resize-80-jai.jpg"), bos
//				.toByteArray());
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
//	@Test
//	// round: 3.73 [+- 1.22], round.block: 0.11 [+- 0.15], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 9, GC.time: 0.17, time.total: 5.17, time.warmup: 0.01,
//	// time.bench: 5.17
//	public void resizePng() throws Exception {
//		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
//		//
//		PlanarImage srcImage = JAI.create("fileload",
//				"custom/input/image/Tulips.png");
//		// System.out.println(image);
//		//
//		float scale = 0.8f;
//		ParameterBlock pb = new ParameterBlock();
//		pb.addSource(srcImage);
//
//		pb.add(scale);
//		pb.add(scale);
//		pb.add(1.0F);
//		pb.add(1.0F);
//
//		pb.add(new InterpolationNearest());// ;InterpolationBilinear());
//		srcImage = JAI.create("scale", pb);
//		BufferedImage destImage = srcImage.getAsBufferedImage();
//
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ImageIO.write(destImage, "png", bos);
//	}
//
//	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
//	@Test
//	// round: 0.20 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
//	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.95, time.warmup: 0.75,
//	// time.bench: 0.20
//	public void resizePngToFile() throws Exception {
//		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
//		//
//		PlanarImage srcImage = JAI.create("fileload",
//				"custom/input/image/Tulips.png");
//		// System.out.println(image);
//		//
//		float scale = 0.8f;
//		ParameterBlock pb = new ParameterBlock();
//		pb.addSource(srcImage);
//
//		pb.add(scale);
//		pb.add(scale);
//		pb.add(1.0F);
//		pb.add(1.0F);
//
//		pb.add(new InterpolationNearest());// ;InterpolationBilinear());
//		srcImage = JAI.create("scale", pb);
//		BufferedImage destImage = srcImage.getAsBufferedImage();
//		
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ImageIO.write(destImage, "png", bos);
//
//		// save to file
//		FileUtils.writeByteArrayToFile(new File(
//				"custom/input/image/Tulips-resize-80-jai.png"), bos
//				.toByteArray());
//	}
}
