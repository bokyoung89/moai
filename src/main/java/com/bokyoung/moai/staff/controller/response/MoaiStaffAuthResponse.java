package com.bokyoung.moai.staff.controller.response;

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
public class MoaiStaffAuthResponse {

    @Schema(description = "accessToken")
    String accessToken;

    @Schema(description = "refreshToken")
    String refreshToken;
}
