package com.w.consumer.redis;

public class SeckillKey extends BasePrefix {

	private SeckillKey(String prefix) {
		super(prefix);
	}
	public static SeckillKey isProductsOverKey = new SeckillKey("isProductsOverKey");
}
