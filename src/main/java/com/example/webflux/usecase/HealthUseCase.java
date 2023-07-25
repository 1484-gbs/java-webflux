package com.example.webflux.usecase;

import com.example.webflux.repository.WebClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class HealthUseCase {
    private final WebClientRepository<Void, String> repository;

    public Mono<String> execute(ServerRequest req) {
        return repository.get(req, "/health", String.class);
    }
}
