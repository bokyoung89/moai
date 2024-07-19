package com.bokyoung.moai.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class MoaiStaffReissueResponseDto {

    @Schema(description = "accessToken")
    String accessToken;

    @Schema(description = "refreshToken")
    String refreshToken;
}
