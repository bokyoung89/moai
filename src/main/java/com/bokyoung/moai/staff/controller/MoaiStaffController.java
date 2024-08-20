package com.bokyoung.moai.staff.controller;

import com.bokyoung.moai.common.response.SuccessOrFailResponse;
import com.bokyoung.moai.staff.controller.request.MoaiStaffRequest;
import com.bokyoung.moai.staff.controller.response.MoaiStaffAuthResponse;
import com.bokyoung.moai.staff.controller.response.MoaiStaffResponse;
import com.bokyoung.moai.staff.domain.authority.JwtToken;
import com.bokyoung.moai.staff.service.MoaiStaffService;
import com.bokyoung.moai.staff.service.dto.MoaiStaffDto;
import com.bokyoung.moai.staff.service.dto.MoaiStaffSignUpRequestDto;
import com.bokyoung.moai.staff.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "Moai Staff", description = "Moai 사용자 계정 요청을 처리합니다.")
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
            description = "마이디 운영 관리 지표 웹에 로그인 요청을 처리합니다."
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @PostMapping("/moai/staff/login")
    public MoaiStaffAuthResponse login(@Valid @RequestBody MoaiStaffRequest request, HttpServletResponse res) {

        JwtToken jwtToken = moaiStaffService.login(request);
        //AccessToken Header에 담아서 반환
        res.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, jwtToken.getAccessToken());
        //RefreshToken Cookie에 담아서 반환
        res.addCookie(jwtUtil.createCookie(JwtUtil.REFRESH_TOKEN_HEADER, jwtToken.getRefreshToken()));

        return mapper.map(jwtToken, MoaiStaffAuthResponse.class);
    }


    @Operation(summary = "Moai 로그인 재인증",
            description = "refresh 토큰을 검증해 access 토큰을 재발급합니다.",
            security = {
                    @SecurityRequirement(name = "Authorization"),
                    @SecurityRequirement(name = "refreshToken")
            }
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @PostMapping("/moai/staff/reissue")
    public SuccessOrFailResponse reissue(@Parameter(hidden = true) @CookieValue("refreshToken") String refreshToken, HttpServletResponse res) {

        JwtToken jwtToken = moaiStaffService.reissueToken(refreshToken);
        //새로운 accessToken, refreshToken 반환
        res.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, jwtToken.getAccessToken());
        res.addCookie(jwtUtil.createCookie(JwtUtil.REFRESH_TOKEN_HEADER, jwtToken.getRefreshToken()));

        SuccessOrFailResponse successOrFailResponse = SuccessOrFailResponse.builder()
                .result(true)
                .build();

        return successOrFailResponse;
    }

    @Operation(summary = "Moai 로그아웃",
            description = "refresh Token을 만료해 로그아웃을 처리합니다.",
            security = {
                    @SecurityRequirement(name = "refreshToken")
            }
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @PostMapping("/moai/staff/logout")
    public SuccessOrFailResponse logout(@CookieValue("refreshToken") String refreshToken, HttpServletResponse res) {

        Cookie cookie = moaiStaffService.logout(refreshToken);
        //만료 처리된 RefreshToken을 쿠키에 저장
        res.addCookie(cookie);

        SuccessOrFailResponse successOrFailResponse = SuccessOrFailResponse.builder()
                .result(true)
                .build();

        return successOrFailResponse;
    }
}