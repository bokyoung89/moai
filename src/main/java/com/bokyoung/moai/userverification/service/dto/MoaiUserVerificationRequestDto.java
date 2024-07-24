package com.bokyoung.moai.userverification.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoaiUserVerificationRequestDto {

    @Schema(description = "조회 시작 일자")
    LocalDate startDate;

    @Schema(description = "조회 마지막 일자")
    LocalDate endDate;

}
