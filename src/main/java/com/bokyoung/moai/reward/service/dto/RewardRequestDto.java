package com.bokyoung.moai.reward.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardRequestDto {

    @Schema(description = "시작 날짜")
    LocalDate startDate;

    @Schema(description = "끝 날짜")
    LocalDate endDate;

}
