package com.w.consumer.redis;

public interface KeyPrefix {

	public int expireSeconds();

	public String getPrefix();

}
