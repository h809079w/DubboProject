package com.w.purchaseconsumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.w.common.api.PurchaseApi;

import com.w.common.domain.OrderInfo;
import com.w.common.domain.User;

import com.w.common.redis.RedisService;
import com.w.common.result.CodeMsg;
import com.w.common.result.Result;
import com.w.common.service.UserService;
import com.w.common.vo.OrderDetailVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    UserService userService;

    @Reference
    PurchaseApi purchaseApi;

    @Autowired
    RedisService redisService;




    @RequestMapping("order/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(User user, @RequestParam("orderId") long orderId){
        if(user==null){return null;}
        OrderDetailVo orderDetailVo= purchaseApi.getOrderDetailVo(orderId);
        return Result.success(orderDetailVo);
    }

    @RequestMapping("order/orderinfos")
    @ResponseBody
    public List<OrderInfo> getOrderInfos(User user){
        if (user==null){return null;}
        List<OrderInfo> orderInfoList= purchaseApi.getOrderInfos(user.getId());
        return  orderInfoList;
    }


    @RequestMapping(value="/seckill/reset", method= RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(User user) {
        if(user==null){return null;}
        Boolean booleanResult= purchaseApi.reset();
        return Result.success(booleanResult);
    }

    @HystrixCommand(fallbackMethod = "seckillfallback")
    @RequestMapping(value="/seckill/do_seckill", method=RequestMethod.POST)
    @ResponseBody
    public Result<Integer> seckill(User user,@RequestParam("productId") String productIdString) {
        if(user==null){return null;}

        int result= purchaseApi.seckill(Long.parseLong(productIdString),user.getId());
        if(result==-1){
            return Result.error(CodeMsg.SECKILL_OVER);
        } else if (result==1){
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }else {
            return Result.success(result);
        }
    }


    public  Result<Integer> seckillfallback(User user,@RequestParam("productId") String productIdString) {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/seckill/result", method= RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(User user,@RequestParam("productId")long productId) {
        if(user==null){return null;}
        long finalresult  = purchaseApi.seckillResult(productId,user.getId());
        return Result.success(finalresult);
    }


//支付
    @RequestMapping(value="/order/pay", method= RequestMethod.GET)
    @ResponseBody
    public Result<Long> pay(User user,@RequestParam("orderId")long orderId) {
        if(user==null){return null;}
        long result  = purchaseApi.pay(orderId);
        if (result==-1){return Result.error(CodeMsg.PAY_FAILED);}
        return Result.success(result);
    }
}
