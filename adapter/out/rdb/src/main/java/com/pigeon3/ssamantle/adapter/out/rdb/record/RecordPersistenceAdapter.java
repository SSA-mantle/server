package com.pigeon3.ssamantle.adapter.out.rdb.record;

import com.pigeon3.ssamantle.adapter.out.rdb.record.dto.GameStatisticsDataDto;
import com.pigeon3.ssamantle.adapter.out.rdb.record.entity.RecordEntity;
import com.pigeon3.ssamantle.adapter.out.rdb.record.mapper.RecordMapper;
import com.pigeon3.ssamantle.application.game.port.out.*;
import com.pigeon3.ssamantle.application.user.port.out.GameStatisticsData;
import com.pigeon3.ssamantle.application.user.port.out.LoadGameStatisticsPort;
import com.pigeon3.ssamantle.domain.model.record.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecordPersistenceAdapter implements
        SaveRecordPort,
        UpdateRecordPort,
        LoadTodayRecordPort,
        LoadGameStatisticsPort {

    private final RecordMapper recordMapper;

    @Override
    public Record save(Record record) {
        RecordEntity entity = RecordEntity.builder()
            .userId(record.getUserId())
            .problemId(record.getProblemId())
            .failCount(record.getFailCount())
            .solvedAt(record.getSolvedAt())
            .giveUpAt(record.getGiveUpAt())
            .createdAt(record.getCreatedAt())
            .build();

        recordMapper.insert(entity);

        RecordEntity savedEntity = recordMapper.findById(entity.getId());
        return savedEntity.toDomain();
    }

    @Override
    public Record update(Record record) {
        RecordEntity entity = RecordEntity.builder()
            .id(record.getId())
            .userId(record.getUserId())
            .problemId(record.getProblemId())
            .failCount(record.getFailCount())
            .solvedAt(record.getSolvedAt())
            .giveUpAt(record.getGiveUpAt())
            .createdAt(record.getCreatedAt())
            .build();

        recordMapper.update(entity);

        RecordEntity updatedEntity = recordMapper.findById(entity.getId());
        return updatedEntity.toDomain();
    }

    @Override
    public Optional<Record> loadByUserIdAndProblemId(Long userId, Long problemId) {
        RecordEntity entity = recordMapper.findByUserIdAndProblemId(userId, problemId);
        return Optional.ofNullable(entity)
            .map(RecordEntity::toDomain);
    }

    @Override
    public GameStatisticsData loadStatisticsByUserId(Long userId) {
        GameStatisticsDataDto dto = recordMapper.findStatisticsByUserId(userId);
        return dto != null ? dto.toDomain() : new GameStatisticsData(0L, 0L, 0.0);
    }
}
