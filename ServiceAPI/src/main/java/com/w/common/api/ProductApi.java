package com.w.common.api;

import com.w.common.vo.ProductsDetailVo;
import com.w.common.vo.ProductsVo;
import java.util.List;

public interface ProductApi {

    public List<ProductsVo> list();
    public List<ProductsVo> list2(Integer pageNum);

    //静态渲染detail
    public ProductsDetailVo detail(long productId, long userId);

    //手动渲染detai
    public ProductsVo detail2(long productId) ;

}
