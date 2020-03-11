package com.w.consumer.exception;


import com.w.consumer.result.CodeMsg;

public class BussinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private CodeMsg cm;

	public BussinessException(CodeMsg cm) {
		super(cm.toString());
		this.cm = cm;
	}

	public CodeMsg getCm() {
		return cm;
	}

}
