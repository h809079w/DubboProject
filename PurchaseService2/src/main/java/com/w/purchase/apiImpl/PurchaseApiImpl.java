package com.w.purchase.apiImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.w.common.api.PurchaseApi;
import com.w.common.domain.OrderInfo;
import com.w.common.domain.SeckillOrder;
import com.w.common.domain.User;
import com.w.common.redis.OrderKey;
import com.w.common.redis.ProductsKey;
import com.w.common.redis.RedisService;
import com.w.common.redis.SeckillKey;
import com.w.common.service.UserService;
import com.w.purchase.rabbitmq.MQSender;
import com.w.purchase.rabbitmq.SeckillMessage;

import com.w.purchase.service.OrderService;
import com.w.purchase.service.ProductsService;
import com.w.purchase.service.SeckillService;
import com.w.common.vo.OrderDetailVo;
import com.w.common.vo.ProductsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = PurchaseApi.class)
public class PurchaseApiImpl implements PurchaseApi,InitializingBean {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    ProductsService productsService;

    @Autowired
    MQSender sender;

    private HashMap<Long, Boolean> localOverMap =  new HashMap<Long, Boolean>();

    /**
     * 系统初始化
     * */
    public void afterPropertiesSet() throws Exception {
        List<ProductsVo> productsVos = productsService.listProductsVoFromDAO();
        if(productsVos == null) {
            return;
        }
        for(ProductsVo productsVo : productsVos) {
            //redis添加库存
            redisService.set(ProductsKey.getProductStockKey, ""+productsVo.getId(), productsVo.getStockCount());
            localOverMap.put(productsVo.getId(), false);
        }
    }
    @Override
    public OrderDetailVo getOrderDetailVo(long orderId) {
        OrderInfo orderInfo = orderService.getOrderInfoByOrderIdFromDAO(orderId);
        long productId = orderInfo.getProductId();
        ProductsVo productsVo = productsService.getProductsVoByProductIdFromDAO(productId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrderInfo(orderInfo);
        orderDetailVo.setProductsVo(productsVo);
        return orderDetailVo;
    }

    @Override
    public List<OrderInfo> getOrderInfos(long userId) {
        return orderService.getOrderInfosByUserIdFromDAO(userId);
    }

    @Override
    public Boolean reset() {
        List<ProductsVo> productsVoList = productsService.listProductsVoFromDAO();
        for(ProductsVo productsVo : productsVoList) {
            productsVo.setStockCount(10);
            redisService.set(ProductsKey.getProductStockKey, ""+productsVo.getId(), 10);
            localOverMap.put(productsVo.getId(), false);
        }
        redisService.delete(OrderKey.getSeckillOrderByUidPidKey);
        redisService.delete(SeckillKey.isProductsOverKey);
        seckillService.resetDAO(productsVoList);
//		return Result.success(true);
        return true;
    }

    @HystrixCommand(
            threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "2"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "200")},//限流
            commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//熔断
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000") })
    @Override
    public Integer seckill(long productId, long userId) {
        User user=userService.getUserById(userId);
        //内存标记，减少redis访问
        boolean over = localOverMap.get(productId);
        if(over) {
//			return "SECKILL_OVER";
            return -1;
        }
        //redis预减库存
        long stock = redisService.decr(ProductsKey.getProductStockKey, ""+productId);//10
        if(stock < 0) {
            localOverMap.put(productId, true);
//			return "SECKILL_OVER";
            return -1;
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdProductIdFromRedis(user.getId(), productId);
        if(order != null) {
//			return "REPEATE_SECKILL";
            return 1;
        }
        //入队
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setUser(user);
        seckillMessage.setProductId(productId);

        sender.sendSeckillMessageToDLX(seckillMessage);//先写入正常队列，再写入死信队列一份
//		return "InLine";
        return 0;
    }

    @Override
    public long seckillResult(long productId, long userId) {
        long result  = seckillService.getSeckillResult(userId, productId);
        return result;
    }

    @Override
    public long pay(long orderId) {
        OrderInfo orderInfo=orderService.getOrderInfoByOrderIdFromDAO(orderId);
        if (orderInfo.getStatus()==-1){return -1;}//支付失败
        return orderService.PayOrdersAndInfoByUserIdAndProductIdFromDAO(orderId);
    }
}
