package com.bokyoung.moai.service;

import com.bokyoung.moai.controller.request.MoaiStaffRequest;
import com.bokyoung.moai.service.dto.MoaiStaffDto;
import com.bokyoung.moai.service.dto.MoaiStaffLoginResponseDto;
import com.bokyoung.moai.service.dto.MoaiStaffReissueResponseDto;
import com.bokyoung.moai.service.dto.MoaiStaffSignUpRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MoaiStaffService {

    public MoaiStaffLoginResponseDto login(MoaiStaffRequest request, HttpServletResponse response);

    MoaiStaffDto signUp(MoaiStaffSignUpRequestDto requestDto);

    MoaiStaffReissueResponseDto reissueToken(HttpServletRequest request, HttpServletResponse response);
}
