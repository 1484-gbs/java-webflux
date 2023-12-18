package com.example.webflux.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
        @JsonProperty("one_time_token")
        String token,
        @JsonProperty("otp_req_id")
        String otpReqId
) {

}
