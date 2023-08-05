package com.example.webflux.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetSkillPerTypeResponse {
    private SkillPerTypeResponse skills;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SkillPerTypeResponse {
        private List<SkillResponse> language;
        private List<SkillResponse> framework;
        private List<SkillResponse> database;
        private List<SkillResponse> infra;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class SkillResponse {
            @JsonProperty("id")
            private Long skillId;
            @JsonProperty("name")
            private String skillName;
            @JsonProperty("has_skill")
            private boolean hasSkill;
        }

    }

}
