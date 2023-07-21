package com.example.webflux.function;

import com.example.webflux.usecase.GetEmployeePageInfoUseCase;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
public class EmployeeFunction extends AbstractFunction {

    private final GetEmployeePageInfoUseCase getEmployeePageInfoUseCase;

    @Bean
    public RouterFunction<ServerResponse> getEmployeePageInfo() {
        return route(
                GET("/employee/{employeeId}"),
                req -> (
                        getEmployeePageInfoUseCase.execute(req)
                                .flatMap(res -> ok().bodyValue(res))
                                .onErrorResume(super::createErrorResponse)
                )
        );
    }
}
