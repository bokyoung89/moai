package com.bokyoung.moai.staff.filter;

import com.bokyoung.moai.common.security.UserDetailsServiceImpl;
import com.bokyoung.moai.staff.exception.UnauthorizedException;
import com.bokyoung.moai.staff.util.JwtUtil;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    //doFilterInternal 진입을 막기 위한 url 제외 설정
    private final static String[] excludePath = {"/moai/staff/**", "/swagger-ui/**", "/v3/api-docs/**"};
    private static AntPathMatcher antPathMatcher;
    private final JwtUtil jwtUtil;


    @PostConstruct
    public void initPatternMatcher() {
        antPathMatcher = new AntPathMatcher();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 제외할 url 설정
        return Arrays.stream(excludePath)
                .anyMatch(pattern -> antPathMatcher.match(pattern, request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.getJwtFromHeader(req);

        if(StringUtils.hasText(accessToken)) {
            try {
                if(jwtUtil.validateToken(accessToken)) {
                    Authentication authentication = jwtUtil.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (UnauthorizedException e) {
                handleTokenException(res, e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    private void handleTokenException(HttpServletResponse res, String errorMessage) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
    }
}
