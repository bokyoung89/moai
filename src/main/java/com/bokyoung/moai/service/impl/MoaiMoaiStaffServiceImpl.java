package com.bokyoung.moai.service.impl;

import com.bokyoung.moai.constant.MoaiStaffRole;
import com.bokyoung.moai.controller.request.MoaiStaffLoginRequest;
import com.bokyoung.moai.domain.MoaiStaff;
import com.bokyoung.moai.exception.MydArgumentException;
import com.bokyoung.moai.exception.domain.ErrorCode;
import com.bokyoung.moai.repository.MoaiStaffRepository;
import com.bokyoung.moai.service.MoaiStaffService;
import com.bokyoung.moai.service.dto.MoaiStaffLoginResponseDto;
import com.bokyoung.moai.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
        String accessToken = jwtUtil.createAccessToken(moaiStaff.getUserId(), moaiStaff.getRole());
        String refreshToken = jwtUtil.createRefreshToken(moaiStaff.getUserId());

        return MoaiStaffLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
