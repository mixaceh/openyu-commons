package org.openyu.commons.im4java;

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
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.core.Operation;
import org.im4java.process.ProcessStarter;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BenchmarkIm4JavaTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ProcessStarter.setGlobalSearchPath(System.getenv("IM4JAVA_TOOLPATH"));
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void compressJpgToFile() throws Exception {
		IMOperation op = new IMOperation();
		op.addImage("custom/input/image/Tulips.jpg");
		op.compress("JPEG");
		op.addImage("custom/input/image/Tulips-compress-80-im.jpg");

		// //這裡不加參數或者參數為false是使用ImageMagick，true是使用GraphicsMagick
		// GraphicsMagick
		ConvertCmd cmd = new ConvertCmd(true);

		// ImageMagick
		// ConvertCmd cmd = new ConvertCmd(false);
		// cmd.setSearchPath("C:/Program Files/ImageMagick-6.9.1-Q8");
		//
		// Caused by: org.im4java.core.CommandException: convert.exe: Wrong JPEG
		// library version: library is 90, caller expects 80
		// `custom/input/image/Tulips.jpg' @ error/jpeg.c/JPEGErrorHandler/322.

		cmd.run(op);
	}
}
