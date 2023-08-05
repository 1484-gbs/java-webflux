package com.example.webflux.response;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GetEmployeePageInfoResponse {
    GetEmployeeResponse employee;
    GetSkillPerTypeResponse.SkillPerTypeResponse skills;
    List<GetPositionsResponse.GetPositionResponse> positions;
}
