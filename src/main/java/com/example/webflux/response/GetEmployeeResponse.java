package com.example.webflux.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetEmployeeResponse(
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("first_name_kana")
        String firstNameKana,
        @JsonProperty("last_name_kana")
        String lastNameKana,
        String birthday,
        Integer age,
        String gender,
        String tel,
        @JsonProperty("position_id")
        Long positionId,
        List<Long> skills,
        @JsonProperty("photo_url")
        String photoUrl,
        @JsonProperty("salary_of_month")
        Integer salaryOfMonth
) {
}
