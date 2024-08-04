package com.bokyoung.moai.staff.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoaiStaffLoginResponse {

    @Schema(description = "accessToken")
    String accessToken;

    @Schema(description = "refreshToken")
    String refreshToken;
}
