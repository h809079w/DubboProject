package com.w.recommender.service;

import com.w.recommender.dao.RecommenderDao;
import com.w.common.vo.ProductsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RecommenderService {

    @Autowired
    RecommenderDao recommenderDao;
    @Autowired
    ProductsService productsService;

    public List<ProductsVo> getRecsByUserId(Long userId){
        List<Long> ProductsIdList=recommenderDao.getUserRecsByUserId(userId);
        List<ProductsVo> ProductsVoList=new LinkedList<ProductsVo>();
        for (Long productId:ProductsIdList){
            ProductsVo productsVo=productsService.getProductsVoByProductIdFromDAO(productId);
            ProductsVoList.add(productsVo);
        }
        return ProductsVoList;
    }

    public List<ProductsVo> getRecsByProductId(Long productId){
        List<Long> ProductsIdList=recommenderDao.getProductRecsByProductId(productId);
        List<ProductsVo> ProductsVoList=new LinkedList<ProductsVo>();
        for (Long productId2:ProductsIdList){
            ProductsVo productsVo=productsService.getProductsVoByProductIdFromDAO(productId2);
            ProductsVoList.add(productsVo);
        }
        return ProductsVoList;
    }

    public List<ProductsVo> getStreamRecsByUserId(Long userId) {
//        RecsLine格式为productId：score|productId：score|productId：score|productId：score
        String RecsLine = recommenderDao.getStreamRecsByUserId(userId);
//        System.out.print("___________product_score_list__________"+RecsLine);

        String[] product_score_list = RecsLine.trim().split("\\|");

        Long productId;
        ProductsVo productsVo;
        List<ProductsVo> ProductsVoList = new LinkedList<ProductsVo>();
        for (String product_score : product_score_list) {
//            System.out.print("___________product_score__________"+product_score);
            productId = Long.parseLong(product_score.trim().split(":")[0]);
            productsVo = productsService.getProductsVoByProductIdFromDAO(productId);
            ProductsVoList.add(productsVo);
        }
        return ProductsVoList;
    }
}
