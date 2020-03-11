package com.w.consumer.redis;

public class ProductsKey extends BasePrefix {

	private ProductsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static ProductsKey getProductsVoListHtmlKey = new ProductsKey(60, "ProductsVoListHtmlKey");
	public static ProductsKey getProductsDetailHtmlKey = new ProductsKey(60, "ProductsDetailHtmlKey");
	public static ProductsKey getProductStockKey = new ProductsKey(0, "ProductStockKey");
}


