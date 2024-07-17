package com.bokyoung.moai.util;

import static io.jsonwebtoken.Jwts.*;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;

import com.bokyoung.moai.constant.MoaiStaffRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    //accessToken category
    public static final String ACCESS_TOKEN_HEADER = "access";
    //refreshToken category
    public static final String REFRESH_TOKEN_HEADER = "refresh";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
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
            builder()
                    .claim("userId", userId)
                    .claim("role", role)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME))
                    .signWith(key)
                    .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(String userId) {

        return builder()
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))
                .signWith(key)
                .compact();
    }

    // RefreshToken Cookie에 저장
    public Cookie createCookie(String key, String value) {
            Cookie cookie = new Cookie(key, value); // Name-Value
            cookie.setMaxAge(30 * 24 * 60 * 60);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);

            return cookie;
    }

    // header에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 토큰 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT 토큰 입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT token is empty, 잘못된 JWT 토큰 입니다.", e);
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token).getPayload();
    }
}
