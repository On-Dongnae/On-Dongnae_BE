package com.semo.group1.on_dongnae.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 모든 사용자는 무조건 제일 먼저 JWTFilter를 거쳐야함
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 사용자의 HTTP request의 헤더에서 "Authorization" 값을 뽑아옴
        String bearerToken = request.getHeader("Authorization");
        String token = null;

        // 2. 만약 사용자가 "Bearer ....."의 형태(JWT 국제 룰)인 JWT일 경우
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7); // "Bearer "라는 띄어쓰기 포함 7글자를 떼어내고 순수 JWT만 추출
        }

        // 3. JWT 진짜 인거 확인 + JwtFilter 확인
        if (token != null && jwtUtil.validateToken(token)) {
            // 4. 팔찌에서 이메일을 뽑아냄
            String email = jwtUtil.getEmailFromToken(token);

            // 5. 사용자가 정당한 사용자임을 Spring Security 내부 storage에 강제로 로그인 데이터를 저장
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 6. 감사 종류 후 controller로 통과
        // (주의: JWT가 없거나 짭인 사람도 여기서 바로 접근 금지 X 통과 O => 뒤에 있는 매니저가 JWT 없는 사용자 걸러냄
        filterChain.doFilter(request, response);
    }
}
