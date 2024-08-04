package com.bokyoung.moai.userverification.repository.projection;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public interface MoaiUserVerificationRouteProjection {

    @Schema(description = "일자")
    LocalDate getDate();

    @Schema(description = "유입 경로")
    String getRoute();

    @Schema(description = "본인인증 사용자 수")
    Long getCount();
}
