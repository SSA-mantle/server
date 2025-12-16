package com.pigeon3.ssamantle.adapter.in.http.game.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SubmitGuessRequest(
    @NotBlank(message = "추측 단어는 필수입니다.")
    String word,

    @NotNull(message = "실패 횟수는 필수입니다.")
    @PositiveOrZero(message = "실패 횟수는 0 이상이어야 합니다.")
    Integer failCount
) {}
