package com.bokyoung.moai.reward.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardDto {

    @Schema(description = "주")
    int week;

    @Schema(description = "총액")
    Long amount_sum;

    @Schema(description = "거래 건수")
    Long trade_count;
}
