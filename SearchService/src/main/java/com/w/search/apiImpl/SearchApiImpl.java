package com.w.search.apiImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.w.common.api.SearchApi;
import com.w.common.domain.Product;

import com.w.search.elastic.document.EsProduct;
import com.w.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;


@Service(interfaceClass = SearchApi.class)
public class SearchApiImpl implements SearchApi {
    @Autowired
    SearchService searchService;


    @Override
    public int importAll() {
        //        System.out.println("--------------------------------");
        return searchService.importAll();
    }

    @Override
    public List<Product> doSearch(String query) {
        System.out.println("--------------------------------");
        List<EsProduct> EsProductList =searchService.Search(query);
        List<Product> productList=new LinkedList<Product>();
        for (EsProduct esProduct:EsProductList){
            Product product=new Product();
            product.setId(esProduct.getId());
            product.setName(esProduct.getName());
            product.setImg(esProduct.getImg());
            product.setPrice(esProduct.getPrice());
            product.setIdcode(esProduct.getIdcode());
            product.setDetail(esProduct.getDetail());
            product.setStockCount(esProduct.getStockCount());
            product.setStartDate(esProduct.getStartDate());
            product.setEndDate(esProduct.getEndDate());
            productList.add(product);
            product=null;
        }
        return productList;
    }
}
