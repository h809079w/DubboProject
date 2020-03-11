package com.w.search.service;


import com.w.search.dao.ProductDao;
import com.w.search.elastic.document.EsProduct;
import com.w.search.elastic.repository.EsProductRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    ProductDao productDao;

    @Autowired
    EsProductRepository esProductRepository;

    public int importAll() {
        List<EsProduct> EsProductList=productDao.listEsProducts();
        Iterable<EsProduct> esProductIterable = esProductRepository.saveAll(EsProductList);
        Iterator<EsProduct> iterator = esProductIterable.iterator();
        int result = 0;
        while (iterator.hasNext()) {
            result++;
            iterator.next();
        }
        return result;
    }

    public List<EsProduct> Search(String q) {
//        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(q);
        WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name",
                "*"+q+"*");
        Iterable<EsProduct> searchResult = esProductRepository.search(queryBuilder);
        Iterator<EsProduct> iterator = searchResult.iterator();
        List<EsProduct> list = new ArrayList<EsProduct>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}
