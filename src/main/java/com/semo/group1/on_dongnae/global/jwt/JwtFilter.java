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

    // ⛔ 모든 요청(손님)은 무조건 제일 먼저 이 문지기를 거쳐가야 합니다!
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 손님이 내민 HTTP 요청의 머리말 부분(Header)에서 "Authorization" 값을 뽑아옵니다.
        String bearerToken = request.getHeader("Authorization");
        String token = null;

        // 2. 만약 손님이 "Bearer 어쩌구저쩌구" 하는 형태(JWT 국제 룰)로 팔찌를 들이밀었다면?
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7); // "Bearer "라는 띄어쓰기 포함 7글자 딱지를 떼어내고 순수 팔찌(토큰)만 획득!
        }

        // 3. 팔찌가 있고 + 기계(jwtUtil)에 돌려보니 짭이 아니라고(true) 떨어졌다면!
        if (token != null && jwtUtil.validateToken(token)) {
            // 4. 팔찌에서 이메일을 뽑아냄
            String email = jwtUtil.getEmailFromToken(token);

            // 5. "이 손님은 신원이 확실한 VIP(이메일 O)입니다!" 라고 식당(Spring Security) 내부 장부에 강제로 로그인 도장을 쾅 찍어줌.
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 6. 감사가 끝났으니 다음 문지기 혹은 식당 내부(컨트롤러)로 입장 통과!
        // (주의: 팔찌가 없거나 짭인 사람도 여기서 바로 안 때리고 일단 통과는 시켜줍니다. 어차피 뒤에 있는 총괄 매니저가 일반 손님은 다 내쫓을 예정입니다.)
        filterChain.doFilter(request, response);
    }
}
