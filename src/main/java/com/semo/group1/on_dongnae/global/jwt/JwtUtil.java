package com.semo.group1.on_dongnae.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "OnDongnaeSecretKeyForJwtTokenGenerationMustBeLongEnough";

    // 💡 최신 버전 특징 1: Key 대신 더 강력하고 안전한 SecretKey 전용 객체를 사용합니다.
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    private final long EXPIRATION_TIME = 1000 * 60 * 60L;

    public String createToken(String email) {

        //
        return Jwts.builder()
                .subject(email) // 아이디(이메일) ? => 그냥 이메일
                .issuedAt(new Date()) // 발급 시간
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(key) // Key 길이를 분석해 알아서 가장 안전한 암호화 자동 지정
                .compact();
    }

    // 4. JWT의 검증 및 유효기간 확인
    public boolean validateToken(String token) {
        try {
            // 마스터키를 이용해 JWT 확인
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true; // 에러 없이 확인되었다면 조작 안 됨 (true)
        } catch (Exception e) {
            return false; // 위조되었거나 시간이 지나 만료된 JWT면 팅겨냄 (false)
        }
    }

    // 정상 JWT로 확인되면, 안쪽에 몰래 적어뒀던 이메일 글자만 추출
    public String getEmailFromToken(String token) {

        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

}
