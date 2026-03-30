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

@Configuration
@EnableWebSecurity // "스프링아, 지금부터 내가 짜는 보안 규칙을 이 거대한 식당의 전체 룰로 활성화해 줘!"
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // 아까 고용한 무서운 팔찌 문지기를 매니저 사무실에 데려옵니다.
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 💡 평문 비밀번호를 알아볼 수 없게 찌그러뜨리는(암호화) 기계 등록 (곧 가입 로직에서 다시 쓸 겁니다!)
    @Bean
    public PasswordEncoder passwordEncoder() {
        // return new BCryptPasswordEncoder();
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }

    // 💡 식당 전체의 강력한 보안 규칙(Rule) 장부
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 공격 방어 끄기 (요즘 유행하는 VIP 팔찌(JWT) 방식에서는 거의 안 쓰는 구식 방어막입니다)
                .csrf(csrf -> csrf.disable())

                // 2. 스프링 기본 기억력 시스템(세션) 끄기! (우리는 식당 장부에 기록을 안 남기고, 100% 손님의 팔찌 유무로만 판단할 겁니다)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. 구역별 출입 통제 규칙
                .authorizeHttpRequests(auth -> auth
                        // 프론트가 회원가입/로그인하는 창구는 무조건 프리패스 (여기까지 막으면 아무도 가입을 못 하니까요!)
                        .requestMatchers("/api/users/signup", "/api/users/login", "/error").permitAll()
                        // 그 외에 피드 달기, 미션하기 등등 '모든' 기능은 무조건 팔찌를 찬(로그인한) 사람만 입장 가능!
                        .anyRequest().authenticated()
                )

                // 4. 제일 중요: 스프링의 기본 입장 게이트(아이디/비번 확인) 바로 앞단에,
                // 우리가 방금 만든 '문지기(JwtFilter)'를 떡하니 세워둬서 모든 손님의 팔찌를 일일이 까보게 만듭니다.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
