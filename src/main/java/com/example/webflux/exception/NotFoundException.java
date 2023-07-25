package com.example.webflux.exception;

import com.example.webflux.response.ErrorResponse;

public class NotFoundException extends ApplicationException {

    public NotFoundException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

}
