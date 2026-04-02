package com.semo.group1.on_dongnae.module.score.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyScoreResponseDto {
    private Integer totalScore;
    private List<ScoreHistoryDto> history;
}
