package com.example.webflux.exception;

import com.example.webflux.response.ErrorResponse;
import lombok.Getter;
import lombok.Setter;

public class ApplicationException extends RuntimeException{

    @Getter
    @Setter
    private ErrorResponse errorResponse;

    public ApplicationException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
