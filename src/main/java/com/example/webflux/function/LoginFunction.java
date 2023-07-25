package com.example.webflux.function;

import com.example.webflux.usecase.LoginUseCase;
import com.example.webflux.usecase.TwoFactorAuthUseCase;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
public class LoginFunction extends AbstractFunction {

    private final LoginUseCase loginUseCase;
    private final TwoFactorAuthUseCase twoFactorAuthUseCase;

    @Bean
    public RouterFunction<ServerResponse> login() {
        return route(
                POST("/login"),
                req -> {
                    return loginUseCase.execute(req)
                            .flatMap(res -> ok().bodyValue(res))
                            .onErrorResume(super::createErrorResponse);
                })
                .andRoute(
                        POST("/2fa"),
                        req -> {
                            return twoFactorAuthUseCase.execute(req)
                                    .flatMap(res -> ok().bodyValue(res))
                                    .onErrorResume(super::createErrorResponse);
                        }
                );
    }
}
