package org.openyu.commons.httpclient;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.junit.Test;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.thread.ThreadHelper;

public class BenchmarkHttpClient3Test extends BaseTestSupporter {

	// ---------------------------------------------------
	// http
	// ---------------------------------------------------
	public static HttpClient createHttpClient() {
		HttpClient result = null;
		try {

			result = new HttpClient();
			// 使用多緒HttpClient
			MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
			HttpConnectionManagerParams managerParams = httpConnectionManager
					.getParams();
			managerParams.setDefaultMaxConnectionsPerHost(100);
			managerParams.setMaxTotalConnections(200);
			// 連線超時
			managerParams.setConnectionTimeout(5 * 1000);
			// 讀取超時
			managerParams.setSoTimeout(5 * 1000);
			//
			result.setHttpConnectionManager(httpConnectionManager);
			//
			HttpClientParams params = result.getParams();
			// http.protocol.content-charset
			params.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			// 失敗 retry 3 次
			params.setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(3, false));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Test
	public void httpClient() {
		HttpClient httpClient = createHttpClient();
		System.out.println(httpClient);
		assertNotNull(httpClient);
	}

	// ---------------------------------------------------
	// http
	// ---------------------------------------------------
	public static class HttpTest extends BenchmarkHttpClient3Test {

		@Test
		// get: 10000 requests, 10359000 bytes / 17374 ms. = 596.24 BYTES/MS,
		// 582.26 K/S, 0.57 MB/S
		//
		// receive: 10000 requests, 31672385 bytes / 14260 ms. = 2221.06
		// BYTES/MS, 2169.01 K/S, 2.12 MB/S
		//
		// most get have a limit of 8192 bytes (8KB)
		public void httpGet() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 1 * 1024;// 1k
			//
			final String URL = "http://10.16.210.180:8080/jl/stress";
			// reset counter
			// http://10.16.210.180:8080/jl/stress?r=1
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			// 
			final HttpClient httpClient = createHttpClient();
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								// 要釋放
								GetMethod getMethod = null;
								// 要關閉
								InputStream in = null;
								try {
									getMethod = new GetMethod(URL);
									//
									NameValuePair userIdParam = new NameValuePair(
											"userId", userId);
									NameValuePair bytesParam = new NameValuePair(
											"bytes", new String(buff));
									NameValuePair[] params = { userIdParam,
											bytesParam };
									getMethod.setQueryString(params);
									//
									int statusCode = httpClient
											.executeMethod(getMethod);

									System.out.println("I[" + userId + "] R["
											+ i + "], " + statusCode);
									// 200=SC_OK
									// 400=SC_BAD_REQUEST
									if (statusCode == HttpStatus.SC_OK) {
										// in = post.getResponseBodyAsStream();
										// buff = IoHelper.read(in);
										// System.out.println(buff.length);
										// System.out.println(new String(buff));

										timesCounter.incrementAndGet();
										byteCounter.addAndGet(ByteHelper
												.toByteArray(userId).length);
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (in != null) {
										in.close();
									}
									if (getMethod != null) {
										getMethod.releaseConnection();
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
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("get: " + timesCounter.get() + " requests, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}

		@Test
		// post: 10000 requests, 102519000 bytes / 125022 ms. = 820.01 BYTES/MS,
		// 800.79 K/S, 0.78 MB/S
		//
		// receive: 10000 requests, 315547453 bytes / 121751 ms. = 2591.74
		// BYTES/MS, 2531.0 K/S, 2.47 MB/S
		public void httpPost() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String URL = "http://10.16.210.180:8080/jl/stress";
			// reset counter
			// http://10.16.210.180:8080/jl/stress?r=1
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			final HttpClient httpClient = createHttpClient();
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								// 要釋放
								PostMethod postMethod = null;
								// 要關閉
								InputStream in = null;
								try {
									postMethod = new PostMethod(URL);
									// params
									NameValuePair userIdParam = new NameValuePair(
											"userId", userId);
									NameValuePair bytesParam = new NameValuePair(
											"bytes", new String(buff));
									NameValuePair[] params = { userIdParam,
											bytesParam };
									postMethod.setRequestBody(params);
									//
									int statusCode = httpClient
											.executeMethod(postMethod);

									System.out.println("I[" + userId + "] R["
											+ i + "], " + statusCode);
									//
									if (statusCode == HttpStatus.SC_OK) {
										// 返回內容
										// in = post.getResponseBodyAsStream();
										// buff = IoHelper.read(in);
										// System.out.println(buff.length);
										// System.out.println(new String(buff));

										timesCounter.incrementAndGet();
										byteCounter.addAndGet(ByteHelper
												.toByteArray(userId).length);
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (in != null) {
										in.close();
									}
									if (postMethod != null) {
										postMethod.releaseConnection();
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
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("post: " + timesCounter.get() + " requests, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}

		@Test
		// post/receive: 10000 requests, 319332367 bytes / 123217 ms. = 2591.63
		// BYTES/MS, 2530.88 K/S, 2.47 MB/S
		public void httpPostReturn() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String URL = "http://10.16.210.180:8080/jl/stressReturn";
			// reset counter
			// http://10.16.210.180:8080/jl/stressReturn?r=1
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			final HttpClient httpClient = createHttpClient();
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
			long beg = System.currentTimeMillis();
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//
				final String userId = "TEST_USER_" + i;
				service.submit(new Runnable() {
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								// 要釋放
								PostMethod postMethod = null;
								// 要關閉
								InputStream in = null;
								try {
									postMethod = new PostMethod(URL);
									// params
									NameValuePair userIdParam = new NameValuePair(
											"userId", userId);
									NameValuePair bytesParam = new NameValuePair(
											"bytes", new String(buff));
									NameValuePair[] params = { userIdParam,
											bytesParam };
									postMethod.setRequestBody(params);
									//
									int statusCode = httpClient
											.executeMethod(postMethod);

									System.out.println("I[" + userId + "] R["
											+ i + "], " + statusCode);
									//
									if (statusCode == HttpStatus.SC_OK) {
										// 返回內容
										// org.apache.commons.httpclient.AutoCloseInputStream
										in = postMethod.getResponseBodyAsStream();
										buff = IoHelper.read(in);
										// System.out.println(buff.length);
										// System.out.println(new String(buff));

										timesCounter.incrementAndGet();
										// 32469, 32111...
										byteCounter.addAndGet(buff.length);
									}
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (in != null) {
										in.close();
									}
									if (postMethod != null) {
										postMethod.releaseConnection();
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
			double result = NumberHelper.round(
					byteCounter.get() / (double) dur, 2);
			double kresult = NumberHelper
					.round((byteCounter.get() / (double) 1024)
							/ (dur / (double) 1000), 2);
			double mbresult = NumberHelper.round((byteCounter.get()
					/ (double) 1024 / (double) 1024)
					/ (dur / (double) 1000), 2);
			//
			System.out.println("post/receive: " + timesCounter.get()
					+ " requests, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, "
					+ mbresult + " MB/S");
		}
	}
}
