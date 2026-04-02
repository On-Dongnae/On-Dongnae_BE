package com.semo.group1.on_dongnae.module.admin.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.admin.dto.RejectionRequestDto;
import com.semo.group1.on_dongnae.module.admin.service.AdminVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/verifications")
@RequiredArgsConstructor
public class AdminVerificationController {

    private final AdminVerificationService adminVerificationService;

    @PatchMapping("/{verificationId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveVerification(@PathVariable("verificationId") Long verificationId) {
        adminVerificationService.approveVerification(verificationId);
        return ResponseEntity.ok(ApiResponse.ok("미션 인증이 승인되었습니다."));
    }

    @PatchMapping("/{verificationId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectVerification(
            @PathVariable("verificationId") Long verificationId,
            @RequestBody RejectionRequestDto request) {
        adminVerificationService.rejectVerification(verificationId, request.getReason());
        return ResponseEntity.ok(ApiResponse.ok("미션 인증이 반려되었습니다."));
    }
}
