package com.w.common.redis;

public class SeckillKey extends BasePrefix {

	private SeckillKey(String prefix) {
		super(prefix);
	}
	public static SeckillKey isProductsOverKey = new SeckillKey("isProductsOverKey");
}
