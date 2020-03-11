package com.w.consumer.redis;

public class OrderKey extends BasePrefix {

	public OrderKey(String prefix) {
		super(prefix);
	}
	public static OrderKey getSeckillOrderByUidPidKey = new OrderKey("SeckillOrderByUidPidKey");
}
