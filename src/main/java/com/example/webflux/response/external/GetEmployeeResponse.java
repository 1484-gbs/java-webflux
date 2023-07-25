package com.example.webflux.response.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GetEmployeeResponse {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("first_name_kana")
    private String firstNameKana;
    @JsonProperty("last_name_kana")
    private String lastNameKana;
    private String birthday;
    private Integer age;
    private String gender;
    private String tel;
    @JsonProperty("position_id")
    private Long positionId;
    private List<Long> skills;
    @JsonProperty("photo_url")
    private String photoUrl;
    @JsonProperty("salary_of_month")
    private Integer salaryOfMonth;
}
