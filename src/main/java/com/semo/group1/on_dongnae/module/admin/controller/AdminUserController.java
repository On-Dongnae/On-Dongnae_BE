package com.semo.group1.on_dongnae.module.admin.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.admin.dto.SuspensionRequestDto;
import com.semo.group1.on_dongnae.module.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping("/{userId}/suspend")
    public ResponseEntity<ApiResponse<Void>> suspendUser(
            @PathVariable("userId") Long userId,
            @RequestBody SuspensionRequestDto request) {
        adminUserService.suspendUser(userId, request.getReason());
        return ResponseEntity.ok(ApiResponse.ok("해당 유저가 정지되었습니다."));
    }

    @PostMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable("userId") Long userId) {
        adminUserService.activateUser(userId);
        return ResponseEntity.ok(ApiResponse.ok("해당 유저가 다시 활성화되었습니다."));
    }
}
