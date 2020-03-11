package com.w.search.elastic.repository;



import com.w.search.elastic.document.EsProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsProductRepository extends ElasticsearchRepository<EsProduct, Long>{

}
