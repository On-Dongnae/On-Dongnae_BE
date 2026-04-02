package com.semo.group1.on_dongnae.module.score.dto;

import com.semo.group1.on_dongnae.entity.Score;
import com.semo.group1.on_dongnae.entity.enums.ScoreType;

import com.semo.group1.on_dongnae.module.score.service.ScoreService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ScoreHistoryDto {
    private Integer amount; // 획득 점수
    private ScoreType type; // 획득 유형
    private LocalDateTime createdAt; // 획득 일시
    private Long referenceId; // 연관 ID, ex) missionId

    // entity -> dto 변환
    public static ScoreHistoryDto fromEntity(Score score) {
        return ScoreHistoryDto.builder()
                .amount(score.getAmount())
                .type(score.getType())
                .createdAt(score.getCreatedAt())
                .referenceId(score.getReferenceId())
                .build();
    }
}
