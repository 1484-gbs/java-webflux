package com.example.webflux.usecase;

import com.example.webflux.repository.WebClientRepository;
import com.example.webflux.request.LoginRequest;
import com.example.webflux.response.LoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class LoginUseCase {

    private final WebClientRepository<LoginRequest, LoginResponse> repository;

    public Mono<LoginResponse> execute(ServerRequest req) {
        return repository.post(req, "/login", LoginRequest.class, LoginResponse.class);
    }
}
