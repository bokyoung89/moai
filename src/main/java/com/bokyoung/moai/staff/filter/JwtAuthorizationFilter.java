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

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final static String[] excludePath = {"/moai/staff/**", "/swagger-ui/**", "/v3/api-docs/**"};
    private static AntPathMatcher antPathMatcher;

    @PostConstruct
    public void initPatterMatcher() {
        antPathMatcher = new AntPathMatcher();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 제외할 url 설정
        return Arrays.stream(excludePath)
                .anyMatch(pattern -> antPathMatcher.match(pattern, request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException, UnauthorizedException {
        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if(StringUtils.hasText(tokenValue)) {
            try {
                if(jwtUtil.validateToken(tokenValue)) {
                    Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                    setAuthentication(info.get("userId", String.class));
                }
            } catch (UnauthorizedException e) {
                req.setAttribute("jwtException", e);
            }
        }

        filterChain.doFilter(req, res);
    }

    //인증 처리
    private void setAuthentication(String userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}