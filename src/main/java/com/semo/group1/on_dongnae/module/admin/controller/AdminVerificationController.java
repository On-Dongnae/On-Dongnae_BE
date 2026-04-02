package com.semo.group1.on_dongnae.module.admin.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.admin.dto.AiVerificationRequestDto;
import com.semo.group1.on_dongnae.module.admin.dto.RejectionRequestDto;
import com.semo.group1.on_dongnae.module.admin.service.AdminVerificationService;
import com.semo.group1.on_dongnae.entity.enums.VerificationStatus;
import com.semo.group1.on_dongnae.module.verification.dto.VerificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/verifications")
@RequiredArgsConstructor
public class AdminVerificationController {

    private final AdminVerificationService adminVerificationService;

    // 1. 미션 인증 리스트 조회 (관리자용)
    @GetMapping
    public ResponseEntity<ApiResponse<List<VerificationDto>>> getVerifications(
            @RequestParam(value = "status", required = false, defaultValue = "PENDING") VerificationStatus status) {
        List<VerificationDto> response = adminVerificationService.getVerificationsByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok("인증 리스트 조회 성공", response));
    }

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

    // 4. AI 분석 결과 반영 (AI 서버용)
    @PatchMapping("/{verificationId}/ai-result")
    public ResponseEntity<ApiResponse<Void>> saveAiResult(
            @PathVariable("verificationId") Long verificationId,
            @RequestBody AiVerificationRequestDto request) {
        adminVerificationService.saveAiResult(verificationId, request);
        return ResponseEntity.ok(ApiResponse.ok("AI 분석 결과가 성공적으로 반영되었습니다."));
    }
}
