package com.bokyoung.moai.staff.service.impl;

import com.bokyoung.moai.staff.constant.MoaiStaffRoleType;
import com.bokyoung.moai.staff.controller.request.MoaiStaffRequest;
import com.bokyoung.moai.staff.domain.MoaiStaff;
import com.bokyoung.moai.staff.domain.authority.JwtToken;
import com.bokyoung.moai.staff.exception.MydArgumentException;
import com.bokyoung.moai.staff.exception.UnauthorizedException;
import com.bokyoung.moai.staff.exception.domain.ErrorCode;
import com.bokyoung.moai.staff.repository.MoaiStaffRepository;
import com.bokyoung.moai.staff.service.MoaiStaffService;
import com.bokyoung.moai.staff.service.dto.MoaiStaffDto;
import com.bokyoung.moai.staff.service.dto.MoaiStaffSignUpRequestDto;
import com.bokyoung.moai.staff.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import static com.bokyoung.moai.staff.constant.MoaiStaffRoleType.CXO;
import static com.bokyoung.moai.staff.exception.domain.ErrorCode.ERR_ALREADY_EXISTS_OBJECT;

@Service
@RequiredArgsConstructor
public class MoaiStaffServiceImpl implements MoaiStaffService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MoaiStaffRepository moaiStaffRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private static final MoaiStaffRoleType moaiStaffRoleType = CXO;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

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
        //토큰 정보와 DB 유저 정보를 비교
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        return jwtUtil.createToken(authentication);
    }

    @Override
    public JwtToken reissueToken(String refreshToken) {
        if (refreshToken == null) {
            throw new MydArgumentException(ErrorCode.ERR_NOT_FOUND_OBJECT, "refreshToken is empty");
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new MydArgumentException(ErrorCode.ERR_EXPIRED_TOKEN, "Expired refreshToken");
        }

        // 인증 정보를 기반으로 토큰 생성
        String userId = jwtUtil.getUserId(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(userDetails);

        return jwtUtil.createToken(authenticationToken);
    }

    @Override
    public Cookie logout(String refreshToken) {

        // 쿠키 만료
        return jwtUtil.expireCookie("refreshToken", refreshToken);
    }

    // 사용자 인증 처리
    private static UsernamePasswordAuthenticationToken getAuthenticationToken(UserDetails user) {

        // 계정 상태 확인
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked.");
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired.");
        } else if (!user.isEnabled()) {
            throw new DisabledException("비활성화 된 사용자 또는 회사입니다.");
        } else if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        return authenticationToken;
    }
}
