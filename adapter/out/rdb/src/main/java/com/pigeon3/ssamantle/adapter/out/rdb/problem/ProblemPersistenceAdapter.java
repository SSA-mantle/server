package com.pigeon3.ssamantle.adapter.out.rdb.problem;

import com.pigeon3.ssamantle.adapter.out.rdb.problem.entity.ProblemEntity;
import com.pigeon3.ssamantle.adapter.out.rdb.problem.mapper.ProblemMapper;
import com.pigeon3.ssamantle.application.game.port.out.LoadTodayProblemPort;
import com.pigeon3.ssamantle.application.game.port.out.SaveProblemPort;
import com.pigeon3.ssamantle.domain.model.problem.Problem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProblemPersistenceAdapter implements LoadTodayProblemPort, SaveProblemPort {

    private final ProblemMapper problemMapper;

    @Override
    public Optional<Problem> loadByDate(LocalDate date) {
        ProblemEntity entity = problemMapper.findByDate(date);
        return Optional.ofNullable(entity)
            .map(ProblemEntity::toDomain);
    }

    @Override
    public Problem save(Problem problem) {
        ProblemEntity entity = ProblemEntity.fromDomain(problem);
        problemMapper.insert(entity);
        return entity.toDomain();
    }
}
