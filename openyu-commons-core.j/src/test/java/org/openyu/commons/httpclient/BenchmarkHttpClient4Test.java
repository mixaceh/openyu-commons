package org.openyu.commons.httpclient;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.thread.ThreadHelper;

public class BenchmarkHttpClient4Test extends BaseTestSupporter {

	// ---------------------------------------------------
	// http
	// ---------------------------------------------------
	public static HttpClient createHttpClient() {
		HttpClient result = null;
		try {
			// result = HttpClientBuilder.create().build();
			// result = HttpClients.createDefault();

			// HttpClientBuilder
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setMaxConnPerRoute(100);
			builder.setMaxConnTotal(200);
			builder.setRetryHandler(new DefaultHttpRequestRetryHandler());

			// RequestConfig
			RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
			requestConfigBuilder.setConnectTimeout(5 * 1000);
			requestConfigBuilder.setSocketTimeout(5 * 1000);
			//
			RequestConfig requestConfig = requestConfigBuilder.build();
			builder.setDefaultRequestConfig(requestConfig);

			// ConnectionConfig
			ConnectionConfig.Builder connectionConfigBuilder = ConnectionConfig
					.custom();
			connectionConfigBuilder.setBufferSize(8192);
			connectionConfigBuilder.setCharset(Charset.forName("UTF-8"));
			//
			ConnectionConfig connectionConfig = connectionConfigBuilder.build();
			builder.setDefaultConnectionConfig(connectionConfig);
			//
			result = builder.build();

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

	/**
	 * Server: Apache-Coyote/1.1
	 * 
	 * Set-Cookie: JSESSIONID=7C09C512101EF47F7B9149FB7091DCE0; Path=/jl
	 *
	 * Content-Type: text/html;charset=UTF-8
	 *
	 * Content-Length: 709
	 *
	 * Date: Mon, 05 Jan 2015 03:02:58 GMT
	 *
	 * @param response
	 */
	public static void printHeader(HttpResponse response) {
		Header[] headers = response.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			System.out.println(headers[i]);
		}
	}

	// ---------------------------------------------------
	// native
	// ---------------------------------------------------
	public static class NativeTest extends BenchmarkHttpClient4Test {

		@Test
		// get: 10000 requests, 10359000 bytes / 17194 ms. = 602.48 BYTES/MS,
		// 588.36 K/S, 0.57 MB/S
		//
		// receive: 10000 requests, 18445493 bytes / 14029 ms. = 1314.81
		// BYTES/MS, 1284.0 K/S, 1.25 MB/S
		public void nativeGet() throws Exception {
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

								URL url = null;
								// 要關閉
								HttpURLConnection httpConn = null;
								// 要關閉
								InputStream in = null;
								try {
									StringBuilder encodedParams = new StringBuilder();
									//
									encodedParams.append("userId=");
									encodedParams.append(URLEncoder.encode(
											userId, "UTF-8"));
									//
									encodedParams.append("&bytes=");
									encodedParams.append(URLEncoder.encode(
											new String(buff), "UTF-8"));
									// 拼湊get請求的URL字串，使用URLEncoder.encode對特殊和不可見字元進行編碼
									url = new URL(URL + "?" + encodedParams);
									httpConn = (HttpURLConnection) url
											.openConnection();

									// 進行連接，但是實際上get
									// request要在下一句的connection.getInputStream()函數中才會真正發到伺服器
									httpConn.connect();

									// // 獲取代碼返回值
									int statusCode = httpConn.getResponseCode();
									// // 獲取返回內容類別型
									// String type = httpConn.getContentType();
									// // 獲取返回內容的字元編碼
									// String encoding = httpConn
									// .getContentEncoding();
									// // 獲取返回內容長度，單位位元組
									// int length = httpConn.getContentLength();
									// // 獲取完整的頭資訊Map
									// Map<String, List<String>> map = httpConn
									// .getHeaderFields();
									// System.out.println(map);

									// 返回內容
									// sun.net.www.protocol.http.HttpURLConnection$HttpInputStream@5ece2187
									// 必須有, 否則會沒發到伺服器
									in = httpConn.getInputStream();

									System.out.println("I[" + userId + "] R["
											+ i + "], " + statusCode);

									// buff = IoHelper.read(in);
									// System.out.println(buff.length);
									// System.out.println(new String(buff));

									timesCounter.incrementAndGet();
									byteCounter.addAndGet(ByteHelper
											.toByteArray(userId).length);
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (in != null) {
										in.close();
									}
									if (httpConn != null) {
										httpConn.disconnect();
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
		// post: 10000 requests, 102519000 bytes / 205312 ms. = 499.33 BYTES/MS,
		// 487.63 K/S, 0.48 MB/S
		//
		// receive: 10000 requests, 315585897 bytes / 201849 ms. = 1563.48
		// BYTES/MS, 1526.83 K/S, 1.49 MB/S
		public void nativePost() throws Exception {
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

								URL url = null;
								// 要關閉
								HttpURLConnection httpConn = null;
								// 要關閉
								InputStream in = null;
								try {
									StringBuilder encodedParams = new StringBuilder();
									//
									encodedParams.append("userId=");
									encodedParams.append(URLEncoder.encode(
											userId, "UTF-8"));
									//
									encodedParams.append("&bytes=");
									encodedParams.append(URLEncoder.encode(
											new String(buff), "UTF-8"));
									//
									url = new URL(URL);
									httpConn = (HttpURLConnection) url
											.openConnection();

									// 設置是否向connection輸出，因為這個是post請求，參數要放在http正文內，因此需要設為true
									httpConn.setDoOutput(true);
									// Read from the connection. Default is
									// true.
									httpConn.setDoInput(true);
									// Set the post method. Default is GET
									httpConn.setRequestMethod("POST");
									// Post cannot use caches
									// Post 請求不能使用緩存
									httpConn.setUseCaches(false);
									// connection.setFollowRedirects(true);
									httpConn.setInstanceFollowRedirects(true);

									// Set the content type to urlencoded,
									// because we will write
									// some URL-encoded content to the
									// connection. Settings above must be set
									// before connect!
									// 配置本次連接的Content-type，配置為application/x-www-form-urlencoded的
									// 意思是正文是urlencoded編碼過的form參數，下面我們可以看到我們對正文內容使用URLEncoder.encode
									// 進行編碼
									httpConn.setRequestProperty("Content-Type",
											"application/x-www-form-urlencoded");
									// 連接，從url.openConnection()至此的配置必須要在connect之前完成，
									// 要注意的是connection.getOutputStream會隱含的進行connect。
									httpConn.connect();
									//
									DataOutputStream out = new DataOutputStream(
											httpConn.getOutputStream());
									// The URL-encoded contend
									// 正文，正文內容其實跟get的URL中'?'後的參數字串一致
									out.writeBytes(encodedParams.toString());
									out.flush();
									out.close(); // flush and close

									// // 獲取代碼返回值
									int statusCode = httpConn.getResponseCode();
									// // 獲取返回內容類別型
									// String type = httpConn.getContentType();
									// // 獲取返回內容的字元編碼
									// String encoding = httpConn
									// .getContentEncoding();
									// // 獲取返回內容長度，單位位元組
									// int length = httpConn.getContentLength();
									// // 獲取完整的頭資訊Map
									// Map<String, List<String>> map = httpConn
									// .getHeaderFields();
									// System.out.println(map);

									// 返回內容
									// sun.net.www.protocol.http.HttpURLConnection$HttpInputStream@5ece2187
									// 必須有,否則會沒發到伺服器
									in = httpConn.getInputStream();

									System.out.println("I[" + userId + "] R["
											+ i + "], " + statusCode);

									// buff = IoHelper.read(in);
									// System.out.println(buff.length);
									// System.out.println(new String(buff));

									timesCounter.incrementAndGet();
									byteCounter.addAndGet(ByteHelper
											.toByteArray(userId).length);
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (in != null) {
										in.close();
									}
									if (httpConn != null) {
										httpConn.disconnect();
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
		// post/receive: 10000 requests, 82047512 bytes / 143394 ms. = 572.18
		// BYTES/MS, 558.77 K/S, 0.55 MB/S
		public void nativePostReturn() throws Exception {
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

								URL url = null;
								// 要關閉
								HttpURLConnection httpConn = null;
								// 要關閉
								InputStream in = null;
								try {
									StringBuilder encodedParams = new StringBuilder();
									//
									encodedParams.append("userId=");
									encodedParams.append(URLEncoder.encode(
											userId, "UTF-8"));
									//
									encodedParams.append("&bytes=");
									encodedParams.append(URLEncoder.encode(
											new String(buff), "UTF-8"));
									//
									url = new URL(URL);
									httpConn = (HttpURLConnection) url
											.openConnection();

									// 設置是否向connection輸出，因為這個是post請求，參數要放在http正文內，因此需要設為true
									httpConn.setDoOutput(true);
									// Read from the connection. Default is
									// true.
									httpConn.setDoInput(true);
									// Set the post method. Default is GET
									httpConn.setRequestMethod("POST");
									// Post cannot use caches
									// Post 請求不能使用緩存
									httpConn.setUseCaches(false);
									// connection.setFollowRedirects(true);
									httpConn.setInstanceFollowRedirects(true);

									// Set the content type to urlencoded,
									// because we will write
									// some URL-encoded content to the
									// connection. Settings above must be set
									// before connect!
									// 配置本次連接的Content-type，配置為application/x-www-form-urlencoded的
									// 意思是正文是urlencoded編碼過的form參數，下面我們可以看到我們對正文內容使用URLEncoder.encode
									// 進行編碼
									httpConn.setRequestProperty("Content-Type",
											"application/x-www-form-urlencoded");
									// 連接，從url.openConnection()至此的配置必須要在connect之前完成，
									// 要注意的是connection.getOutputStream會隱含的進行connect。
									httpConn.connect();
									//
									DataOutputStream out = new DataOutputStream(
											httpConn.getOutputStream());
									// The URL-encoded contend
									// 正文，正文內容其實跟get的URL中'?'後的參數字串一致
									out.writeBytes(encodedParams.toString());
									out.flush();
									out.close(); // flush and close

									// // 獲取代碼返回值
									int statusCode = httpConn.getResponseCode();
									// // 獲取返回內容類別型
									// String type = httpConn.getContentType();
									// // 獲取返回內容的字元編碼
									// String encoding = httpConn
									// .getContentEncoding();
									// // 獲取返回內容長度，單位位元組
									// int length = httpConn.getContentLength();
									// // 獲取完整的頭資訊Map
									// Map<String, List<String>> map = httpConn
									// .getHeaderFields();
									// System.out.println(map);

									// 返回內容
									// sun.net.www.protocol.http.HttpURLConnection$HttpInputStream@5ece2187
									// 必須有,否則會沒發到伺服器
									in = httpConn.getInputStream();

									System.out.println("I[" + userId + "] R["
											+ i + "], " + statusCode);

									buff = IoHelper.read(in);
									// System.out.println(buff.length);
									// System.out.println(new String(buff));

									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (in != null) {
										in.close();
									}
									if (httpConn != null) {
										httpConn.disconnect();
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

	// ---------------------------------------------------
	// http
	// ---------------------------------------------------
	public static class HttpTest extends BenchmarkHttpClient4Test {

		@Test
		// get: 10000 requests, 10359000 bytes / 19078 ms. = 542.98 BYTES/MS,
		// 530.26 K/S, 0.52 MB/S
		//
		// receive: 10000 requests, 9745852 bytes / 15897 ms. = 613.06 BYTES/MS,
		// 598.69 K/S, 0.58 MB/S
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
								HttpGet httpGet = null;
								// 要關閉
								InputStream in = null;
								try {
									httpGet = new HttpGet(URL);

									// params
									List<NameValuePair> params = new LinkedList<NameValuePair>();
									params.add(new BasicNameValuePair("userId",
											userId));
									params.add(new BasicNameValuePair("bytes",
											new String(buff)));
									// encode
									StringBuilder encodedParams = new StringBuilder();
									encodedParams.append(EntityUtils
											.toString(new UrlEncodedFormEntity(
													params)));
									httpGet.setURI(new URI(httpGet.getURI()
											.toString() + "?" + encodedParams));
									//
									HttpResponse response = httpClient
											.execute(httpGet);
									int statusCode = response.getStatusLine()
											.getStatusCode();

									System.out.println("I[" + userId + "] R["
											+ i + "], " + statusCode);
									// 200=SC_OK
									// 400=SC_BAD_REQUEST
									if (statusCode == HttpStatus.SC_OK) {
										// 返回內容
										// HttpEntity entity = response
										// .getEntity();
										// // 用EntityUtils.toByteArray
										// buff =
										// EntityUtils.toByteArray(entity);

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
									if (httpGet != null) {
										httpGet.releaseConnection();
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

		// post: 10000 requests, 102519000 bytes / 146371 ms. = 700.41 BYTES/MS,
		// 683.99 K/S, 0.67 MB/S
		//
		// receive: 10000 requests, 96405353 bytes / 143142 ms. = 673.49
		// BYTES/MS, 657.71 K/S, 0.64 MB/S
		@Test
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
								HttpPost httpPost = null;
								// 要關閉
								InputStream in = null;
								try {
									httpPost = new HttpPost(URL);
									// params
									List<NameValuePair> params = new LinkedList<NameValuePair>();
									params.add(new BasicNameValuePair("userId",
											userId));
									params.add(new BasicNameValuePair("bytes",
											new String(buff)));
									httpPost.setEntity(new UrlEncodedFormEntity(
											params));
									//
									HttpResponse response = httpClient
											.execute(httpPost);
									int statusCode = response.getStatusLine()
											.getStatusCode();

									System.out.println("I[" + userId + "] R["
											+ i + "], " + statusCode);
									//
									if (statusCode == HttpStatus.SC_OK) {
										// 返回內容
										// HttpEntity entity = response
										// .getEntity();
										// // 用EntityUtils.toByteArray
										// buff =
										// EntityUtils.toByteArray(entity);

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
									if (httpPost != null) {
										httpPost.releaseConnection();
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
		// post/receive: 10000 requests, 100143953 bytes / 143756 ms. = 696.62
		// BYTES/MS, 680.3 K/S, 0.66 MB/S
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
								HttpPost httpPost = null;
								// 要關閉
								InputStream in = null;
								try {
									httpPost = new HttpPost(URL);
									// params
									List<NameValuePair> params = new LinkedList<NameValuePair>();
									params.add(new BasicNameValuePair("userId",
											userId));
									params.add(new BasicNameValuePair("bytes",
											new String(buff)));
									httpPost.setEntity(new UrlEncodedFormEntity(
											params));
									//
									HttpResponse response = httpClient
											.execute(httpPost);
									int statusCode = response.getStatusLine()
											.getStatusCode();

									System.out.println("I[" + userId + "] R["
											+ i + "], " + statusCode);
									//
									if (statusCode == HttpStatus.SC_OK) {
										// 返回內容
										HttpEntity entity = response
												.getEntity();
										// 用EntityUtils.toByteArray
										buff = EntityUtils.toByteArray(entity);

										// 或用
										// org.apache.http.conn.EofSensorInputStream
										// in =
										// response.getEntity().getContent();
										// buff = IoHelper.read(in);
										// System.out.println(buff.length);
										// System.out.println(new String(buff));

										timesCounter.incrementAndGet();
										// 10013, 9966...
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
									if (httpPost != null) {
										httpPost.releaseConnection();
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

//
// URL url = new URL(urlString);
//
// // Open a HTTP connection to the URL
//
// conn = (HttpURLConnection) url.openConnection();
//
// // Allow Inputs
// conn.setDoInput(true);
//
// // Allow Outputs
// conn.setDoOutput(true);
//
// // Don't use a cached copy.
// conn.setUseCaches(false);
//
// // Use a post method.
// conn.setRequestMethod("POST");
//
// conn.setRequestProperty("Connection", "Keep-Alive");
//
// conn.setRequestProperty("Content-Type",
// "multipart/form-data;boundary=" + boundary);