package com.bokyoung.moai.staff.service.impl;

import com.bokyoung.moai.staff.constant.MoaiStaffRoleType;
import com.bokyoung.moai.staff.controller.request.MoaiStaffRequest;
import com.bokyoung.moai.staff.domain.MoaiStaff;
import com.bokyoung.moai.staff.domain.authority.JwtToken;
import com.bokyoung.moai.staff.exception.MydArgumentException;
import com.bokyoung.moai.staff.exception.domain.ErrorCode;
import com.bokyoung.moai.staff.repository.MoaiStaffRepository;
import com.bokyoung.moai.staff.service.MoaiStaffService;
import com.bokyoung.moai.staff.service.dto.MoaiStaffDto;
import com.bokyoung.moai.staff.service.dto.MoaiStaffSignUpRequestDto;
import com.bokyoung.moai.staff.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import static com.bokyoung.moai.staff.exception.domain.ErrorCode.ERR_ALREADY_EXISTS_OBJECT;

@Service
@RequiredArgsConstructor
public class MoaiStaffServiceImpl implements MoaiStaffService {

    private final MoaiStaffRepository moaiStaffRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private static final MoaiStaffRoleType moaiStaffRoleType = MoaiStaffRoleType.CXO;

    private final JwtUtil jwtUtil;

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
    @Transactional
    public JwtToken login(MoaiStaffRequest request) {
        String userName = request.getUserId();
        String password = request.getPassword();

        MoaiStaff moaiStaff = moaiStaffRepository.findByUserId(userName).orElseThrow(() ->
                new MydArgumentException(ErrorCode.ERR_NOT_FOUND_OBJECT, "사용자 정보가 없습니다."));

        if (!passwordEncoder.matches(password, moaiStaff.getPassword())) {
            throw new MydArgumentException(ErrorCode.ERR_INVALID_PARAMETER, "비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 시 accessToken, refreshToken 생성
        return jwtUtil.createToken(moaiStaff.getUserId(), moaiStaff.getRole().getAuthority());
    }

    @Override
    public JwtToken reissueToken(String refreshToken) {
        //토큰 검증
        if (refreshToken == null) {
            throw new MydArgumentException(ErrorCode.ERR_NOT_FOUND_OBJECT, "리프레시 토큰 정보가 없습니다.");
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new MydArgumentException(ErrorCode.ERR_EXPIRED_TOKEN, "리프레시 토큰이 만료되었습니다.");
        }

        //사용자 정보 조회 후 accessToken, refreshToken 생성
        String userId = jwtUtil.getUserId(refreshToken);

        MoaiStaff moaiStaff = moaiStaffRepository.findByUserId(userId).orElseThrow(() ->
                new MydArgumentException(ErrorCode.ERR_NOT_FOUND_OBJECT, "사용자 정보가 없습니다."));

        return jwtUtil.createToken(moaiStaff.getUserId(), moaiStaff.getRole().getAuthority());
    }

    @Override
    public Cookie logout(String refreshToken) {

        // 쿠키 만료
        return jwtUtil.expireCookie("refreshToken", refreshToken);
    }
}
