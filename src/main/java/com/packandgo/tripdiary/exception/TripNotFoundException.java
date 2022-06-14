package com.packandgo.tripdiary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException() {
        super();
    }
    public TripNotFoundException(String message) {
        super(message);
    }
}