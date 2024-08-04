package com.bokyoung.moai.reward.repositoy.projection;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public interface ChallengeRewardProjection {

    @Schema(description = "주")
    LocalDate getDate();

    @Schema(description = "챌린지 지급 총액")
    Long getAmountSum();
}
