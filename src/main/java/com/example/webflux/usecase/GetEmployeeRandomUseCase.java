package com.example.webflux.usecase;

import com.example.webflux.repository.WebClientRepository;
import com.example.webflux.response.external.GetEmployeeRandomResponse;
import com.example.webflux.response.external.GetEmployeeResponse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GetEmployeeRandomUseCase {
    private final WebClientRepository<Void, GetEmployeeRandomResponse> randomRepository;
    private final WebClientRepository<Void, GetEmployeeResponse> employeeRepository;

    public Flux<GetEmployeeResponse> execute(ServerRequest req) {

        val count = req.queryParam("count").orElse(null);
        val queryParam = new LinkedMultiValueMap<String, String>();
        if (Objects.nonNull(count)) {
            queryParam.put("count", List.of(count));
        }

        return randomRepository.get(req, "/employee/random", queryParam, GetEmployeeRandomResponse.class)
                .map(GetEmployeeRandomResponse::getEmployees)
                .flatMapMany(e -> {
                    return Flux.fromIterable(e).flatMap(e1 -> {
                        return employeeRepository.get(req, "/employee/" + e1.getEmployeeId(), GetEmployeeResponse.class);
                    });
                });
    }

}
