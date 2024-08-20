package com.bokyoung.moai.staff.util;

import com.bokyoung.moai.staff.domain.authority.JwtToken;
import com.bokyoung.moai.staff.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

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
    private final Long accessTokenExpiration = 120 * 60 * 1000L; // 120분
    // REFRESH_TOKEN 만료시간
    private final Long refreshTokenExpiration = 30 * 24 * 60 * 60 * 1000L; // 한달


    //TODO : b2b 환경설정과 동일하게 변경 (private String SECRET_KEY = ServerConfig.get(EnvironmentKey.MOAI_JWT_SECRET_KEY)
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
    public JwtToken createToken(String userId, String role) {

        //accessToken 생성
        String accessToken = Jwts.builder()
                .claim("userId", userId)
                .claim("role", "ROLE_"+role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key)
                .compact();

        //refreshToken 생성
        String refreshToken = Jwts.builder()
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(key)
                .compact();

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    // header에서 토큰 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // RefreshToken Cookie 생성
    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setPath("/moai/staff");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }

    // RefreshToken 만료
    public Cookie expireCookie(String key, String value) {
        Cookie cookie = new Cookie(key, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/moai/staff");
        cookie.setMaxAge(0); // 쿠키 삭제
        return cookie;
    }


    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
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
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = getUserInfoFromToken(accessToken);

        if (claims.get("role") == null) {
            throw new UnauthorizedException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        UserDetails principal = new User((String) claims.get("userId"), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 만료 여부 확인하기
    public boolean isExpired(String token) {
        return Jwts.parser().setSigningKey(key).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // user_id 가져오기
    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(key).build().parseSignedClaims(token).getPayload().get("userId", String.class);
    }
}