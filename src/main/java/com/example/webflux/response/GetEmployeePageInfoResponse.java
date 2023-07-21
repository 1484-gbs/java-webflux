package com.example.webflux.response;

import com.example.webflux.response.external.GetEmployeesResponse;
import com.example.webflux.response.external.GetPositionsResponse;
import com.example.webflux.response.external.GetSkillPerTypeResponse;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GetEmployeePageInfoResponse {
    GetEmployeesResponse employee;
    GetSkillPerTypeResponse.SkillPerTypeResponse skills;
    List<GetPositionsResponse.GetPositionResponse> positions;
}
