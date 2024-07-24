package com.bokyoung.moai.userverification.service;

import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationRequestDto;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationResponseDto;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationRouteResponseDto;

import java.util.List;

public interface MoaiUserVerificationService {

    List<MoaiUserVerificationResponseDto> getUserVerificationCountByPeriod(MoaiUserVerificationRequestDto requestDto);

    List<MoaiUserVerificationRouteResponseDto> getUserVerificationCountByRoute(MoaiUserVerificationRequestDto requestDto);
}
