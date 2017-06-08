package org.openyu.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.input.AutoCloseInputStream;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class IoHelperTest extends BaseTestSupporter {

	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void write() throws Exception {
		final String FILE_NAME = "custom/output/test.log";
		final int LENGTH_OF_BYTES = 10 * 1024;// 10k
		//
		OutputStream value = IoHelper.createOutputStream(FILE_NAME);
		//
		byte[] buff = ByteHelper.toByteArray("中文測試123@中文測試123@中文測試123");
		System.out.println("length: " + buff.length);// 47
		boolean writed = IoHelper.write(value, buff);
		System.out.println(writed);
		//
		buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
		System.out.println("length: " + buff.length);// 10240
		writed = IoHelper.write(value, buff);
		System.out.println(writed);
		//
		IoHelper.close(value);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void createInputStream() throws Exception {
		final String FILE_NAME = "custom/output/test.log";
		//
		InputStream value = IoHelper.createInputStream(FILE_NAME);
		System.out.println(value);
		assertNotNull(value);
		//
		System.out.println(value.available());// 10287
		//
		IoHelper.close(value);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void createOutputStream() {
		final String FILE_NAME = "custom/output/outputStream.log";
		//
		OutputStream value = IoHelper.createOutputStream(FILE_NAME);
		System.out.println(value);
		assertNotNull(value);
		//
		byte[] buff = ByteHelper.toByteArray("中文測試123");
		IoHelper.write(value, buff);
		//
		IoHelper.close(value);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void createZipOutputStream() throws Exception {
		final String FILE_NAME = "custom/output/zipOutputStream.zip";
		//
		ZipOutputStream value = IoHelper.createZipOutputStream(FILE_NAME);
		System.out.println(value);
		assertNotNull(value);
		//
		String fileName = "custom/output/test.log";
		byte[] contents = IoHelper.read(fileName);
		ZipEntry zipEntry = new ZipEntry(fileName);
		value.putNextEntry(zipEntry);
		value.write(contents);

		// 須加 close,強制寫入
		IoHelper.close(value);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void createWriter() {
		final String FILE_NAME = "custom/output/writer.log";
		//
		Writer value = IoHelper.createWriter(FILE_NAME);
		//
		String buff = "中文測試123";
		IoHelper.write(value, buff);
		// 須加 close,強制寫入
		IoHelper.close(value);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void createPrintWriter() {
		final String FILE_NAME = "custom/output/printWriter.log";
		//
		PrintWriter writer = IoHelper.createPrintWriter(FILE_NAME);
		//
		String buff = "中文測試123";
		IoHelper.write(writer, buff);
		// 須加 close,強制寫入
		IoHelper.close(writer);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void available() {
		final String FILE_NAME = "custom/output/test.log";
		//
		InputStream value = IoHelper.createInputStream(FILE_NAME);
		// 第1次available
		int result = IoHelper.available(value);
		System.out.println(result);

		// 把資料讀完,再取available
		byte[] buff = IoHelper.read(value);
		System.out.println(new String(buff));

		// 第2次available
		result = IoHelper.available(value);
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void availableWithAutoCloseInputStream() throws Exception {
		final String FILE_NAME = "custom/output/test.log";
		//
		InputStream value = IoHelper.createInputStream(FILE_NAME);
		// httpclient 3
		value = new AutoCloseInputStream(value);

		// 第1次available
		int result = IoHelper.available(value);
		System.out.println(result);
		// close
		value.close();

		// close會造成available=0, 第2次available=0
		result = IoHelper.available(value);
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void availableWithEofSensorInputStream() throws Exception {
		final String FILE_NAME = "custom/output/test.log";
		//
		InputStream value = IoHelper.createInputStream(FILE_NAME);
		// httpclient 4
		value = new EofSensorInputStream(value, null);

		// 第1次available
		int result = IoHelper.available(value);
		System.out.println(result);
		// close
		value.close();

		// / close會造成available=0, 第2次available會丟出exception
		// java.io.IOException: Attempted read on closed stream.
		result = IoHelper.available(value);
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void read() {
		final String FILE_NAME = "pom.xml";
		InputStream value = IoHelper.createInputStream(FILE_NAME);

		// 第1次讀
		byte[] buff = IoHelper.read(value);
		System.out.println(new String(buff));
		assertNotNull(buff);

		// 第2次讀,InputStream無法再讀一次,傳回null
		buff = IoHelper.read(value);
		System.out.println(buff);
		assertNull(buff);
		//
		IoHelper.close(value);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void readWithFileName() {
		final String FILE_NAME = "pom.xml";

		byte[] buff = IoHelper.read(FILE_NAME);
		System.out.println(new String(buff));
		assertNotNull(buff);
		//
		buff = IoHelper.read("fileNotExist.log");
		System.out.println(buff);
		assertNull(buff);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void readWithAction() {
		final String FILE_NAME = "pom.xml";
		File file = new File(FILE_NAME);
		// 檔案總長度
		long length = file.length();
		System.out.println("file length: " + length);// 1959
		//
		InputStream value = IoHelper.createInputStream(file);
		//
		final int BLOCK_LENGTH = 16;
		int expectedBlockCount = (int) Math.ceil((length / (BLOCK_LENGTH * 1.0d)));
		System.out.println("expectedBlockCount: " + expectedBlockCount);
		//
		final AtomicInteger actualBlockCount = new AtomicInteger();

		// true=繼續往下一段byte[], false=中斷
		final boolean READ_NEXT_BLOCK = false;
		//
		byte[] buff = IoHelper.read(value, BLOCK_LENGTH, new InputStreamCallback() {
			public boolean doInAction(byte[] blockArray) {
				// System.out
				// .println("block length: " + blockArray.length);

				actualBlockCount.incrementAndGet();

				// true=繼續往下一段byte[], false=中斷
				return READ_NEXT_BLOCK;
				// return false;
			}
		});

		System.out.println("actualBlockCount: " + actualBlockCount);
		System.out.println("byteArray length: " + buff.length);// 358912

		assertNotNull(buff);

		if (READ_NEXT_BLOCK) {
			assertEquals(expectedBlockCount, actualBlockCount.get());
		} else {
			assertEquals(1, actualBlockCount.get());
		}

		//
		IoHelper.close(value);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void readString() {
		String result = null;
		// 1.data/json/java.util.Date.json
		result = IoHelper.readString("data/json/java.util.Date.json");
		//
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void writeToString() {
		boolean result = false;
		// 1.data/json/java.util.Date.json
		result = IoHelper.writeToString("data/json/test.txt", "test123");
		//
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void closeCloseable() throws FileNotFoundException {
		RandomAccessFile file = new RandomAccessFile(new File("pom.xml").getAbsoluteFile(), "rw");
		IoHelper.close(file);
	}
}
