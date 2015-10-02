package org.openyu.commons.mqo.rabbitmq.supporter;

import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.mqo.rabbitmq.RabbitMqo;
import org.openyu.commons.mqo.rabbitmq.ex.RabbitMqoException;
import org.openyu.commons.mqo.supporter.BaseMqoSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitMqoSupporter extends BaseMqoSupporter implements RabbitMqo {

	private static final long serialVersionUID = -2991155603948202419L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(RabbitMqoSupporter.class);

	private RabbitTemplate rabbitTemplate;

	private RabbitAdmin rabbitAdmin;

	public RabbitMqoSupporter() {
	}

	/**
	 * 檢查設置
	 * 
	 * @throws IllegalArgumentException
	 */
	protected final void checkConfig() {
		AssertHelper.notNull(rabbitTemplate, "The RabbitTemplate is required");
		AssertHelper.notNull(this.rabbitAdmin, "The RabbitAdmin is required");
	}

	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public RabbitAdmin getRabbitAdmin() {
		return rabbitAdmin;
	}

	public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
		this.rabbitAdmin = rabbitAdmin;
	}

	public String createQueue(String queueName) {
		try {
			Queue queue = new Queue(queueName, false);
			return rabbitAdmin.declareQueue(queue);
		} catch (Exception ex) {
			throw new RabbitMqoException(ex);
		}
	}

	public boolean deleteQueue(String queueName) {
		try {
			return rabbitAdmin.deleteQueue(queueName);
		} catch (Exception ex) {
			throw new RabbitMqoException(ex);
		}
	}

	public void send(String routingKey, byte[] values) {
		try {
			if (ByteHelper.notEmpty(values)) {
				rabbitTemplate.convertAndSend(routingKey, values);
			}
		} catch (Exception ex) {
			throw new RabbitMqoException(ex);
		}
	}

	public byte[] receive(String queueName) {
		byte[] result = null;
		try {
			org.springframework.amqp.core.Message message = rabbitTemplate
					.receive(queueName);
			if (message != null) {
				result = message.getBody();
			}
		} catch (Exception ex) {
			throw new RabbitMqoException(ex);
		}
		return result;
	}

}
