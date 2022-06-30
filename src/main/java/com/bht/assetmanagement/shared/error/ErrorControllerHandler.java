package com.bht.assetmanagement.shared.error;

import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
@ResponseBody
public class ErrorControllerHandler {
    private ErrorMessage message = new ErrorMessage();

    @ExceptionHandler(EntryNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handleEntryNotFoundException(EntryNotFoundException ex) {
        message.setMessage(ex.getMessage());
        return message;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleRunTimeException(RuntimeException ex) {
        message.setMessage(ex.getMessage());
        return message;
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleAuthException(AuthenticationException ex) {
        if (ex instanceof BadCredentialsException) {
            message.setMessage("Invalid password.");
        } else if(ex instanceof DisabledException) {
            message.setMessage("Activate your account!");
        } else {
            message.setMessage(ex.getMessage());
        }

        return message;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleJwtException(ExpiredJwtException ex) {
        message.setMessage(ex.getMessage());

        return message;
    }
}


