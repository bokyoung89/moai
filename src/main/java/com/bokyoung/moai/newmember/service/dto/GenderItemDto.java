package com.bokyoung.moai.newmember.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenderItemDto {

    @Schema(description = "성별")
    private String gender;

    @Schema(description = "사용자 수")
    private Long count;

}
