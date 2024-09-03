package com.bokyoung.moai.newmember.controller.response;

import com.bokyoung.moai.newmember.service.dto.AgeItemDto;
import com.bokyoung.moai.newmember.service.dto.GenderItemDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

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
public class NewMemberResponse {

    @Schema(description = "일자",  example = "2024-09-02")
    LocalDate date;

    @Schema(description = "신규 사용자 수",  example = "10")
    Long count;
}
