package com.w.recommender.apiImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.w.common.api.RecommenderApi;
import com.w.common.domain.Rating;
import com.w.recommender.service.RateService;
import com.w.recommender.service.RecommenderService;
import com.w.common.vo.ProductsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = RecommenderApi.class)
public class RecommederApiImpl implements RecommenderApi {
    private static Logger log = LoggerFactory.getLogger(RecommederApiImpl.class);


    @Autowired
    RecommenderService recommenderService;

    @Autowired
    RateService rateService;

    @Override
    public List<ProductsVo> getUserRecs(Long userId){
        return recommenderService.getRecsByUserId(userId);
    }

    @Override
    public List<ProductsVo> getProductRecs(Long productId){
        return recommenderService.getRecsByProductId(productId);
    }

    @Override
    public List<ProductsVo> getStreamRecs(Long userId){
        return recommenderService.getStreamRecsByUserId(userId);
    }

    @Override
    public String finishRate(Rating rating){
        rating.setTimeStamp(System.currentTimeMillis()/1000);
        rateService.InsertRatingToDAO(rating);
        rateService.updateRatingToRedis(rating);
        //埋点
        log.info("PRODUCT_RATING_PREFIX" + ":" + rating.getUserId() +"|"+ rating.getProductId() +"|"+ rating.getScore() +"|"+ rating.getTimeStamp());
        return  "评分成功";
    }
}
