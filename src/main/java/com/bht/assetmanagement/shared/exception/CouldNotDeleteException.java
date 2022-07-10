package com.bht.assetmanagement.shared.exception;

public class CouldNotDeleteException extends RuntimeException {
    public CouldNotDeleteException(String msg) {
        super(msg);
    }

    public CouldNotDeleteException() {
        super();
    }
}
