package org.openyu.commons.mqo.rabbitmq.supporter;

import static org.junit.Assert.assertNotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.AMQP.Queue.DeleteOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.thread.ThreadHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BenchmarkRmMqoSupporterTest extends BaseTestSupporter {

	private static RabbitMqoSupporter rmMqoSupporter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"META-INF/applicationContext-commons-core.xml",//
				"applicationContext-init.xml",//
				"applicationContext-rabbitmq.xml",//
		});
		rmMqoSupporter = (RabbitMqoSupporter) applicationContext
				.getBean("rmMqoSupporter");
		//
	}

	@Test
	public void rmMqoSupporter() {
		System.out.println(rmMqoSupporter);
		assertNotNull(rmMqoSupporter);
	}

	// ---------------------------------------------------
	// native
	// ---------------------------------------------------
	public static ConnectionFactory createConnectionFactory() throws Exception {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("172.22.31.11");
		connectionFactory.setPort(5672);
		return connectionFactory;
	}

	@Test
	public void connectionFactory() throws Exception {
		ConnectionFactory connectionFactory = createConnectionFactory();
		System.out.println(connectionFactory);
		assertNotNull(connectionFactory);
	}

	public static Connection createConnection() throws Exception {
		ConnectionFactory connectionFactory = createConnectionFactory();
		Connection connection = connectionFactory.newConnection();
		return connection;
	}

	@Test
	public void connection() throws Exception {
		Connection connection = createConnection();
		System.out.println(connection);
		assertNotNull(connection);
	}

	public static Channel createChannel() throws Exception {
		Connection connection = createConnection();
		Channel channel = connection.createChannel();
		return channel;
	}

	@Test
	public void channel() throws Exception {
		Channel channel = createChannel();
		System.out.println(channel);
		assertNotNull(channel);
	}

	@Test
	public void creatQueue() throws Exception {
		String QUEUE_NAME = "TEST_CHENG";
		//
		Channel channel = createChannel();
		DeclareOk declareOk = channel.queueDeclare(QUEUE_NAME, false, false,
				false, null);
		System.out.println(declareOk);
		assertNotNull(declareOk);
	}

	@Test
	public void createExchange() throws Exception {
		String EXCHANGE_NAME = "TEST_CHENG";
		//
		Channel channel = createChannel();
		com.rabbitmq.client.AMQP.Exchange.DeclareOk declareOk = channel
				.exchangeDeclare(EXCHANGE_NAME, "fanout");
		System.out.println(declareOk);
		assertNotNull(declareOk);
	}

	// ---------------------------------------------------
	// native
	// ---------------------------------------------------
	public static class NativeTest extends BenchmarkRmMqoSupporterTest {

		@Test
		// basicPublish: 10000 messages, 102400000 bytes / 44257 ms. = 2313.53
		// BYTES/MS, 2259.3 K/S, 2.21 MB/S
		// basicPublish: 10000 messages, 102400000 bytes / 38715 ms. = 2644.97
		// BYTES/MS, 2582.98 K/S, 2.52 MB/S
		public void nativeBasicPublish() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String QUEUE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			final ConnectionFactory connectionFactory = createConnectionFactory();
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								Connection connection = null;
								Channel channel = null;
								try {
									connection = connectionFactory
											.newConnection();
									channel = connection.createChannel();
									//
									channel.basicPublish("", QUEUE_NAME, null,
											buff);

									System.out.println("I[" + userId + "] M[" + i
											+ "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (channel != null) {
										channel.close();
									}
									if (connection != null) {
										connection.close();
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
			System.out.println("basicPublish: " + timesCounter.get()
					+ " messages, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, "
					+ mbresult + " MB/S");
		}

		@Test
		// basicConsume: 10000 messages, 102400000 bytes / 41838 ms. = 2447.54
		// BYTES/MS, 2390.17 K/S, 2.33 MB/S
		// basicConsume: 10000 messages, 102400000 bytes / 34271 ms. = 2987.95
		// BYTES/MS, 2917.92 K/S, 2.85 MB/S
		public void nativeBasicConsume() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String QUEUE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			final ConnectionFactory connectionFactory = createConnectionFactory();
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								Connection connection = null;
								Channel channel = null;
								try {
									connection = connectionFactory
											.newConnection();
									channel = connection.createChannel();
									//
									QueueingConsumer consumer = new QueueingConsumer(
											channel);
									//
									channel.basicConsume(QUEUE_NAME, true,
											consumer);

									System.out.println("I[" + userId + "] M[" + i
											+ "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (channel != null) {
										channel.close();
									}
									if (connection != null) {
										connection.close();
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
			System.out.println("basicConsume: " + timesCounter.get()
					+ " messages, " + byteCounter.get() + " bytes / " + dur
					+ " ms. = " + result + " BYTES/MS, " + kresult + " K/S, "
					+ mbresult + " MB/S");
		}

		@Test
		// queueDeclare: 10000 queues / 139520 ms. = 0.07 Q/MS, 71.67 Q/S
		public void nativeQueueDeclare() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String QUEUE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			final ConnectionFactory connectionFactory = createConnectionFactory();
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								Connection connection = null;
								Channel channel = null;
								try {
									connection = connectionFactory
											.newConnection();
									channel = connection.createChannel();
									//
									String queueName = QUEUE_NAME + "_" + userId
											+ "_" + i;
									DeclareOk declareOk = channel.queueDeclare(
											queueName, false, false, false,
											null);

									System.out.println("I[" + userId + "] Q[" + i
											+ "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (channel != null) {
										channel.close();
									}
									if (connection != null) {
										connection.close();
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
			double result = NumberHelper.round(timesCounter.get()
					/ (double) dur, 2);
			double sresult = NumberHelper.round(timesCounter.get()
					/ (dur / (double) 1000), 2);
			//
			System.out.println("queueDeclare: " + timesCounter.get()
					+ " queues / " + dur + " ms. = " + result + " Q/MS, "
					+ sresult + " Q/S");
		}

		@Test
		// queueDelete: 10000 queues / 101714 ms. = 0.1 Q/MS, 98.31 Q/S
		public void nativeQueueDelete() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String QUEUE_NAME = "TEST_CHENG";
			//
			final AtomicLong timesCounter = new AtomicLong(0);
			final AtomicLong byteCounter = new AtomicLong(0);
			//
			final ConnectionFactory connectionFactory = createConnectionFactory();
			//
			ExecutorService service = Executors
					.newFixedThreadPool(NUM_OF_THREADS);
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
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								Connection connection = null;
								Channel channel = null;
								try {
									connection = connectionFactory
											.newConnection();
									channel = connection.createChannel();
									//
									String queueName = QUEUE_NAME + "_" + userId
											+ "_" + i;
									DeleteOk deleteOk = channel
											.queueDelete(queueName);

									System.out.println("I[" + userId + "] Q[" + i
											+ "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (channel != null) {
										channel.close();
									}
									if (connection != null) {
										connection.close();
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
			double result = NumberHelper.round(timesCounter.get()
					/ (double) dur, 2);
			double sresult = NumberHelper.round(timesCounter.get()
					/ (dur / (double) 1000), 2);
			//
			System.out.println("queueDelete: " + timesCounter.get()
					+ " queues / " + dur + " ms. = " + result + " Q/MS, "
					+ sresult + " Q/S");
		}

	}

	// ---------------------------------------------------
	// optimized
	// ---------------------------------------------------
	public static class OptimizedTest extends BenchmarkRmMqoSupporterTest {

		@Test
		// send: 10000 messages, 102400000 bytes / 66553 ms. = 1538.62 BYTES/MS,
		// 1502.56 K/S, 1.47 MB/S
		public void optimizedSend() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String QUEUE_NAME = "TEST_CHENG";
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
					//
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								try {
									rmMqoSupporter.send(QUEUE_NAME, buff);

									System.out.println("I[" + userId + "] M[" + i
											+ "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
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
			System.out.println("send: " + timesCounter.get() + " messages, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}

		@Test
		// receive: 10000 messages, 102400000 bytes / 34986 ms. = 2926.89
		// BYTES/MS, 2858.29 K/S, 2.79 MB/S
		public void optimizedReceive() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String QUEUE_NAME = "TEST_CHENG";
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
					//
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								try {
									buff = rmMqoSupporter.receive(QUEUE_NAME);

									System.out.println("I[" + userId + "] M[" + i
											+ "]");
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
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
			System.out.println("receive: " + timesCounter.get() + " messages, "
					+ byteCounter.get() + " bytes / " + dur + " ms. = "
					+ result + " BYTES/MS, " + kresult + " K/S, " + mbresult
					+ " MB/S");
		}

		@Test
		// createQueue: 10000 queues / 53311 ms. = 0.19 Q/MS, 187.58 Q/S
		public void optimizedCeateQueue() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String QUEUE_NAME = "TEST_CHENG";
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
					//
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								try {
									//
									String queueName = QUEUE_NAME + "_" + userId
											+ "_" + i;
									String created = rmMqoSupporter
											.createQueue(queueName);

									System.out.println("I[" + userId + "] Q[" + i
											+ "], " + created);
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
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
			double result = NumberHelper.round(timesCounter.get()
					/ (double) dur, 2);
			double sresult = NumberHelper.round(timesCounter.get()
					/ (dur / (double) 1000), 2);
			//
			System.out.println("createQueue: " + timesCounter.get()
					+ " queues / " + dur + " ms. = " + result + " Q/MS, "
					+ sresult + " Q/S");
		}

		@Test
		// deleteQueue: 10000 queues / 86102 ms. = 0.12 Q/MS, 116.14 Q/S
		public void optimizedDeleteQueue() throws Exception {
			final int NUM_OF_THREADS = 100;
			final int NUM_OF_TIMES = 100;
			final int LENGTH_OF_BYTES = 10 * 1024;// 10k
			//
			final String QUEUE_NAME = "TEST_CHENG";
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
					//
					public void run() {
						try {
							//
							for (int i = 0; i < NUM_OF_TIMES; i++) {
								byte[] buff = ByteHelper
										.randomByteArray(LENGTH_OF_BYTES);
								//
								try {
									//
									String queueName = QUEUE_NAME + "_" + userId
											+ "_" + i;
									// 若無ex,無論是否有queue可刪除,都會傳回true
									boolean deleted = rmMqoSupporter
											.deleteQueue(queueName);

									System.out.println("I[" + userId + "] Q[" + i
											+ "], " + deleted);
									//
									timesCounter.incrementAndGet();
									byteCounter.addAndGet(buff.length);
									//
									ThreadHelper.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
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
			double result = NumberHelper.round(timesCounter.get()
					/ (double) dur, 2);
			double sresult = NumberHelper.round(timesCounter.get()
					/ (dur / (double) 1000), 2);
			//
			System.out.println("deleteQueue: " + timesCounter.get()
					+ " queues / " + dur + " ms. = " + result + " Q/MS, "
					+ sresult + " Q/S");
		}
	}
}
