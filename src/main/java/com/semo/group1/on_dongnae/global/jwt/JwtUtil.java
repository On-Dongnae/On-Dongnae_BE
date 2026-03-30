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

        // 💡 최신 버전 특징 2: set이라는 불필요한 단어가 빠지고 코드가 직관적이게 변했습니다.
        return Jwts.builder()
                .subject(email) // 신분증(이메일)
                .issuedAt(new Date()) // 발급 시간
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(key) // 💡 최신 버전 특징 3: 이제 복잡한 알고리즘 이름을 안 적어도, Key 길이를 분석해 알아서 가장 안전한 암호화를 걸어줍니다!
                .compact();
    }

    // 4. 팔찌(토큰)가 위조되지 않았고, 유효기간이 안 지났는지 깐깐하게 검사하는 메서드
    public boolean validateToken(String token) {
        try {
            // 💡 0.12 최신 문법: verifyWith로 마스터키를 넣고 호두까기처럼 토큰을 까봅니다!
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true; // 에러 없이 까졌다면 조작 안 된 진품 (true)
        } catch (Exception e) {
            return false; // 위조되었거나 시간이 지나 만료된 팔찌면 팅겨냄 (false)
        }
    }

    // 5. 정상 팔찌로 확인되면, 안쪽에 몰래 적어뒀던 신분증(이메일) 글자만 쏙 빼오는 메서드
    public String getEmailFromToken(String token) {
        // 💡 0.12 최신 문법: 구버전의 getBody()가 아니라 getPayload()로 내용을 꺼냅니다.
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

}
