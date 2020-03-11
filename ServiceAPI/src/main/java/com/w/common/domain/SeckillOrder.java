package com.w.common.domain;

import java.io.Serializable;

public class SeckillOrder implements Serializable {
	private Long id;
	private Long userId;
	private Long orderId;
	private Long productId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getGoodsId() {
		return productId;
	}
	public void setGoodsId(Long productId) {
		this.productId = productId;
	}

}
