package com.bokyoung.moai.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MoaiStaffLoginRequest {

    @NotEmpty
    @Size(max = 20, min = 1)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[@$!%?.&])[a-z\\d@$!%?.&]{1,20}$", message = "아이디는 1~20자 내로 입력해주세요.")
    private String userId;

    @NotEmpty
    @Size(max = 100, min = 8)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,16}$", message = "비밀번호는 영문자, 숫자, 특수문자를 포함해 8~20자 내로 입력해주세요.")
    private String password;

}

