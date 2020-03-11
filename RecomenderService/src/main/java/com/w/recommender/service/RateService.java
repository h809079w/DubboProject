package com.w.recommender.service;



import com.w.common.redis.RatingKey;
import com.w.common.redis.RedisService;
import com.w.recommender.dao.RateDao;
import com.w.common.domain.Rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateService {
    @Autowired
    RateDao rateDao;

    @Autowired
    RedisService redisService;

    public void  InsertRatingToDAO(Rating rating){
        rateDao.InsertRating(rating);
    }
    public void updateRatingToRedis(Rating rating) {
        if (redisService.exists(RatingKey.UserRatingKey,""+rating.getUserId()) && redisService.llen(RatingKey.UserRatingKey ,""+rating.getUserId()) >= 10) {
            redisService.rpop(RatingKey.UserRatingKey,""+rating.getUserId());
        }
        redisService.lpush(RatingKey.UserRatingKey,"" + rating.getUserId(), rating.getProductId() + ":" + rating.getScore()+rating.getTimeStamp());
    }

}
