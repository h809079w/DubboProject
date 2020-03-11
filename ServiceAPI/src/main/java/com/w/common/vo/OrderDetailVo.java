package com.w.common.vo;


import com.w.common.domain.OrderInfo;

import java.io.Serializable;

//产品信息和订单信息整体，用于和前端交互
public class OrderDetailVo implements Serializable {
	private ProductsVo productsVo;
	private OrderInfo orderInfo;
	public ProductsVo getProductsVo() {
		return productsVo;
	}
	public void setProductsVo(ProductsVo productsVo) {
		this.productsVo = productsVo;
	}
	public OrderInfo getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
}
