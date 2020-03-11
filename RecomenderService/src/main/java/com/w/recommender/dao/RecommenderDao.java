package com.w.recommender.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RecommenderDao {
     @Select("select productId from user_recs where userId=#{userId} order by score limit 10")
    public List<Long>  getUserRecsByUserId(@Param("userId") Long userId);

//    @Select("select productId2 from product_recs where productId=#{productId} order by score limit 10")
     @Select("select productId2 from itemcfproductrecs where productId=#{productId} order by score limit 10")
    public List<Long>  getProductRecsByProductId(@Param("productId") Long productId);

     @Select("select recs from stream_recs where userId=#{userId}")
     public String getStreamRecsByUserId(@Param("userId") Long userId);
}
