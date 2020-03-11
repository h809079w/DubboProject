package com.w.common.api;

import com.w.common.domain.OrderInfo;
import com.w.common.vo.OrderDetailVo;

import java.util.List;

public interface PurchaseApi {




    public OrderDetailVo getOrderDetailVo(long orderId) ;


    public List<OrderInfo> getOrderInfos(long userId);


    public Boolean reset() ;


    public Integer seckill( long productId, long userId);

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */

    public long seckillResult(long productId,long userId);


    public long pay(long orderId);

}
