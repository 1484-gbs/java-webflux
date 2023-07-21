package com.example.webflux.function;

import com.example.webflux.exception.ApplicationException;
import com.example.webflux.exception.BadRequestException;
import com.example.webflux.exception.NotFoundException;
import com.example.webflux.exception.UnAuthorizedException;
import com.example.webflux.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

public class AbstractFunction {
    protected Mono<ServerResponse> createErrorResponse(Throwable ex) {
        if (ex instanceof BadRequestException be) {
            return badRequest().bodyValue(be.getErrorResponse());
        }
        if (ex instanceof UnAuthorizedException uae) {
            return status(HttpStatus.UNAUTHORIZED.value()).bodyValue(uae.getErrorResponse());
        }
        if (ex instanceof NotFoundException ne) {
            return status(HttpStatus.NOT_FOUND.value()).bodyValue(ne.getErrorResponse());
        }
        if (ex instanceof ApplicationException ae) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR.value()).bodyValue(ae.getErrorResponse());
        }
        return status(HttpStatus.INTERNAL_SERVER_ERROR.value()).bodyValue(
                new ErrorResponse("unexpected error.", ex.getMessage()));
    }
}
