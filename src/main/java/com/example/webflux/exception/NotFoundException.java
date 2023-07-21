package com.example.webflux.exception;

import com.example.webflux.response.ErrorResponse;
import lombok.Getter;
import lombok.Setter;

public class NotFoundException extends RuntimeException {

    @Getter
    @Setter
    private ErrorResponse errorResponse;

    public NotFoundException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

}
