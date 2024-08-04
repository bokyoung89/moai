package com.bokyoung.moai.userverification.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoaiUserVerificationResponseDto {

    @Schema(description = "일자")
    LocalDate date;

    @Schema(description = "본인인증 사용자 수")
    Long count;
}
