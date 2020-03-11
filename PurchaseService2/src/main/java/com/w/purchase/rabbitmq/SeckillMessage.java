package com.w.purchase.rabbitmq;


import com.w.common.domain.User;

public class SeckillMessage {
	private User user;
	private long productId;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
}
