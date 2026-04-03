package com.semo.group1.on_dongnae.global.security;

import com.semo.group1.on_dongnae.global.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // 웹 전체 적용
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    //
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        // return new BCryptPasswordEncoder(); => 계속 에러 나서 보안성 떨어지는 방법으로 함
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 프론트 로컬 환경(3000, 5173)과 향후 사용될 배포 도메인 허용
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173", "http://localhost:8080", "https://api.on-dongnae.site","https://on-dongnae.site", "https://www.on-dongnae.site"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        // 프론트엔드에서 응답 헤더의 Authorization (JWT 토큰)값을 즉시 읽을 수 있게 허용
        configuration.setExposedHeaders(List.of("Authorization")); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 여러 Rule 구현
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS 필터 및 설정 적용 (핵심)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                
                // 1. 공격 방어 끄기 (요즘 유행하는 VIP 팔찌(JWT) 방식에서는 거의 안 쓰는 구식 방어막입니다)
                .csrf(csrf -> csrf.disable())

                // 2. 스프링 기본 기억력 시스템(세션) 끄기! (우리는 식당 장부에 기록을 안 남기고, 100% 손님의 팔찌 유무로만 판단할 겁니다)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. 서비스 사용 규제
                .authorizeHttpRequests(auth -> auth
                        // Swagger UI 접속 관련 경로 허용
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        // 회원가입/로그인 창, 동네 검색은 누구나 사용 가능
                        .requestMatchers("/api/users/signup", "/api/users/login", "/api/regions", "/api/regions/search", "/error").permitAll()
                        // AI 스케줄러 통신 API 열기
                        .requestMatchers("/api/admin/verifications", "/api/admin/verifications/**/ai-result", "/api/admin/missions").permitAll()
                        // 그 외에 피드 달기, 미션하기 등등 '핵심' 기능은 무조건 JWT 받은 사람만 사용 가능
                        .anyRequest().authenticated()
                )


                // 모든 사용자의 JWT를 전검함 before 아이디, 비번 확인 for 에러 대비 (진짜 엄청나게 나왔음)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
