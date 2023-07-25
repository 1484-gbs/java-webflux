package com.example.webflux.response;

import com.example.webflux.response.external.GetEmployeeResponse;
import com.example.webflux.response.external.GetPositionsResponse;
import com.example.webflux.response.external.GetSkillPerTypeResponse;
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
