package com.bokyoung.moai.staff.controller;

import com.bokyoung.moai.staff.controller.request.MoaiStaffRequest;
import com.bokyoung.moai.staff.controller.response.MoaiStaffAuthResponse;
import com.bokyoung.moai.staff.controller.response.MoaiStaffResponse;
import com.bokyoung.moai.staff.service.MoaiStaffService;
import com.bokyoung.moai.staff.service.dto.MoaiStaffAuthResponseDto;
import com.bokyoung.moai.staff.service.dto.MoaiStaffDto;
import com.bokyoung.moai.staff.service.dto.MoaiStaffSignUpRequestDto;
import com.bokyoung.moai.staff.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Tag(name = "모아이", description = "Moai Staff API")
public class MoaiStaffController {

    private final ModelMapper mapper;
    private final MoaiStaffService moaiStaffService;
    private final JwtUtil jwtUtil;


    @PostMapping("/moai/staff/signup")
    @Operation(summary = "Moai 회원가입",
        description = """
                마이디 운영 관리 지표 앱의 관리자 회원가입 API
            """
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoaiStaffResponse.class)))
    public MoaiStaffResponse signUp(@Valid @RequestBody MoaiStaffRequest request) {

        MoaiStaffSignUpRequestDto requestDto = mapper.map(request, MoaiStaffSignUpRequestDto.class);
        MoaiStaffDto responseDto = moaiStaffService.signUp(requestDto);

        return mapper.map(responseDto, MoaiStaffResponse.class);
    }

    @Operation(summary = "Moai 로그인 인증",
        description = "마이디 운영 관리 지표 웹에 로그인한다."
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @PostMapping("/moai/staff/login")
    public MoaiStaffAuthResponse login(@Valid @RequestBody MoaiStaffRequest request) {

        MoaiStaffAuthResponseDto responseDto = moaiStaffService.login(request);

        return mapper.map(responseDto, MoaiStaffAuthResponse.class);
    }

    @Operation(summary = "Moai 로그인 재인증",
        description = "refresh 토큰을 검증해 access 토큰을 재발급한다.",
        security = {
            @SecurityRequirement(name = "access"),
            @SecurityRequirement(name = "refresh")
        }
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @PostMapping("/moai/staff/reissue")
    public MoaiStaffAuthResponse reissue(Authentication authentication) {

        UserDetails user = (UserDetails) authentication.getPrincipal();

        // 사용자 권한 가져오기
        String authority = null;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            authority = grantedAuthority.getAuthority();
            break;
        }

        MoaiStaffAuthResponseDto responseDto = moaiStaffService.reissueToken(user.getUsername(), authority);

        return mapper.map(responseDto, MoaiStaffAuthResponse.class);
    }
}

