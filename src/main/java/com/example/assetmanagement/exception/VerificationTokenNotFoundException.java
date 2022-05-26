package com.example.assetmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Invalid token")
public class VerificationTokenNotFoundException extends RuntimeException {

    public VerificationTokenNotFoundException(String msg) {
        super(msg);
    }

    public VerificationTokenNotFoundException() {
        super();
    }
}
