package com.example.assetmanagement.exception;

public class EntryNotFoundException extends RuntimeException {

    public EntryNotFoundException(String msg) {
        super(msg);
    }

    public EntryNotFoundException() {
        super();
    }
}