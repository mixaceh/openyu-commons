package org.openyu.commons.mqo.rabbitmq;

import org.openyu.commons.mqo.BaseMqo;

/**
 * RabbitMQ Message Queue Object
 */
public interface RabbitMqo extends BaseMqo {

	/**
	 * 建立queue
	 * 
	 * @param queueName
	 * @return
	 */
	String createQueue(String queueName);

	/**
	 * 刪除queue
	 * 
	 * @param queueName
	 * @return
	 */
	boolean deleteQueue(String queueName);

	/**
	 * 發送
	 *
	 * @param routingKey
	 * @param values
	 * @throws AmqpException
	 */
	void send(String routingKey, byte[] values);

	/**
	 * 接收
	 *
	 * @param queueName
	 * @return
	 */
	byte[] receive(String queueName);
}
