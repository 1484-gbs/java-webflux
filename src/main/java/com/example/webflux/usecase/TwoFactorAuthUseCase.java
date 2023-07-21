package com.example.webflux.usecase;

import com.example.webflux.repository.WebClientRepository;
import com.example.webflux.request.TwoFactorAuthRequest;
import com.example.webflux.response.TwoFactorAuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TwoFactorAuthUseCase {
    private final WebClientRepository<TwoFactorAuthRequest, TwoFactorAuthResponse> repository;

    public Mono<TwoFactorAuthResponse> execute(ServerRequest req) {
        return repository.post(
                req,
                "/2fa",
                TwoFactorAuthRequest.class,
                TwoFactorAuthResponse.class
        );
    }
}
