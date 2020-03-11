package com.w.recommender.dao;


import com.w.common.domain.Rating;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RateDao {
    @Insert("insert into rating (userId, productId, score,timestamp)values(#{userId}, #{productId}, #{score}, #{timeStamp})")
    public long InsertRating(Rating rating);
}
