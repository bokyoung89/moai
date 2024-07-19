package com.bokyoung.moai.service.impl;

import com.bokyoung.moai.constant.MoaiStaffRoleType;
import com.bokyoung.moai.controller.request.MoaiStaffRequest;
import com.bokyoung.moai.domain.MoaiStaff;
import com.bokyoung.moai.exception.MydArgumentException;
import com.bokyoung.moai.exception.domain.ErrorCode;
import com.bokyoung.moai.repository.MoaiStaffRepository;
import com.bokyoung.moai.service.MoaiStaffService;
import com.bokyoung.moai.service.dto.MoaiStaffDto;
import com.bokyoung.moai.service.dto.MoaiStaffLoginResponseDto;
import com.bokyoung.moai.service.dto.MoaiStaffReissueResponseDto;
import com.bokyoung.moai.service.dto.MoaiStaffSignUpRequestDto;
import com.bokyoung.moai.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.bokyoung.moai.constant.MoaiStaffRoleType.CXO;
import static com.bokyoung.moai.exception.domain.ErrorCode.ERR_ALREADY_EXISTS_OBJECT;

@Service
@RequiredArgsConstructor
public class MoaiStaffServiceImpl implements MoaiStaffService {

    private final MoaiStaffRepository moaiStaffRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private static final MoaiStaffRoleType moaiStaffRoleType = CXO;

    private final JwtUtil jwtUtil;


    @Override
    @Transactional
    public MoaiStaffLoginResponseDto login(MoaiStaffRequest request, HttpServletResponse response) {
        String userName = request.getUserId();
        String password = request.getPassword();

        MoaiStaff moaiStaff = moaiStaffRepository.findByUserId(userName).orElseThrow(() ->
                new MydArgumentException(ErrorCode.ERR_NOT_FOUND_OBJECT, "사용자 정보가 없습니다."));

        if (!passwordEncoder.matches(password, moaiStaff.getPassword())) {
            throw new MydArgumentException(ErrorCode.ERR_INVALID_PARAMETER, "비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 시 accessToken, refreshToken 생성
        String accessToken = jwtUtil.createAccessToken(JwtUtil.ACCESS_TOKEN_HEADER, moaiStaff.getUserId(), moaiStaff.getRole().getAuthority());
        String refreshToken = jwtUtil.createRefreshToken(JwtUtil.REFRESH_TOKEN_HEADER, moaiStaff.getUserId(), moaiStaff.getRole().getAuthority());

        return MoaiStaffLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public MoaiStaffDto signUp(MoaiStaffSignUpRequestDto moaiStaffSignUpRequestDto) {
        String userId = moaiStaffSignUpRequestDto.getUserId();
        validateUserIdExist(userId);
        MoaiStaff moaiStaff = createMoaiStaffFromRequest(moaiStaffSignUpRequestDto);
        MoaiStaff savedMoaiStaff = saveMoaiStaff(moaiStaff);
        return mapMoaiStaffToDto(savedMoaiStaff);
    }

    private void validateUserIdExist(String userId) {
        if (moaiStaffRepository.existsByUserId(userId)) {
            throw new MydArgumentException(ERR_ALREADY_EXISTS_OBJECT);
        }
    }

    private MoaiStaff createMoaiStaffFromRequest(MoaiStaffSignUpRequestDto requestDto) {
        String rawPassword = requestDto.getPassword();
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        return MoaiStaff.builder()
                .userId(requestDto.getUserId())
                .password(encryptedPassword)
                .role(moaiStaffRoleType)
                .build();
    }

    private MoaiStaff saveMoaiStaff(MoaiStaff moaiStaff) {
        return moaiStaffRepository.save(moaiStaff);
    }

    private MoaiStaffDto mapMoaiStaffToDto(MoaiStaff moaiStaff) {
        return modelMapper.map(moaiStaff, MoaiStaffDto.class);
    }


    @Override
    public MoaiStaffReissueResponseDto reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
        }

        if (refresh == null) {
            throw new MydArgumentException(ErrorCode.ERR_NOT_FOUND_OBJECT, "리프레시 토큰 정보가 없습니다.");
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new MydArgumentException(ErrorCode.ERR_EXPIRED_TOKEN, "리프레시 토큰의 유효기간이 만료되었습니다.");
        }

        String userId = jwtUtil.getUserId(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccessToken = jwtUtil.createAccessToken(JwtUtil.ACCESS_TOKEN_HEADER, userId, role);
        String newRefreshToken = jwtUtil.createRefreshToken(JwtUtil.REFRESH_TOKEN_HEADER, userId, role);

        return MoaiStaffReissueResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
