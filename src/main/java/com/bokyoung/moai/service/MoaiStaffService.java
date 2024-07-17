package com.bokyoung.moai.service;

import com.bokyoung.moai.controller.request.MoaiStaffLoginRequest;
import com.bokyoung.moai.service.dto.MoaiStaffLoginResponseDto;
import com.bokyoung.moai.service.dto.MoaiStaffReissueResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MoaiStaffService {

    MoaiStaffLoginResponseDto login(MoaiStaffLoginRequest request, HttpServletResponse response);

    MoaiStaffReissueResponseDto reissueToken(HttpServletRequest request, HttpServletResponse response);
}
