package com.semo.group1.on_dongnae.module.admin.dto;

import com.semo.group1.on_dongnae.entity.enums.VerificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AiVerificationRequestDto {
    private VerificationStatus status; // APPROVED or REJECTED
    private BigDecimal confidence;    // AI 분석 신뢰도 (0.0 ~ 1.0)
    private String reason;            // 반려 시 사유
}
