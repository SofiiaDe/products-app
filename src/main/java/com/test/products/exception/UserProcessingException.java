package com.test.products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserProcessingException extends RuntimeException {

    public UserProcessingException(String errorMessage) {
        super(errorMessage);
    }
}
