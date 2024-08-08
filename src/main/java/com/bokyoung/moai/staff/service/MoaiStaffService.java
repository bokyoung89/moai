package com.bokyoung.moai.staff.service;

import com.bokyoung.moai.staff.controller.request.MoaiStaffRequest;
import com.bokyoung.moai.staff.domain.authority.JwtToken;
import com.bokyoung.moai.staff.service.dto.MoaiStaffDto;
import com.bokyoung.moai.staff.service.dto.MoaiStaffSignUpRequestDto;

import javax.servlet.http.Cookie;

public interface MoaiStaffService {

    JwtToken login(MoaiStaffRequest request);

    MoaiStaffDto signUp(MoaiStaffSignUpRequestDto requestDto);

    JwtToken reissueToken(String refreshToken);

    Cookie logout(String refreshToken);

}
