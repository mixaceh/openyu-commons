package org.openyu.commons.fto.commons.net.ftp.supporter;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.fto.supporter.CommonFtoSupporter;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BenchmarkFtpClientFtoSupporterTest extends BaseTestSupporter {

	private static CommonFtoSupporter ftpClientFtoSupporter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"applicationContext-bean.xml", //
				"applicationContext-ftp-client.xml",//
		});
		ftpClientFtoSupporter = (CommonFtoSupporter) applicationContext.getBean("ftpClientFtoSupporter");
	}

	@Test
	public void ftpClientFtoSupporter() {
		System.out.println(ftpClientFtoSupporter);
		assertNotNull(ftpClientFtoSupporter);
	}

	// ---------------------------------------------------
	// native
	// ---------------------------------------------------
	public static FTPClient createFTPClient() {
		FTPClient result = null;
		try {
			result = new FTPClient();
			//
			result.setConnectTimeout(5000);
			result.setSendBufferSize(128 * 1024);
			result.setReceiveBufferSize(128 * 1024);
			//
			result.connect("hpc", 8888);
			boolean login = result.login("root", "1111");
			if (!login) {
				System.out.println("login: " + login);
				return null;
			}
			//
			int reply = result.getReplyCode();
			// FTPReply stores a set of constants for FTP reply codes.
			if (!FTPReply.isPositiveCompletion(reply)) {
				result.disconnect();
				return null;
			}
			//
			result.enterLocalPassiveMode();
			result.setFileType(FTPClient.BINARY_FILE_TYPE);
			result.setControlEncoding("UTF-8");
			result.changeWorkingDirectory("/");
			//
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Test
	public void ftpClient() {
		FTPClient ftpClient = createFTPClient();
		System.out.println(ftpClient);
		assertNotNull(ftpClient);
	}

	@Test
	public void createDir() throws Exception {
		FTPClient ftpClient = createFTPClient();
		String DIR = "TEST_BENCHMARK";
		boolean maked = ftpClient.makeDirectory(DIR);
		System.out.println(maked);
	}

	@Test
	public void storeFile() throws Exception {
		final int LENGTH_OF_BYTES = 10 * 1024;// 10k
		String DIR = "TEST_BENCHMARK";
		//
		InputStream in = null;
		FTPClient ftpClient = null;
		try {
			ftpClient = createFTPClient();
			//
			in = new ByteArrayInputStream(ByteHelper.randomByteArray(LENGTH_OF_BYTES));
			boolean writed = ftpClient.storeFile(DIR + "/TEST.txt", in);
			System.out.println(writed);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(in);
			if (ftpClient != null) {
				ftpClient.disconnect();
			}
		}
	}

	// ---------------------------------------------------
	// native
	// ---------------------------------------------------
	public static class NativeTest extends BenchmarkFtpClientFtoSupporterTest {

		@Test
		// storeFile: 10000 files, 102400000 bytes / 845318 ms. = 121.14
		// BYTES/MS, 118.3 K/S, 0.12 MB/S
		// storeFile: 10000 files, 102400000 bytes / 683663 ms. = 149.78
		// BYTES/MS, 146.27 K/S, 0.14 MB/S
		public void nativeStoreFile() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String DIR = "TEST_BENCHMARK";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								//
								FTPClient ftpClient = null;
								InputStream in = null;
								try {
									ftpClient = createFTPClient();
									//
									in = new ByteArrayInputStream(buff);
									// 0_0.txt
									String fileName = userId + "_" + i + ".txt";
									boolean writed = ftpClient.storeFile(DIR + "/" + fileName, in);

									System.out.println("I[" + userId + "] F[" + i + "], " + writed);
									//
									if (writed) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									IoHelper.close(in);
									try {
										if (ftpClient != null) {
											ftpClient.logout();
											ftpClient.disconnect();
										}
									} catch (Exception ex) {
									}
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("storeFile: " + timesCounter.get() + " files, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

		@Test
		// retrieveFile: 10000 files, 102400000 bytes / 27530 ms. = 3719.58
		// BYTES/MS, 3632.4 K/S, 3.55 MB/S
		public void nativeRetrieveFile() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String DIR = "TEST_BENCHMARK";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								//
								FTPClient ftpClient = null;
								OutputStream out = null;
								//
								try {
									ftpClient = createFTPClient();
									//
									out = new ByteArrayOutputStream();
									String fileName = userId + "_" + i + ".txt";
									boolean read = ftpClient.retrieveFile(DIR + "/" + fileName, out);

									System.out.println("I[" + userId + "] F[" + i + "], " + read);
									//
									if (read) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									IoHelper.close(out);
									try {
										if (ftpClient != null) {
											ftpClient.logout();
											ftpClient.disconnect();
										}
									} catch (Exception ex) {
									}
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("retrieveFile: " + timesCounter.get() + " files, " + byteCounter.get() + " bytes / "
					+ dur + " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

		@Test
		// rename: 10000 files / 17490 ms. = 0.57 F/MS, 571.76 F/S
		// rename: 10000 files / 24834 ms. = 0.4 F/MS, 402.47 F/S
		public void nativeRename() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String DIR = "TEST_BENCHMARK";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								//
								FTPClient ftpClient = null;
								OutputStream out = null;
								//
								try {
									ftpClient = createFTPClient();
									//
									out = new ByteArrayOutputStream();
									String fileName = userId + "_" + i + ".txt";
									String newName = "RENAME_" + fileName;
									boolean renamed = ftpClient.rename(DIR + "/" + fileName, DIR + "/" + newName);

									System.out.println("I[" + userId + "] F[" + i + "], " + renamed);
									//
									if (renamed) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									IoHelper.close(out);
									try {
										if (ftpClient != null) {
											ftpClient.logout();
											ftpClient.disconnect();
										}
									} catch (Exception ex) {
									}
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(timesCounter.get() / (double) dur, 2);
			double sresult = NumberHelper.round(timesCounter.get() / (dur / (double) 1000), 2);
			//
			System.out.println("rename: " + timesCounter.get() + " files / " + dur + " ms. = " + result + " F/MS, "
					+ sresult + " F/S");
		}

		@Test
		// deleteFile: 10000 files / 24450 ms. = 0.41 F/MS, 408.79 F/S
		public void nativeDeleteFile() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String DIR = "TEST_BENCHMARK";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								//
								FTPClient ftpClient = null;
								OutputStream out = null;
								//
								try {
									ftpClient = createFTPClient();
									//
									out = new ByteArrayOutputStream();
									String fileName = userId + "_" + i + ".txt";
									String newName = "RENAME_" + fileName;
									boolean deleted = ftpClient.deleteFile(DIR + "/" + newName);

									System.out.println("I[" + userId + "] F[" + i + "], " + deleted);
									//
									if (deleted) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									IoHelper.close(out);
									try {
										if (ftpClient != null) {
											ftpClient.logout();
											ftpClient.disconnect();
										}
									} catch (Exception ex) {
									}
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(timesCounter.get() / (double) dur, 2);
			double sresult = NumberHelper.round(timesCounter.get() / (dur / (double) 1000), 2);
			//
			System.out.println("deleteFile: " + timesCounter.get() + " files, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " F/MS, " + sresult + " F/S");
		}
	}

	// ---------------------------------------------------
	// optimized
	// ---------------------------------------------------
	public static class OptimizedTest extends BenchmarkFtpClientFtoSupporterTest {

		@Test
		// write: 10000 files, 102400000 bytes / 512460 ms. = 199.82 BYTES/MS,
		// 195.14 K/S, 0.19 MB/S
		public void optimizedWrite() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String DIR = "TEST_BENCHMARK";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					//
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								InputStream in = null;
								try {
									//
									in = new ByteArrayInputStream(buff);
									// 0_0.txt
									String fileName = userId + "_" + i + ".txt";
									boolean writed = ftpClientFtoSupporter.write(DIR + "/" + fileName, in);

									System.out.println("I[" + userId + "] F[" + i + "], " + writed);
									//
									if (writed) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									IoHelper.close(in);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("write: " + timesCounter.get() + " files, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

		@Test
		// read: 10000 files, 102400000 bytes / 15862 ms. = 6455.68 BYTES/MS,
		// 6304.38 K/S, 6.16 MB/S
		public void optimizedRead() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String DIR = "TEST_BENCHMARK";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								OutputStream out = null;
								//
								try {
									out = new ByteArrayOutputStream();
									String fileName = userId + "_" + i + ".txt";
									boolean read = ftpClientFtoSupporter.read(DIR + "/" + fileName, out);

									System.out.println("I[" + userId + "] F[" + i + "], " + read);
									//
									if (read) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									IoHelper.close(out);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper.round((byteCounter.get() / (double) 1024) / (dur / (double) 1000), 2);
			double mbresult = NumberHelper
					.round((byteCounter.get() / (double) 1024 / (double) 1024) / (dur / (double) 1000), 2);
			//
			System.out.println("read: " + timesCounter.get() + " files, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, " + mbresult + " MB/S");
		}

		@Test
		// rename: 10000 files / 15798 ms. = 0.63 F/MS, 632.99 F/S
		public void optimizedRename() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String DIR = "TEST_BENCHMARK";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								OutputStream out = null;
								//
								try {
									out = new ByteArrayOutputStream();
									String fileName = userId + "_" + i + ".txt";
									String newName = "RENAME_" + fileName;
									boolean renamed = ftpClientFtoSupporter.rename(DIR + "/" + fileName,
											DIR + "/" + newName);

									System.out.println("I[" + userId + "] F[" + i + "], " + renamed);
									//
									if (renamed) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									IoHelper.close(out);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						//
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(timesCounter.get() / (double) dur, 2);
			double sresult = NumberHelper.round(timesCounter.get() / (dur / (double) 1000), 2);
			//
			System.out.println("rename: " + timesCounter.get() + " files / " + dur + " ms. = " + result + " F/MS, "
					+ sresult + " F/S");
		}

		@Test
		// delete: 10000 files / 15845 ms. = 0.63 F/MS, 630.81 F/S
		public void optimizedDelete() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String DIR = "TEST_BENCHMARK";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper.randomByteArray(LENGTH_OF_BYTES);
								OutputStream out = null;
								//
								try {
									out = new ByteArrayOutputStream();
									String fileName = userId + "_" + i + ".txt";
									String newName = "RENAME_" + fileName;
									boolean deleted = ftpClientFtoSupporter.delete(DIR + "/" + newName);

									System.out.println("I[" + userId + "] F[" + i + "], " + deleted);
									//
									if (deleted) {
										timesCounter.incrementAndGet();
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									IoHelper.close(out);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
						}
						ThreadHelper.sleep(3 * 1000);
					}
				});
				//
				ThreadHelper.sleep(50);
			}
			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			//
			long end = System.currentTimeMillis();
			long dur = (end - beg);
			double result = NumberHelper.round(timesCounter.get() / (double) dur, 2);
			double sresult = NumberHelper.round(timesCounter.get() / (dur / (double) 1000), 2);
			//
			System.out.println("delete: " + timesCounter.get() + " files / " + dur + " ms. = " + result + " F/MS, "
					+ sresult + " F/S");
		}
	}

}
