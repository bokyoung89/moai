package com.bokyoung.moai.staff.filter;

import com.bokyoung.moai.staff.exception.MydArgumentException;
import com.bokyoung.moai.staff.exception.domain.ErrorCode;
import com.bokyoung.moai.staff.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            throw new MydArgumentException(ErrorCode.ERR_NOT_FOUND_OBJECT, "refresh token is null");
        }

        //refresh token 만료 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new MydArgumentException(ErrorCode.ERR_EXPIRED_TOKEN, "Expired refresh token");
        }

        //refresh token cookie값 유효시간 0으로 만료
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
