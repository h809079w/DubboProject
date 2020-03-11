package com.w.common.api;

import com.w.common.domain.Product;

import java.util.List;

public interface SearchApi {
    public int importAll();
    public List<Product> doSearch(String query);
}
