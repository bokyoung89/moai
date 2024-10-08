package com.bokyoung.moai.staff.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MoaiStaffSignUpResponseDto {

    @Schema(description = "사용자 ID")
    String userId;
}
