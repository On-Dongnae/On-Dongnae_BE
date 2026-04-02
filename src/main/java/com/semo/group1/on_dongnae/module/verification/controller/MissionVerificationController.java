package com.semo.group1.on_dongnae.module.verification.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.verification.dto.VerificationDto;
import com.semo.group1.on_dongnae.module.verification.service.MissionVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Verification (인증)", description = "미션 수행 결과 제출 및 인증 상태 확인")
@RestController
@RequestMapping("/api/verifications")
@RequiredArgsConstructor
public class MissionVerificationController {

    private final MissionVerificationService verificationService;

    @Operation(summary = "미션 인증 제출", description = "수행한 미션의 사진과 내용을 제출합니다. AI 분석 또는 관리자 승인 대기 상태로 들어갑니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<VerificationDto>> verifyMission(
            @RequestParam("userMissionId") Long userMissionId,
            @RequestParam(value = "content", required = false) String content,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        VerificationDto result = verificationService.verifyMission(userMissionId, content, images);
        return ResponseEntity.ok(ApiResponse.created("미션 인증이 성공적으로 접수되어 대기 상태로 전환되었습니다.", result));
    }
}
