package com.example.webflux.exception;

import com.example.webflux.response.ErrorResponse;
import lombok.Getter;
import lombok.Setter;

public class BadRequestException extends RuntimeException{

    @Getter
    @Setter
    private ErrorResponse errorResponse;

    public BadRequestException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

}
