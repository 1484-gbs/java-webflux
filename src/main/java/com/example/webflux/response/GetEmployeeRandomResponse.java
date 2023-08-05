package com.example.webflux.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetEmployeeRandomResponse(
        List<GetEmployeesRandom> employees
) {
    public record GetEmployeesRandom(
            @JsonProperty("employee_id")
            Long employeeId,
            String name
    ) {
    }
}
