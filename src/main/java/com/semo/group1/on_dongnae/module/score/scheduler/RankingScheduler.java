package com.semo.group1.on_dongnae.module.score.scheduler;

import com.semo.group1.on_dongnae.module.score.cache.RankingCache;
import com.semo.group1.on_dongnae.module.score.dto.UserRanking;
import com.semo.group1.on_dongnae.module.score.dto.RegionRanking;
import com.semo.group1.on_dongnae.module.score.repository.ScoreRepository;

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

public class RankingScheduler {
    private final ScoreRepository scoreRepository;
    private final RankingCache rankingCache;

    // 1.초기화 주기 설정 : 매월 1일 0시
    private LocalDateTime getStartOfMonth() {
        return YearMonth.now().atDay(1).atStartOfDay();
    }
    // 1-1. 다음달 초기화 날짜 데이터
    // ex) 이번달 랭킹은 _월 1일에 초기화됩니다.
    private LocalDateTime getNextMonth() {
        return getStartOfMonth().plusMonths(1);
    }

    // 2. 개인 랭킹 : 10분에 1번씩 업데이트 => 상위 n명일 때 m = n + (알파)
    @Scheduled(cron = "0 0/10 * * * *")
    public void updateUserRankingCache(int m) {
        // 상위 n명의 user들을 임시 저장(캐싱)
        List<UserRanking> rankings = scoreRepository.findUserRankings(
                getStartOfMonth(), getNextMonth(), PageRequest.of(0, m));
        // 캐싱해온 것을 바로 전달
        rankingCache.updateUserRankings(rankings);
        // 터미널에 진행 현황 메모
        log.info("==> [10분 배치] 개인 랭킹 캐시 갱신 완료 ( {}건 남음 )", rankings.size());

    }

    // 3. 동네 랭킹 : 매일 0시(자정)에 1번 업데이트 => 상위 n개일 때 m = n + (알파)
    @Scheduled(cron = "0 0 0 * * *")
    public void updateRegionRankingCache(int m) {
        // 싱위 n개의 동네들을 임시 저장(캐싱)
        List<RegionRanking> rankings = scoreRepository.findRegionRankings(
                getStartOfMonth(), getNextMonth(), PageRequest.of(0, m));
        // 캐싱해온 것을 바로 전달
        rankingCache.updateRegionRankings(rankings);
        // 터미널에 진행 현황 메모
        log.info("==> [1일 배치] 동네별 랭킹 캐시 갱신 완료 ( {}건 남음 )", rankings.size());
    }


}
