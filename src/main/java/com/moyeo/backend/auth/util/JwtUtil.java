package com.moyeo.backend.auth.util;

import com.moyeo.backend.auth.domain.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료 시간
    private final long TOKEN_TIME = 60 * 60 * 1000L;    // 60분

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // JWT 토큰 생성
    public String createToken(String userId, String nickname) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(userId) // 사용자 식별값(ID)
                .claim("nickname", nickname)
                .claim(AUTHORIZATION_KEY, Role.USER) // 사용자 권한
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))   // 만료 시간
                .setIssuedAt(date)  // 발급일
                .signWith(key, signatureAlgorithm)  // 암호화 알고리즘
                .compact();
    }
}
