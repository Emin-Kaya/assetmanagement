package com.bht.assetmanagement.shared.exception;

public class DublicateEntryException extends RuntimeException {

    public DublicateEntryException(String msg) {
        super(msg);
    }

    public DublicateEntryException() {
        super();
    }
}