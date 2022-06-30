package com.bht.assetmanagement.shared.exception;


public class ExpiredJwtException extends RuntimeException {
    public ExpiredJwtException(String msg) {
        super(msg);
    }

    public ExpiredJwtException() {
        super();
    }
}
