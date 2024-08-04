package com.bokyoung.moai.reward.controller;

import com.bokyoung.moai.aspect.Auth;
import com.bokyoung.moai.reward.controller.response.ChallengeRewardResponse;
import com.bokyoung.moai.reward.controller.response.RewardDataResponse;
import com.bokyoung.moai.reward.controller.response.RewardResponse;
import com.bokyoung.moai.reward.service.ChallengeRewardService;
import com.bokyoung.moai.reward.service.dto.ChallengeRewardRequestDto;
import com.bokyoung.moai.reward.service.dto.RewardDto;
import com.bokyoung.moai.reward.service.dto.RewardRequestDto;
import com.bokyoung.moai.reward.service.impl.RewardServiceImpl;
import com.bokyoung.moai.staff.constant.MoaiStaffRoleType;
import com.bokyoung.moai.userverification.controller.response.MoaiUserVerificationResponse;
import com.bokyoung.moai.userverification.controller.response.MoaiUserVerificationRouteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Reward", description = "Reward API. 리워드 지급액, 사용된 포인트 총액 요청을 처리한다.")
public class RewardController {

    private final RewardServiceImpl rewardService;
    private final ChallengeRewardService challengeRewardService;
    private final ModelMapper mapper;
}
