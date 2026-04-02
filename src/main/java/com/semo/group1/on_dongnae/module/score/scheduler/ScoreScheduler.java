package com.semo.group1.on_dongnae.module.score.scheduler;

import com.semo.group1.on_dongnae.module.score.cache.RankingCache;
import com.semo.group1.on_dongnae.module.score.dto.UserRanking;
import com.semo.group1.on_dongnae.module.score.dto.RegionRanking;
import com.semo.group1.on_dongnae.module.score.repository.ScoreRepository;
import com.semo.group1.on_dongnae.module.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScoreScheduler {

    private final UserRepository userRepository;

    // 1.초기화 주기 설정 : 매월 1일 0시
    private LocalDateTime getStartOfMonth() {
        return YearMonth.now().atDay(1).atStartOfDay();
    }

    // 1-1. 다음달 초기화 날짜 데이터
    // ex) 이번달 랭킹은 _월 1일에 초기화됩니다.
    private LocalDateTime getNextMonth() {
        return getStartOfMonth().plusMonths(1);
    }

    // 2. 개인 점수 초기화
    @Scheduled(cron = "0 0 0 1 * *")
    public void resetMonthlyScores() {
        log.info("==> [월간 초기화] 모든 유저의 점수 초기화를 시작합니다.");

        userRepository.resetAllUserScores(); // UserRepository => module 만들어놓음

        log.info("==> [월간 초기화] 모든 유저의 점수가 0으로 초기화되었습니다.");
    }
}

