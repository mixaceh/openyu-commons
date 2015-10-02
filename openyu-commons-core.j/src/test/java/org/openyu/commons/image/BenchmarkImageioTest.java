package org.openyu.commons.image;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.lang.SystemHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BenchmarkImageioTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	public void iioRegistry() throws Exception {
		IIORegistry iioRegistry = IIORegistry.getDefaultInstance();
		Iterator<Class<?>> itor = iioRegistry.getCategories();
		while (itor.hasNext()) {
			System.out.println(itor.next());
		}
		// class javax.imageio.spi.ImageReaderSpi
		// class javax.imageio.spi.ImageWriterSpi
		// class javax.imageio.spi.ImageInputStreamSpi
		// class javax.imageio.spi.ImageOutputStreamSpi
		// class javax.imageio.spi.ImageTranscoderSpi
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	public void getReaderFormatNames() throws Exception {
		SystemHelper.println(ImageIO.getReaderFormatNames());
		// raw
		// tif
		// jpeg
		// JFIF
		// WBMP
		// jpeg-lossless
		// jpeg-ls
		// pcx
		// PNM
		// JPG
		// wbmp
		// PNG
		// JPEG
		// jpeg 2000
		// tiff
		// BMP
		// JPEG2000
		// RAW
		// JPEG-LOSSLESS
		// jpeg2000
		// GIF
		// TIF
		// TIFF
		// bmp
		// jpg
		// PCX
		// pnm
		// png
		// jfif
		// JPEG 2000
		// JPEG-LS
		// gif
		// Benchma

		SystemHelper.println(ImageIO.getReaderMIMETypes());
		// image/pcx
		// image/x-portable-anymap
		// image/x-png
		// image/tiff
		// image/x-pcx
		// image/x-portable-pixmap
		// image/vnd.wap.wbmp
		// image/jpeg2000
		// image/x-portable-bitmap
		// image/x-pc-paintbrush
		// image/x-bmp
		// image/png
		// image/jpeg
		// image/jp2
		// image/x-windows-bmp
		// image/gif
		// image/bmp
		// image/x-windows-pcx
		// image/x-portable-graymap
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	public void getWriterFormatNames() throws Exception {
		SystemHelper.println(ImageIO.getWriterFormatNames());
		// raw
		// jpeg
		// tif
		// JFIF
		// WBMP
		// jpeg-ls
		// jpeg-lossless
		// pcx
		// PNM
		// JPG
		// wbmp
		// PNG
		// JPEG
		// jpeg 2000
		// tiff
		// BMP
		// JPEG2000
		// RAW
		// JPEG-LOSSLESS
		// jpeg2000
		// GIF
		// TIF
		// TIFF
		// jpg
		// bmp
		// PCX
		// pnm
		// png
		// jfif
		// JPEG-LS
		// JPEG 2000
		// gif

		SystemHelper.println(ImageIO.getWriterMIMETypes());
		// image/pcx
		// image/x-portable-anymap
		// image/x-pcx
		// image/tiff
		// image/x-png
		// image/x-portable-pixmap
		// image/vnd.wap.wbmp
		// image/jpeg2000
		// image/x-portable-bitmap
		// image/x-pc-paintbrush
		// image/x-bmp
		// image/jpeg
		// image/png
		// image/jp2
		// image/x-windows-bmp
		// image/gif
		// image/bmp
		// image/x-windows-pcx
		// image/x-portable-graymap

		SystemHelper.println(ImageIO.getWriterFileSuffixes());
		// jpeg
		// tif
		// jls
		// pbm
		// pcx
		// bmp
		// jpg
		// wbmp
		// ppm
		// png
		// jfif
		// jp2
		// pgm
		// gif
		// tiff
	}

	public ImageReader getImageReaderByFormatName(String formatName) {
		ImageReader result = null;
		Iterator<ImageReader> it = ImageIO
				.getImageReadersByFormatName(formatName);
		while (it.hasNext()) {
			ImageReader candidate = it.next();
			result = candidate;
			break;
		}
		return result;
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.20 [+- 0.02], round.block: 0.07 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.21, time.warmup: 0.00,
	// time.bench: 0.21
	public void getImageReaderByFormatName() throws Exception {
		String[] value = ImageIO.getReaderFormatNames();
		for (String formatName : value) {
			ImageReader imageReader = getImageReaderByFormatName(formatName);
			System.out.println(formatName + ": " + imageReader);
		}
	}

	public ImageWriter getImageWriterByFormatName(String formatName) {
		ImageWriter result = null;
		Iterator<ImageWriter> it = ImageIO
				.getImageWritersByFormatName(formatName);
		while (it.hasNext()) {
			ImageWriter candidate = it.next();
			// com.sun.imageio.plugins.png.PNGImageWriter
			// com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriter
			// if ("png".equalsIgnoreCase(formatName)
			// && candidate.getClass().getName()
			// .equals("com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriter"))
			// {
			// result = candidate;
			// } else if (!"png".equalsIgnoreCase(formatName)) {
			// result = candidate;
			// } else {
			// // result = candidate;
			// // System.out.println(candidate);
			// }
			result = candidate;
			break;
		}
		return result;
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.20 [+- 0.02], round.block: 0.07 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.21, time.warmup: 0.00,
	// time.bench: 0.21
	public void getImageWriterByFormatName() throws Exception {
		String[] value = ImageIO.getWriterFormatNames();
		for (String formatName : value) {
			ImageWriter imageWriter = getImageWriterByFormatName(formatName);
			System.out.println(formatName + ": " + imageWriter);
		}
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.64 [+- 0.24], round.block: 0.05 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 6, GC.time: 0.09, time.total: 2.05, time.warmup: 0.00,
	// time.bench: 2.05
	//
	// jai
	// round: 0.92 [+- 0.10], round.block: 0.15 [+- 0.13], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.05, time.total: 1.01, time.warmup: 0.01,
	// time.bench: 1.01
	public void compressJpg() throws Exception {

		// Retrieve jpg image to be compressed
		RenderedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.jpg"));

		// Find a writer
		// JPEGImageWriter
		// Iterator<ImageWriter> it =
		// ImageIO.getImageWritersByFormatName("jpg");
		// ImageWriter writer = null;
		// while (it.hasNext()) {
		// ImageWriter candidate = it.next();
		// writer = candidate;
		// break;
		// }
		//
		ImageWriter writer = getImageWriterByFormatName("jpg");
		// Prepare output file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream destImage = ImageIO.createImageOutputStream(baos);
		writer.setOutput(destImage);

		// Set the compression quality
		ImageWriteParam iwparam = writer.getDefaultWriteParam();

		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// JPEG, JPEG-LOSSLESS, JPEG-LS
		iwparam.setCompressionType("JPEG");
		iwparam.setCompressionQuality(0.8f);

		// Write the image
		writer.write(null, new IIOImage(srcImage, null, null), iwparam);

		destImage.close();
		writer.dispose();
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.05 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.29, time.warmup: 0.24,
	// time.bench: 0.05
	public void compressJpgToFile() throws Exception {
		// Retrieve jpg image to be compressed
		RenderedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.jpg"));
		// Find a writer
		// JPEGImageWriter
		// Iterator<ImageWriter> it =
		// ImageIO.getImageWritersByFormatName("jpg");
		// ImageWriter writer = null;
		// while (it.hasNext()) {
		// ImageWriter candidate = it.next();
		// writer = candidate;
		// break;
		// }
		ImageWriter writer = getImageWriterByFormatName("jpg");
		// System.out.println(writer);
		// Prepare output file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream destImage = ImageIO.createImageOutputStream(baos);
		writer.setOutput(destImage);

		// CLibJPEGImageWriteParam
		ImageWriteParam iwparam = writer.getDefaultWriteParam();

		// Set the compression quality
		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// JPEG, JPEG-LOSSLESS, JPEG-LS
		iwparam.setCompressionType("JPEG");
		iwparam.setCompressionQuality(0.8f);

		// Write the image
		writer.write(null, new IIOImage(srcImage, null, null), iwparam);
		destImage.close();
		writer.dispose();

		// save to file
		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-compress-80-imageio.jpg"), baos
				.toByteArray());
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 0.92 [+- 0.11], round.block: 0.06 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.03, time.total: 1.07, time.warmup: 0.01,
	// time.bench: 1.07
	public void compressJpgByImageReader() throws Exception {
		// Retrieve jpg image to be compressed
		// RenderedImage srcImage = ImageIO.read(new File(
		// "custom/input/image/Tulips.jpg"));
		// Find a writer
		// JPEGImageWriter
		// Iterator<ImageWriter> it =
		// ImageIO.getImageWritersByFormatName("jpg");
		// ImageWriter writer = null;
		// while (it.hasNext()) {
		// ImageWriter candidate = it.next();
		// writer = candidate;
		// break;
		// }
		ImageWriter writer = getImageWriterByFormatName("jpg");
		// System.out.println(writer);

		// 用ImageReader讀會比較慢,還是用ImageIO.read比較快
		ImageReader reader = getImageReaderByFormatName("jpg");
		// System.out.println(reader);
		File file = new File("custom/input/image/Tulips.jpg");
		ImageInputStream srcImage = ImageIO.createImageInputStream(file);
		reader.setInput(srcImage);

		// Prepare output file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream destImage = ImageIO.createImageOutputStream(baos);
		writer.setOutput(destImage);

		// CLibJPEGImageWriteParam
		ImageWriteParam iwparam = writer.getDefaultWriteParam();

		// Set the compression quality
		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// JPEG, JPEG-LOSSLESS, JPEG-LS
		iwparam.setCompressionType("JPEG");
		iwparam.setCompressionQuality(0.8f);

		// Write the image
		// writer.write(null, new IIOImage(srcImage, null, null), iwparam);

		IIOImage iioImg = new IIOImage(reader.read(0), null,
				reader.getImageMetadata(0));
		writer.write(null, iioImg, iwparam);

		srcImage.close();
		destImage.close();

		writer.dispose();
		reader.dispose();
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.10 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.37, time.warmup: 0.28,
	// time.bench: 0.09
	public void compressJpgToFileByImageReader() throws Exception {
		// Retrieve jpg image to be compressed
		// RenderedImage srcImage = ImageIO.read(new File(
		// "custom/input/image/Tulips.jpg"));
		// Find a writer
		// JPEGImageWriter
		// Iterator<ImageWriter> it =
		// ImageIO.getImageWritersByFormatName("jpg");
		// ImageWriter writer = null;
		// while (it.hasNext()) {
		// ImageWriter candidate = it.next();
		// writer = candidate;
		// break;
		// }
		ImageWriter writer = getImageWriterByFormatName("jpg");
		// System.out.println(writer);
		// 用ImageReader讀會比較慢,還是用ImageIO.read比較快
		ImageReader reader = getImageReaderByFormatName("jpg");
		// System.out.println(reader);
		File file = new File("custom/input/image/Tulips.jpg");
		ImageInputStream srcImage = ImageIO.createImageInputStream(file);
		reader.setInput(srcImage);

		// Prepare output file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream destImage = ImageIO.createImageOutputStream(baos);
		writer.setOutput(destImage);

		// CLibJPEGImageWriteParam
		ImageWriteParam iwparam = writer.getDefaultWriteParam();

		// Set the compression quality
		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// JPEG, JPEG-LOSSLESS, JPEG-LS
		iwparam.setCompressionType("JPEG");
		iwparam.setCompressionQuality(0.8f);

		// Write the image
		// writer.write(null, new IIOImage(srcImage, null, null), iwparam);

		IIOImage iioImg = new IIOImage(reader.read(0), null,
				reader.getImageMetadata(0));
		writer.write(null, iioImg, iwparam);

		srcImage.close();
		destImage.close();

		writer.dispose();
		reader.dispose();

		// save to file
		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-compress-80-imageio-reader.jpg"),
				baos.toByteArray());
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 2.72 [+- 0.85], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 6, GC.time: 0.07, time.total: 4.07, time.warmup: 0.01,
	// time.bench: 4.06
	public void compressPng() throws Exception {
		// Retrieve jpg image to be compressed
		BufferedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.png"));
		// Find a writer
		// ImageWriter writer =
		// ImageIO.getImageWritersByFormatName("png").next();

		// PNGImageWriter -> CLibPNGImageWriter in JAI
		Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = null;
		while (it.hasNext()) {
			ImageWriter candidate = it.next();
			if (candidate
					.getClass()
					.getName()
					.equals("com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriter")) {
				writer = candidate; // This is the one we want
				break;
			}
		}
		// ImageWriter writer = getImageWriterByFormatName("png");
		// ImageWriter writer= ImageIO
		// .getImageWritersByFormatName("png").next();
		// System.out.println(writer);
		// Prepare output file
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageOutputStream destImg = ImageIO.createImageOutputStream(bos);
		writer.setOutput(destImg);

		// CLibPNGImageWriteParam
		ImageWriteParam iwparam = writer.getDefaultWriteParam();
		// System.out.println("canWriteCompressed: "
		// + iwparam.canWriteCompressed());

		// Set the compression quality
		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwparam.setCompressionType("HUFFMAN_ONLY");
		iwparam.setCompressionQuality(0.8f);
		// Write the image
		writer.write(null, new IIOImage(srcImage, null, null), iwparam);
		destImg.close();
		writer.dispose();
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	// round: 0.16 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.50, time.warmup: 0.35,
	// time.bench: 0.16
	public void compressPngToFile() throws Exception {
		// Retrieve jpg image to be compressed
		BufferedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.png"));
		// Find a writer
		// ImageWriter writer =
		// ImageIO.getImageWritersByFormatName("png").next();

		// PNGImageWriter -> CLibPNGImageWriter in JAI
		// Iterator<ImageWriter> it =
		// ImageIO.getImageWritersByFormatName("png");
		// ImageWriter writer = null;
		// while (it.hasNext()) {
		// ImageWriter candidate = it.next();
		// if (candidate.getClass().getSimpleName()
		// .equals("CLibPNGImageWriter")) {
		// writer = candidate; // This is the one we want
		// break;
		// }
		// }
		ImageWriter writer = getImageWriterByFormatName("png");
		// System.out.println(writer);
		// Prepare output file
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageOutputStream destImage = ImageIO.createImageOutputStream(bos);
		writer.setOutput(destImage);

		// CLibPNGImageWriteParam
		ImageWriteParam iwparam = writer.getDefaultWriteParam();
		// Set the compression quality
		// System.out.println("canWriteCompressed: "
		// + iwparam.canWriteCompressed());

		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		//
		// DEFAULT, FILTERED, HUFFMAN_ONLY
		iwparam.setCompressionType("HUFFMAN_ONLY");
		iwparam.setCompressionQuality(0.8f);
		// System.out.println(iwparam.getCompressionType());

		// IIOMetadata metadata = writer.getDefaultImageMetadata(
		// new ImageTypeSpecifier(srcImage), iwparam);
		// System.out.println(metadata);

		// // Define progressive mode
		// iwparam.setProgressiveMode(ImageWriteParam.MODE_COPY_FROM_METADATA);
		//
		// // Deine destination type - used the ColorModel and SampleModel of
		// the
		// // Input Image

		// iwparam.setDestinationType(new ImageTypeSpecifier(srcImage
		// .getColorModel(), srcImage.getSampleModel()));

		// Write the image
		writer.write(null, new IIOImage(srcImage, null, null), iwparam);
		destImage.close();
		writer.dispose();
		// save to file
		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-compress-80-imageio.png"), bos
				.toByteArray());
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 3.83 [+- 0.76], round.block: 0.12 [+- 0.02], round.gc: 0.00 [+-
	// 0.00], GC.calls: 9, GC.time: 0.13, time.total: 4.24, time.warmup: 0.00,
	// time.bench: 4.24
	public void compressTiff() throws Exception {
		// Retrieve jpg image to be compressed
		RenderedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.tif"));
		// Find a writer
		// TIFFImageWriter
		// Iterator<ImageWriter> it =
		// ImageIO.getImageWritersByFormatName("tif");
		// ImageWriter writer = null;
		// while (it.hasNext()) {
		// ImageWriter candidate = it.next();
		// writer = candidate;
		// break;
		// }
		ImageWriter writer = getImageWriterByFormatName("tif");
		// System.out.println(writer);
		// Prepare output file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream destImage = ImageIO.createImageOutputStream(baos);
		writer.setOutput(destImage);

		// TIFFImageWriteParam
		ImageWriteParam iwparam = writer.getDefaultWriteParam();
		// System.out.println(iwparam);

		// Set the compression quality
		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// CCITT RLE, CCITT T.4, CCITT T.6, LZW, Old JPEG, JPEG, ZLib,
		// PackBits,Deflate, EXIF JPEG
		iwparam.setCompressionType("LZW");
		iwparam.setCompressionQuality(0.8f);

		// Write the image
		writer.write(null, new IIOImage(srcImage, null, null), iwparam);
		destImage.close();
		writer.dispose();
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	public void compressTiffToFile() throws Exception {
		// Retrieve jpg image to be compressed
		RenderedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.tif"));
		// Find a writer
		// TIFFImageWriter
		// Iterator<ImageWriter> it =
		// ImageIO.getImageWritersByFormatName("tif");
		// ImageWriter writer = null;
		// while (it.hasNext()) {
		// ImageWriter candidate = it.next();
		// writer = candidate;
		// break;
		// }
		ImageWriter writer = getImageWriterByFormatName("tif");
		// System.out.println(writer);
		// Prepare output file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream destImage = ImageIO.createImageOutputStream(baos);
		writer.setOutput(destImage);

		// TIFFImageWriteParam
		ImageWriteParam iwparam = writer.getDefaultWriteParam();
		// System.out.println(iwparam);

		// Set the compression quality
		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// CCITT RLE, CCITT T.4, CCITT T.6, LZW, Old JPEG, JPEG, ZLib,
		// PackBits,Deflate, EXIF JPEG
		iwparam.setCompressionType("LZW");
		iwparam.setCompressionQuality(0.8f);

		// Write the image
		writer.write(null, new IIOImage(srcImage, null, null), iwparam);
		destImage.close();
		writer.dispose();

		// save to file
		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-compress-80-imageio.tif"), baos
				.toByteArray());
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	public void compressBmp() throws Exception {
		// Retrieve jpg image to be compressed
		RenderedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.bmp"));
		// Find a writer
		// BMPImageWriter
		// Iterator<ImageWriter> it =
		// ImageIO.getImageWritersByFormatName("bmp");
		// ImageWriter writer = null;
		// while (it.hasNext()) {
		// ImageWriter candidate = it.next();
		// writer = candidate;
		// break;
		// }
		ImageWriter writer = getImageWriterByFormatName("bmp");
		// System.out.println(writer);
		// Prepare output file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream destImage = ImageIO.createImageOutputStream(baos);
		writer.setOutput(destImage);

		// BMPImageWriteParam
		ImageWriteParam iwparam = writer.getDefaultWriteParam();
		// System.out.println(iwparam);

		// Set the compression quality
		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// BI_RGB, BI_RLE8, BI_RLE4, BI_BITFIELDS, BI_JPEG, BI_PNG
		iwparam.setCompressionType("BI_RGB");// 壓縮沒效果,所以只能轉成jpg
		iwparam.setCompressionQuality(0.8f);

		// Write the image
		writer.write(null, new IIOImage(srcImage, null, null), iwparam);
		destImage.close();
		writer.dispose();
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	public void compressBmpToFile() throws Exception {
		// Retrieve jpg image to be compressed
		RenderedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.bmp"));
		// Find a writer
		// BMPImageWriter
		// Iterator<ImageWriter> it =
		// ImageIO.getImageWritersByFormatName("bmp");
		// ImageWriter writer = null;
		// while (it.hasNext()) {
		// ImageWriter candidate = it.next();
		// writer = candidate;
		// break;
		// }
		ImageWriter writer = getImageWriterByFormatName("bmp");
		// System.out.println(writer);
		// Prepare output file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream destImage = ImageIO.createImageOutputStream(baos);
		writer.setOutput(destImage);

		// BMPImageWriteParam
		ImageWriteParam iwparam = writer.getDefaultWriteParam();
		// System.out.println(iwparam);

		// Set the compression quality
		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// BI_RGB, BI_RLE8, BI_RLE4, BI_BITFIELDS, BI_JPEG, BI_PNG
		iwparam.setCompressionType("BI_RGB");// 壓縮沒效果,所以只能轉成jpg
		iwparam.setCompressionQuality(0.8f);

		// Write the image
		writer.write(null, new IIOImage(srcImage, null, null), iwparam);
		destImage.close();
		writer.dispose();

		// save to file
		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-compress-80-imageio.bmp"), baos
				.toByteArray());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	public void compressGifToFile() throws Exception {
		// Retrieve jpg image to be compressed
		RenderedImage srcImage = ImageIO.read(new File(
				"custom/input/image/Tulips.gif"));
		// Find a writer
		// GIFImageWriter
		// Iterator<ImageWriter> it =
		// ImageIO.getImageWritersByFormatName("bmp");
		// ImageWriter writer = null;
		// while (it.hasNext()) {
		// ImageWriter candidate = it.next();
		// writer = candidate;
		// break;
		// }
		ImageWriter writer = getImageWriterByFormatName("gif");
		// System.out.println(writer);
		// Prepare output file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream destImage = ImageIO.createImageOutputStream(baos);
		writer.setOutput(destImage);

		// GIFImageWriteParam
		ImageWriteParam iwparam = writer.getDefaultWriteParam();
		// System.out.println(iwparam);

		// Set the compression quality
		// NOTE: Any method named [set|get]Compression.* throws
		// UnsupportedOperationException if false
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// LZW, lzw
		iwparam.setCompressionType("LZW");// 壓縮沒啥效果
		iwparam.setCompressionQuality(0.8f);

		// Write the image
		writer.write(null, new IIOImage(srcImage, null, null), iwparam);
		destImage.close();
		writer.dispose();

		// save to file
		FileUtils.writeByteArrayToFile(new File(
				"custom/input/image/Tulips-compress-80-imageio.gif"), baos
				.toByteArray());
	}
}
