package com.semo.group1.on_dongnae.module.verification.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.verification.dto.VerificationDto;
import com.semo.group1.on_dongnae.module.verification.service.MissionVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/verifications")
@RequiredArgsConstructor
public class MissionVerificationController {

    private final MissionVerificationService verificationService;

    // 1. 미션 인증 업로드 (multipart/form-data)
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<VerificationDto>> verifyMission(
            @RequestParam("userMissionId") Long userMissionId,
            @RequestParam(value = "content", required = false) String content,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
            
        VerificationDto result = verificationService.verifyMission(userMissionId, content, images);
        return ResponseEntity.ok(ApiResponse.created("미션 인증이 성공적으로 접수되어 대기 상태로 전환되었습니다.", result));
    }
}
