package com.bokyoung.moai.service.impl;

import com.bokyoung.moai.exception.MydArgumentException;
import com.bokyoung.moai.service.MoaiStaffService;
import com.bokyoung.moai.service.dto.MoaiStaffReissueResponseDto;
import com.bokyoung.moai.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("사용자 인증 인가 Test")
class StaffServiceImplTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private MoaiStaffServiceImpl moaiStaffService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReissueTokenSuccess() {
        String refreshToken = "validRefreshToken";
        String newAccessToken = "newAccessToken";
        String userId = "testUser";
        String role = "ROLE_USER";

        Cookie[] cookies = {new Cookie("refresh", refreshToken)};
        when(request.getCookies()).thenReturn(cookies);
        when(jwtUtil.isExpired(refreshToken)).thenReturn(false);
        when(jwtUtil.getUserId(refreshToken)).thenReturn(userId);
        when(jwtUtil.getRole(refreshToken)).thenReturn(role);
        when(jwtUtil.createAccessToken(JwtUtil.ACCESS_TOKEN_HEADER, userId, role)).thenReturn(newAccessToken);

        MoaiStaffReissueResponseDto responseDto = moaiStaffService.reissueToken(request, response);

        assertNotNull(responseDto);
        assertEquals(newAccessToken, responseDto.getAccessToken());
    }

    @Test
    public void testReissueTokenNoToken() {
        when(request.getCookies()).thenReturn(null);

        assertThrows(MydArgumentException.class, () -> {
            moaiStaffService.reissueToken(request, response);
        });
    }

    @Test
    public void testReissueTokenExpired() {
        String refreshToken = "expiredRefreshToken";

        Cookie[] cookies = {new Cookie("refresh", refreshToken)};
        when(request.getCookies()).thenReturn(cookies);
        doThrow(new ExpiredJwtException(null, null, "Token expired")).when(jwtUtil).isExpired(refreshToken);

        assertThrows(MydArgumentException.class, () -> {
            moaiStaffService.reissueToken(request, response);
        });
    }
}