package com.bokyoung.moai.reward.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeRewardResponse {

    @Schema(description = "주")
    int week;

    @Schema(description = "챌린지 지급 총액")
    Long total_amount;
}
