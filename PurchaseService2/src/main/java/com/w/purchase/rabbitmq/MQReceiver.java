package com.w.purchase.rabbitmq;



import com.w.common.domain.OrderInfo;
import com.w.common.domain.SeckillOrder;
import com.w.common.domain.User;

import com.w.common.redis.RedisService;
import com.w.purchase.service.OrderService;
import com.w.purchase.service.ProductsService;
import com.w.purchase.service.SeckillService;
import com.w.common.vo.ProductsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

		@Autowired
		RedisService redisService;

		@Autowired
		ProductsService productsService;

		@Autowired
		OrderService orderService;

		@Autowired
		SeckillService seckillService;

		@RabbitListener(queues= MQConfig.SECKILL_QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
			SeckillMessage sm  = RedisService.stringToBean(message, SeckillMessage.class);
			User smUser = sm.getUser();
			long productId = sm.getProductId();

			ProductsVo productsVo = productsService.getProductsVoByProductIdFromDAO(productId);
	    	int stock = productsVo.getStockCount();
	    	if(stock <= 0) {
	    		return;
	    	}
	    	//判断是否已经秒杀到了
	    	SeckillOrder order = orderService.getSeckillOrderByUserIdProductIdFromRedis(smUser.getId(), productId);
	    	if(order != null) {
	    		return;
	    	}
	    	//减库存 下订单 写入秒杀订单
	    	seckillService.seckill(smUser, productsVo);
		}


		//进入死信接收队列的取消订单
		@RabbitListener(queues = MQConfig.DELAY_RECEIVE_QUEUE)
		public void CancelOrder(String message) {
			log.info("receive message:"+message);
			SeckillMessage sm  = RedisService.stringToBean(message, SeckillMessage.class);
			User smUser = sm.getUser();
			long productId = sm.getProductId();
			OrderInfo orderInfo=orderService.getOrderInfoByUserIdProductIdFromDAO(smUser.getId(),productId);
			//只有还未支付的订单需要取消
			if(orderInfo.getStatus()==0) {
				orderService.CancelOrdersAndInfoByUserIdAndProductIdFromDAO(smUser.getId(), productId);
			}
		}




}
