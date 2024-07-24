package com.bokyoung.moai.userverification.controller;

import com.bokyoung.moai.userverification.controller.response.MoaiUserVerificationResponse;
import com.bokyoung.moai.userverification.service.MoaiUserVerificationService;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationRequestDto;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Moai", description = "Moai 본인인증 사용자 수 API")
public class MoaiUserVerificationController {

    private final ModelMapper mapper;
    private final MoaiUserVerificationService moaiUserVerificationService;

    @GetMapping("/moai/verification/count")
    @Operation(summary = "기간별 본인인증 사용자 수 조회",
        description = "본인인증한 사용자의 수를 기간별(최근 4주, 이번달)로 조회한다.",
        security = @SecurityRequirement(name = "X-Auth-Token")
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoaiUserVerificationResponse.class)))
    public List<MoaiUserVerificationResponse> getUserVerificationCountByPeriod (
        @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        MoaiUserVerificationRequestDto requestDto = MoaiUserVerificationRequestDto.builder()
            .startDate(startDate).endDate(endDate).build();

        List<MoaiUserVerificationResponseDto> verificationResponseDto = moaiUserVerificationService.getUserVerificationCountByPeriod(requestDto);

        return mapper.map(verificationResponseDto, new TypeToken<List<MoaiUserVerificationResponse>>(){}.getType());
    }
}
