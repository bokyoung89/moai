package com.bokyoung.moai.staff.service.impl;

import com.bokyoung.moai.staff.constant.MoaiStaffRoleType;
import com.bokyoung.moai.staff.controller.request.MoaiStaffRequest;
import com.bokyoung.moai.staff.domain.MoaiStaff;
import com.bokyoung.moai.staff.exception.MydArgumentException;
import com.bokyoung.moai.staff.exception.domain.ErrorCode;
import com.bokyoung.moai.staff.repository.MoaiStaffRepository;
import com.bokyoung.moai.staff.service.MoaiStaffService;
import com.bokyoung.moai.staff.service.dto.MoaiStaffAuthResponseDto;
import com.bokyoung.moai.staff.service.dto.MoaiStaffDto;
import com.bokyoung.moai.staff.service.dto.MoaiStaffSignUpRequestDto;
import com.bokyoung.moai.staff.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import static com.bokyoung.moai.staff.constant.MoaiStaffRoleType.CXO;
import static com.bokyoung.moai.staff.exception.domain.ErrorCode.ERR_ALREADY_EXISTS_OBJECT;

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
    public MoaiStaffAuthResponseDto login(MoaiStaffRequest request) {
        String userName = request.getUserId();
        String password = request.getPassword();

        MoaiStaff moaiStaff = moaiStaffRepository.findByUserId(userName).orElseThrow(() ->
            new MydArgumentException(ErrorCode.ERR_NOT_FOUND_OBJECT, "사용자 정보가 없습니다."));

        if (!passwordEncoder.matches(password, moaiStaff.getPassword())) {
            throw new MydArgumentException(ErrorCode.ERR_INVALID_PARAMETER, "비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.createAccessToken(moaiStaff.getUserId(), moaiStaff.getRole().getAuthority());
        String refreshToken = jwtUtil.createRefreshToken(moaiStaff.getUserId());

        return MoaiStaffAuthResponseDto.builder()
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
    public MoaiStaffAuthResponseDto reissueToken(String userId, String authority) {

        String newAccessToken = jwtUtil.createAccessToken(userId, authority);
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        return MoaiStaffAuthResponseDto.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .build();
    }
}
