package com.example.webflux.exception;

import com.example.webflux.response.ErrorResponse;

public class UnAuthorizedException extends ApplicationException {

    public UnAuthorizedException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

}
