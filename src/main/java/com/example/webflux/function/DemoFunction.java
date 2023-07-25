package com.example.webflux.function;

import com.example.webflux.usecase.HealthUseCase;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
public class DemoFunction {

    private final WebClient webClient;

    private final HealthUseCase healthUseCase;


    @Bean
    public RouterFunction<ServerResponse> health() {
        return route(
                GET("/health"),
                req -> ok().body(Mono.just(Map.of("health", "ok")), Object.class))
                .andRoute(
                        GET("/health2"),
                        req -> ok().body(Flux.just("health", "health"), Object.class)
                ).andRoute(
                        GET("/health/8080"),
                        this::test2
                );
    }

    public Mono<ServerResponse> test(ServerRequest req) {
        val ret = webClient.get()
                .uri("/health")
                .retrieve()
                .bodyToMono(String.class);
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(ret, Object.class);
    }

    public Mono<ServerResponse> test2(ServerRequest req) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(healthUseCase.execute(req), Object.class);
    }

}
