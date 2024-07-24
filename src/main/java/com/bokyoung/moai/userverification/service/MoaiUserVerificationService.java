package com.bokyoung.moai.userverification.service;

import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationRequestDto;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationResponseDto;

import java.util.List;

public interface MoaiUserVerificationService {

    List<MoaiUserVerificationResponseDto> getUserVerificationCountByPeriod(MoaiUserVerificationRequestDto requestDto);
}
