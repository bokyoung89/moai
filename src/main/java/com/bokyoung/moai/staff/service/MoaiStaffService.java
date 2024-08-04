package com.bokyoung.moai.staff.service;

import com.bokyoung.moai.staff.controller.request.MoaiStaffRequest;
import com.bokyoung.moai.staff.service.dto.MoaiStaffAuthResponseDto;
import com.bokyoung.moai.staff.service.dto.MoaiStaffDto;
import com.bokyoung.moai.staff.service.dto.MoaiStaffSignUpRequestDto;

public interface MoaiStaffService {

    MoaiStaffAuthResponseDto login(MoaiStaffRequest request);

    MoaiStaffDto signUp(MoaiStaffSignUpRequestDto requestDto);

    MoaiStaffAuthResponseDto reissueToken(String userId, String authority);
}
