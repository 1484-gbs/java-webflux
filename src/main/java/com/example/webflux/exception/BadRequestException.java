package com.example.webflux.exception;

import com.example.webflux.response.ErrorResponse;

public class BadRequestException extends ApplicationException {

    public BadRequestException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
