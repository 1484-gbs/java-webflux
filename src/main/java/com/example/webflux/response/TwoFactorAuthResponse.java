package com.example.webflux.response;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TwoFactorAuthResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken
) {
}
