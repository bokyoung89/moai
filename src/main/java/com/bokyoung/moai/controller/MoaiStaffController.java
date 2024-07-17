package com.bokyoung.moai.controller;

import com.bokyoung.moai.controller.request.MoaiStaffLoginRequest;
import com.bokyoung.moai.service.MoaiStaffService;
import com.bokyoung.moai.service.dto.MoaiStaffLoginResponseDto;
import com.bokyoung.moai.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "모아이", description = "Moai Staff API")
public class MoaiStaffController {
    private final MoaiStaffService moaiStaffService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Moai 로그인 인증",
            description = "마이디 운영 관리 지표 앱에 로그인한다.",
            security = @SecurityRequirement(name = "X-Auth-Token")
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @PostMapping("/moai/staff/login")
    public void login(@Valid @RequestBody MoaiStaffLoginRequest request, HttpServletResponse response) {

        MoaiStaffLoginResponseDto responseDto = moaiStaffService.login(request, response);
        //AccessToken Header에 담아서 반환
        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, responseDto.getAccessToken());
        //RefreshToken Cookie에 담아서 반환
        response.addCookie(jwtUtil.createCookie(JwtUtil.REFRESH_TOKEN_HEADER, responseDto.getRefreshToken()));
    }
}

