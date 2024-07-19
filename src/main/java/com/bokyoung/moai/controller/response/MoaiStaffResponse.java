package com.bokyoung.moai.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoaiStaffResponse {

    @Schema(description = "사용자 ID", example = "test.123")
    String userId;
}