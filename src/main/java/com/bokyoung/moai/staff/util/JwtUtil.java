package com.bokyoung.moai.staff.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    //accessToken key값
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    //refreshToken key값
    public static final String REFRESH_TOKEN_HEADER = "refreshToken";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // ACCESS_TOKEN 만료시간
    private final Long ACCESS_TOKEN_TIME = 120 * 60 * 1000L; // 120분
    // REFRESH_TOKEN 만료시간
    private final Long REFRESH_TOKEN_TIME = 30 * 24 * 60 * 60 * 1000L; // 한달

    @Value("${jwt.secret-key}")
    private String secretKey;
    private Key key;

    // secret키 HS256 암호화 알고리즘으로 암호화
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // AccessToken 생성
    public String createAccessToken(String userId, String role) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .claim("userId", userId)
                        .claim("role", role)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME))
                        .signWith(key)
                        .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(String userId) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .claim("userId", userId)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))
                        .signWith(key)
                        .compact();
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);

        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith(BEARER_PREFIX)) {
            return refreshToken.substring(7);
        }

        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            return accessToken.substring(7);
        }

        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            this.isExpired(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않은 JWT 토큰 서명입니다.", e);
            throw new UnauthorizedException("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT 토큰 입니다.", e);
            throw new UnauthorizedException("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            throw new UnauthorizedException("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT token is empty, 잘못된 JWT 토큰 입니다.", e);
            throw new UnauthorizedException("JWT claims string is empty.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 만료 여부 확인하기
    public boolean isExpired(String token) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());

    }
}
