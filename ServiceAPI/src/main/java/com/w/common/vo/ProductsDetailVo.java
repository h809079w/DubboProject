package com.w.common.vo;


import com.w.common.domain.User;

import java.io.Serializable;

public class ProductsDetailVo implements Serializable {
	private int miaoshaStatus = 0;
	private int remainSeconds = 0;
	private ProductsVo productsVo ;
	private User user;

	public int getMiaoshaStatus() {
		return miaoshaStatus;
	}

	public void setMiaoshaStatus(int miaoshaStatus) {
		this.miaoshaStatus = miaoshaStatus;
	}

	public int getRemainSeconds() {
		return remainSeconds;
	}

	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}

	public ProductsVo getProductsVo() {
		return productsVo;
	}

	public void setProductsVo(ProductsVo productsVo) {
		this.productsVo = productsVo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
