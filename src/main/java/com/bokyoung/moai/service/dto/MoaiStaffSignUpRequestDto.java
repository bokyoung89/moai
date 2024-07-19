package com.bokyoung.moai.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoaiStaffSignUpRequestDto {
    @Schema(description = "사용자 ID")
    String userId;

    @Schema(description = "사용자 비밀번호")
    String password;
}
