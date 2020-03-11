package com.w.product.service;



import com.w.common.domain.Product;
import com.w.common.vo.ProductsVo;
import com.w.product.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductsService {
    @Autowired
    ProductDao productDao;

    public List<ProductsVo> listProductsVoFromDAO(){
        return productDao.listProductsVo();
    }

    public ProductsVo getProductsVoByProductIdFromDAO(long ProductId) {
        return productDao.getProductVoByProductId(ProductId);
    }

    public List<ProductsVo> getProductsVoByProductTagFromDAO(String tag) {
        return productDao.getProductVoByProductTag(tag);
    }


    public boolean reduceStockFromDAO(ProductsVo productsVo) {
        Product p = new Product();
        p.setId(productsVo.getId());
        int ret = productDao.reduceStock(p);
        return ret > 0;
    }

    public void resetStockFromDAO(List<ProductsVo> productsVoList) {
        for(ProductsVo productsVo : productsVoList ) {
            Product p = new Product();
            p.setId(productsVo.getId());
            p.setStockCount(productsVo.getStockCount());
            productDao.resetStock(p);
        }
    }

}
