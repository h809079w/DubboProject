package com.w.productconsumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.w.common.api.RecommenderApi;
import com.w.common.service.UserService;
import com.w.common.domain.Rating;
import com.w.common.domain.User;


import com.w.common.redis.RatingKey;
import com.w.common.redis.RedisService;
import com.w.common.result.Result;
import com.w.common.vo.ProductsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/recommender")
public class RecommenderController {

    private static Logger log = LoggerFactory.getLogger(RecommenderController.class);



    @Reference
    RecommenderApi recommenderApi;

    @RequestMapping(value = "/userrecs2")
    @ResponseBody
    public List<ProductsVo> getUserRecs2(User user) {

        if (user == null) {
            return null;
        }
        List<ProductsVo> productsVoList = recommenderApi.getUserRecs(user.getId());


        return productsVoList;
    }


    @RequestMapping(value = "/productrecs2/{productId}")
    @ResponseBody
    public List<ProductsVo> getProductRecs2(User user, @PathVariable("productId") long productId) {

        if (user == null) {
            return null;
        }
        List<ProductsVo> productsVoList = recommenderApi.getProductRecs(productId);

        return productsVoList;
    }


    @RequestMapping(value = "/streamrecs2")
    @ResponseBody
    public List<ProductsVo> getStreamRecs2(User user) {

        if (user == null) {
            return null;
        }
        List<ProductsVo> productsVoList = recommenderApi.getStreamRecs(user.getId());

        return productsVoList;
    }


    @RequestMapping(value = "/finish_rate", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Result<String> finishRate(Rating rating) {
        log.info("-------------------------------rating---------------------------------------");
        log.info(rating.toString());
        String result = recommenderApi.finishRate(rating);
        return Result.success(result);
    }

//    @RequestMapping(value = "/finish_rate2", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @RequestMapping(value = "/finish_rate2")
    @ResponseBody
    public Result<String> finishRate2(User user,
                                      @RequestParam("productId") long productTd, @RequestParam("score") Double score) {
        if (user == null) {
            return null;
        }
        Rating rating = new Rating();
        rating.setUserId(user.getId());
        rating.setProductId(productTd);
        rating.setScore(score);
        log.info("-------------------------------rating---------------------------------------");
        log.info(rating.toString());
        String result = recommenderApi.finishRate(rating);
        return Result.success(result);
    }
}
