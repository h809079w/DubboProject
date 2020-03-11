package com.w.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.w.common.api.RecommenderApi;
import com.w.common.domain.Rating;
import com.w.common.domain.User;
import com.w.consumer.redis.RatingKey;
import com.w.consumer.redis.RedisService;
import com.w.consumer.result.Result;
import com.w.consumer.service.UserService;

import com.w.common.vo.ProductsVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/recommender")
public class RecommenderController {

    private static Logger log = LoggerFactory.getLogger(RecommenderController.class);


    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Reference
    RecommenderApi recommenderApi;

    @RequestMapping(value = "/userrecs",produces = "text/html")
    @ResponseBody
    public String getUserRecs(HttpServletRequest request, HttpServletResponse response, Model model){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        model.addAttribute("user",user);

        String html=null;
        List<ProductsVo> productsVoList=recommenderApi.getUserRecs(user.getId());

        model.addAttribute("productsVoList", productsVoList);

        WebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("user_recs", ctx);

        return html;

    }


    @RequestMapping(value="/productrecs/{productId}", produces="text/html")
    @ResponseBody
    public String getProductRecs(HttpServletRequest request, HttpServletResponse response, Model model,@PathVariable("productId") long productId){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        model.addAttribute("user",user);

        String html=null;
        List<ProductsVo> productsVoList=recommenderApi.getProductRecs(productId);

        model.addAttribute("productsVoList", productsVoList);

        WebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("products_recs", ctx);
        return html;
    }

    @RequestMapping(value="/streamrecs", produces="text/html")
    @ResponseBody
    public String getStreamRecs(HttpServletRequest request, HttpServletResponse response, Model model){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        model.addAttribute("user",user);
        Long userId=user.getId();
        String html=null;
        List<ProductsVo> productsVoList=recommenderApi.getStreamRecs(userId);

        model.addAttribute("productsVoList", productsVoList);

        WebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("stream_recs", ctx);

        return html;
    }

    @RequestMapping(value="/do_rate",produces="text/html")
    @ResponseBody
    public String rate(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam("productId")long productId){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        model.addAttribute("user",user);
        model.addAttribute("productId",productId);

        String html=null;
//        取html缓存
        html = redisService.get(RatingKey.RatingUidPidHtml, ""+user.getId()+" "+productId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        WebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("rate", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(RatingKey.RatingUidPidHtml, ""+user.getId()+" "+productId, html);
        }
        return html;
    }

    @RequestMapping(value="/finish_rate",consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Result<String> finishRate(Rating rating){
        log.info("-------------------------------rating---------------------------------------");
        log.info(rating.toString());
        String result=recommenderApi.finishRate(rating);
        return Result.success(result);
    }

}
