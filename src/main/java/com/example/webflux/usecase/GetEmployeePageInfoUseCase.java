package com.example.webflux.usecase;

import com.example.webflux.repository.WebClientRepository;
import com.example.webflux.response.GetEmployeePageInfoResponse;
import com.example.webflux.response.GetEmployeeResponse;
import com.example.webflux.response.GetPositionsResponse;
import com.example.webflux.response.GetSkillPerTypeResponse;
import com.example.webflux.response.GetSkillPerTypeResponse.SkillPerTypeResponse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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
                        .skills(setHasSkill(tuple2.getT1().skills(), tuple2.getT2().getSkills()))
                        .positions(setEmployeePosition(tuple2.getT3(), tuple2.getT1().positionId()).getPositions())
                                .build()
                )
        );
    }

    private SkillPerTypeResponse setHasSkill(List<Long> hasSkillIds, SkillPerTypeResponse skills) {
        val skillMap = Stream.of(skills.getLanguage(),
                        skills.getFramework(),
                        skills.getDatabase(),
                        skills.getInfra())
                .flatMap(Collection::stream)
                .toList();
        hasSkillIds.forEach(skillId -> {
            skillMap.stream()
                    .filter(s -> s.getSkillId().equals(skillId))
                    .findFirst()
                    .ifPresent(s -> s.setHasSkill(true));
        });
        return skills;
    }

    private GetPositionsResponse setEmployeePosition(GetPositionsResponse positions, Long positionId) {
        positions.getPositions().stream().filter(p -> p.getPositionId().equals(positionId)).findFirst().ifPresent(p -> p.setEmployeesPosition(true));
        return positions;
    }
}