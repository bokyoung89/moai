package com.bokyoung.moai.service.impl;

import com.bokyoung.moai.constant.MoaiStaffRole;
import com.bokyoung.moai.controller.request.MoaiStaffLoginRequest;
import com.bokyoung.moai.domain.MoaiStaff;
import com.bokyoung.moai.exception.MydArgumentException;
import com.bokyoung.moai.exception.domain.ErrorCode;
import com.bokyoung.moai.repository.MoaiStaffRepository;
import com.bokyoung.moai.service.MoaiStaffService;
import com.bokyoung.moai.service.dto.MoaiStaffLoginResponseDto;
import com.bokyoung.moai.service.dto.MoaiStaffReissueResponseDto;
import com.bokyoung.moai.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoaiMoaiStaffServiceImpl implements MoaiStaffService {

    private final MoaiStaffRepository moaiStaffRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Transactional
    public MoaiStaffLoginResponseDto login(MoaiStaffLoginRequest request, HttpServletResponse response) {
        String userName = request.getUserId();
        String password = request.getPassword();

        MoaiStaff moaiStaff = moaiStaffRepository.findByUserId(userName).orElseThrow(() ->
            new MydArgumentException(ErrorCode.ERR_NOT_FOUND_OBJECT, "사용자 정보가 없습니다."));

        if (!passwordEncoder.matches(password, moaiStaff.getPassword())) {
            throw new MydArgumentException(ErrorCode.ERR_INVALID_PARAMETER, "비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 시 accessToken, refreshToken 생성
        String accessToken = jwtUtil.createAccessToken(JwtUtil.ACCESS_TOKEN_HEADER, moaiStaff.getUserId(), moaiStaff.getRole());
        String refreshToken = jwtUtil.createRefreshToken(JwtUtil.REFRESH_TOKEN_HEADER, moaiStaff.getUserId(), moaiStaff.getRole());

        return MoaiStaffLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public MoaiStaffReissueResponseDto reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            new MydArgumentException(ErrorCode.ERR_NOT_FOUND_OBJECT, "토큰 정보가 없습니다.");
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            new MydArgumentException(ErrorCode.ERR_EXPIRED_TOKEN, "유효기간이 만료되었습니다.");
        }

        String userId = jwtUtil.getUserId(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccessToken = jwtUtil.createAccessToken(JwtUtil.ACCESS_TOKEN_HEADER, userId, role);

        return MoaiStaffReissueResponseDto.builder()
                .accessToken(newAccessToken)
                .build();
    }
}
