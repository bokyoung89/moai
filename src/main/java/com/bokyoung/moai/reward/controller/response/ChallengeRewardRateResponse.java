package com.bokyoung.moai.reward.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeRewardRateResponse {

    @Schema(description = "주")
    int week;

    @Schema(description = "챌린지 성공 비율")
    Long totalRate;
}
