package com.bokyoung.moai.reward.service;

import com.bokyoung.moai.reward.service.dto.ChallengeRewardRequestDto;
import com.bokyoung.moai.reward.service.dto.ChallengeRewardResponseDto;

import java.util.List;

public interface ChallengeRewardService {


    List<ChallengeRewardResponseDto> getChallengeRewardAmountAll(ChallengeRewardRequestDto requestDto);
}
