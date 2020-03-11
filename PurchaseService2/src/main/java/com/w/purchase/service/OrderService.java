package com.w.purchase.service;




import com.w.common.redis.OrderKey;
import com.w.common.redis.RedisService;
import com.w.purchase.dao.OrderDao;
import com.w.common.domain.OrderInfo;
import com.w.common.domain.SeckillOrder;
import com.w.common.domain.User;

import com.w.common.vo.ProductsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

	@Autowired
	OrderDao orderDao;

	@Autowired
	RedisService redisService;

	public SeckillOrder getSeckillOrderByUserIdProductIdFromRedis(long userId, long productId) {
		return redisService.get(OrderKey.getSeckillOrderByUidPidKey, ""+userId+"_"+productId, SeckillOrder.class);
	}

	public OrderInfo getOrderInfoByUserIdProductIdFromDAO(long userId, long productId) {
		return orderDao.getOrderInfoByUserIdProductId(userId,productId);
	}

	public OrderInfo getOrderInfoByOrderIdFromDAO(long orderId) {
		return orderDao.getOrderInfoByOrderId(orderId);
	}

	public List<OrderInfo> getOrderInfosByUserIdFromDAO(long userId){
		return orderDao.getOrderInfosByUserId(userId);
	}

	@Transactional
	public OrderInfo createOrderAndInfo(User user, ProductsVo productsVo) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setProductCount(1);
		orderInfo.setProductId(productsVo.getId());
		orderInfo.setProductName(productsVo.getName());
		orderInfo.setProductPrice(productsVo.getPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		orderDao.insertOrderInfo(orderInfo);
		SeckillOrder seckillOrder = new SeckillOrder();
		seckillOrder.setGoodsId(productsVo.getId());
		seckillOrder.setOrderId(orderInfo.getId());
		seckillOrder.setUserId(user.getId());
		orderDao.insertSeckillOrder(seckillOrder);

		redisService.set(OrderKey.getSeckillOrderByUidPidKey, ""+user.getId()+"_"+productsVo.getId(), seckillOrder);

		return orderInfo;
	}

	public void CancelOrdersAndInfoByUserIdAndProductIdFromDAO(long userId, long productId) {
		orderDao.CancelOrderInfoByUserIdAndProductId(userId,productId);
	}

	public long PayOrdersAndInfoByUserIdAndProductIdFromDAO(long orderId) {
		return orderDao.PayOrderInfoByUserIdAndProductId(orderId);
	}

	public void deleteOrdersAndInfosFromDAO() {
		orderDao.deleteOrderInfos();
		orderDao.deleteSeckillOrders();
	}

}
