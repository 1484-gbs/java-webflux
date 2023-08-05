package com.example.webflux.repository;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class WebClientRepository<T1, T2> {
    private final WebClient webClient;

    public Mono<T2> post(ServerRequest req, String uri, Class<? extends T1> reqClazz, Class<? extends T2> resClazz) {
        return webClient.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(req.headers()))
                .body(req.bodyToMono(reqClazz), reqClazz)
                .exchangeToMono(res -> res.bodyToMono(resClazz));
    }


    public Mono<T2> get(ServerRequest req, String uri, LinkedMultiValueMap<String, String> queryParams, Class<? extends T2> resClazz) {
        return get(req,
                UriComponentsBuilder
                        .fromUriString(uri)
                        .queryParams(queryParams)
                        .build()
                        .toUriString(),
                resClazz);
    }

    public Mono<T2> get(ServerRequest req, String uri, Class<? extends T2> resClazz) {
        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(req.headers()))
                .exchangeToMono(res -> res.bodyToMono(resClazz));
    }

    private String getAuthorizationHeader(ServerRequest.Headers headers) {
        return headers.header(HttpHeaders.AUTHORIZATION)
                .stream()
                .filter(h -> h.startsWith("Bearer "))
                .findFirst()
                .orElse(null);
    }
}
