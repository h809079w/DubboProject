package com.w.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.w.common.api.PurchaseApi;
import com.w.common.domain.OrderInfo;
import com.w.common.domain.User;
import com.w.consumer.redis.RedisService;
import com.w.consumer.result.CodeMsg;
import com.w.consumer.result.Result;
import com.w.consumer.service.UserService;
import com.w.common.vo.OrderDetailVo;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;



    @RequestMapping("order/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model,
                                      @RequestParam("orderId") long orderId){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        model.addAttribute("user",user);
        OrderDetailVo orderDetailVo= purchaseApi.getOrderDetailVo(orderId);
        return Result.success(orderDetailVo);
    }

    @RequestMapping("order/orderinfos")
//    @ResponseBody
    public String getOrderInfos(Model model){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        model.addAttribute("user",user);
        List<OrderInfo> orderInfoList= purchaseApi.getOrderInfos(user.getId());
        model.addAttribute("orderInfoList",orderInfoList);
        return "orderinfos";
    }


    @RequestMapping(value="/seckill/reset", method= RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        model.addAttribute("user",user);
        Boolean booleanResult= purchaseApi.reset();
        return Result.success(booleanResult);
    }

    @RequestMapping(value="/seckill/do_seckill", method=RequestMethod.POST)
    @ResponseBody
    public Result<Integer> seckill(Model model,
                                   @RequestParam("productId") String productIdString) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        model.addAttribute("user", user);

        int result= purchaseApi.seckill(Long.parseLong(productIdString),user.getId());
        if(result==-1){
            return Result.error(CodeMsg.SECKILL_OVER);
        } else if (result==1){
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }else {
            return Result.success(result);
        }
    }
    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/seckill/result", method= RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model,
                                      @RequestParam("productId")long productId) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        model.addAttribute("user", user);
        long finalresult  = purchaseApi.seckillResult(productId,user.getId());
        return Result.success(finalresult);
    }

}
