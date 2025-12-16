package com.pigeon3.ssamantle.application.game.port.in;

public record SubmitGuessCommand(
    Long userId,
    String guessWord,
    int failCount
) {
    public SubmitGuessCommand {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
        if (guessWord == null || guessWord.isBlank()) {
            throw new IllegalArgumentException("추측 단어는 필수입니다.");
        }
        if (failCount < 0) {
            throw new IllegalArgumentException("실패 횟수는 0 이상이어야 합니다.");
        }
    }
}
