package com.semo.group1.on_dongnae.module.admin.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.admin.dto.AdminUserResponseDto;
import com.semo.group1.on_dongnae.module.admin.dto.SuspensionRequestDto;
import com.semo.group1.on_dongnae.module.admin.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin (관리자)", description = "관리자 전용 기능 (유저 관리)")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "전체 유저 리스트 조회", description = "관리자가 서비스의 모든 유저 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminUserResponseDto>>> getUserList() {
        List<AdminUserResponseDto> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok("유저 리스트 조회 성공", users));
    }

    @Operation(summary = "유저 정지", description = "관리자가 특정 유저를 서비스 이용 불가능 상태로 정지시킵니다.")
    @PostMapping("/{userId}/suspend")
    public ResponseEntity<ApiResponse<Void>> suspendUser(
            @PathVariable("userId") Long userId,
            @RequestBody SuspensionRequestDto request) {
        adminUserService.suspendUser(userId, request.getReason());
        return ResponseEntity.ok(ApiResponse.ok("해당 유저가 정지되었습니다."));
    }

    @Operation(summary = "유저 정지 해제", description = "정지된 유저를 활성화 상태로 변경합니다.")
    @PostMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable("userId") Long userId) {
        adminUserService.activateUser(userId);
        return ResponseEntity.ok(ApiResponse.ok("해당 유저가 다시 활성화되었습니다."));
    }
}
