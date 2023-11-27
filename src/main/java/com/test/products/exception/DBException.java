package com.test.products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DBException extends RuntimeException {

    public DBException(String message) {
        super(message);
    }
}
