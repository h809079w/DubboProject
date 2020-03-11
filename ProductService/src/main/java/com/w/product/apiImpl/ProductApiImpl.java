package com.w.product.apiImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.w.common.api.ProductApi;
import com.w.common.domain.User;
import com.w.common.redis.RedisService;
import com.w.common.service.UserService;
import com.w.common.vo.ProductsDetailVo;
import com.w.common.vo.ProductsVo;
import com.w.product.dao.ProductDao;

import com.w.product.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;



@Service(interfaceClass = ProductApi.class)
public class ProductApiImpl implements ProductApi {
    @Autowired
    ProductDao productDao;

    @Autowired
    ProductsService productsService;

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;

    @Override
    public List<ProductsVo> list() {
        return productsService.listProductsVoFromDAO();
    }

    @Override
    public List<ProductsVo> list2(Integer pageNum) {
        pageNum=pageNum==0?1:pageNum;
        PageHelper.startPage(pageNum,5);
        List<ProductsVo> productsVoList=productsService.listProductsVoFromDAO();
        return productsVoList;
    }

    @Override
    public ProductsDetailVo detail(long productId, long userId) {
        ProductsVo  productsVo= productsService.getProductsVoByProductIdFromDAO(productId);
        long startAt = productsVo.getStartDate().getTime();
        long endAt = productsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        ProductsDetailVo vo = new ProductsDetailVo();
        vo.setProductsVo(productsVo);

        User user=userService.getUserById(userId);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return vo;

    }
    @Override
    public ProductsVo detail2(long productId) {
        return null;
    }
}
