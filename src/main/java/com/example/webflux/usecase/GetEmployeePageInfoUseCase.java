package com.example.webflux.usecase;

import com.example.webflux.repository.WebClientRepository;
import com.example.webflux.response.GetEmployeePageInfoResponse;
import com.example.webflux.response.external.GetEmployeeResponse;
import com.example.webflux.response.external.GetPositionsResponse;
import com.example.webflux.response.external.GetSkillPerTypeResponse;
import com.example.webflux.response.external.GetSkillPerTypeResponse.SkillPerTypeResponse;
import com.example.webflux.response.external.GetSkillPerTypeResponse.SkillPerTypeResponse.SkillResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetEmployeePageInfoUseCase {

    private final WebClientRepository<Void, GetEmployeeResponse> employeeRepository;
    private final WebClientRepository<Void, GetSkillPerTypeResponse> skillRepository;
    private final WebClientRepository<Void, GetPositionsResponse> positionRepository;

    public Mono<GetEmployeePageInfoResponse> execute(ServerRequest req) {
        return Mono.zip(
                employeeRepository.get(req, "/employee/" + req.pathVariable("employeeId"), GetEmployeeResponse.class),
                skillRepository.get(req, "/skill/per_type", GetSkillPerTypeResponse.class),
                positionRepository.get(req, "/position", GetPositionsResponse.class)
        ).map(tuple2 -> (
                        GetEmployeePageInfoResponse.builder()
                                .employee(tuple2.getT1())
                                .skills(setHasSkill(tuple2.getT1().getSkills(), tuple2.getT2().getSkills()))
                                .positions(setEmployeePosition(tuple2.getT3(), tuple2.getT1().getPositionId()).getPositions())
                                .build()
                )
        );
    }

    private SkillPerTypeResponse setHasSkill(List<Long> hasSkillIds, SkillPerTypeResponse skills) {
        hasSkillIds.forEach(skillId -> {
            getHasSkill(skills.getLanguage(), skillId).ifPresentOrElse(skill -> skill.setHasSkill(true), () -> {
                getHasSkill(skills.getFramework(), skillId).ifPresentOrElse(skill -> skill.setHasSkill(true), () -> {
                    getHasSkill(skills.getDatabase(), skillId).ifPresentOrElse(skill -> skill.setHasSkill(true), () -> {
                        getHasSkill(skills.getInfra(), skillId).ifPresent(skill -> skill.setHasSkill(true));
                    });
                });
            });
        });
        return skills;
    }

    private Optional<SkillResponse> getHasSkill(List<SkillResponse> skillPerType, Long skillId) {
        return skillPerType.stream().filter(s -> skillId.equals(s.getSkillId())).findFirst();
    }

    private GetPositionsResponse setEmployeePosition(GetPositionsResponse positions, Long positionId) {
        positions.getPositions().stream().filter(p -> p.getPositionId().equals(positionId)).findFirst().ifPresent(p -> p.setEmployeesPosition(true));
        return positions;
    }
}
