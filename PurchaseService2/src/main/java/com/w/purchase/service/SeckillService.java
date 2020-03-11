package com.w.purchase.service;




import com.w.common.domain.OrderInfo;
import com.w.common.domain.SeckillOrder;
import com.w.common.domain.User;

import com.w.common.redis.RedisService;
import com.w.common.redis.SeckillKey;
import com.w.common.vo.ProductsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeckillService {

	@Autowired
    ProductsService productsService;

	@Autowired
    OrderService orderService;

	@Autowired
	RedisService redisService;


	@Transactional
	public OrderInfo seckill(User user, ProductsVo productsVo) {
		//减库存 下订单 写入秒杀订单
		boolean success = productsService.reduceStockFromDAO(productsVo);
		if(success) {
			//order_info maiosha_order
			return orderService.createOrderAndInfo(user, productsVo);//里面同时将order插入了redis
		}else {
			setProductsOverRedis(productsVo.getId());
			return null;
		}
	}

	public long getSeckillResult(Long userId, long goodsId) {
		SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdProductIdFromRedis(userId, goodsId);
		if(seckillOrder != null) {//秒杀成功
			return seckillOrder.getOrderId();
		}else {
			boolean isOver = getProductsOverRedis(goodsId);
			if(isOver) {
				return -1;
			}else {
				return 0;
			}
		}
	}

	private void setProductsOverRedis(Long goodsId) {
		redisService.set(SeckillKey.isProductsOverKey, ""+goodsId, true);
	}

	private boolean getProductsOverRedis(long goodsId) {
		return redisService.exists(SeckillKey.isProductsOverKey, ""+goodsId);
	}

	public void resetDAO(List<ProductsVo> productsVoList) {
		productsService.resetStockFromDAO(productsVoList);
		orderService.deleteOrdersAndInfosFromDAO();
	}


}
