package com.semo.group1.on_dongnae.module.score.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UserRanking {
    private Long userId;
    private String nickname;
    private Long totalScore;
}
