package com.bokyoung.moai.newmember.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Map;
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
public class NewMemberRouteResponse {

    @Schema(description = "일자")
    LocalDate date;

    @Schema(description = "채널별 신규 사용자 리스트")
    Map<String, Long> count;
}
