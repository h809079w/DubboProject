package com.w.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.w.common.api.ProductApi;
import com.w.common.domain.User;
import com.w.consumer.redis.ProductsKey;
import com.w.consumer.result.Result;
import com.w.common.vo.ProductsDetailVo;
import com.w.common.vo.ProductsVo;
import com.w.consumer.redis.RedisService;
import com.w.consumer.service.UserService;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static Logger log = LoggerFactory.getLogger(ProductController.class);

    @Reference
    ProductApi productApi;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired()
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        log.info(username);
        model.addAttribute("user", user);
//        //首先取html缓存
        String html = redisService.get(ProductsKey.getProductsVoListHtmlKey, "", String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        //没有取到html缓存则手动渲染
        List<ProductsVo> productsVoList = productApi.list();
//        log.info(String.valueOf(productsVoList));
        model.addAttribute("productsVoList", productsVoList);
        WebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("products_list", ctx);

        if(!StringUtils.isEmpty(html)) {
            redisService.set(ProductsKey.getProductsVoListHtmlKey, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/to_list2",produces = "text/html")
    @ResponseBody
    public String list2(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        log.info(username);
        model.addAttribute("user", user);
        model.addAttribute("pageNum", pageNum);
//        //首先取html缓存
        String html=null;
//        String html = redisService.get(ProductsKey.getProductsVoListHtmlKey, "", String.class);
//        if(!StringUtils.isEmpty(html)) {
//            return html;
//        }
        //没有取到html缓存则手动渲染
        List<ProductsVo> productsVoList = productApi.list2(pageNum);
//        log.info(String.valueOf(productsVoList));
        model.addAttribute("productsVoList", productsVoList);
        WebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("products_list", ctx);

//        if(!StringUtils.isEmpty(html)) {
//            redisService.set(ProductsKey.getProductsVoListHtmlKey, "", html);
//        }
        return html;
    }


    //静态渲染detail,produce设置返回对于http请求accept的类型
    @RequestMapping(value="/to_detail/{productId}")
    @ResponseBody
    public Result<ProductsDetailVo> detail(Model model,
                                           @PathVariable("productId")long productId) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        long userId=Long.parseLong(username);
        ProductsDetailVo productsDetailVo =productApi.detail(productId,userId);
        model.addAttribute("user", productsDetailVo.getUser());
        return Result.success(productsDetailVo);
    }


    //手动渲染detail,produce设置返回对于http请求accept的类型
    @RequestMapping(value="/to_detail2/{productId}",produces="text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model,
                          @PathVariable("productId")long productId) {

        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user=userService.getUserById(Long.parseLong(username));
        model.addAttribute("user", user);

        //取html缓存
        String html = redisService.get(ProductsKey.getProductsDetailHtmlKey, ""+productId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        //手动渲染
        ProductsVo productsVo = productApi.detail2(productId);
        model.addAttribute("product", productsVo);

        long startAt = productsVo.getStartDate().getTime();
        long endAt = productsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            seckillStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
//        return "goods_detail";

        WebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("products_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(ProductsKey.getProductsDetailHtmlKey, ""+productId, html);
        }
        return html;
    }

}
