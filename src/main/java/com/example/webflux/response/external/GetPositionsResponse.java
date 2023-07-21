package com.example.webflux.response.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetPositionsResponse {
    private List<GetPositionResponse> positions;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GetPositionResponse {
        @JsonProperty("id")
        private Long positionId;
        @JsonProperty("name")
        private String positionName;
        @JsonProperty("is_employees_position")
        private boolean isEmployeesPosition;
    }
}
