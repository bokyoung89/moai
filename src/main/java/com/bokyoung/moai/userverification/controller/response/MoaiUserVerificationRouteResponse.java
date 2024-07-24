package com.bokyoung.moai.userverification.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoaiUserVerificationRouteResponse {

    @Schema(description = "일자")
    LocalDate day;

    @Schema(description = "채널별 본인인증 사용자 리스트")
    Map<String, Long> count;
}
