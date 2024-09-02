package com.bokyoung.moai.newmember.service.dto;

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
@NoArgsConstructor
@AllArgsConstructor
public class NewMemberResponseDto {

    @Schema(description = "조회 일자", example = "2024-09-02")
    LocalDate date;

    @Schema(description = "신규 사용자 수", example = "20")
    Long totalCount;

    @Schema(description = "성별 사용자 리스트")
    List<GenderItemDto> genderCount;

    @Schema(description = "연령별 사용자 리스트")
    List<AgeItemDto> ageCount;
}
