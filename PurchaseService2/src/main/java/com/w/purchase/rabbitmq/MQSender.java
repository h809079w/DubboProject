package com.w.purchase.rabbitmq;

import com.w.common.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MQSender {

	private static Logger log = LoggerFactory.getLogger(MQSender.class);

	@Autowired
    AmqpTemplate amqpTemplate ;

	public void sendSeckillMessage(SeckillMessage mm) {
		String msg = RedisService.beanToString(mm);
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
	}


	public void sendSeckillMessageToDLX(SeckillMessage mm) {
		String msg = RedisService.beanToString(mm);
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
		amqpTemplate.convertAndSend(MQConfig.DELAY_EXCHANGE, MQConfig.DELAY_KEY, msg,message -> {
			message.getMessageProperties().setExpiration("30000");
			return message;
		});
	}

}
