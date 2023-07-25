package com.example.webflux.function;

import com.example.webflux.usecase.GetEmployeePageInfoUseCase;
import com.example.webflux.usecase.GetEmployeeRandomUseCase;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
public class EmployeeFunction extends AbstractFunction {

    private final GetEmployeePageInfoUseCase getEmployeePageInfoUseCase;
    private final GetEmployeeRandomUseCase getEmployeeRandomUseCase;

    @Bean
    public RouterFunction<ServerResponse> getEmployeePageInfo() {
        return nest(path("/employee"),
                route().GET("/random",
                                req -> (
                                        getEmployeeRandomUseCase.execute(req)
                                                .collectList()
                                                .flatMap(res -> ok().body(BodyInserters.fromValue(res)))
                                                .onErrorResume(super::createErrorResponse)
                                )
                        ).GET("/{employeeId}",
                                req -> (
                                        getEmployeePageInfoUseCase.execute(req)
                                                .flatMap(res -> ok().bodyValue(res))
                                                .onErrorResume(super::createErrorResponse)
                                )
                        )
                        .build()
        );
    }
}
