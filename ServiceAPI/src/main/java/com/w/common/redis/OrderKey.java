package com.w.common.redis;

public class OrderKey extends BasePrefix {

	public OrderKey(String prefix) {
		super(prefix);
	}
	public static OrderKey getSeckillOrderByUidPidKey = new OrderKey("SeckillOrderByUidPidKey");
}
