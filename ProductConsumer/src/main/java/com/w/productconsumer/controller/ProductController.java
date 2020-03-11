package com.w.productconsumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.w.common.api.ProductApi;


import com.w.common.domain.User;


import com.w.common.result.Result;
import com.w.common.vo.ProductsDetailVo;
import com.w.common.vo.ProductsVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static Logger log = LoggerFactory.getLogger(ProductController.class);

    @Reference
    ProductApi productApi;


    @RequestMapping(value = "/to_list")
    @ResponseBody
    public List<ProductsVo> list(User user){

        if (user==null){return null;}
        else {
            List<ProductsVo> productsVoList = productApi.list();
            return productsVoList;
        }

    }

    @RequestMapping(value = "/to_list2")
    @ResponseBody
    public List<ProductsVo> list2( User user, @RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum) {

        if(user==null){return null;}
        List<ProductsVo> productsVoList = productApi.list2(pageNum);
        return productsVoList;
    }

    //静态渲染detail,produce设置返回对于http请求accept的类型
    @RequestMapping(value="/to_detail/{productId}")
    @ResponseBody
    public Result<ProductsDetailVo> detail(@PathVariable("productId")long productId, User user) {
        if(user==null){return null;}
        ProductsDetailVo productsDetailVo =productApi.detail(productId,user.getId());
        return Result.success(productsDetailVo);
    }


}
