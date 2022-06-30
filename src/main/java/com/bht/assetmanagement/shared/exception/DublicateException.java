package com.bht.assetmanagement.shared.exception;

public class DublicateException  extends RuntimeException {

    public DublicateException(String msg) {
        super(msg);
    }

    public DublicateException() {
        super();
    }
}