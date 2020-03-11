package com.w.common.api;

import com.w.common.domain.Rating;
import com.w.common.vo.ProductsVo;

import java.util.List;

public interface RecommenderApi {

    public List<ProductsVo> getUserRecs(Long userId);

    public List<ProductsVo> getProductRecs(Long productId);

    public List<ProductsVo> getStreamRecs(Long userId);

    public String finishRate(Rating rating);

}
