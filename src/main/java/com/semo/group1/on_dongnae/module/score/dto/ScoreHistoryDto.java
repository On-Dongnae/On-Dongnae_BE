package com.semo.group1.on_dongnae.module.score.dto;

import com.semo.group1.on_dongnae.entity.Score;
import com.semo.group1.on_dongnae.entity.enums.ScoreType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScoreHistoryDto {
    private Long id;
    private Integer amount;
    private ScoreType type;
    private Long referenceId;
    private LocalDateTime createdAt;

    public static ScoreHistoryDto fromEntity(Score score) {
        return ScoreHistoryDto.builder()
                .id(score.getId())
                .amount(score.getAmount())
                .type(score.getType())
                .referenceId(score.getReferenceId())
                .createdAt(score.getCreatedAt())
                .build();
    }
}
