package com.w.indexconsumer.exception;


import com.w.common.result.CodeMsg;

public class ZookeeperException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public ZookeeperException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
