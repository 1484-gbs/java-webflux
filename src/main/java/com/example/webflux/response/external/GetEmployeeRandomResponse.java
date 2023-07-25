package com.example.webflux.response.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetEmployeeRandomResponse {
    private List<GetEmployeesRandom> employees;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GetEmployeesRandom {
        @JsonProperty("employee_id")
        private Long employeeId;
        private String name;
    }
}
