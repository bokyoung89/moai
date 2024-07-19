package com.bokyoung.moai.controller;

import com.bokyoung.moai.controller.request.MoaiStaffRequest;
import com.bokyoung.moai.controller.response.MoaiStaffResponse;
import com.bokyoung.moai.service.MoaiStaffService;
import com.bokyoung.moai.service.dto.MoaiStaffDto;
import com.bokyoung.moai.service.dto.MoaiStaffLoginResponseDto;
import com.bokyoung.moai.service.dto.MoaiStaffReissueResponseDto;
import com.bokyoung.moai.service.dto.MoaiStaffSignUpRequestDto;
import com.bokyoung.moai.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public void login(@Valid @RequestBody MoaiStaffRequest request, HttpServletResponse response) {

        MoaiStaffLoginResponseDto responseDto = moaiStaffService.login(request, response);
        //AccessToken Header에 담아서 반환
        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, responseDto.getAccessToken());
        //RefreshToken Cookie에 담아서 반환
        response.addCookie(jwtUtil.createCookie(JwtUtil.REFRESH_TOKEN_HEADER, responseDto.getRefreshToken()));
    }

    @Operation(summary = "Moai 토큰 재발급",
            description = "refresh 토큰을 검증해 access 토큰을 재발급한다.",
            security = {
                    @SecurityRequirement(name = "access"),
                    @SecurityRequirement(name = "refresh")
            }
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @PostMapping("/moai/staff/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {

        MoaiStaffReissueResponseDto responseDto = moaiStaffService.reissueToken(request, response);
        //새로운 accessToken, refreshToken Header에 담아서 반환
        response.setHeader(JwtUtil.ACCESS_TOKEN_HEADER, responseDto.getAccessToken());
        response.addCookie(jwtUtil.createCookie(JwtUtil.REFRESH_TOKEN_HEADER, responseDto.getRefreshToken()));
    }
}

