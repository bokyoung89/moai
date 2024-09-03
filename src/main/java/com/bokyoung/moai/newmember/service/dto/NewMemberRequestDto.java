package com.bokyoung.moai.newmember.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewMemberRequestDto {

    @Schema(description = "조회 시작 일자")
    LocalDate startDate;

    @Schema(description = "조회 마지막 일자")
    LocalDate endDate;
}
