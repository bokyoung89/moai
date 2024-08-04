package com.bokyoung.moai.staff.service.dto;

import com.bokyoung.moai.staff.constant.MoaiStaffRoleType;
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
public class MoaiStaffDto {

    @Schema(description = "사용자 ID")
    String userId;

    @Schema(description = "사용자 비밀번호")
    String password;

    @Schema(description = "사용자 권한")
    MoaiStaffRoleType role;

}
