package com.example.webflux.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TwoFactorAuthRequest {
    @JsonProperty("login_id")
    private String loginId;
    @JsonProperty("one_time_token")
    private String oneTimeToken;
    @JsonProperty("otp_req_id")
    private String otpReqId;
}
