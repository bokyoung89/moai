package com.bokyoung.moai.reward.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeRewardResponseDto {

    @Schema(description = "주")
    int week;

    @Schema(description = "챌린지 지급 총액")
    Long total_amount;
}
