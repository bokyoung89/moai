package com.bokyoung.moai.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoaiStaffRequest {

    @Schema(description = "사용자 ID", example = "test.123")
    @Pattern(regexp = "^([^@\\s\\t]{1,20})$", message = "User ID must be 1-20 characters long, and a string that contains at least one alphabetic character and a period, and no '@' character")
    String userId;

    @Schema(description = "사용자 비밀번호", example = "123d456!")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,20}$", message = "Password must be at least 8 characters long and include one letter, one number, and one special character")
    String password;
}
