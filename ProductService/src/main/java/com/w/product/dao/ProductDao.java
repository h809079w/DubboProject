package com.w.product.dao;



import com.w.common.domain.Product;
import com.w.common.vo.ProductsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductDao {
    @Select("select * from product")
    public List<ProductsVo> listProductsVo();

    //不使用@Param注解时，参数只能有一个，并且是Javabean
    @Select("select* from product where id = #{productId}")
    public ProductsVo getProductVoByProductId(@Param("productId") long productId);


    @Update("update product set stock_count = stock_count - 1 where id = #{id} and stock_count > 0")
    public int reduceStock(Product p);

    @Update("update product set stock_count = #{stockCount} where id = #{id}")
    public int resetStock(Product p);

    //分类(在数据表中detail字段为标签) eg.外设产品|鼠标|电脑/办公
    @Select("select * from product where(detail like #{tag})")
    public List<ProductsVo> getProductVoByProductTag(@Param("tag") String tag);
}
