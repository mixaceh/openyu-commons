import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.SystemHelper;
import org.openyu.commons.mqo.rabbitmq.supporter.RabbitMqoSupporter;
import org.openyu.commons.thread.ThreadHelper;

public class ApplicationContextRabbitmqTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"applicationContext-init.xml",//
				"META-INF/applicationContext-commons-core.xml",//
				"applicationContext-rabbitmq.xml",//
		});
	}

	@Test
	public void rabbitConnectionFactory() throws Exception {
		CachingConnectionFactory bean = (CachingConnectionFactory) applicationContext
				.getBean("rabbitConnectionFactory");
		System.out.println(bean);
		assertNotNull(bean);
		//
		Connection connection = bean.createConnection();
		System.out.println(connection);
		assertNotNull(connection);
	}

	@Test
	public void rabbitTemplate() throws Exception {
		RabbitTemplate bean = (RabbitTemplate) applicationContext
				.getBean("rabbitTemplate");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void rabbitAdmin() throws Exception {
		RabbitAdmin bean = (RabbitAdmin) applicationContext
				.getBean("rabbitAdmin");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void rmMqoSupporter() throws Exception {
		RabbitMqoSupporter bean = (RabbitMqoSupporter) applicationContext
				.getBean("rmMqoSupporter");
		System.out.println(bean);
		assertNotNull(bean);
	}

	@Test
	public void declareQueue() throws Exception {
		RabbitAdmin bean = (RabbitAdmin) applicationContext
				.getBean("rabbitAdmin");
		//
		String QUEUE_NAME = "TEST_BENCHMARK";
		Queue queue = new Queue(QUEUE_NAME, false);
		String name = bean.declareQueue(queue);
		System.out.println(name);
	}

	@Test
	public void declareExchange() throws Exception {
		RabbitAdmin bean = (RabbitAdmin) applicationContext
				.getBean("rabbitAdmin");
		//
		String EXCHANGE_NAME = "TEST_EXCHANGE";
		TopicExchange exchange = new TopicExchange(EXCHANGE_NAME);
		bean.declareExchange(exchange);
		//
		String QUEUE_NAME = "TEST_EXCHANGE";
		Queue queue = new Queue(QUEUE_NAME, false);
		bean.declareQueue(queue);
		//
		// binding
		Binding binding = BindingBuilder.bind(queue).to(exchange)
				.with(QUEUE_NAME);
		System.out.println(binding);
	}

	@Test
	public void send() throws Exception {
		RabbitTemplate bean = (RabbitTemplate) applicationContext
				.getBean("rabbitTemplate");
		//
		String QUEUE_NAME = "TEST_BENCHMARK";
		bean.convertAndSend(QUEUE_NAME, new byte[] { 1, 2, 3 });
	}

	@Test
	public void receive() throws Exception {
		RabbitTemplate bean = (RabbitTemplate) applicationContext
				.getBean("rabbitTemplate");
		//
		String QUEUE_NAME = "TEST_BENCHMARK";
		Message message = bean.receive(QUEUE_NAME);
		byte[] buff = message.getBody();
		SystemHelper.println(buff);
	}

	@Test
	public void rabbitContainer() throws Exception {
		CachingConnectionFactory bean = (CachingConnectionFactory) applicationContext
				.getBean("rabbitConnectionFactory");
		//
		String QUEUE_NAME = "TEST_BENCHMARK";
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(bean);
		container.setQueueNames(QUEUE_NAME);
		container.setMessageListener(new MessageAdapter());
		//
		ThreadHelper.sleep(1 * 60 * 1000);
	}

	public static class MessageAdapter implements MessageListener {

		public void onMessage(Message message) {
			byte[] buff = message.getBody();
			SystemHelper.println(buff);
		}

	}

}
