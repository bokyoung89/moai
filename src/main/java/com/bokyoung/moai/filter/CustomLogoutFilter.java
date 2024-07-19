package com.bokyoung.moai.filter;

import com.bokyoung.moai.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
@Slf4j(topic = "로그아웃 시 refresh token 만료")
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //로그아웃 url 및 POST 요청 검증
        String requestUri = request.getRequestURI();

        if (!requestUri.matches("^\\/moai/staff/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();

        if(!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        //refresh token 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        //refresh token null 체크
        if(refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //refresh token 만료 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            //status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //refresh token cookie값 유효시간 0으로 만료
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
