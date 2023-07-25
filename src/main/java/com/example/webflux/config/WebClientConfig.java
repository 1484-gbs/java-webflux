package com.example.webflux.config;

import com.example.webflux.exception.ApplicationException;
import com.example.webflux.exception.BadRequestException;
import com.example.webflux.exception.NotFoundException;
import com.example.webflux.exception.UnAuthorizedException;
import com.example.webflux.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class WebClientConfig {

    @Value("${app.external.base-url}")
    private String externalBaseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(externalBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(errorResponseFilter());
                })
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.info("Request: {} {}", request.method(), request.url());
            return Mono.just(request);
        });
    }

    private ExchangeFilterFunction errorResponseFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            val status = response.statusCode();
            log.info("Response status: {}", response.statusCode());

            if (status.is2xxSuccessful()) {
                return Mono.just(response);
            }
            if (HttpStatus.UNAUTHORIZED.equals(status)) {
                return Mono.error(new UnAuthorizedException(new ErrorResponse()));
            }
            if (HttpStatus.BAD_REQUEST.equals(status)) {
                return response.bodyToMono(ErrorResponse.class)
                        .flatMap(body -> Mono.error(new BadRequestException(body)));
            }
            if (HttpStatus.NOT_FOUND.equals(status)) {
                return response.bodyToMono(ErrorResponse.class)
                        .flatMap(body -> Mono.error(new NotFoundException(body)));
            }
            if (status.is5xxServerError()) {
                return response.bodyToMono(ErrorResponse.class)
                        .flatMap(body -> Mono.error(new ApplicationException(body)));
            }
            return Mono.error(new ApplicationException(new ErrorResponse("unexpected webclient error.")));
        });

    }
}
