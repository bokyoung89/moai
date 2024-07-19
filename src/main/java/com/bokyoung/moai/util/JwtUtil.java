package com.bokyoung.moai.util;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

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

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    //accessToken key값
    public static final String ACCESS_TOKEN_HEADER = "access";
    //refreshToken key값
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

    // Token 생성
    private String createToken(String category, String userId, String role, long expirationTime) {
        return Jwts.builder()
                .claim("category", category)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(HS256, key)
                .compact();
    }

    // AccessToken 생성
    public String createAccessToken(String category, String userId, String role) {
        return BEARER_PREFIX + createToken(category, userId, role, ACCESS_TOKEN_TIME);
    }

    // RefreshToken 생성
    public String createRefreshToken(String category, String userId, String role) {
        return createToken(category, userId, role, REFRESH_TOKEN_TIME);
    }

    // RefreshToken Cookie 생성
    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value); // Name-Value
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }

    // header에서 토큰 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
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
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 만료 여부 확인하기
    public boolean isExpired(String refresh) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(refresh).getBody().getExpiration().before(new Date());

    }

    // user_id 가져오기
    public String getUserId(String refresh) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(refresh).getBody().get("userId", String.class);

    }

    // role 가져오기
    public String getRole(String refresh) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(refresh).getBody().get("role", String.class);
    }
}
