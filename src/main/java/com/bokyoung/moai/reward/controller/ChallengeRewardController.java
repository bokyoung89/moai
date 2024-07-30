package com.bokyoung.moai.reward.controller;

import com.bokyoung.moai.aspect.Auth;
import com.bokyoung.moai.reward.controller.response.ChallengeRewardResponse;
import com.bokyoung.moai.reward.service.ChallengeRewardService;
import com.bokyoung.moai.reward.service.dto.ChallengeRewardRequestDto;
import com.bokyoung.moai.reward.service.dto.ChallengeRewardResponseDto;
import com.bokyoung.moai.staff.constant.MoaiStaffRoleType;
import com.bokyoung.moai.userverification.controller.response.MoaiUserVerificationResponse;
import com.bokyoung.moai.userverification.controller.response.MoaiUserVerificationRouteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "ChallengeReward", description = "그룹루틴 챌린지 성공 리워드 조회 API")
public class ChallengeRewardController {

    private final ModelMapper mapper;

    private final ChallengeRewardService challengeRewardService;

    @GetMapping("/moai/challenge/challenges/rewards")
    @Operation(summary = "그룹루틴 챌린지 지급 총액",
            description = "챌린지 참여 성공 후 지급 받은 금액의 총액을 조회한다",
            security = @SecurityRequirement(name = "Authorization")
    )
    @Auth(type = MoaiStaffRoleType.CXO)
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoaiUserVerificationResponse.class)))
    public List<ChallengeRewardResponse> getChallengeRewardAmountAll(@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                     @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        ChallengeRewardRequestDto requestDto = ChallengeRewardRequestDto.builder()
                .startDate(startDate).endDate(endDate).build();

        List<ChallengeRewardResponseDto> challengeRewardResponseDto = challengeRewardService.getChallengeRewardAmountAll(requestDto);

        return mapper.map(challengeRewardResponseDto, new TypeToken<List<ChallengeRewardResponse>>(){}.getType());
    }
}
