package org.openyu.commons.thumbnails;

import java.io.ByteArrayOutputStream;

import net.coobird.thumbnailator.Thumbnails;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BenchmarkThumbnailsTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 1.96 [+- 0.58], round.block: 0.00 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 9, GC.time: 0.08, time.total: 2.72, time.warmup: 0.00,
	// time.bench: 2.72
	public void resizeJpg() throws Exception {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Thumbnails.of("custom/input/image/Tulips.jpg").scale(0.8f)
				.toOutputStream(bos);

		// Thumbnails.of("custom/input/image/Tulips.jpg").scale(0.8f)
		// .antialiasing(Antialiasing.ON)
		// .toFile("custom/input/image/Tulips-resize-80-thumbnails.jpg");
	}

	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency = 100)
	@Test
	// round: 3.69 [+- 1.25], round.block: 0.02 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 8, GC.time: 0.10, time.total: 5.49, time.warmup: 0.00,
	// time.bench: 5.49
	public void resizePng() throws Exception {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Thumbnails.of("custom/input/image/Tulips.png").scale(0.8f)
				.toOutputStream(bos);

		// Thumbnails.of("custom/input/image/Tulips.jpg").scale(0.8f)
		// .toFile("custom/input/image/Tulips-resize-80-thumbnails.png");
	}
}
