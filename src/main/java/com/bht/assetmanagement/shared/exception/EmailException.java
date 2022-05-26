package com.bht.assetmanagement.shared.exception;


public class EmailException extends RuntimeException {
    public EmailException(String msg) {
        super(msg);
    }

    public EmailException() {
        super();
    }
}
