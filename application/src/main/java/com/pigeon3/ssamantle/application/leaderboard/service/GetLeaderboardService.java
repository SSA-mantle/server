package com.pigeon3.ssamantle.application.leaderboard.service;

import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.leaderboard.port.in.GetLeaderboardCommand;
import com.pigeon3.ssamantle.application.leaderboard.port.in.GetLeaderboardResponse;
import com.pigeon3.ssamantle.application.leaderboard.port.in.GetLeaderboardUseCase;
import com.pigeon3.ssamantle.application.leaderboard.port.out.LoadLeaderboardPort;
import com.pigeon3.ssamantle.application.leaderboard.port.out.LoadUsersByIdsPort;
import com.pigeon3.ssamantle.domain.model.leaderboard.vo.LeaderboardEntry;
import com.pigeon3.ssamantle.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetLeaderboardService implements GetLeaderboardUseCase {

    private static final int TOP_RANKERS_LIMIT = 50;

    private final LoadLeaderboardPort loadLeaderboardPort;
    private final LoadUsersByIdsPort loadUsersByIdsPort;

    @Transactional(readOnly = true)
    @Override
    public GetLeaderboardResponse execute(GetLeaderboardCommand command) {
        LocalDate targetDate = command.getEffectiveDate();

        // 1. 상위 100명 조회
        List<LoadLeaderboardPort.LeaderboardRankDto> topRankers =
            loadLeaderboardPort.loadTopRankers(targetDate, TOP_RANKERS_LIMIT);

        // 2. 본인 순위 조회
        Optional<LoadLeaderboardPort.LeaderboardRankDto> myRankDto =
            loadLeaderboardPort.loadUserRank(targetDate, command.userId());

        // 3. 사용자 ID 목록 수집 (중복 제거)
        List<Long> userIds = new ArrayList<>();
        topRankers.forEach(dto -> userIds.add(dto.userId()));
        myRankDto.ifPresent(dto -> {
            if (!userIds.contains(dto.userId())) {
                userIds.add(dto.userId());
            }
        });

        // 4. 사용자 정보 일괄 조회
        Map<Long, User> userMap = loadUsersByIdsPort.loadByIds(userIds);

        // 5. LeaderboardEntry 변환
        List<LeaderboardEntry> topEntries = topRankers.stream()
            .map(dto -> toLeaderboardEntry(dto, userMap))
            .toList();

        LeaderboardEntry myEntry = myRankDto
            .map(dto -> toLeaderboardEntry(dto, userMap))
            .orElse(null);

        // 6. 응답 생성
        return GetLeaderboardResponse.of(targetDate, topEntries, myEntry);
    }

    private LeaderboardEntry toLeaderboardEntry(
            LoadLeaderboardPort.LeaderboardRankDto dto,
            Map<Long, User> userMap) {

        User user = userMap.get(dto.userId());
        if (user == null) {
            throw ApplicationException.of(ExceptionType.USER_NOT_FOUND);
        }

        LocalDateTime solvedAt = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(dto.solvedAtEpochMilli()),
            ZoneId.systemDefault()
        );

        return LeaderboardEntry.of(
            dto.rank(),
            dto.userId(),
            user.getNickname().getValue(),
            dto.failCount(),
            solvedAt
        );
    }
}
