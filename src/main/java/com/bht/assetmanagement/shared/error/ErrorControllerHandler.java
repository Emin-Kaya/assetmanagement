package com.bht.assetmanagement.shared.error;

import com.bht.assetmanagement.shared.exception.CouldNotDeleteException;
import com.bht.assetmanagement.shared.exception.DublicateEntryException;
import com.bht.assetmanagement.shared.exception.EmailException;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@ResponseBody
public class ErrorControllerHandler {
    private ErrorMessage message = new ErrorMessage();

    @ExceptionHandler(EntryNotFoundException.class)
    public ErrorMessage handleEntryNotFoundException(EntryNotFoundException ex) {
        message.setStatus(HttpStatus.NOT_FOUND);
        message.setMessage(ex.getMessage());
        return message;
    }

    @ExceptionHandler(RuntimeException.class)
    public ErrorMessage handleRunTimeException(RuntimeException ex) {
        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage(ex.getMessage());
        return message;
    }


    @ExceptionHandler(AuthenticationException.class)
    public ErrorMessage handleAuthException(AuthenticationException ex) {
        message.setStatus(HttpStatus.BAD_REQUEST);
        if (ex instanceof BadCredentialsException) {
            message.setMessage("Invalid password.");
        } else if(ex instanceof DisabledException) {
            message.setMessage("Activate your account!");
        } else {
            message.setMessage(ex.getMessage());
        }

        return message;
    }


    @ExceptionHandler(CouldNotDeleteException.class)
    public ErrorMessage handleCouldNotDeleteException(CouldNotDeleteException ex) {
        message.setStatus(HttpStatus.NOT_FOUND);
        message.setMessage(ex.getMessage());
        return message;
    }

    @ExceptionHandler(DublicateEntryException.class)
    public ErrorMessage handleDublicateEntryException(DublicateEntryException ex) {
        message.setStatus(HttpStatus.CONFLICT);
        message.setMessage(ex.getMessage());
        return message;
    }

    @ExceptionHandler(EmailException.class)
    public ErrorMessage handleEmailException(EmailException ex) {
        message.setStatus(HttpStatus.NOT_FOUND);
        message.setMessage(ex.getMessage());
        return message;
    }
}


