package com.w.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.w.common.api.SearchApi;
import com.w.consumer.redis.RedisService;
import com.w.consumer.service.UserService;
import com.w.common.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    private static Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;


    @Reference
    SearchApi searchApi;

    @RequestMapping("/import_all")
    @ResponseBody
    public int importAll(){
        return searchApi.importAll();
    }

    @RequestMapping("/do_search")
//    @ResponseBody
    public String doSearch(@RequestParam("query") String query, Model model){
        List<Product> EsProductList=searchApi.doSearch(query);
        model.addAttribute("productsVoList",EsProductList);
        return "search_list";
    }
}
