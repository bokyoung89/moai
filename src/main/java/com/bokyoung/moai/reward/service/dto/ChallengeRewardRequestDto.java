package com.bokyoung.moai.reward.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeRewardRequestDto {

    @Schema(description = "조회 시작 일자")
    LocalDate startDate;

    @Schema(description = "조회 마지막 일자")
    LocalDate endDate;

    @Schema(description = "category")
    String category;

    @Schema(description = "type")
    String type;
}
