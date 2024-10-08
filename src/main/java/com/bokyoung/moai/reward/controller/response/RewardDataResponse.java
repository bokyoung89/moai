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
public class RewardDataResponse {

    @Schema(description = "주", example = "주")
    private int week;

    @Schema(description = "총액", example = "100")
    private Long amount_sum;

    @Schema(description = "건수", example = "100")
    private Long trade_count;

}
