package com.semo.group1.on_dongnae.module.user.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.user.dto.LogInRequest;
import com.semo.group1.on_dongnae.module.user.dto.SignUpRequest;
import com.semo.group1.on_dongnae.module.user.dto.UserProfileDto;
import com.semo.group1.on_dongnae.module.user.dto.UserProfileUpdateDto;
import com.semo.group1.on_dongnae.module.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User (회원)", description = "회원가입, 로그인 및 내 정보 관리")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "새로운 유저 계정을 생성합니다.")
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
        userService.signUp(request);
        return ResponseEntity.ok("회원 가입이 성공적으로 완료되었습니다.");
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LogInRequest request) {
        String successMessage = userService.login(request);
        return ResponseEntity.ok(successMessage);
    }

    @Operation(summary = "내 프로필 조회", description = "현재 로그인한 유저 자신의 프로필 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getMyProfile() {
        UserProfileDto profile = userService.getMyProfile();
        return ResponseEntity.ok(ApiResponse.ok("프로필 조회 성공", profile));
    }

    @Operation(summary = "내 프로필 수정", description = "내 프로필 정보(닉네임 등)를 업데이트합니다.")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateMyProfile(@RequestBody UserProfileUpdateDto request) {
        UserProfileDto profile = userService.updateMyProfile(request);
        return ResponseEntity.ok(ApiResponse.ok("프로필이 수정되었습니다.", profile));
    }

    @Operation(summary = "프로필 이미지 변경", description = "내 프로필 이미지를 S3에 업로드하고 변경합니다.")
    @PostMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserProfileDto>> updateProfileImage(
            @RequestPart("image") MultipartFile image) {
        UserProfileDto profile = userService.updateProfileImage(image);
        return ResponseEntity.ok(ApiResponse.ok("프로필 이미지가 변경되었습니다.", profile));
    }
}
