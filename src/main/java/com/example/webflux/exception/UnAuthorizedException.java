package com.example.webflux.exception;

import com.example.webflux.response.ErrorResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class UnAuthorizedException extends RuntimeException{

    @Getter
    @Setter
    private ErrorResponse errorResponse;

    public UnAuthorizedException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

}
