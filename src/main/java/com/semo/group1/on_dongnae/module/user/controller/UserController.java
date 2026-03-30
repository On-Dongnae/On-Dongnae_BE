package com.semo.group1.on_dongnae.module.user.controller;

import com.semo.group1.on_dongnae.module.user.controller.UserController;
import com.semo.group1.on_dongnae.module.user.dto.LogInRequest;
import com.semo.group1.on_dongnae.module.user.dto.SignUpRequest;
import com.semo.group1.on_dongnae.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    // 1. 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<String>signUp(@RequestBody SignUpRequest request) {
        userService.signUp(request);

        return ResponseEntity.ok("회원 가입이 성공적으로 완료되었습니다.");
    }
    // 2. 로그인 API
    @PostMapping("/login")
    public ResponseEntity<String>login(@RequestBody LogInRequest request) {
        // JWT 토큰을 return 하기 위해 사전 작업
        String successMessage = userService.login(request);
        return ResponseEntity.ok(successMessage);
    }


}
