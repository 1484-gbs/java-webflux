package com.example.webflux.response;

public record LoginResponse(
        String token,
        String otpReqId
) {

}
