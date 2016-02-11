package org.openyu.commons.jmagick;

import java.awt.Dimension;

import magick.ColorspaceType;
import magick.ImageInfo;
import magick.MagickImage;
import magick.QuantizeInfo;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BenchmarkJmagickTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Caused by: java.lang.UnsatisfiedLinkError:
		// lib-native\jmagick-6.3.9-Q8.dll:
		// Can't load IA 32-bit .dll on a AMD 64-bit platform
		// System.loadLibrary("jmagick-6.3.9-Q8");

		// download from https://github.com/ferbar/jmagick/tree/master/bin
		// libJMagick-6.4.0-64.dll, but still failed
		//
		// Caused by: java.lang.UnsatisfiedLinkError:
		// lib-native\JMagick-6.8.9-Q16-64.dll:
		// The specified procedure could not be found
		System.loadLibrary("libJMagick-6.4.0-64");
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 1, concurrency = 1)
	@Test
	public void compressPngToFile() throws Exception {
		ImageInfo info = new ImageInfo("custom/input/image/Tulips.png");
		MagickImage srcImage = new MagickImage(info);
		Dimension orgDim = srcImage.getDimension();
		int width = (int) orgDim.getWidth();
		int height = (int) orgDim.getHeight();
		System.out.println("orginal Colors: " + srcImage.getNumberColors());
		System.out.println("orginal Depth: " + srcImage.getDepth());
		System.out.println("orginal Colorspace: " + srcImage.getColorspace());
		System.out.println("orginal Total colors " + srcImage.getTotalColors());
		System.out.println("orginal Resolution units: " + srcImage.getUnits());

		// clear all the metadata
		srcImage.profileImage("*", null);
		srcImage.setImageFormat("PNG");

		/*
		 * If I use the scaleImage here, I can get one small image, but the
		 * quality is worse.
		 */
		srcImage = srcImage.scaleImage((int) (width * 0.8),
				(int) (height * 0.8));
		srcImage = srcImage.scaleImage(width, height);

		srcImage.setDepth(1);

		QuantizeInfo quantizeInfo = new QuantizeInfo();
		quantizeInfo.setColorspace(ColorspaceType.CMYKColorspace);// XYZColorspace
		quantizeInfo.setNumberColors(8);
		quantizeInfo.setTreeDepth(1);
		quantizeInfo.setColorspace(0);
		quantizeInfo.setDither(0);

		srcImage.quantizeImage(quantizeInfo);

		System.out.println("last Colors: " + srcImage.getNumberColors());
		System.out.println("last Depth: " + srcImage.getDepth());
		System.out.println("last Colorspace: " + srcImage.getColorspace());
		System.out.println("last Total colors " + srcImage.getTotalColors());
		System.out.println("last Resolution units: " + srcImage.getUnits());

		// TODO justify to write in cache file
		srcImage.setFileName("custom/input/image/Tulips-compress-80-jmagick.png");
		srcImage.writeImage(info);
	}
}
