package com.bokyoung.moai.filter;

import com.bokyoung.moai.common.security.UserDetailsServiceImpl;
import com.bokyoung.moai.exception.MydArgumentException;
import com.bokyoung.moai.exception.domain.ErrorCode;
import com.bokyoung.moai.util.JwtUtil;
import io.jsonwebtoken.Claims;
import java.io.IOException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        //토큰이 없다면 다음 필터로 넘김
        if(StringUtils.hasText(tokenValue)) {
            log.info(tokenValue);

            //토큰 만료 여부 확인. 만료 시 다음 필터로 넘기지 않음
            try {
                jwtUtil.isExpired(tokenValue);
            } catch (ExpiredJwtException e) {
                log.error("Expired access token", e);
                throw new MydArgumentException(ErrorCode.ERR_EXPIRED_TOKEN);
            }

            // 토큰이 access인지 확인 (발급시 페이로드에 명시)
            String category = jwtUtil.getCategory(tokenValue);

            if(!category.equals("access")) {
                log.error("invalid access token");
                throw new MydArgumentException(ErrorCode.ERR_INVALID_TOKEN);
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.get("userId", String.class));
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);

        return;
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
