package com.pigeon3.ssamantle.application.game.port.in;

public record GetAnswerHistoryCommand(
    Long userId,
    DateType dateType
) {
    public GetAnswerHistoryCommand {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
        if (dateType == null) {
            throw new IllegalArgumentException("dateType은 필수입니다.");
        }
    }

    public enum DateType {
        TODAY,      // 오늘
        YESTERDAY   // 어제
    }
}
