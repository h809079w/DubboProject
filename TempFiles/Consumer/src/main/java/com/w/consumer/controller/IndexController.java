package com.w.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.w.common.api.ProductApi;
import com.w.consumer.redis.RedisService;
import com.w.consumer.service.UserService;
import com.w.common.vo.ProductsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    private static Logger log = LoggerFactory.getLogger(IndexController.class);

    @Reference
    ProductApi productApi;

    @RequestMapping(value = "/index")
    public String index(Model model){
        List<ProductsVo> productsVoList = productApi.list();
        model.addAttribute("productsVoList", productsVoList);
        return "index";
    }
    @RequestMapping(value = "/newindex")
    public String newindex(Model model){
        List<ProductsVo> productsVoList = productApi.list();
        model.addAttribute("productsVoList", productsVoList);
        return "newindex";
    }

}
