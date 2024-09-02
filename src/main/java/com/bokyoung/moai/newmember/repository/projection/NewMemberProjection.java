package com.bokyoung.moai.newmember.repository.projection;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public interface NewMemberProjection {

    @Schema(description = "일자")
    LocalDate getDate();

    @Schema(description = "성별")
    String getGender();

    @Schema(description = "연령별")
    String getAgeRange();
}
