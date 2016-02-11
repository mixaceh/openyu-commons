package org.openyu.commons.rabbitmq;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

public class RabbitMqTest {

	protected static ConnectionFactory factory;

	protected static Connection connection;

	protected static Channel channel;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = new ConnectionFactory();
		factory.setHost("10.16.212.150");
		factory.setPort(5672);
		connection = factory.newConnection();
		channel = connection.createChannel();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		channel.close();
		connection.close();
	}

	@Test
	public void connection() {
		System.out.println(connection);
		assertNotNull(connection);// amqp://guest@10.16.212.150:5672/
	}

	@Test
	public void channel() {
		System.out.println(channel);
		assertNotNull(channel);// AMQChannel(amqp://guest@10.16.212.150:5672/,1)
	}

	@Test
	public void send() throws Exception {
		String QUEUE_NAME = "TEST_QUEUE";
		//
		String message = "Hello World";
		DeclareOk declare = channel.queueDeclare(QUEUE_NAME, false, false,
				false, null);
		// System.out.println("declare: " + declare);
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
	}

	@Test
	public void receive() throws Exception {
		String QUEUE_NAME = "TEST_QUEUE";
		//
		DeclareOk declare = channel.queueDeclare(QUEUE_NAME, false, false,
				false, null);
		// System.out.println("declare: " + declare);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);
		//
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(message);
		}
	}

	@Test
	public void sendPersistent() throws Exception {
		String QUEUE_NAME = "TEST_PERSISTENT";
		//
		String message = "Hello World";
		DeclareOk declare = channel.queueDeclare(QUEUE_NAME, true, false,
				false, null);// durable=true
		// System.out.println("declare: " + declare);
		channel.basicPublish("", QUEUE_NAME,
				MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
	}

	@Test
	public void receivePersistent() throws Exception {
		String QUEUE_NAME = "TEST_PERSISTENT";
		//
		DeclareOk declare = channel.queueDeclare(QUEUE_NAME, true, false,
				false, null);// durable=true
		channel.basicQos(1);//
		// System.out.println("declare: " + declare);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, false, consumer);
		//
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(message);
			//
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}

	@Test
	public void sendExchange() throws Exception {
		String EXCHANGE_NAME = "TEST_EXCHANGE";
		//
		String message = "Hello World";
		com.rabbitmq.client.AMQP.Exchange.DeclareOk declare = channel
				.exchangeDeclare(EXCHANGE_NAME, "fanout");
		// System.out.println("declare: " + declare);
		channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
	}

	@Test
	public void receiveExchange() throws Exception {
		String EXCHANGE_NAME = "TEST_EXCHANGE";
		//
		com.rabbitmq.client.AMQP.Exchange.DeclareOk declare = channel
				.exchangeDeclare(EXCHANGE_NAME, "fanout");
		// System.out.println("declare: " + declare);
		String queueName = channel.queueDeclare().getQueue();
		System.out.println("queueName: " + queueName);
		channel.queueBind(queueName, EXCHANGE_NAME, "");
		//
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);
		//
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(message);
		}
	}

	@Test
	public void sendExchangeDirect() throws Exception {
		String EXCHANGE_NAME = "TEST_EXCHANGE_DIRECT";
		//
		String message = "Hello World";
		com.rabbitmq.client.AMQP.Exchange.DeclareOk declare = channel
				.exchangeDeclare(EXCHANGE_NAME, "direct");
		// System.out.println("declare: " + declare);
		String severity = "info";
		channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
		System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
	}

	@Test
	public void receiveExchangeDirect() throws Exception {
		String EXCHANGE_NAME = "TEST_EXCHANGE_DIRECT";
		//
		com.rabbitmq.client.AMQP.Exchange.DeclareOk declare = channel
				.exchangeDeclare(EXCHANGE_NAME, "direct");
		// System.out.println("declare: " + declare);
		String queueName = channel.queueDeclare().getQueue();
		System.out.println("queueName: " + queueName);
		//
		channel.queueBind(queueName, EXCHANGE_NAME, "info");
		channel.queueBind(queueName, EXCHANGE_NAME, "error");
		//
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);
		//
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			String routingKey = delivery.getEnvelope().getRoutingKey();

			System.out.println(" [x] Received '" + routingKey + "':'" + message
					+ "'");
		}
	}

	@Test
	public void sendExchangeTopic() throws Exception {
		String EXCHANGE_NAME = "TEST_EXCHANGE_TOPIC";
		//
		String message = "Hello World";
		com.rabbitmq.client.AMQP.Exchange.DeclareOk declare = channel
				.exchangeDeclare(EXCHANGE_NAME, "topic");
		// System.out.println("declare: " + declare);
		String routingKey = "kern.critical";
		channel.basicPublish(EXCHANGE_NAME, routingKey, null,
				message.getBytes());
		System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
	}

	@Test
	public void receiveExchangeTopic() throws Exception {
		String EXCHANGE_NAME = "TEST_EXCHANGE_TOPIC";
		//
		com.rabbitmq.client.AMQP.Exchange.DeclareOk declare = channel
				.exchangeDeclare(EXCHANGE_NAME, "topic");
		// System.out.println("declare: " + declare);
		String queueName = channel.queueDeclare().getQueue();
		System.out.println("queueName: " + queueName);
		//
		channel.queueBind(queueName, EXCHANGE_NAME, "#");
		channel.queueBind(queueName, EXCHANGE_NAME, "kern.*");
		channel.queueBind(queueName, EXCHANGE_NAME, "*.critical");
		//
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);
		//
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			String routingKey = delivery.getEnvelope().getRoutingKey();

			System.out.println(" [x] Received '" + routingKey + "':'" + message
					+ "'");
		}
	}

	private static int fib(int n) {
		if (n == 0)
			return 0;
		if (n == 1)
			return 1;
		return fib(n - 1) + fib(n - 2);
	}

	@Test
	public void rpcServer() throws Exception {
		String QUEUE_NAME = "TEST_RPC";
		//
		DeclareOk declare = channel.queueDeclare(QUEUE_NAME, false, false,
				false, null);
		// System.out.println("declare: " + declare);
		channel.basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, false, consumer);

		System.out.println(" [x] Awaiting RPC requests");

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();

			BasicProperties props = delivery.getProperties();
			BasicProperties replyProps = new BasicProperties.Builder()
					.correlationId(props.getCorrelationId()).build();

			String message = new String(delivery.getBody());
			int n = Integer.parseInt(message);

			System.out.println(" [.] fib(" + message + ")");
			String response = "" + fib(n);

			channel.basicPublish("", props.getReplyTo(), replyProps,
					response.getBytes());

			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}

	private String rpcCall(String message) throws Exception {
		String QUEUE_NAME = "TEST_RPC";

		String replyQueueName = channel.queueDeclare().getQueue();
		System.out.println("replyQueueName: " + replyQueueName);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(replyQueueName, true, consumer);

		String response = null;
		String corrId = java.util.UUID.randomUUID().toString();

		BasicProperties props = new BasicProperties.Builder()
				.correlationId(corrId).replyTo(replyQueueName).build();

		channel.basicPublish("", QUEUE_NAME, props, message.getBytes());

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {
				response = new String(delivery.getBody());
				break;
			}
		}
		return response;
	}

	@Test
	public void rpcClient() throws Exception {
		String reonse = rpcCall("5");
		System.out.println(reonse);
	}
}
