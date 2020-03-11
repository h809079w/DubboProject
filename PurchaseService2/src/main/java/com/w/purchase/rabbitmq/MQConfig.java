package com.w.purchase.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {

	public static final String SECKILL_QUEUE = "seckill.queue";

	public static final String DELAY_EXCHANGE="user.order.delay_exchange";

	public static final String DELAY_QUEUE="user.order.delay_queue";


	public static final String DELAY_KEY="user.order.delay_key";

	public static final String DELAY_RECEIVE_EXCHAGE="user.order.receive_exchange";

	public static final String DELAY_RECEIVE_QUEUE="user.order.receive_queue";
//	public static final String DELAY_RECEIVE_KEY="user.order.receive_key";

//	public static final String QUEUE = "queue";
//	public static final String TOPIC_QUEUE1 = "topic.queue1";
//	public static final String TOPIC_QUEUE2 = "topic.queue2";
//	public static final String HEADER_QUEUE = "header.queue";
//	public static final String TOPIC_EXCHANGE = "topicExchage";
//	public static final String FANOUT_EXCHANGE = "fanoutxchage";
//	public static final String HEADERS_EXCHANGE = "headersExchage";

	/**
	 * Direct模式 交换机Exchange
	 * */
	@Bean
	public Queue queue() {
		return new Queue(SECKILL_QUEUE, true);
	}

	/**
	 * 死信交换机
	 *
	 * @return
	 */
	@Bean
	public DirectExchange userOrderDelayExchange() {
		return new DirectExchange("user.order.delay_exchange");
	}

	/**
	 * 死信队列(当这个队列中有过期消息时，会自动放到设定的交换机上重新路由到另外一个队列)
	 *
	 * @return
	 */
	@Bean
	public Queue userOrderDelayQueue() {
		Map<String, Object> map = new HashMap<>(16);
		map.put("x-dead-letter-exchange", DELAY_RECEIVE_EXCHAGE);//转发的交换机
//		map.put("x-dead-letter-routing-key", "user.order.receive_key");
		map.put("x-dead-letter-routing-key", DELAY_KEY);
		return new Queue("user.order.delay_queue", true, false, false, map);
	}

	/**
	 * 给死信队列绑定交换机
	 *
	 * @return
	 */
	@Bean
	public Binding userOrderDelayBinding() {
		return BindingBuilder.bind(userOrderDelayQueue()).to(userOrderDelayExchange()).with("user.order.delay_key");
	}

	/**
	 * 死信接收交换机
	 *
	 * @return
	 */
	@Bean
	public DirectExchange userOrderReceiveExchange() {
		return new DirectExchange(DELAY_RECEIVE_EXCHAGE);
	}

	/**
	 * 死信接收队列
	 *
	 * @return
	 */
	@Bean
	public Queue userOrderReceiveQueue() {
		return new Queue(DELAY_RECEIVE_QUEUE);
	}

	/**
	 * 死信交换机绑定消费队列
	 *
	 * @return
	 */
	@Bean
	public Binding userOrderReceiveBinding() {
//		return BindingBuilder.bind(userOrderReceiveQueue()).to(userOrderReceiveExchange()).with("user.order.receive_key");
		return BindingBuilder.bind(userOrderReceiveQueue()).to(userOrderReceiveExchange()).with(DELAY_KEY);

	}

}
